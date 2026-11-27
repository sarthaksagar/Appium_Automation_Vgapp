package com.vgcares.automation.enums;

/**
 * Supported execution roles for the VG Cares Mobile Automation Framework.
 */
public enum UserRole {
    COMMON("COMMON"),
    PATIENT("PATIENT"),
    DOCTOR("DOCTOR");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}
