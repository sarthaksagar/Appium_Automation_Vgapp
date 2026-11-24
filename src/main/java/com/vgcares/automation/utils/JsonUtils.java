package com.vgcares.automation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public final class JsonUtils {

    private static final Logger log = LogManager.getLogger(JsonUtils.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtils() {}

    /**
     * Reads a JSON file into a JsonNode tree.
     */
    public static JsonNode readJsonTree(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                log.error("JSON file does not exist at path: {}", filePath);
                return null;
            }
            return mapper.readTree(file);
        } catch (IOException e) {
            log.error("Failed to read JSON file at {}: {}", filePath, e.getMessage(), e);
            return null;
        }
    }

    /**
     * Alias for readJsonTree for backwards-compatibility.
     */
    public static JsonNode readJsonFile(String filePath) {
        return readJsonTree(filePath);
    }

    /**
     * Maps a specific node in a JSON file to a target POJO class.
     */
    public static <T> T readJsonAs(String filePath, Class<T> clazz) {
        try {
            return mapper.readValue(new File(filePath), clazz);
        } catch (IOException e) {
            log.error("Failed to deserialize JSON file to {}: {}", clazz.getSimpleName(), e.getMessage(), e);
            return null;
        }
    }
}
