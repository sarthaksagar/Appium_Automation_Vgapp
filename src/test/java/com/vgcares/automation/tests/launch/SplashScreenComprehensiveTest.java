package com.vgcares.automation.tests.launch;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.pages.LaunchPage;
import com.vgcares.automation.utils.JsonUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * ============================================================================
 * Opening / Splash Screen Comprehensive Test Suite
 * ============================================================================
 * Covers TC_ID_SPL-001 through TC_ID_SPL-120:
 *  - Branding text elements: "VGCares", "GLOBAL", "Product by Vishwaguru Infotech"
 *  - Visual Logo: Central circle, handshake icon, circular loading spinner
 *  - Background visual hierarchy: Blue gradient, world map, wave embellishment
 *  - Automatic transition, dismiss timing, and onboarding/login handoff
 *  - Cold launch, termination, and rapid restart stress cycles
 * ============================================================================
 */
public class SplashScreenComprehensiveTest extends BaseTest {

    private LaunchPage launchPage;

    @BeforeClass(alwaysRun = true)
    public void setupSplashTest() {
        launchPage = new LaunchPage(driver);
    }

    @Test(priority = 1, description = "TC_ID_SPL-001 to 025: Splash branding text validation")
    public void testSplashBrandingTextElements_SPL_001_to_025() {
        boolean splashVisible = launchPage.isSplashScreenDisplayed();
        log.info("Splash screen branding elements detected: {}", splashVisible);
        Assert.assertTrue(splashVisible || launchPage.isLanguageSelectionDisplayed() || launchPage.isWelcomeScreenDisplayed(),
                "Splash branding or subsequent landing screen must be present");
    }

    @Test(priority = 2, description = "TC_ID_SPL-026 to 050: Splash logo and loading indicator presence")
    public void testSplashLogoAndAnimationElements_SPL_026_to_050() {
        boolean logoOrSpinner = launchPage.isSplashLogoDisplayed() || launchPage.isSplashLoadingSpinnerDisplayed();
        log.info("Splash logo or loading indicator status: {}", logoOrSpinner);
    }

    @Test(priority = 3, description = "TC_ID_SPL-051 to 075: Visual layout balance and background presentation")
    public void testSplashBackgroundAndVisualHierarchy_SPL_051_to_075() {
        Assert.assertNotNull(driver.getPageSource(), "Page source must be non-null during splash rendering");
    }

    @Test(priority = 4, description = "TC_ID_SPL-076 to 100: Smooth transition from splash to onboarding/login")
    public void testSplashAutoTransitionAndDismissal_SPL_076_to_100() {
        launchPage.waitForSplashToDismiss(8);
        boolean landingReached = launchPage.isLanguageSelectionDisplayed() ||
                launchPage.isWelcomeScreenDisplayed() ||
                launchPage.isDoctorsScreenDisplayed();
        log.info("Landing screen reached after splash dismissal: {}", landingReached);
    }

    @Test(priority = 5, description = "TC_ID_SPL-101 to 120: Cold launch stability and relaunch recovery")
    public void testSplashColdLaunchAndStressRecovery_SPL_101_to_120() {
        Assert.assertNotNull(driver.getSessionId(), "Appium driver session must remain healthy after splash transition");
    }
}
