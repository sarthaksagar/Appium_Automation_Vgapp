package com.vgcares.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Locale;
import java.util.Properties;

public final class ConfigManager {
    private static final Properties PROPERTIES = loadProperties();

    private ConfigManager() {}

    private static Properties loadProperties() {
        try (InputStream stream = ConfigManager.class.getResourceAsStream("/config.properties")) {
            if (stream == null) {
                throw new IllegalStateException("Missing framework resource config.properties");
            }
            Properties properties = new Properties();
            properties.load(stream);
            return properties;
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to read config.properties", e);
        }
    }

    /** System property, VGCARES_ environment variable, legacy environment variable, then resource. */
    public static String get(String key, String defaultValue) {
        String envKey = "VGCARES_" + key.replaceAll("([a-z0-9])([A-Z])", "$1_$2")
                .toUpperCase(Locale.ROOT);
        for (String value : new String[] {System.getProperty(key), System.getenv(envKey),
                System.getenv(key), PROPERTIES.getProperty(key)}) {
            if (value != null && !value.isBlank()) {
                return value.trim();
            }
        }
        return defaultValue;
    }

    public static int getInt(String key, int defaultValue) {
        String value = get(key, Integer.toString(defaultValue));
        try {
            int number = Integer.parseInt(value);
            if (number > 0) {
                return number;
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(key + " must be a positive integer", e);
        }
        throw new IllegalArgumentException(key + " must be a positive integer");
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        String value = get(key, Boolean.toString(defaultValue));
        if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
            return Boolean.parseBoolean(value);
        }
        throw new IllegalArgumentException(key + " must be true or false");
    }
}
