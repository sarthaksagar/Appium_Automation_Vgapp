package com.vgcares.automation.tests.app;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/**
 * Starter skeleton for App Launch tests.
 * Populate module test data in src/test/resources/testdata/common/launch-data.json
 * and enable @Test methods when adding your test cases.
 */
public class AppLaunchTest extends BaseTest {

    @Test(enabled = false, groups = {"common", "smoke", "regression"},
          description = "TC_APP_STARTER: Add App Launch verification steps here")
    public void starterAppLaunchTemplate() {
        JsonNode data = ModuleDataLoader.common("launch-data.json");
        StepLogger.step("Loaded common/launch-data.json (" + data.path("module").asText() + ")");
        // TODO: Add your App Launch test steps and assertions using getDriver() and LaunchPage
    }
}
