package com.vgcares.automation.workflows.patient;

import com.vgcares.automation.driver.DriverManager;
import com.vgcares.automation.enums.UserRole;
import com.vgcares.automation.pages.BiometricPage;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.pages.LoginPage;
import com.vgcares.automation.pages.patient.PatientHomePage;
import com.vgcares.automation.utils.ExecutionUtils;
import com.vgcares.automation.workflows.common.OnboardingWorkflow;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Enforces Patient authentication lifecycle and prevents Doctor session leakage.
 */
public final class PatientAuthWorkflow {

    private static final Logger log = LogManager.getLogger(PatientAuthWorkflow.class);

    private PatientAuthWorkflow() {}

    public static PatientHomePage loginAsPatient(AndroidDriver driver, String email, String password) {
        ExecutionUtils.setActiveRole(UserRole.PATIENT);
        if (DriverManager.getAuthenticatedRole() == UserRole.DOCTOR) {
            log.info("Active session belongs to DOCTOR; logging out cleanly before Patient authentication.");
            new HomePage(driver).logout();
            DriverManager.setAuthenticatedRole(UserRole.COMMON);
        }

        OnboardingWorkflow.ensureAuthScreenReady(driver);
        LoginPage loginPage = new LoginPage(driver).openLoginTab();
        log.info("Authenticating as PATIENT ({})", ExecutionUtils.maskSecret(email));
        loginPage.login(email, password);
        new BiometricPage(driver).skipIfPresent();

        PatientHomePage patientHome = new PatientHomePage(driver);
        if (patientHome.isPatientDashboardLoaded()) {
            DriverManager.setAuthenticatedRole(UserRole.PATIENT);
        }
        return patientHome;
    }

    public static PatientHomePage ensureAuthenticatedPatient(AndroidDriver driver, String email, String password) {
        ExecutionUtils.setActiveRole(UserRole.PATIENT);
        PatientHomePage existing = new PatientHomePage(driver);
        if (DriverManager.getAuthenticatedRole() == UserRole.PATIENT && existing.isPatientDashboardLoaded()) {
            return existing;
        }
        return loginAsPatient(driver, email, password);
    }

    public static void logout(AndroidDriver driver) {
        if (DriverManager.getAuthenticatedRole() == UserRole.PATIENT) {
            new PatientHomePage(driver).logout();
            DriverManager.setAuthenticatedRole(UserRole.COMMON);
        }
    }
}
