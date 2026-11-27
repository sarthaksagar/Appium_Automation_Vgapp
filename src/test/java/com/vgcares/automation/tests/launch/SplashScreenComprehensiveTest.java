package com.vgcares.automation.tests.launch;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/**
 * Starter skeleton for Splash Screen tests.
 * Populate module test data in src/test/resources/testdata/common/splash-data.json.
 */
public class SplashScreenComprehensiveTest extends BaseTest {

    @Test(enabled = false, groups = {"common", "regression"},
          description = "TC_SPLASH_STARTER: Add Splash Screen test steps here")
    public void starterSplashTemplate() {
        JsonNode data = ModuleDataLoader.common("splash-data.json");
        StepLogger.step("Loaded common/splash-data.json (" + data.path("module").asText() + ")");
        // TODO: Use new LaunchPage(getDriver()) to implement your Splash Screen cases
    }
}
