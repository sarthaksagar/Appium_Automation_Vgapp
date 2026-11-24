package com.vgcares.automation.testdata;

import org.testng.SkipException;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.*;

public class AuthFixturesUnitTest {
    @Test
    public void generatesUniqueSignupDataAndValidatesTheDomain() {
        String email = AuthFixtures.uniqueEmail("example.invalid");
        assertTrue(email.endsWith("@example.invalid"));
        assertNotEquals(email, AuthFixtures.uniqueEmail("example.invalid"));
        assertTrue(AuthFixtures.newPassword().matches("Qa9![a-f0-9]{12}"));
        expectThrows(IllegalArgumentException.class, () -> AuthFixtures.uniqueEmail("@example.invalid"));
    }

    @Test
    public void loadsCredentialsOnlyFromEnvironmentAndRedactsRepresentation() {
        var env = Map.of("VGCARES_DOCTOR_EMAIL", "doctor@example.invalid", "VGCARES_DOCTOR_PASSWORD", "fixture-value");
        QaCredentials account = QaCredentials.doctor(env::get);
        assertEquals(account.email(), "doctor@example.invalid");
        assertEquals(account.password(), "fixture-value");
        assertFalse(account.toString().contains(account.email()));
        assertFalse(account.toString().contains(account.password()));
        assertTrue(expectThrows(SkipException.class, () -> QaCredentials.doctor(key -> null))
                .getMessage().contains("VGCARES_DOCTOR_EMAIL"));
    }
}
