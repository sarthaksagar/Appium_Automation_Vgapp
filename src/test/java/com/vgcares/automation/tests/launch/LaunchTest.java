package com.vgcares.automation.tests.launch;

import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.pages.LaunchPage;
import com.vgcares.automation.pages.LaunchPage.Screen;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/** Verifies the first launch through the Login and Sign Up screen in one Appium session. */
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
        launchPage.acceptPermissionIfPresent();
        screen = launchPage.waitForLaunchScreen();
        log.info("App opened on {}", screen);
    }

    @Test(priority = 2, description = "TC_ID_LAUNCH-002: Select English and verify Continue opens onboarding",
          groups = {"smoke", "regression", "launch"}, dependsOnMethods = "testAppLaunchInitialState")
    public void testLanguageSelectionAndProceed() {
        launchPage.acceptPermissionIfPresent();
        if (screen != Screen.LANGUAGE) {
            throw new SkipException("The device opened past language selection.");
        }

        launchPage.selectEnglish().clickContinue();
        screen = launchPage.waitForScreen(Screen.WELCOME, Screen.AUTH);
    }

    @Test(priority = 3, description = "TC_ID_LAUNCH-003: Verify onboarding slides in order using Next",
          groups = {"smoke", "regression", "launch"}, dependsOnMethods = "testAppLaunchInitialState")
    public void testOnboardingWalkthroughSlides() {
        if (screen == Screen.AUTH || screen == Screen.GLOBAL_HEALTHCARE) {
            throw new SkipException("The device opened past the slides that use Next.");
        }
        Assert.assertNotEquals(screen, Screen.LANGUAGE, "Language selection must finish before onboarding.");

        while (screen != Screen.GLOBAL_HEALTHCARE) {
            screen = launchPage.advanceToNextSlide(screen);
            log.info("Onboarding advanced to {}", screen);
        }
    }

    @Test(priority = 4, description = "TC_ID_LAUNCH-004: Verify Global Healthcare and the Get Started button",
          groups = {"smoke", "regression", "launch"}, dependsOnMethods = "testAppLaunchInitialState")
    public void testGlobalHealthcareScreenAndGetStartedButton() {
        if (screen == Screen.AUTH) {
            throw new SkipException("The device already completed onboarding.");
        }
        Assert.assertEquals(screen, Screen.GLOBAL_HEALTHCARE, "Onboarding must reach Global Healthcare.");
        launchPage.waitForGetStartedButton();
    }

    @Test(priority = 5, description = "TC_ID_LAUNCH-005: Tap Get Started and verify navigation to authentication",
          groups = {"smoke", "regression", "launch"}, dependsOnMethods = "testGlobalHealthcareScreenAndGetStartedButton")
    public void testGetStartedTransitionsToAuthScreen() {
        launchPage.clickGetStarted();
        screen = launchPage.waitForScreen(Screen.AUTH);
    }

    @Test(priority = 6, description = "TC_ID_LAUNCH-006: Verify both Login and Sign Up tabs are enabled",
          groups = {"smoke", "regression", "launch"}, dependsOnMethods = "testAppLaunchInitialState")
    public void testAuthScreenReadyForNextModules() {
        Assert.assertEquals(screen, Screen.AUTH, "The app must reach the Login and Sign Up screen.");
        launchPage.waitForAuthTabs();
    }
}
