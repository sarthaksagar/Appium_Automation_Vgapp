package com.vgcares.automation.tests.auth;

import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/**
 * Dynamic Login Test Suite for both Doctor and Patient roles.
 * Uses credentials supplied at runtime via .env or OS environment variables (QaCredentials).
 */
public class LoginTest extends BaseTest {

    @Test(description = "TC_ID_LOGIN-DYN-001: Verify dynamic live Doctor login navigates to Doctor Dashboard",
          groups = {"doctor", "smoke", "regression"}, priority = 1)
    public void testDynamicLiveLoginToDashboard() {
        StepLogger.step("Authenticate dynamically using QA Doctor credentials");
        loginAsQaDoctor();
        StepLogger.pass("Doctor logged in and Doctor Dashboard verified");
    }

    @Test(description = "TC_ID_LOGIN-DYN-002: Verify dynamic live Patient login navigates to Patient Dashboard",
          groups = {"patient", "smoke", "regression"}, priority = 2)
    public void testPatientRoleLoginToDashboard() {
        StepLogger.step("Authenticate dynamically using QA Patient credentials");
        loginAsQaPatient();
        StepLogger.pass("Patient logged in and Patient Dashboard verified");
    }
}
