package com.vgcares.automation.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public final class ConfigManager {
    private static final Properties PROPERTIES = loadProperties();
    private static final Map<String, String> DOT_ENV = loadDotEnv(Path.of(".env"));

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

    public static Map<String, String> loadDotEnv(Path envFile) {
        if (envFile == null || !Files.isRegularFile(envFile)) {
            return Collections.emptyMap();
        }
        Map<String, String> values = new HashMap<>();
        try {
            for (String rawLine : Files.readAllLines(envFile, StandardCharsets.UTF_8)) {
                String line = rawLine.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                int eq = line.indexOf('=');
                if (eq <= 0) {
                    continue;
                }
                String k = line.substring(0, eq).trim();
                String v = line.substring(eq + 1).trim();
                if ((v.startsWith("\"") && v.endsWith("\"")) || (v.startsWith("'") && v.endsWith("'"))) {
                    if (v.length() >= 2) {
                        v = v.substring(1, v.length() - 1);
                    }
                }
                values.put(k, v);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to read .env file at " + envFile, e);
        }
        return Collections.unmodifiableMap(values);
    }

    /**
     * Configuration precedence:
     * 1. Java system property (-Dkey=value)
     * 2. OS environment variable (VGCARES_KEY)
     * 3. OS environment variable (exact key)
     * 4. Local untracked .env file (VGCARES_KEY or exact key)
     * 5. Bundled config.properties resource
     * 6. Default fallback
     */
    public static String get(String key, String defaultValue) {
        String envKey = "VGCARES_" + key.replaceAll("([a-z0-9])([A-Z])", "$1_$2")
                .toUpperCase(Locale.ROOT);
        for (String value : new String[] {
                System.getProperty(key),
                System.getenv(envKey),
                System.getenv(key),
                DOT_ENV.get(envKey),
                DOT_ENV.get(key),
                PROPERTIES.getProperty(key)
        }) {
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
