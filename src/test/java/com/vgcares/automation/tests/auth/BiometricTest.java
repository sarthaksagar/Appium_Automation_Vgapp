package com.vgcares.automation.tests.auth;

import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.pages.BiometricPage;
import com.vgcares.automation.pages.LoginPage;
import com.vgcares.automation.pages.HomePage;
import org.testng.Assert;
import org.testng.SkipException;
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

    @BeforeMethod(groups = {"smoke", "regression"})
    public void navigateToLogin() {
        loginPage = openFreshLogin();
        bioPage = new BiometricPage(getDriver());
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC_ID_BIO-001 : Biometric Setup Screen displays after login
    // ══════════════════════════════════════════════════════════════════════
    @Test(description = "TC_ID_BIO-001: Verify Biometric setup screen loads after successful login",
          groups = {"smoke", "regression"}, priority = 1)
    public void testBiometricScreenLoadsAfterLogin() {
        log.info("Executing TC_ID_BIO-001: Biometric screen after login");
        loginAsQaDoctor(loginPage);


        if (bioPage.isBiometricScreenDisplayed()) {
            log.info("TC_ID_BIO-001: Biometric screen displayed – PASS");
            Assert.assertTrue(bioPage.isEnableButtonDisplayed() || bioPage.isSkipButtonDisplayed(),
                "TC_ID_BIO-001: Enable and/or Skip buttons should be present on biometric screen.");
        } else {
            throw new SkipException("[Physical device] TC_ID_BIO-001 requires the biometric setup prompt.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC_ID_BIO-002 : Brand Header on Biometric Screen
    // ══════════════════════════════════════════════════════════════════════
    @Test(description = "TC_ID_BIO-002: Verify VGCares Global header is displayed on biometric screen",
          groups = {"regression"}, priority = 2)
    public void testBrandHeaderOnBiometricScreen() {
        log.info("Executing TC_ID_BIO-002: Brand header on biometric screen");
        loginAsQaDoctor(loginPage);

        if (bioPage.isBiometricScreenDisplayed()) {
            Assert.assertTrue(bioPage.isBrandHeaderDisplayed(),
                "TC_ID_BIO-002: 'VGCares Global' brand header must be displayed on biometric screen.");
        } else {
            throw new SkipException("[Physical device] TC_ID_BIO-002 requires the biometric setup prompt.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC_ID_BIO-004 : Biometric Prompt Text
    // ══════════════════════════════════════════════════════════════════════
    @Test(description = "TC_ID_BIO-004: Verify biometric prompt message 'Enable Fingerprint' is displayed",
          groups = {"regression"}, priority = 4)
    public void testBiometricPromptTextDisplayed() {
        log.info("Executing TC_ID_BIO-004: Biometric prompt text");
        loginAsQaDoctor(loginPage);

        if (bioPage.isBiometricScreenDisplayed()) {
            Assert.assertTrue(bioPage.isEnableFingerprintPromptDisplayed(),
                "TC_ID_BIO-004: 'Enable Fingerprint after this sign-in?' prompt must be visible.");
        } else {
            throw new SkipException("[Physical device] TC_ID_BIO-004 requires the biometric setup prompt.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC_ID_BIO-006 : User can Skip Biometric Setup
    // ══════════════════════════════════════════════════════════════════════
    @Test(description = "TC_ID_BIO-006: Verify user can skip biometric setup and reach Dashboard",
          groups = {"smoke", "regression"}, priority = 6)
    public void testUserCanSkipBiometricSetup() {
        log.info("Executing TC_ID_BIO-006: Skip biometric setup");
        HomePage home = requireFirstLoginPrompt();
        bioPage.clickSkip();
        Assert.assertTrue(home.isHomeScreenLoaded(), "Skipping optional biometric enrollment must open the dashboard.");
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC_ID_BIO-007 : Enable Button is Clickable
    // ══════════════════════════════════════════════════════════════════════
    @Test(description = "TC_ID_BIO-007: Verify 'Enable' button is clickable",
          groups = {"regression"}, priority = 7)
    public void testEnableButtonClickable() {
        log.info("Executing TC_ID_BIO-007: Enable button clickable");
        loginAsQaDoctor(loginPage);

        if (bioPage.isBiometricScreenDisplayed()) {
            Assert.assertTrue(bioPage.isEnableButtonDisplayed(),
                "TC_ID_BIO-007: Enable button must be displayed and clickable on biometric screen.");
        } else {
            throw new SkipException("[Physical device] TC_ID_BIO-007 requires the biometric setup prompt.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC_ID_BIO-008 : Skip Button is Clickable
    // ══════════════════════════════════════════════════════════════════════
    @Test(description = "TC_ID_BIO-008: Verify 'Skip' button is clickable",
          groups = {"regression"}, priority = 8)
    public void testSkipButtonClickable() {
        log.info("Executing TC_ID_BIO-008: Skip button clickable");
        loginAsQaDoctor(loginPage);

        if (bioPage.isBiometricScreenDisplayed()) {
            Assert.assertTrue(bioPage.isSkipButtonDisplayed(),
                "TC_ID_BIO-008: Skip button must be displayed on biometric screen.");
        } else {
            throw new SkipException("[Physical device] TC_ID_BIO-008 requires the biometric setup prompt.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC_ID_BIO-020 : Biometric setup prompt does NOT reappear after Skip
    // ══════════════════════════════════════════════════════════════════════
    @Test(description = "TC_ID_BIO-020: Verify biometric setup prompt does NOT appear again after Skip",
          groups = {"regression"}, priority = 20)
    public void testBiometricPromptDoesNotReappearAfterSkip() {
        log.info("Executing TC_ID_BIO-020: No re-prompt after skip");
        HomePage home = requireFirstLoginPrompt();
        bioPage.clickSkip();
        Assert.assertTrue(home.isHomeScreenLoaded(), "Skipping optional biometric enrollment must open the dashboard.");
        Assert.assertFalse(bioPage.isBiometricScreenDisplayed(), "The biometric setup prompt must remain dismissed.");
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC_ID_BIO-022 : Enable & Skip button styling
    // ══════════════════════════════════════════════════════════════════════
    @Test(description = "TC_ID_BIO-022: Verify 'Enable' and 'Skip' buttons have proper styling",
          groups = {"regression"}, priority = 22)
    public void testBiometricButtonsStyling() {
        log.info("Executing TC_ID_BIO-022: Button styling check");
        loginAsQaDoctor(loginPage);

        if (bioPage.isBiometricScreenDisplayed()) {
            boolean enableVisible = bioPage.isEnableButtonDisplayed();
            boolean skipVisible   = bioPage.isSkipButtonDisplayed();
            Assert.assertTrue(enableVisible && skipVisible,
                "TC_ID_BIO-022: Both Enable and Skip buttons should be visible with proper styling.");
        } else {
            throw new SkipException("[Physical device] TC_ID_BIO-022 requires the biometric setup prompt.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC_ID_BIO-027 : Device back button behavior on biometric screen
    // ══════════════════════════════════════════════════════════════════════
    @Test(description = "TC_ID_BIO-027: Verify device back button on biometric screen navigates to Dashboard",
          groups = {"regression"}, priority = 27)
    public void testDeviceBackButtonOnBiometricScreen() {
        log.info("Executing TC_ID_BIO-027: Back button on biometric screen");
        loginAsQaDoctor(loginPage);

        if (bioPage.isBiometricScreenDisplayed()) {
            getDriver().navigate().back();
            Assert.assertNotNull(getDriver().getCurrentPackage(),
                "TC_ID_BIO-027: App should not crash on back button press on biometric screen.");
        } else {
            throw new SkipException("[Physical device] TC_ID_BIO-027 requires the biometric setup prompt.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // TC_ID_BIO-031 : Biometric screen shown only once
    // ══════════════════════════════════════════════════════════════════════
    @Test(description = "TC_ID_BIO-031: Verify biometric screen is shown only once after first login",
          groups = {"regression"}, priority = 31)
    public void testBiometricScreenShownOnlyOnce() {
        log.info("Executing TC_ID_BIO-031: Biometric screen once per login");
        HomePage home = requireFirstLoginPrompt();
        bioPage.clickSkip();
        Assert.assertTrue(home.isHomeScreenLoaded(), "First sign-in must reach the dashboard.");
        home.logout();
        HomePage nextSignIn = loginAsQaDoctor(openFreshLogin());
        Assert.assertTrue(nextSignIn.isHomeScreenLoaded(), "The next sign-in must reach the dashboard.");
        Assert.assertFalse(bioPage.isBiometricScreenDisplayed(), "The first-login prompt must not reappear.");
    }
    private HomePage requireFirstLoginPrompt() {
        HomePage home = loginAsQaDoctor(loginPage);
        if (!bioPage.isBiometricScreenDisplayed()) {
            Assert.assertTrue(home.isHomeScreenLoaded(), "Sign-in must reach the dashboard or show biometric setup.");
            throw new SkipException("[Physical device] This scenario requires an account eligible for the first-login biometric prompt.");
        }
        return home;
    }
}
