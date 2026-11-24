package com.vgcares.automation.tests.launch;

import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.pages.LaunchPage;
import com.vgcares.automation.pages.LaunchPage.Screen;
import com.vgcares.automation.utils.StepLogger;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Launch & Onboarding Test Suite
 * Covers Language selection, Welcome/Namaste presentation, Onboarding slides, and Get Started.
 */
public class LaunchTest extends BaseTest {

    private LaunchPage launchPage;
    private Screen screen;

    @BeforeClass(alwaysRun = true)
    public void setupLaunchPage() {
        launchPage = new LaunchPage(getDriver());
    }

    @Test(priority = 1, description = "TC_ID_LAUNCH-001: Verify app launches successfully and displays launch/auth UI",
          groups = {"smoke", "regression", "launch"})
    public void testAppLaunchInitialState() {
        StepLogger.step("Launch VGCares Global application on physical device");
        launchPage.acceptPermissionIfPresent();
        screen = launchPage.waitForLaunchScreen();
        StepLogger.pass("Application launched and active on screen: " + screen);
        Assert.assertNotNull(screen, "App must display a recognized screen upon launch.");
    }

    @Test(priority = 2, description = "TC_ID_LAUNCH-002: Select English and verify Continue opens onboarding",
          groups = {"smoke", "regression", "launch"}, dependsOnMethods = "testAppLaunchInitialState")
    public void testLanguageSelectionAndProceed() {
        launchPage.acceptPermissionIfPresent();
        if (screen != Screen.LANGUAGE) {
            StepLogger.info("App already past language screen; skipping language selection.");
            throw new SkipException("The device opened past language selection.");
        }

        StepLogger.step("Select English language option");
        launchPage.selectEnglish();
        StepLogger.pass("English language selected");

        StepLogger.step("Tap Continue button");
        launchPage.clickContinue();
        screen = launchPage.waitForScreen(Screen.WELCOME, Screen.AUTH);
        StepLogger.pass("Navigated from language selection to: " + screen);
    }

    @Test(priority = 3, description = "TC_ID_LAUNCH-003: Verify onboarding slides in order using Next",
          groups = {"smoke", "regression", "launch"}, dependsOnMethods = "testAppLaunchInitialState")
    public void testOnboardingWalkthroughSlides() {
        if (screen == Screen.AUTH || screen == Screen.GLOBAL_HEALTHCARE) {
            StepLogger.info("App already past slide walkthrough; skipping Next navigation.");
            throw new SkipException("The device opened past the slides that use Next.");
        }
        Assert.assertNotEquals(screen, Screen.LANGUAGE, "Language selection must finish before onboarding.");

        while (screen != Screen.GLOBAL_HEALTHCARE) {
            StepLogger.step("Advance from slide: " + screen);
            screen = launchPage.advanceToNextSlide(screen);
            StepLogger.pass("Onboarding advanced to slide: " + screen);
        }
    }

    @Test(priority = 4, description = "TC_ID_LAUNCH-004: Verify Global Healthcare and the Get Started button",
          groups = {"smoke", "regression", "launch"}, dependsOnMethods = "testAppLaunchInitialState")
    public void testGlobalHealthcareScreenAndGetStartedButton() {
        if (screen == Screen.AUTH) {
            StepLogger.info("App already past onboarding; skipping Global Healthcare slide check.");
            throw new SkipException("The device already completed onboarding.");
        }
        StepLogger.step("Verify final onboarding slide: Global Healthcare");
        Assert.assertEquals(screen, Screen.GLOBAL_HEALTHCARE, "Onboarding must reach Global Healthcare.");
        launchPage.waitForGetStartedButton();
        StepLogger.pass("Global Healthcare slide and Get Started button verified");
    }

    @Test(priority = 5, description = "TC_ID_LAUNCH-005: Tap Get Started and verify navigation to authentication",
          groups = {"smoke", "regression", "launch"}, dependsOnMethods = "testGlobalHealthcareScreenAndGetStartedButton")
    public void testGetStartedTransitionsToAuthScreen() {
        StepLogger.step("Tap 'Get Started' button");
        launchPage.clickGetStarted();
        screen = launchPage.waitForScreen(Screen.AUTH);
        StepLogger.pass("Successfully transitioned to Authentication screen (Login / Sign Up)");
    }

    @Test(priority = 6, description = "TC_ID_LAUNCH-006: Verify both Login and Sign Up tabs are enabled",
          groups = {"smoke", "regression", "launch"}, dependsOnMethods = "testAppLaunchInitialState")
    public void testAuthScreenReadyForNextModules() {
        StepLogger.step("Verify Login and Sign Up tabs are ready on Auth screen");
        launchPage.waitForAuthTabs();
        StepLogger.pass("Login and Sign Up tabs verified and clickable");
    }
}
