package com.vgcares.automation.tests.home;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/**
 * Starter skeleton for Doctor Home / Dashboard tests.
 * Populate module test data in src/test/resources/testdata/doctor/home-data.json.
 */
public class HomeTest extends DoctorBaseTest {

    @Test(enabled = false, groups = {"doctor", "smoke", "regression"},
          description = "TC_HOME_STARTER: Add Doctor Home Dashboard test steps here")
    public void starterDoctorHomeTemplate() {
        JsonNode data = ModuleDataLoader.doctor("home-data.json");
        StepLogger.step("Loaded doctor/home-data.json (" + data.path("module").asText() + ")");
        // TODO: Use ensureDoctorSession() / new HomePage(getDriver()) to add Home tests
    }
}
