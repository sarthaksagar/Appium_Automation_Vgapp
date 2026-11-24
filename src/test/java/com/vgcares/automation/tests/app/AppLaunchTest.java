package com.vgcares.automation.tests.app;

import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.config.ConfigManager;
import com.vgcares.automation.pages.LaunchPage;
import com.vgcares.automation.utils.WaitUtils;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class AppLaunchTest extends BaseTest {
    @Test(description = "TC_APP_001: Verify the VG Cares QA app launches on a physical Android phone",
            groups = {"smoke", "regression"})
    public void testAppLaunch() {
        String expectedPackage = ConfigManager.get("appPackage", "");
        LaunchPage launchPage = new LaunchPage(getDriver());
        String currentActivity = WaitUtils.getWait(getDriver(), 15).until(d -> {
            launchPage.acceptPermissionIfPresent();
            if (expectedPackage.equals(getDriver().getCurrentPackage())
                    && !d.findElements(By.id("android:id/content")).isEmpty()) {
                return getDriver().currentActivity();
            }
            return null;
        });
        Assert.assertFalse(currentActivity.isBlank(), "VG Cares must have a foreground activity.");
        log.info("VG Cares foreground package {} and activity {}", expectedPackage, currentActivity);
    }
}
