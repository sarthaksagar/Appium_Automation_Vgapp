package com.vgcares.automation.tests.e2e.crossrole;

import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.driver.DriverManager;
import com.vgcares.automation.enums.UserRole;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.pages.LoginPage;
import com.vgcares.automation.pages.patient.PatientHomePage;
import com.vgcares.automation.testdata.QaCredentials;
import com.vgcares.automation.utils.ExecutionUtils;
import com.vgcares.automation.utils.StepLogger;
import com.vgcares.automation.workflows.doctor.DoctorAuthWorkflow;
import com.vgcares.automation.workflows.patient.PatientAuthWorkflow;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Verified Cross-Role Sequential Execution End-to-End Test on One Physical Device.
 * Validates clean authentication handoff: Patient Login -> Verify -> Logout -> Doctor Login -> Verify -> Logout.
 */
public class CrossRoleSessionTransitionTest extends BaseTest {

    @Test(priority = 1, groups = {"e2e", "regression"},
          description = "TC_ID_E2E_ROLE-001: Verify clean sequential Patient-to-Doctor role transition on single device")
    public void testSequentialPatientToDoctorSessionHandoff() {
        ExecutionUtils.setActiveRole(UserRole.PATIENT);
        QaCredentials patient = QaCredentials.patient();
        QaCredentials doctor = QaCredentials.doctor();

        StepLogger.step("Step 1: Authenticate as PATIENT and verify Patient Dashboard");
        PatientHomePage patientHome = PatientAuthWorkflow.loginAsPatient(
                getDriver(), patient.email(), patient.password());
        Assert.assertEquals(DriverManager.getAuthenticatedRole(), UserRole.PATIENT,
                "Role must be PATIENT after Patient login.");
        Assert.assertTrue(patientHome.isPatientDashboardLoaded(),
                "Patient Dashboard must load.");
        StepLogger.pass("Patient login and dashboard verified");

        StepLogger.step("Step 2: Logout Patient session cleanly before switching to Doctor");
        PatientAuthWorkflow.logout(getDriver());
        Assert.assertEquals(DriverManager.getAuthenticatedRole(), UserRole.COMMON,
                "Role must reset to COMMON after Patient logout.");
        StepLogger.pass("Patient session closed cleanly");

        StepLogger.step("Step 3: Authenticate as DOCTOR and verify Doctor Dashboard");
        ExecutionUtils.setActiveRole(UserRole.DOCTOR);
        HomePage doctorHome = DoctorAuthWorkflow.loginAsDoctor(
                getDriver(), doctor.email(), doctor.password());
        Assert.assertEquals(DriverManager.getAuthenticatedRole(), UserRole.DOCTOR,
                "Role must be DOCTOR after Doctor login.");
        Assert.assertTrue(doctorHome.isHomeScreenLoaded(),
                "Doctor Dashboard must load.");
        StepLogger.pass("Doctor login and dashboard verified");

        StepLogger.step("Step 4: Logout Doctor session cleanly");
        DoctorAuthWorkflow.logout(getDriver());
        LoginPage loginPage = new LoginPage(getDriver());
        Assert.assertEquals(DriverManager.getAuthenticatedRole(), UserRole.COMMON,
                "Role must reset to COMMON after Doctor logout.");
        Assert.assertTrue(loginPage.isLoginButtonDisplayed() || loginPage.isLoginTabDisplayed(),
                "Should return cleanly to Auth Login screen.");
        StepLogger.pass("Cross-role sequential session transition completed cleanly");
    }
}
