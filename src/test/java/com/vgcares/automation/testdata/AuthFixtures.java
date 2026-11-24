package com.vgcares.automation.testdata;

import java.util.UUID;

public final class AuthFixtures {
    private AuthFixtures() {}

    public static String uniqueEmail(String domain) {
        if (!domain.matches("[A-Za-z0-9-]+(?:\\.[A-Za-z0-9-]+)+")) {
            throw new IllegalArgumentException("The signup email domain must be a valid DNS name.");
        }
        return "vgcares-qa-" + UUID.randomUUID() + "@" + domain;
    }

    public static String newPassword() {
        return "Qa9!" + UUID.randomUUID().toString().replace("-", "").substring(0, 12);
    }
}
