package com.vgcares.automation.config;

import com.vgcares.automation.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {

    private static final Logger log = LogManager.getLogger(ConfigManager.class);
    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private ConfigManager() {}

    private static void loadProperties() {
        try {
            // First attempt: via ClassLoader
            InputStream stream = ConfigManager.class.getClassLoader().getResourceAsStream("config.properties");
            if (stream != null) {
                properties.load(stream);
                log.info("Loaded config.properties from classpath.");
            } else {
                // Fallback attempt: filesystem path
                try (FileInputStream fis = new FileInputStream(FrameworkConstants.CONFIG_FILE_PATH)) {
                    properties.load(fis);
                    log.info("Loaded config.properties from path: {}", FrameworkConstants.CONFIG_FILE_PATH);
                }
            }
        } catch (Exception e) {
            log.error("Failed to load config.properties: {}", e.getMessage(), e);
        }
    }

    /**
     * Retrieves property with hierarchy: System Property > Environment Variable > config.properties.
     */
    public static String get(String key) {
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.trim().isEmpty()) {
            return sysProp.trim();
        }

        String envProp = System.getenv(key);
        if (envProp != null && !envProp.trim().isEmpty()) {
            return envProp.trim();
        }

        String propValue = properties.getProperty(key);
        if (propValue != null) {
            return propValue.trim();
        }

        log.warn("Property '{}' not found in config or environment variables.", key);
        return "";
    }

    public static String get(String key, String defaultValue) {
        String value = get(key);
        return (value == null || value.trim().isEmpty()) ? defaultValue : value;
    }

    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            log.warn("Property '{}' with value '{}' is not a valid integer. Using default {}.", key, value, defaultValue);
            return defaultValue;
        }
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }
}
