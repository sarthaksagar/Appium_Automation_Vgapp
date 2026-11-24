package com.vgcares.automation.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

public final class JsonUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() {}

    public static JsonNode readJsonTree(String filePath) {
        try {
            JsonNode tree = MAPPER.readTree(new File(filePath));
            if (tree == null || tree.isMissingNode()) {
                throw new IllegalArgumentException("JSON test data is empty at " + filePath);
            }
            return tree;
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to read JSON test data at " + filePath, e);
        }
    }

    public static JsonNode readJsonFile(String filePath) {
        return readJsonTree(filePath);
    }

    public static <T> T readJsonAs(String filePath, Class<T> type) {
        try {
            return MAPPER.readValue(new File(filePath), type);
        } catch (IOException e) {
            throw new UncheckedIOException("Unable to read JSON test data at " + filePath, e);
        }
    }
}
