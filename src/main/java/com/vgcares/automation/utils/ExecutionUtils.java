package com.vgcares.automation.utils;

import com.vgcares.automation.config.ConfigManager;
import com.vgcares.automation.constants.FrameworkConstants;
import com.vgcares.automation.enums.ExecutionMode;
import com.vgcares.automation.enums.UserRole;

/**
 * Holds thread-safe runtime execution context (active role, suite label, execution mode)
 * and centralized demonstration delay logic for Learning vs Standard modes.
 */
public final class ExecutionUtils {

    private static volatile UserRole activeRole = UserRole.COMMON;
    private static volatile String activeSuiteName = resolveDefaultSuiteLabel();
    private static final String EXECUTION_ID = "VG-" + new java.text.SimpleDateFormat("yyyyMMdd-HHmmss").format(new java.util.Date());

    private ExecutionUtils() {}

    public static String getExecutionId() {
        return EXECUTION_ID;
    }

    public static ExecutionMode getExecutionMode() {
        return ExecutionMode.fromString(ConfigManager.get("execution.mode", "learning"));
    }

    public static boolean isLearningMode() {
        return getExecutionMode() == ExecutionMode.LEARNING;
    }

    public static int getActionDelayMillis() {
        return isLearningMode() ? ConfigManager.getInt("action.delay.ms", FrameworkConstants.ACTION_DELAY_MILLIS) : 0;
    }

    public static void applyActionDelayIfLearning() {
        if (isLearningMode()) {
            try {
                Thread.sleep(getActionDelayMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static UserRole getActiveRole() {
        return activeRole;
    }

    public static void setActiveRole(UserRole role) {
        activeRole = role == null ? UserRole.COMMON : role;
    }

    public static String getActiveSuiteName() {
        return activeSuiteName;
    }

    public static void setActiveSuiteName(String suiteName) {
        if (suiteName != null && !suiteName.isBlank()) {
            activeSuiteName = suiteName.trim();
        }
    }

    public static String maskSecret(String value) {
        if (value == null || value.isBlank()) {
            return "[unset]";
        }
        if (value.contains("@")) {
            int at = value.indexOf('@');
            if (at <= 2) {
                return "***" + value.substring(at);
            }
            return value.substring(0, 2) + "***" + value.substring(at);
        }
        return "********";
    }

    private static String resolveDefaultSuiteLabel() {
        String suiteProp = System.getProperty("suiteXmlFile", "");
        if (suiteProp.toLowerCase().contains("smoke")) {
            return "SMOKE";
        }
        if (suiteProp.toLowerCase().contains("regression")) {
            return "REGRESSION";
        }
        if (suiteProp.toLowerCase().contains("e2e")) {
            return "E2E";
        }
        if (suiteProp.toLowerCase().contains("unit")) {
            return "UNIT";
        }
        return "CUSTOM";
    }
}
