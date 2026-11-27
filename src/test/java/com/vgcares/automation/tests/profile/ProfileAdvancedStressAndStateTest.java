package com.vgcares.automation.tests.profile;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/** Starter skeleton for Profile Advanced Stress & State tests (doctor/profile-data.json). */
public class ProfileAdvancedStressAndStateTest extends DoctorBaseTest {

    @Test(enabled = false, groups = {"doctor", "regression"},
          description = "TC_PROFILE_STATE_STARTER: Add Profile State & Stress test steps here")
    public void starterProfileStateTemplate() {
        JsonNode data = ModuleDataLoader.doctor("profile-data.json");
        StepLogger.step("Loaded doctor/profile-data.json (" + data.path("module").asText() + ")");
    }
}
