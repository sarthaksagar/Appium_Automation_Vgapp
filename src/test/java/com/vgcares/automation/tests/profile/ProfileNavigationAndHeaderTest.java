package com.vgcares.automation.tests.profile;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/** Starter skeleton for Profile Navigation & Header tests (doctor/profile-data.json). */
public class ProfileNavigationAndHeaderTest extends DoctorBaseTest {

    @Test(enabled = false, groups = {"doctor", "regression"},
          description = "TC_PROFILE_NAV_STARTER: Add Profile Navigation & Header test steps here")
    public void starterProfileNavigationTemplate() {
        JsonNode data = ModuleDataLoader.doctor("profile-data.json");
        StepLogger.step("Loaded doctor/profile-data.json (" + data.path("module").asText() + ")");
        // TODO: Use ensureDoctorSession().openMyProfile() and new ProfilePage(getDriver())
    }
}
