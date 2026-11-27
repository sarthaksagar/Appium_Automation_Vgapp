package com.vgcares.automation.tests.navigation;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/**
 * Starter skeleton for Navigation Drawer tests.
 * Populate module test data in src/test/resources/testdata/doctor/navigation-data.json.
 */
public class NavigationDrawerComprehensiveTest extends DoctorBaseTest {

    @Test(enabled = false, groups = {"doctor", "regression"},
          description = "TC_NAV_STARTER: Add Navigation Drawer test steps here")
    public void starterNavigationDrawerTemplate() {
        JsonNode data = ModuleDataLoader.doctor("navigation-data.json");
        StepLogger.step("Loaded doctor/navigation-data.json (" + data.path("module").asText() + ")");
        // TODO: Use ensureDoctorSession().openSideDrawer() to verify drawer items
    }
}
