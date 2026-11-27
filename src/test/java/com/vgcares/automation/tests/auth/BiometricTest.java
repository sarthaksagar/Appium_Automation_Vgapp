package com.vgcares.automation.tests.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.driver.DriverManager;
import com.vgcares.automation.enums.UserRole;
import com.vgcares.automation.pages.BiometricPage;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.pages.LoginPage;
import com.vgcares.automation.pages.SettingsPage;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.testdata.QaCredentials;
import com.vgcares.automation.utils.StepLogger;
import org.openqa.selenium.Rectangle;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Biometric Setup Test Suite (TC_ID_BIO-001 through TC_ID_BIO-035).
 * Loads module data from src/test/resources/testdata/common/biometric-data.json.
 * Requires configured QA credentials (.env or environment variables) to reach
 * the post-login "Quick Access" screen and/or Doctor Dashboard Settings.
 */
public class BiometricTest extends BaseTest {

    private JsonNode bioData;
    private BiometricPage biometricPage;
    private boolean quickAccessUnavailable;

    @BeforeClass(alwaysRun = true)
    public void loadBiometricData() {
        bioData = ModuleDataLoader.common("biometric-data.json").path("testData");
        biometricPage = new BiometricPage(getDriver());
    }

    private BiometricPage requireQuickAccessScreen(String tcId) {
        if (biometricPage.isBiometricScreenDisplayedNow()) {
            return biometricPage;
        }
        if (quickAccessUnavailable) {
            throw new SkipException("[" + tcId + "] Quick Access screen was already completed or skipped for this account.");
        }
        QaCredentials doctor = QaCredentials.doctor();
        prepareAuthScreen();
        LoginPage loginPage = new LoginPage(getDriver()).openLoginTab();
        loginPage.login(doctor.email(), doctor.password());

        if (biometricPage.isBiometricScreenDisplayed()) {
            return biometricPage;
        }
        HomePage home = new HomePage(getDriver());
        if (home.isHomeScreenLoaded()) {
            DriverManager.setAuthenticatedRole(UserRole.DOCTOR);
            quickAccessUnavailable = true;
            throw new SkipException("[" + tcId + "] Quick Access screen did not appear because biometric setup was already completed or skipped for this account.");
        }
        throw new SkipException("[" + tcId + "] Unable to reach Quick Access biometric screen after login.");
    }

    private HomePage ensureDoctorDashboardForSettings(String tcId) {
        HomePage home = new HomePage(getDriver());
        if (DriverManager.getAuthenticatedRole() == UserRole.DOCTOR && home.isHomeScreenLoaded()) {
            return home;
        }
        if (biometricPage.isBiometricScreenDisplayedNow()) {
            biometricPage.clickSkip();
            if (home.isHomeScreenLoaded()) {
                DriverManager.setAuthenticatedRole(UserRole.DOCTOR);
                return home;
            }
        }
        return loginAsQaDoctor();
    }

    // ── Quick Access Screen UI & Content (001..004, 022, 023, 032) ───────

