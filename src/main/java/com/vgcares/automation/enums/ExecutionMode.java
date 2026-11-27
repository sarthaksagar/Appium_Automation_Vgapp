package com.vgcares.automation.enums;

/**
 * Controls test execution speed:
 * LEARNING applies a visible demonstration delay after meaningful UI actions;
 * STANDARD runs at normal condition-based wait speed for CI/CD regression.
 */
public enum ExecutionMode {
    LEARNING("learning"),
    STANDARD("standard");

    private final String configValue;

    ExecutionMode(String configValue) {
        this.configValue = configValue;
    }

    public String configValue() {
        return configValue;
    }

    public static ExecutionMode fromString(String value) {
        if (value == null) {
            return LEARNING;
        }
        String normalized = value.trim();
        for (ExecutionMode mode : values()) {
            if (mode.configValue.equalsIgnoreCase(normalized) || mode.name().equalsIgnoreCase(normalized)) {
                return mode;
            }
        }
        throw new IllegalArgumentException("Unsupported execution.mode: '" + value + "'. Use 'learning' or 'standard'.");
    }
}
