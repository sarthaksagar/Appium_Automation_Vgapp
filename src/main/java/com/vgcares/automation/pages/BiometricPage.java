package com.vgcares.automation.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

/**
 * Page Object for Biometric Setup screen (Quick Access screen shown after first successful login).
 * Covers TC_ID_BIO-001 through TC_ID_BIO-038.
 */
public class BiometricPage extends BasePage {

    // ── UI Elements ───────────────────────────────────────────────────────
    private final By quickAccessTitle  = By.xpath("//*[contains(@text,'Quick Access') or contains(@content-desc,'Quick Access')]");
    private final By enableFingerprintMsg = By.xpath(
            "//*[contains(@text,'Enable Fingerprint') or contains(@content-desc,'Enable Fingerprint')]");
    private final By enableButton      = By.xpath(
            "//android.widget.Button[contains(@content-desc,'Enable') or contains(@text,'Enable')]");
    private final By skipButton        = By.xpath(
            "//android.widget.Button[contains(@content-desc,'Skip') or contains(@text,'Skip')]");
    private final By brandHeader       = By.xpath(
            "//*[contains(@text,'VGCares Global') or contains(@content-desc,'VGCares Global')]");

    // ── Toast Messages ─────────────────────────────────────────────────────
    private final By successToast = By.xpath(
            "//*[contains(@text,'Fingerprint enabled') or contains(@content-desc,'Fingerprint enabled') " +
            "or contains(@text,'enabled successfully') or contains(@content-desc,'enabled successfully')]");
    private final By errorToast = By.xpath(
            "//*[contains(@text,'failed') or contains(@content-desc,'failed') " +
            "or contains(@text,'not available') or contains(@content-desc,'not available') " +
            "or contains(@text,'cancelled') or contains(@content-desc,'cancelled')]");

    public BiometricPage(AndroidDriver driver) {
        super(driver);
    }

    // ── Actions ──────────────────────────────────────────────────────────
    public void clickEnable() {
        log.info("Biometric: Clicking Enable button");
        click(enableButton);
    }

    public void clickSkip() {
        log.info("Biometric: Clicking Skip button");
        click(skipButton);
    }

    // ── State Checks ─────────────────────────────────────────────────────
    public boolean isBiometricScreenDisplayed() {
        return isElementDisplayed(quickAccessTitle, 10);
    }

    public boolean isEnableButtonDisplayed() {
        return isElementDisplayed(enableButton, 8);
    }

    public boolean isSkipButtonDisplayed() {
        return isElementDisplayed(skipButton, 8);
    }

    public boolean isBrandHeaderDisplayed() {
        return isElementDisplayed(brandHeader, 8);
    }

    public boolean isEnableFingerprintPromptDisplayed() {
        return isElementDisplayed(enableFingerprintMsg, 8);
    }

    public boolean isSuccessToastDisplayed() {
        return isElementDisplayed(successToast, 8);
    }

    public boolean isErrorToastDisplayed() {
        return isElementDisplayed(errorToast, 8);
    }

    /**
     * If the biometric setup screen is showing, skip it to proceed to Dashboard.
     */
    public void skipIfPresent() {
        try {
            if (isBiometricScreenDisplayed()) {
                log.info("Biometric setup screen detected – skipping.");
                clickSkip();
            }
        } catch (Exception e) {
            log.debug("Biometric screen not present or already dismissed: {}", e.getMessage());
        }
    }
}