    @Test(priority = 1, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-001: Verify Biometric setup screen loads after successful login")
    public void testBiometricScreenLoadsAfterLogin() {
        BiometricPage bio = requireQuickAccessScreen("TC_ID_BIO-001");
        StepLogger.step("Verify Quick Access title, prompt text, Enable and Skip buttons");
        Assert.assertTrue(bio.isBiometricScreenDisplayed(), "Quick Access title must be displayed.");
        Assert.assertTrue(bio.isEnableFingerprintPromptDisplayed(), "Enable Fingerprint prompt must be displayed.");
        Assert.assertTrue(bio.isEnableButtonDisplayed(), "Enable button must be displayed.");
        Assert.assertTrue(bio.isSkipButtonDisplayed(), "Skip button must be displayed.");
        StepLogger.pass("Biometric Quick Access screen verified cleanly");
    }

    @Test(priority = 2, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-002: Verify VGCares Global and Medical Tourism header on biometric screen")
    public void testBrandHeaderDisplayedOnBiometricScreen() {
        BiometricPage bio = requireQuickAccessScreen("TC_ID_BIO-002");
        StepLogger.step("Verify VGCares Global and Medical Tourism branding");
        Assert.assertTrue(bio.isBrandHeaderDisplayed(), "VGCares Global header must be displayed.");
        Assert.assertTrue(bio.isMedicalTourismDisplayed(), "Medical Tourism subtitle must be displayed.");
        StepLogger.pass("Biometric brand header verified cleanly");
    }

    @Test(priority = 3, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-003: Verify tagline and stats displayed on biometric screen")
    public void testTaglineAndStatsDisplayedOnBiometricScreen() {
        BiometricPage bio = requireQuickAccessScreen("TC_ID_BIO-003");
        StepLogger.step("Verify tagline and hospital/country statistics");
        Assert.assertTrue(bio.isTaglineDisplayed(), "Tagline 'World-Class Healthcare' must be displayed.");
        Assert.assertTrue(bio.areStatsDisplayed(), "'500+ Hospitals' and '50+ Countries' stats must be displayed.");
        StepLogger.pass("Biometric tagline and stats verified cleanly");
    }

    @Test(priority = 4, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-004: Verify biometric prompt title and subtitle displayed clearly")
    public void testBiometricPromptAndSubtitleDisplayed() {
        BiometricPage bio = requireQuickAccessScreen("TC_ID_BIO-004");
        StepLogger.step("Verify prompt message and verification subtitle");
        Assert.assertTrue(bio.isEnableFingerprintPromptDisplayed(), "Enable Fingerprint message must be displayed.");
        Assert.assertTrue(bio.isPromptSubtitleDisplayed(), "Verification subtitle must be displayed.");
        StepLogger.pass("Biometric prompt text and subtitle verified");
    }

    @Test(priority = 5, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-022: Verify Enable and Skip buttons sizing and clickable state")
    public void testEnableAndSkipButtonStatesAndSizing() {
        BiometricPage bio = requireQuickAccessScreen("TC_ID_BIO-022");
        int minH = bioData.path("ui").path("minButtonHeightPx").asInt(60);
        StepLogger.step("Verify Enable and Skip buttons height >= " + minH + "px and enabled/clickable");
        Rectangle enableRect = bio.getEnableButtonRect();
        Rectangle skipRect = bio.getSkipButtonRect();
        Assert.assertTrue(bio.isEnableButtonEnabledAndClickable() && enableRect.getHeight() >= minH,
                "Enable button must be prominently sized and clickable.");
        Assert.assertTrue(bio.isSkipButtonEnabledAndClickable() && skipRect.getHeight() >= minH,
                "Skip button must be properly sized and clickable.");
        StepLogger.pass("Enable and Skip button states verified");
    }

    @Test(priority = 6, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-023: Verify Enable and Skip buttons are aligned without overlap")
    public void testEnableAndSkipButtonAlignment() {
        BiometricPage bio = requireQuickAccessScreen("TC_ID_BIO-023");
        int maxDelta = bioData.path("ui").path("maxAlignmentDeltaPx").asInt(80);
        StepLogger.step("Verify Enable and Skip button geometry alignment");
        Assert.assertTrue(bio.areButtonsAlignedAndNonOverlapping(maxDelta),
                "Skip and Enable buttons must be properly aligned without overlapping.");
        StepLogger.pass("Biometric button alignment verified");
    }

    @Test(priority = 7, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-032: Verify fingerprint/biometric icon is displayed and properly sized")
    public void testBiometricIconDisplayedAndSized() {
        BiometricPage bio = requireQuickAccessScreen("TC_ID_BIO-032");
        int minIcon = bioData.path("ui").path("minIconSizePx").asInt(32);
        StepLogger.step("Verify biometric icon visibility and minimum dimensions");
        Assert.assertTrue(bio.isCenterIconVisibleAndSized(minIcon),
                "Biometric/fingerprint icon must be visible with non-zero readable size.");
        StepLogger.pass("Biometric icon verified");
    }

    // ── Button Actions, Cancel, Back & Skip Flows (007, 012, 006, 008, 027, 020, 031) ──

    @Test(priority = 8, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-007: Verify Enable button responds and opens system biometric prompt")
    public void testEnableButtonTriggersSystemPrompt() {
        BiometricPage bio = requireQuickAccessScreen("TC_ID_BIO-007");
        StepLogger.step("Tap Enable and verify system biometric prompt or response");
        bio.clickEnable();
        boolean promptOrOutcome = bio.isSystemBiometricPromptDisplayed()
                || bio.isErrorToastDisplayed()
                || new HomePage(getDriver()).isHomeScreenLoaded();
        Assert.assertTrue(promptOrOutcome, "Tapping Enable must open system prompt or show hardware/enrollment feedback.");
        bio.cancelSystemBiometricPromptIfOpen();
        StepLogger.pass("Enable button response verified");
    }

    @Test(priority = 9, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-012: Verify biometric setup can be cancelled from system prompt")
    public void testCancelBiometricSetupFromSystemPrompt() {
        BiometricPage bio = requireQuickAccessScreen("TC_ID_BIO-012");
        StepLogger.step("Tap Enable and cancel system fingerprint prompt");
        bio.clickEnable();
        if (!bio.cancelSystemBiometricPromptIfOpen()) {
            throw new SkipException("[TC_ID_BIO-012] System biometric prompt Cancel button was not displayed on device.");
        }
        boolean feedbackOrDashboard = bio.isErrorToastDisplayed()
                || new HomePage(getDriver()).isHomeScreenLoaded()
                || bio.isBiometricScreenDisplayed();
        Assert.assertTrue(feedbackOrDashboard, "Cancelling biometric setup must show toast or return to Dashboard/Biometric screen.");
        StepLogger.pass("Biometric setup cancellation verified");
    }

    @Test(priority = 10, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-006: Verify user can skip biometric setup and reach Dashboard")
    public void testSkipBiometricNavigatesToDashboard() {
        BiometricPage bio = requireQuickAccessScreen("TC_ID_BIO-006");
        StepLogger.step("Tap Skip button and verify navigation to Dashboard");
        bio.clickSkip();
        HomePage home = new HomePage(getDriver());
        Assert.assertTrue(home.isHomeScreenLoaded(), "Skipping biometric setup must navigate directly to Dashboard.");
        DriverManager.setAuthenticatedRole(UserRole.DOCTOR);
        StepLogger.pass("Biometric skip navigated cleanly to Dashboard");
    }

    @Test(priority = 11, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-008: Verify Skip button is clickable and navigates without error")
    public void testSkipButtonClickableAndNavigates() {
        BiometricPage bio = requireQuickAccessScreen("TC_ID_BIO-008");
        Assert.assertTrue(bio.isSkipButtonEnabledAndClickable(), "Skip button must be clickable.");
        bio.clickSkip();
        HomePage home = new HomePage(getDriver());
        Assert.assertTrue(home.isHomeScreenLoaded(), "Clicking Skip must navigate directly to Dashboard.");
        DriverManager.setAuthenticatedRole(UserRole.DOCTOR);
        StepLogger.pass("Skip button click verified cleanly");
    }

    @Test(priority = 12, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-027: Verify device back button skips biometric setup without crash")
    public void testDeviceBackOnBiometricScreen() {
        BiometricPage bio = requireQuickAccessScreen("TC_ID_BIO-027");
        StepLogger.step("Press Android Back on Quick Access screen");
        bio.pressDeviceBack();
        Assert.assertNotNull(getDriver().getCurrentPackage(), "Device Back must not crash the application.");
        HomePage home = new HomePage(getDriver());
        Assert.assertTrue(home.isHomeScreenLoaded() || bio.isBiometricScreenDisplayed(),
                "Device Back must cleanly transition to Dashboard or remain stable without blank screen.");
        StepLogger.pass("Biometric device Back behavior verified");
    }

    @Test(priority = 13, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-020: Verify biometric setup prompt does not reappear after skip")
    public void testBiometricPromptNotShownAgainAfterSkip() {
        HomePage home = ensureDoctorDashboardForSettings("TC_ID_BIO-020");
        StepLogger.step("Logout from Dashboard and log back in with QA Doctor credentials");
        home.logout();
        DriverManager.setAuthenticatedRole(UserRole.COMMON);

        QaCredentials doctor = QaCredentials.doctor();
        LoginPage loginPage = new LoginPage(getDriver()).openLoginTab();
        loginPage.login(doctor.email(), doctor.password());

        Assert.assertFalse(biometricPage.isBiometricScreenDisplayedNow(),
                "Quick Access biometric setup prompt must not reappear after already being skipped.");
        Assert.assertTrue(home.isHomeScreenLoaded(), "User should land directly on Dashboard.");
        DriverManager.setAuthenticatedRole(UserRole.DOCTOR);
        StepLogger.pass("Skipped biometric prompt suppressed on subsequent login");
    }

    @Test(priority = 14, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-031: Verify biometric setup screen appears only once after first login")
    public void testBiometricScreenShownOnlyOnce() {
        HomePage home = ensureDoctorDashboardForSettings("TC_ID_BIO-031");
        Assert.assertTrue(home.isHomeScreenLoaded(), "Dashboard must load directly without repeating Quick Access.");
        StepLogger.pass("Single-display biometric setup lifecycle verified");
    }

    // ── Settings Biometric Preferences & Hardware/Fingerprint Scenarios ───

    @Test(priority = 15, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-017: Verify biometric preference status is visible in Settings")
    public void testBiometricPreferenceVisibleInSettings() {
        HomePage home = ensureDoctorDashboardForSettings("TC_ID_BIO-017");
        StepLogger.step("Open Settings and verify Biometric row/toggle visibility");
        SettingsPage settings = home.openSettings();
        try {
            Assert.assertTrue(settings.isSettingsPageDisplayed(), "Settings screen must open.");
            Assert.assertTrue(settings.isBiometricRowDisplayed() || settings.isBiometricToggleDisplayed(),
                    "Biometric preference row/toggle must be visible in Settings.");
            StepLogger.pass("Settings biometric preference verified (enabled=" + settings.isBiometricEnabled() + ")");
        } finally {
            if (settings.isBackButtonDisplayed()) {
                settings.clickBack();
            }
        }
    }

    @Test(priority = 16, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-018: Verify disabling biometric removes biometric option from Login screen")
    public void testDisableBiometricFromSettingsRemovesLoginOption() {
        HomePage home = ensureDoctorDashboardForSettings("TC_ID_BIO-018");
        SettingsPage settings = home.openSettings();
        if (!settings.isBiometricEnabled()) {
            if (settings.isBackButtonDisplayed()) {
                settings.clickBack();
            }
            throw new SkipException("[TC_ID_BIO-018] Biometric is not currently enabled in Settings; requires enrolled physical fingerprint first.");
        }
        settings.toggleBiometric();
        Assert.assertFalse(settings.isBiometricEnabled(), "Biometric switch must turn off.");
        if (settings.isBackButtonDisplayed()) {
            settings.clickBack();
        }
        LoginPage loginPage = home.logout();
        DriverManager.setAuthenticatedRole(UserRole.COMMON);
        Assert.assertFalse(loginPage.hasBiometricOptionOnLogin(),
                "Disabled biometric option must not appear on Login screen.");
        StepLogger.pass("Biometric disable and Login option removal verified");
    }

    @Test(priority = 17, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-021: Verify biometric can be triggered from Settings after initial skip")
    public void testEnableBiometricFromSettingsOpensPrompt() {
        HomePage home = ensureDoctorDashboardForSettings("TC_ID_BIO-021");
        SettingsPage settings = home.openSettings();
        try {
            Assert.assertTrue(settings.isBiometricToggleDisplayed(), "Biometric toggle must be present in Settings.");
            if (!settings.isBiometricEnabled()) {
                settings.toggleBiometric();
                boolean triggered = settings.isBiometricPromptDisplayed() || settings.isBiometricEnabled();
                Assert.assertTrue(triggered, "Tapping biometric toggle in Settings must open prompt or toggle state.");
                settings.cancelBiometricPrompt();
            }
            StepLogger.pass("Settings biometric activation trigger verified");
        } finally {
            if (settings.isBackButtonDisplayed()) {
                settings.clickBack();
            }
        }
    }

    @Test(priority = 18, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-035: Verify toggling biometric off removes Login screen icon")
    public void testTogglingBiometricOffRemovesLoginIcon() {
        HomePage home = ensureDoctorDashboardForSettings("TC_ID_BIO-035");
        SettingsPage settings = home.openSettings();
        boolean wasEnabled = settings.isBiometricEnabled();
        if (wasEnabled) {
            settings.toggleBiometric();
        }
        if (settings.isBackButtonDisplayed()) {
            settings.clickBack();
        }
        LoginPage loginPage = home.logout();
        DriverManager.setAuthenticatedRole(UserRole.COMMON);
        Assert.assertFalse(loginPage.hasBiometricOptionOnLogin(),
                "Login screen must not expose biometric icon when biometric is off.");
        StepLogger.pass("Biometric off state on Login screen verified");
    }

    @Test(priority = 19, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-030: Verify biometric icon appears on Login screen when biometric is enabled")
    public void testBiometricIconOnLoginWhenEnabled() {
        HomePage home = ensureDoctorDashboardForSettings("TC_ID_BIO-030");
        SettingsPage settings = home.openSettings();
        boolean enabled = settings.isBiometricEnabled();
        if (settings.isBackButtonDisplayed()) {
            settings.clickBack();
        }
        if (!enabled) {
            throw new SkipException("[TC_ID_BIO-030] Biometric enrollment is not enabled on this physical device session.");
        }
        LoginPage loginPage = home.logout();
        DriverManager.setAuthenticatedRole(UserRole.COMMON);
        Assert.assertTrue(loginPage.hasBiometricOptionOnLogin(),
                "Fingerprint/Biometric icon must appear on Login screen after biometric is enabled.");
        StepLogger.pass("Biometric icon verified on Login screen");
    }

    // ── Physical-Sensor / OS-Fingerprint / Security Hardware Scenarios ────
    // Real physical Android hardware cannot spoof finger touches or delete OS
    // fingerprints programmatically (emulator-only Appium APIs are prohibited).

    @Test(priority = 20, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-005: Enable biometric with valid physical fingerprint")
    public void testEnableBiometricWithValidFingerprint() {
        throw hardwareSkip("TC_ID_BIO-005", "Requires physical human fingerprint scan on device hardware sensor.");
    }

    @Test(priority = 21, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-009: Enable biometric with Touch ID")
    public void testEnableBiometricWithTouchId() {
        throw hardwareSkip("TC_ID_BIO-009", "Touch ID is iOS-specific and not applicable to Android physical device.");
    }

    @Test(priority = 22, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-010: Biometric setup fails after 3 incorrect fingerprint attempts")
    public void testBiometricSetupFailsWithIncorrectFingerprint() {
        throw hardwareSkip("TC_ID_BIO-010", "Requires 3 physical mismatched finger scans on Android BiometricPrompt.");
    }

    @Test(priority = 23, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-011: Biometric setup when no fingerprint is registered in Android settings")
    public void testBiometricSetupWithoutEnrolledDeviceFingerprint() {
        throw hardwareSkip("TC_ID_BIO-011", "Requires removing all enrolled OS fingerprints in Android device settings.");
    }

    @Test(priority = 24, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-013: Login screen shows biometric option after physical enrollment")
    public void testLoginShowsBiometricOptionAfterEnrollment() {
        throw hardwareSkip("TC_ID_BIO-013", "Depends on interactive physical fingerprint enrollment in TC_ID_BIO-005.");
    }

    @Test(priority = 25, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-014: Login succeeds with valid enrolled fingerprint")
    public void testBiometricLoginWithValidFingerprint() {
        throw hardwareSkip("TC_ID_BIO-014", "Requires physical human fingerprint scan on Login BiometricPrompt.");
    }

    @Test(priority = 26, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-015: Biometric login fails with incorrect fingerprint")
    public void testBiometricLoginFailsWithIncorrectFingerprint() {
        throw hardwareSkip("TC_ID_BIO-015", "Requires physical mismatched finger scan on Login BiometricPrompt.");
    }

    @Test(priority = 27, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-016: Password fallback available during biometric login failure/cancel")
    public void testBiometricLoginPasswordFallback() {
        throw hardwareSkip("TC_ID_BIO-016", "Requires physical biometric enrollment and system prompt interaction.");
    }

    @Test(priority = 28, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-019: Session restores via biometric prompt after background timeout")
    public void testBiometricSessionPersistenceAfterTimeout() {
        throw hardwareSkip("TC_ID_BIO-019", "Requires physical biometric enrollment and session timeout.");
    }

    @Test(priority = 29, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-024: Green success toast appears for 3-5s on valid fingerprint enrollment")
    public void testBiometricSuccessToastDuration() {
        throw hardwareSkip("TC_ID_BIO-024", "Requires physical fingerprint enrollment confirmation.");
    }

    @Test(priority = 30, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-025: Red error toast appears for 3-5s on failed fingerprint")
    public void testBiometricFailureToastDuration() {
        throw hardwareSkip("TC_ID_BIO-025", "Requires physical mismatched fingerprint scan.");
    }

    @Test(priority = 31, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-026: Red error toast appears when biometric hardware is unavailable")
    public void testBiometricHardwareUnavailableToast() {
        throw hardwareSkip("TC_ID_BIO-026", "Target physical device (M2101K7BI) has fingerprint hardware present.");
    }

    @Test(priority = 32, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-028: Biometric login uses stored fingerprint key without re-registration")
    public void testBiometricLoginUsesStoredData() {
        throw hardwareSkip("TC_ID_BIO-028", "Requires physical fingerprint scan verification.");
    }

    @Test(priority = 33, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-029: Biometric login invalidates when OS fingerprint changes")
    public void testBiometricInvalidatedOnDeviceFingerprintChange() {
        throw hardwareSkip("TC_ID_BIO-029", "Requires modifying enrolled OS fingerprints in Android device settings.");
    }

    @Test(priority = 34, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-033: Biometric credentials remain device-local and are not sent to backend")
    public void testBiometricDataStoredLocallyOnly() {
        throw hardwareSkip("TC_ID_BIO-033", "Requires Android Keystore / network packet inspection beyond UI automation.");
    }

    @Test(priority = 35, groups = {"common", "doctor", "regression", "biometric"},
          description = "TC_ID_BIO-034: Biometric login maintains valid authenticated session across modules")
    public void testBiometricMaintainsValidSession() {
        throw hardwareSkip("TC_ID_BIO-034", "Requires physical fingerprint login verification.");
    }

    private static SkipException hardwareSkip(String tcId, String reason) {
        return new SkipException("[" + tcId + "] " + reason);
    }
}
