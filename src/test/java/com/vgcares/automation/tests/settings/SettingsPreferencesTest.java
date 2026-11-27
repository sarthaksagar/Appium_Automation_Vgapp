package com.vgcares.automation.tests.settings;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/** Starter skeleton for Settings Preferences tests (doctor/settings-data.json). */
public class SettingsPreferencesTest extends DoctorBaseTest {

    @Test(enabled = false, groups = {"doctor", "regression"},
          description = "TC_SET_PREF_STARTER: Add Settings Preferences test steps here")
    public void starterSettingsPreferencesTemplate() {
        JsonNode data = ModuleDataLoader.doctor("settings-data.json");
        StepLogger.step("Loaded doctor/settings-data.json (" + data.path("module").asText() + ")");
        // TODO: Use ensureDoctorSession().openSettings() and new SettingsPage(getDriver())
    }
}
