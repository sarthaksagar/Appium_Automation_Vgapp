package com.vgcares.automation.tests.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/**
 * Starter skeleton for Forgot Password tests.
 * Populate module test data in src/test/resources/testdata/common/forgot-password-data.json.
 */
public class ForgotPasswordTest extends BaseTest {

    @Test(enabled = false, groups = {"common", "regression"},
          description = "TC_FP_STARTER: Add Forgot Password test steps here")
    public void starterForgotPasswordTemplate() {
        JsonNode data = ModuleDataLoader.common("forgot-password-data.json");
        StepLogger.step("Loaded common/forgot-password-data.json (" + data.path("module").asText() + ")");
        // TODO: Use openFreshLogin().clickForgotPassword() and new ForgotPasswordPage(getDriver())
    }
}
