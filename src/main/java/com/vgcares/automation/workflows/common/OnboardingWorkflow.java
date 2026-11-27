package com.vgcares.automation.workflows.common;

import com.vgcares.automation.config.ConfigManager;
import com.vgcares.automation.driver.DriverManager;
import com.vgcares.automation.enums.UserRole;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.pages.LaunchPage;
import com.vgcares.automation.pages.patient.PatientHomePage;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TimeoutException;

/**
 * Shared workflow for handling initial app launch, system permissions,
 * onboarding bypass, and returning to a clean Authentication screen.
 */
public final class OnboardingWorkflow {

    private static final Logger log = LogManager.getLogger(OnboardingWorkflow.class);

    private OnboardingWorkflow() {}

    public static void ensureAuthScreenReady(AndroidDriver driver) {
        LaunchPage launch = new LaunchPage(driver);
        LaunchPage.Screen screen;
        try {
            screen = launch.waitForAuthEntryScreen();
        } catch (TimeoutException initialTimeout) {
            log.warn("VG Cares did not reach a recognized launch screen. Reopening app once.");
            try {
                String appPackage = ConfigManager.get("appPackage", "");
                driver.terminateApp(appPackage);
                driver.activateApp(appPackage);
                screen = launch.waitForAuthEntryScreen();
            } catch (RuntimeException recoveryFailure) {
                recoveryFailure.addSuppressed(initialTimeout);
                throw recoveryFailure;
            }
        }

        log.info("Preparing authentication from screen: {}", screen);
        if (screen == LaunchPage.Screen.HOME) {
            if (DriverManager.getAuthenticatedRole() == UserRole.PATIENT) {
                new PatientHomePage(driver).logout();
            } else {
                new HomePage(driver).logout();
            }
            DriverManager.setAuthenticatedRole(UserRole.COMMON);
            launch.waitForAuthTabs();
        } else {
            launch.passOnboardingIfPresent(screen);
        }
    }
}
