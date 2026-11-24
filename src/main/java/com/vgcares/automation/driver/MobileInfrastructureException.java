package com.vgcares.automation.driver;

public final class MobileInfrastructureException extends IllegalStateException {
    public MobileInfrastructureException(String category, String message) {
        this(category, message, null);
    }

    public MobileInfrastructureException(String category, String message, Throwable cause) {
        super("[" + category + "] " + message, cause);
    }
}
