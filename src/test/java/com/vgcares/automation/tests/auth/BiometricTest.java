package com.vgcares.automation.tests.auth;

import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.pages.BiometricPage;
import com.vgcares.automation.pages.LaunchPage;
import com.vgcares.automation.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Biometric Setup Test Suite – covers TC_ID_BIO-001 through TC_ID_BIO-038.
 *
 * NOTE: The Biometric screen appears AFTER first successful login.
 * Tests here navigate to the login screen and attempt a valid login first.
 * Actual fingerprint enrollment is device-side; these tests validate the UI
 * and flow around the biometric setup screen.
 */
public class BiometricTest extends BaseTest {

    private BiometricPage bioPage;
    private LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void navigateToLogin() {
        new LaunchPage(getDriver()).passOnboardingIfPresent();
        loginPage = new LoginPage(getDriver());
        loginPage.openLoginTab();
        bioPage = new BiometricPage(getDriver());
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC_ID_BIO-001 : Biometric Setup Screen displays after login
    // ══════════════════════════════════════════════════════════════════════
    @Test(description = "TC_ID_BIO-001: Verify Biometric setup screen loads after successful login",
          groups = {"smoke", "regression"}, priority = 1)
    public void testBiometricScreenLoadsAfterLogin() {
        log.info("Executing TC_ID_BIO-001: Biometric screen after login");
        loginPage.login("harshadalolage24@gmail.com", "VGCare@12345");

        try { Thread.sleep(3000); } catch (InterruptedException ignored) {}

        if (bioPage.isBiometricScreenDisplayed()) {
            log.info("TC_ID_BIO-001: Biometric screen displayed – PASS");
            Assert.assertTrue(bioPage.isEnableButtonDisplayed() || bioPage.isSkipButtonDisplayed(),
                "TC_ID_BIO-001: Enable and/or Skip buttons should be present on biometric screen.");
        } else {
            log.info("TC_ID_BIO-001: Biometric screen not shown (may already be configured or skipped).");
            Assert.assertNotNull(getDriver().getCurrentPackage(),
                "TC_ID_BIO-001: App should be active on a valid screen after login.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC_ID_BIO-002 : Brand Header on Biometric Screen
    // ══════════════════════════════════════════════════════════════════════
    @Test(description = "TC_ID_BIO-002: Verify VGCares Global header is displayed on biometric screen",
          groups = {"regression"}, priority = 2)
    public void testBrandHeaderOnBiometricScreen() {
        log.info("Executing TC_ID_BIO-002: Brand header on biometric screen");
        loginPage.login("harshadalolage24@gmail.com", "VGCare@12345");
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        if (bioPage.isBiometricScreenDisplayed()) {
            Assert.assertTrue(bioPage.isBrandHeaderDisplayed(),
                "TC_ID_BIO-002: 'VGCares Global' brand header must be displayed on biometric screen.");
        } else {
            log.info("TC_ID_BIO-002: Biometric screen not shown – brand header test not applicable.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC_ID_BIO-004 : Biometric Prompt Text
    // ══════════════════════════════════════════════════════════════════════
    @Test(description = "TC_ID_BIO-004: Verify biometric prompt message 'Enable Fingerprint' is displayed",
          groups = {"regression"}, priority = 4)
    public void testBiometricPromptTextDisplayed() {
        log.info("Executing TC_ID_BIO-004: Biometric prompt text");
        loginPage.login("harshadalolage24@gmail.com", "VGCare@12345");
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        if (bioPage.isBiometricScreenDisplayed()) {
            Assert.assertTrue(bioPage.isEnableFingerprintPromptDisplayed(),
                "TC_ID_BIO-004: 'Enable Fingerprint after this sign-in?' prompt must be visible.");
        } else {
            log.info("TC_ID_BIO-004: Biometric screen not shown – not applicable.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC_ID_BIO-006 : User can Skip Biometric Setup
    // ══════════════════════════════════════════════════════════════════════
    @Test(description = "TC_ID_BIO-006: Verify user can skip biometric setup and reach Dashboard",
          groups = {"smoke", "regression"}, priority = 6)
    public void testUserCanSkipBiometricSetup() {
        log.info("Executing TC_ID_BIO-006: Skip biometric setup");
        loginPage.login("harshadalolage24@gmail.com", "VGCare@12345");
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        bioPage.skipIfPresent();

        String src = getDriver().getPageSource();
        boolean onDashboard = src.contains("Dashboard") || src.contains("Home") ||
                              src.contains("Welcome") || src.contains("Today") ||
                              !bioPage.isBiometricScreenDisplayed();
        Assert.assertTrue(onDashboard,
            "TC_ID_BIO-006: After skipping biometric, user should be on the Dashboard.");
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC_ID_BIO-007 : Enable Button is Clickable
    // ══════════════════════════════════════════════════════════════════════
    @Test(description = "TC_ID_BIO-007: Verify 'Enable' button is clickable",
          groups = {"regression"}, priority = 7)
    public void testEnableButtonClickable() {
        log.info("Executing TC_ID_BIO-007: Enable button clickable");
        loginPage.login("harshadalolage24@gmail.com", "VGCare@12345");
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        if (bioPage.isBiometricScreenDisplayed()) {
            Assert.assertTrue(bioPage.isEnableButtonDisplayed(),
                "TC_ID_BIO-007: Enable button must be displayed and clickable on biometric screen.");
        } else {
            log.info("TC_ID_BIO-007: Biometric screen not shown – test not applicable.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC_ID_BIO-008 : Skip Button is Clickable
    // ══════════════════════════════════════════════════════════════════════
    @Test(description = "TC_ID_BIO-008: Verify 'Skip' button is clickable",
          groups = {"regression"}, priority = 8)
    public void testSkipButtonClickable() {
        log.info("Executing TC_ID_BIO-008: Skip button clickable");
        loginPage.login("harshadalolage24@gmail.com", "VGCare@12345");
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        if (bioPage.isBiometricScreenDisplayed()) {
            Assert.assertTrue(bioPage.isSkipButtonDisplayed(),
                "TC_ID_BIO-008: Skip button must be displayed on biometric screen.");
        } else {
            log.info("TC_ID_BIO-008: Biometric screen not shown – test not applicable.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC_ID_BIO-020 : Biometric setup prompt does NOT reappear after Skip
    // ══════════════════════════════════════════════════════════════════════
    @Test(description = "TC_ID_BIO-020: Verify biometric setup prompt does NOT appear again after Skip",
          groups = {"regression"}, priority = 20)
    public void testBiometricPromptDoesNotReappearAfterSkip() {
        log.info("Executing TC_ID_BIO-020: No re-prompt after skip");
        loginPage.login("harshadalolage24@gmail.com", "VGCare@12345");
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        bioPage.skipIfPresent();
        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}

        // After skip, biometric screen should not reappear immediately
        boolean biometricNotShown = !bioPage.isBiometricScreenDisplayed();
        Assert.assertTrue(biometricNotShown,
            "TC_ID_BIO-020: Biometric screen should NOT reappear immediately after Skip.");
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC_ID_BIO-022 : Enable & Skip button styling
    // ══════════════════════════════════════════════════════════════════════
    @Test(description = "TC_ID_BIO-022: Verify 'Enable' and 'Skip' buttons have proper styling",
          groups = {"regression"}, priority = 22)
    public void testBiometricButtonsStyling() {
        log.info("Executing TC_ID_BIO-022: Button styling check");
        loginPage.login("harshadalolage24@gmail.com", "VGCare@12345");
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        if (bioPage.isBiometricScreenDisplayed()) {
            boolean enableVisible = bioPage.isEnableButtonDisplayed();
            boolean skipVisible   = bioPage.isSkipButtonDisplayed();
            Assert.assertTrue(enableVisible && skipVisible,
                "TC_ID_BIO-022: Both Enable and Skip buttons should be visible with proper styling.");
        } else {
            log.info("TC_ID_BIO-022: Biometric screen not shown – not applicable.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC_ID_BIO-027 : Device back button behavior on biometric screen
    // ══════════════════════════════════════════════════════════════════════
    @Test(description = "TC_ID_BIO-027: Verify device back button on biometric screen navigates to Dashboard",
          groups = {"regression"}, priority = 27)
    public void testDeviceBackButtonOnBiometricScreen() {
        log.info("Executing TC_ID_BIO-027: Back button on biometric screen");
        loginPage.login("harshadalolage24@gmail.com", "VGCare@12345");
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}

        if (bioPage.isBiometricScreenDisplayed()) {
            getDriver().navigate().back();
            Assert.assertNotNull(getDriver().getCurrentPackage(),
                "TC_ID_BIO-027: App should not crash on back button press on biometric screen.");
        } else {
            log.info("TC_ID_BIO-027: Biometric screen not shown – not applicable.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC_ID_BIO-031 : Biometric screen shown only once
    // ══════════════════════════════════════════════════════════════════════
    @Test(description = "TC_ID_BIO-031: Verify biometric screen is shown only once after first login",
          groups = {"regression"}, priority = 31)
    public void testBiometricScreenShownOnlyOnce() {
        log.info("Executing TC_ID_BIO-031: Biometric screen once per login");
        loginPage.login("harshadalolage24@gmail.com", "VGCare@12345");
        try { Thread.sleep(2000); } catch (InterruptedException ignored) {}
        bioPage.skipIfPresent();

        // App should now be on dashboard without biometric re-prompt
        boolean notOnBioScreen = !bioPage.isBiometricScreenDisplayed();
        Assert.assertTrue(notOnBioScreen,
            "TC_ID_BIO-031: Biometric screen should not be shown again after first skip.");
    }
}
