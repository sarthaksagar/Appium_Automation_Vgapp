package com.vgcares.automation.pages;

import com.vgcares.automation.utils.WaitUtils;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

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
        acceptPermissionIfPresent();
        try {
            waitForClickable(locator).click();
        } catch (Exception e) {
            if (acceptPermissionIfPresent()) {
                waitForClickable(locator).click();
            } else {
                throw e;
            }
        }
    }

    protected void sendKeys(By locator, String text) {
        log.debug("Typing into element located by: {}", locator);
        WebElement element = waitForVisibility(locator);
        element.click();
        element.clear();
        element.sendKeys(text);
    }

    protected void clearAndSendKeys(By locator, String text) {
        WebElement element = waitForVisibility(locator);
        element.click();
        element.clear();
        if (text != null && !text.isEmpty()) {
            element.sendKeys(text);
        }
    }

    protected String getText(By locator) {
        WebElement element = waitForVisibility(locator);
        String text = element.getText();
        if (text == null || text.trim().isEmpty()) {
            text = element.getAttribute("content-desc");
        }
        log.debug("Read text '{}' from element located by: {}", text, locator);
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

    public void hideKeyboard() {
        try {
            driver.hideKeyboard();
        } catch (Exception ignored) {
            // Keyboard may not be open
        }
    }

    // ── System Permission Dialog Handler (e.g. Android 13 POST_NOTIFICATIONS) ──
    private static final By PERMISSION_ALLOW_BUTTON = By.xpath(
        "//*[contains(@resource-id, 'permission_allow_button') or contains(@resource-id, 'permission_allow_foreground_only_button') " +
        "or @text='Allow' or @text='ALLOW' or @content-desc='Allow' or @content-desc='ALLOW' " +
        "or (contains(@resource-id, 'button1') and (@text='ALLOW' or @text='Allow'))]");

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
        } catch (Exception ignored) {
            // Permission dialog not present or already handled
        }
        return false;
    }
}
