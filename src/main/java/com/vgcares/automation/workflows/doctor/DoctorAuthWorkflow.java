package com.vgcares.automation.workflows.doctor;

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
 * Enforces Doctor authentication lifecycle and prevents Patient session leakage.
 */
public final class DoctorAuthWorkflow {

    private static final Logger log = LogManager.getLogger(DoctorAuthWorkflow.class);

    private DoctorAuthWorkflow() {}

    public static HomePage loginAsDoctor(AndroidDriver driver, String email, String password) {
        ExecutionUtils.setActiveRole(UserRole.DOCTOR);
        if (DriverManager.getAuthenticatedRole() == UserRole.PATIENT) {
            log.info("Active session belongs to PATIENT; logging out cleanly before Doctor authentication.");
            new PatientHomePage(driver).logout();
            DriverManager.setAuthenticatedRole(UserRole.COMMON);
        }

        OnboardingWorkflow.ensureAuthScreenReady(driver);
        LoginPage loginPage = new LoginPage(driver).openLoginTab();
        log.info("Authenticating as DOCTOR ({})", ExecutionUtils.maskSecret(email));
        HomePage homePage = loginPage.loginWithDynamicAccount(email, password);
        new BiometricPage(driver).skipIfPresent();

        if (homePage.isHomeScreenLoaded()) {
            DriverManager.setAuthenticatedRole(UserRole.DOCTOR);
        }
        return homePage;
    }

    public static HomePage ensureAuthenticatedDoctor(AndroidDriver driver, String email, String password) {
        ExecutionUtils.setActiveRole(UserRole.DOCTOR);
        HomePage existing = new HomePage(driver);
        if (DriverManager.getAuthenticatedRole() == UserRole.DOCTOR && existing.isHomeScreenLoaded()) {
            return existing;
        }
        return loginAsDoctor(driver, email, password);
    }

    public static void logout(AndroidDriver driver) {
        if (DriverManager.getAuthenticatedRole() == UserRole.DOCTOR) {
            new HomePage(driver).logout();
            DriverManager.setAuthenticatedRole(UserRole.COMMON);
        }
    }
}
