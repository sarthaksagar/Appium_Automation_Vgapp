package com.vgcares.automation.tests.auth;

import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.testdata.QaCredentials;
import com.vgcares.automation.utils.StepLogger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Dynamic Live Doctor Login Test Suite.
 * Reads live QA credentials dynamically from .env / environment variables.
 */
public class DynamicLiveLoginTest extends DoctorBaseTest {

    @Test(priority = 1, groups = {"doctor", "smoke", "regression", "dynamic"},
          description = "TC_ID_DYN_LOG-001: Verify dynamic Doctor credentials are configured")
    public void testDynamicLiveUserConfigurationCheck() {
        StepLogger.step("Resolve dynamic QA Doctor credentials from environment/.env");
        QaCredentials doctor = QaCredentials.doctor();
        Assert.assertFalse(doctor.email().isBlank(), "Doctor email must be configured.");
        StepLogger.pass("Dynamic Doctor credentials resolved cleanly");
    }

    @Test(priority = 2, groups = {"doctor", "smoke", "regression", "dynamic"},
          description = "TC_ID_DYN_LOG-002: Verify dynamic Doctor authentication and Doctor Dashboard landing")
    public void testDynamicLiveAuthenticationAndDashboardRedirection() {
        StepLogger.step("Login dynamically with QA Doctor account");
        HomePage homePage = loginAsQaDoctor();
        Assert.assertTrue(homePage.isHomeScreenLoaded(),
                "App must authenticate dynamic Doctor account and load Doctor Dashboard");
        StepLogger.pass("Dynamic Doctor login verified");
    }
}
