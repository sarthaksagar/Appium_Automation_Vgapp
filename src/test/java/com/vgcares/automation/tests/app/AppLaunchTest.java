package com.vgcares.automation.tests.app;

import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.config.ConfigManager;
import com.vgcares.automation.pages.LaunchPage;
import com.vgcares.automation.pages.SignUpPage;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class AppLaunchTest extends BaseTest {

    @Test(description = "TC_APP_001: Verify application launches successfully on real Android phone", groups = {"smoke", "regression"})
    public void testAppLaunch() {
        log.info("Executing App Launch Smoke Test on Physical Device");

        Assert.assertNotNull(getDriver(), "Driver must be successfully initialized.");

        String expectedPackage = ConfigManager.get("appPackage", "com.vg.vgcaresglobal.qa");

        // Ensure app is in the foreground
        try {
            getDriver().activateApp(expectedPackage);
        } catch (Exception e) {
            log.warn("activateApp notice: {}", e.getMessage());
        }

        // Wait up to 15 seconds for app package to settle in foreground (bypassing launcher transition)
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(15));
        LaunchPage launchPage = new LaunchPage(getDriver());
        boolean packageLoaded = wait.until(d -> !launchPage.acceptPermissionIfPresent()
            && expectedPackage.equals(getDriver().getCurrentPackage()));

        String currentPackage = getDriver().getCurrentPackage();
        log.info("App settled. Current Package on Device: {}", currentPackage);

        Assert.assertTrue(packageLoaded, "Launched application package should be " + expectedPackage);
        Assert.assertEquals(currentPackage, expectedPackage, 
                "Launched application package does not match expected QA package.");

        // Verify initial UI screen is rendered
        SignUpPage signUpPage = new SignUpPage(getDriver());
        Assert.assertTrue(signUpPage.isCreateAccountButtonDisplayed() || getDriver().getPageSource().contains("vgcares"),
                "Application UI elements should be visible on the screen.");
    }
}

