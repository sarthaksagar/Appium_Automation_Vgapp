package com.vgcares.automation.tests.patient.auth;

import com.vgcares.automation.base.PatientBaseTest;
import com.vgcares.automation.driver.DriverManager;
import com.vgcares.automation.enums.UserRole;
import com.vgcares.automation.pages.LoginPage;
import com.vgcares.automation.pages.patient.PatientHomePage;
import com.vgcares.automation.testdata.QaCredentials;
import com.vgcares.automation.utils.StepLogger;
import com.vgcares.automation.workflows.patient.PatientAuthWorkflow;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Dynamic Live Patient Authentication Test Suite.
 * Reads live QA Patient credentials dynamically from .env / environment variables.
 */
public class PatientAuthTest extends PatientBaseTest {

    @Test(priority = 1, groups = {"patient", "smoke", "regression"},
          description = "TC_ID_PAT_AUTH-001: Verify dynamic QA Patient login reaches Patient Dashboard")
    public void testValidPatientLoginReachesDashboard() {
        StepLogger.step("Authenticate dynamically with QA Patient credentials");
        PatientHomePage patientHome = loginAsQaPatient();
        Assert.assertEquals(DriverManager.getAuthenticatedRole(), UserRole.PATIENT,
                "Active authenticated role must be PATIENT.");
        Assert.assertTrue(patientHome.isPatientDashboardLoaded(),
                "Patient Dashboard must load after valid Patient login.");
        StepLogger.pass("Dynamic Patient login verified");
    }

    @Test(priority = 2, groups = {"patient", "smoke", "regression"},
          description = "TC_ID_PAT_AUTH-002: Verify Patient logout terminates session cleanly")
    public void testPatientLogoutReturnsToLogin() {
        StepLogger.step("Ensure Patient session is active and perform clean logout");
        QaCredentials patient = QaCredentials.patient();
        PatientAuthWorkflow.ensureAuthenticatedPatient(getDriver(), patient.email(), patient.password());
        PatientAuthWorkflow.logout(getDriver());

        LoginPage loginPage = new LoginPage(getDriver());
        Assert.assertEquals(DriverManager.getAuthenticatedRole(), UserRole.COMMON,
                "Authenticated role must reset to COMMON after logout.");
        Assert.assertTrue(loginPage.isLoginButtonDisplayed() || loginPage.isLoginTabDisplayed(),
                "Patient logout must return to Login screen.");
        StepLogger.pass("Patient logout verified cleanly");
    }
}
