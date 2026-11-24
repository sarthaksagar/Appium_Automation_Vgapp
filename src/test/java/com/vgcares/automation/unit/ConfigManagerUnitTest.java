package com.vgcares.automation.unit;

import com.vgcares.automation.config.ConfigManager;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ConfigManagerUnitTest {
    @Test
    public void loadsDefaultsAndRespectsSystemPropertyOverride() {
        String key = "explicitWait";
        String previous = System.getProperty(key);
        try {
            System.clearProperty(key);
            assertTrue(ConfigManager.getInt(key, 999) < 999);
            System.setProperty(key, " 19 ");
            assertEquals(ConfigManager.getInt(key, 999), 19);
        } finally {
            if (previous == null) {
                System.clearProperty(key);
            } else {
                System.setProperty(key, previous);
            }
        }
    }

    @Test
    public void rejectsInvalidNumberAndBooleanValues() {
        String key = "unit.invalid";
        try {
            System.setProperty(key, "-5");
            expectThrows(IllegalArgumentException.class, () -> ConfigManager.getInt(key, 1));
            System.setProperty(key, "not-a-number");
            assertTrue(expectThrows(IllegalArgumentException.class, () -> ConfigManager.getInt(key, 1))
                    .getCause() instanceof NumberFormatException);
            expectThrows(IllegalArgumentException.class, () -> ConfigManager.getBoolean(key, false));
        } finally {
            System.clearProperty(key);
        }
    }
}
