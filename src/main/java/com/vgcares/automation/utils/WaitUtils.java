package com.vgcares.automation.utils;

import com.vgcares.automation.constants.FrameworkConstants;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public final class WaitUtils {

    private WaitUtils() {}

    public static WebDriverWait getWait(AndroidDriver driver, int timeoutInSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds), Duration.ofMillis(FrameworkConstants.POLLING_INTERVAL_MILLIS));
    }

    public static WebDriverWait getWait(AndroidDriver driver) {
        return getWait(driver, FrameworkConstants.DEFAULT_EXPLICIT_WAIT);
    }

    public static WebElement waitForVisibility(AndroidDriver driver, By locator) {
        return getWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForVisibility(AndroidDriver driver, By locator, int timeoutSeconds) {
        return getWait(driver, timeoutSeconds).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static WebElement waitForClickable(AndroidDriver driver, By locator) {
        return getWait(driver).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForClickable(AndroidDriver driver, By locator, int timeoutSeconds) {
        return getWait(driver, timeoutSeconds).until(ExpectedConditions.elementToBeClickable(locator));
    }

    public static WebElement waitForPresence(AndroidDriver driver, By locator) {
        return getWait(driver).until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static boolean waitForInvisibility(AndroidDriver driver, By locator, int timeoutSeconds) {
        return getWait(driver, timeoutSeconds).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static boolean isElementDisplayed(AndroidDriver driver, By locator, int timeoutSeconds) {
        try {
            return waitForVisibility(driver, locator, timeoutSeconds).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public static List<WebElement> waitForAllVisible(AndroidDriver driver, By locator) {
        return getWait(driver).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }
}
