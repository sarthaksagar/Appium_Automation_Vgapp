package com.vgcares.automation.testdata;

import com.vgcares.automation.config.ConfigManager;
import org.testng.SkipException;

import java.util.Objects;
import java.util.function.Function;

public final class QaCredentials {
    private final String email;
    private final String password;

    public QaCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String email() {
        return email;
    }

    public String password() {
        return password;
    }

    public static QaCredentials doctor() {
        return doctor(key -> {
            String val = System.getenv(key);
            if (val == null || val.isBlank()) {
                val = ConfigManager.get(key, "");
            }
            return val;
        });
    }

    public static QaCredentials patient() {
        return patient(key -> {
            String val = System.getenv(key);
            if (val == null || val.isBlank()) {
                val = ConfigManager.get(key, "");
            }
            return val;
        });
    }

    static QaCredentials doctor(Function<String, String> environment) {
        String email = required(environment, "VGCARES_DOCTOR_EMAIL", "QA_DOCTOR_EMAIL");
        String password = required(environment, "VGCARES_DOCTOR_PASSWORD", "QA_DOCTOR_PASSWORD");
        return new QaCredentials(email, password);
    }

    static QaCredentials patient(Function<String, String> environment) {
        String email = required(environment, "VGCARES_PATIENT_EMAIL", "QA_PATIENT_EMAIL");
        String password = required(environment, "VGCARES_PATIENT_PASSWORD", "QA_PATIENT_PASSWORD");
        return new QaCredentials(email, password);
    }

    public static String resetEmail() {
        String val = System.getenv("QA_RESET_EMAIL");
        if (val == null || val.isBlank()) {
            val = System.getenv("VGCARES_RESET_EMAIL");
        }
        if (val == null || val.isBlank()) {
            val = ConfigManager.get("QA_RESET_EMAIL", ConfigManager.get("VGCARES_RESET_EMAIL", "doctor@example.com"));
        }
        return val.trim();
    }

    public static String signupDomain() {
        String val = System.getenv("QA_SIGNUP_EMAIL_DOMAIN");
        if (val == null || val.isBlank()) {
            val = System.getenv("VGCARES_SIGNUP_EMAIL_DOMAIN");
        }
        if (val == null || val.isBlank()) {
            val = ConfigManager.get("QA_SIGNUP_EMAIL_DOMAIN", ConfigManager.get("VGCARES_SIGNUP_EMAIL_DOMAIN", "example.com"));
        }
        return val.trim();
    }

    private static String required(Function<String, String> environment, String primaryKey, String secondaryKey) {
        String value = environment.apply(primaryKey);
        if (value == null || value.isBlank()) {
            value = environment.apply(secondaryKey);
        }
        if (value == null || value.isBlank()) {
            throw new SkipException("[Test data] Set " + secondaryKey + " (or " + primaryKey
                    + ") in .env or OS environment variables for this QA scenario.");
        }
        return value.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QaCredentials that = (QaCredentials) o;
        return Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

    @Override
    public String toString() {
        return "QaCredentials[redacted]";
    }
}
