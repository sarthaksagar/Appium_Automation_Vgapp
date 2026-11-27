package com.vgcares.automation.tests.profile;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/** Starter skeleton for Profile Forms & Validation tests (doctor/profile-data.json). */
public class ProfileFormsAndValidationTest extends DoctorBaseTest {

    @Test(enabled = false, groups = {"doctor", "regression"},
          description = "TC_PROFILE_FORMS_STARTER: Add Profile Forms & Validation test steps here")
    public void starterProfileFormsTemplate() {
        JsonNode data = ModuleDataLoader.doctor("profile-data.json");
        StepLogger.step("Loaded doctor/profile-data.json (" + data.path("module").asText() + ")");
    }
}
