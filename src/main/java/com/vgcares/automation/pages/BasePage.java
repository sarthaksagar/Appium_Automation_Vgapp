package com.vgcares.automation.pages;

import com.vgcares.automation.utils.WaitUtils;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.List;

public abstract class BasePage {

    protected final Logger log = LogManager.getLogger(getClass());
    protected final AndroidDriver driver;

    public BasePage(AndroidDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("Driver instance cannot be null when instantiating page object.");
        }
        this.driver = driver;
    }

    protected WebElement waitForVisibility(By locator) {
        return WaitUtils.waitForVisibility(driver, locator);
    }

    protected WebElement waitForVisibility(By locator, int timeoutSeconds) {
        return WaitUtils.waitForVisibility(driver, locator, timeoutSeconds);
    }

    protected WebElement waitForClickable(By locator) {
        return WaitUtils.waitForClickable(driver, locator);
    }

    protected void click(By locator) {
        log.debug("Clicking element located by: {}", locator);
        try {
            waitForClickable(locator).click();
        } catch (ElementClickInterceptedException | TimeoutException e) {
            if (acceptPermissionIfPresent()) {
                waitForClickable(locator).click();
            } else {
                throw e;
            }
        }
        applyDemonstrationDelay();
    }

    protected void sendKeys(By locator, String text) {
        log.debug("Typing into element located by: {}", locator);
        WebElement element = waitForVisibility(locator);
        element.click();
        element.clear();
        element.sendKeys(text);
        applyDemonstrationDelay();
    }

    protected void clearAndSendKeys(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        element.click();
        element.clear();
        if (text != null && !text.isEmpty()) {
            element.sendKeys(text);
        }
        applyDemonstrationDelay();
    }

    /**
     * Centrally applies a demonstration delay when running in LEARNING MODE.
     * Allows beginners to observe screen transitions and UI interactions clearly on the physical device.
     */
    protected void applyDemonstrationDelay() {
        com.vgcares.automation.utils.ExecutionUtils.applyActionDelayIfLearning();
    }

    protected String getText(By locator) {
        WebElement element = waitForVisibility(locator);
        String text = element.getText();
        if (text == null || text.trim().isEmpty()) {
            text = element.getAttribute("content-desc");
        }
        return text != null ? text.trim() : "";
    }

    protected boolean isElementDisplayed(By locator) {
        return WaitUtils.isElementDisplayed(driver, locator, 10);
    }

    protected boolean isElementDisplayed(By locator, int timeoutSeconds) {
        return WaitUtils.isElementDisplayed(driver, locator, timeoutSeconds);
    }

    protected boolean waitForInvisibility(By locator, int timeoutSeconds) {
        return WaitUtils.waitForInvisibility(driver, locator, timeoutSeconds);
    }

    protected WebElement findElement(By locator, int timeoutSeconds) {
        return waitForVisibility(locator, timeoutSeconds);
    }

    protected List<WebElement> findElements(By locator) {
        return driver.findElements(locator);
    }

    protected boolean isElementDisplayedNow(By locator) {
        try {
            return driver.findElements(locator).stream().anyMatch(WebElement::isDisplayed);
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    public void hideKeyboard() {
        try {
            if (driver.isKeyboardShown()) {
                driver.hideKeyboard();
            }
        } catch (Exception ignored) {
            // Keyboard already dismissed or unavailable on current focus
        }
    }

    // ── System Permission Dialog Handler (e.g. Android 13 POST_NOTIFICATIONS) ──
    private static final By PERMISSION_ALLOW_BUTTON = By.xpath(
        "//*[contains(@resource-id, 'permissioncontroller:id/permission_allow_button')"
        + " or contains(@resource-id, 'permissioncontroller:id/permission_allow_foreground_only_button')"
        + " or contains(@resource-id, 'packageinstaller:id/permission_allow_button')]");

    public boolean acceptPermissionIfPresent() {
        try {
            List<WebElement> elements = driver.findElements(PERMISSION_ALLOW_BUTTON);
            if (!elements.isEmpty()) {
                WebElement el = elements.get(0);
                if (el.isDisplayed()) {
                    el.click();
                    log.info("Tapped Allow on the Android system permission dialog");
                    return true;
                }
            }
        } catch (StaleElementReferenceException ignored) {
            // Permission dialog not present or already handled
        }
        return false;
    }
}
