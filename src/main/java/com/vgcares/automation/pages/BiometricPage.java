package com.vgcares.automation.pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;

/**
 * Page Object for Biometric Setup screen ("Quick Access" screen shown after initial login).
 * Covers TC_ID_BIO-001 through TC_ID_BIO-035.
 */
public class BiometricPage extends BasePage {

    // ── UI Elements ───────────────────────────────────────────────────────
    private final By quickAccessTitle = By.xpath(
            "//*[contains(@text,'Quick Access') or contains(@content-desc,'Quick Access')]");
    private final By brandHeader = By.xpath(
            "//*[contains(@text,'VGCares Global') or contains(@content-desc,'VGCares Global')]");
    private final By medicalTourismSubtitle = By.xpath(
            "//*[contains(@text,'Medical Tourism') or contains(@content-desc,'Medical Tourism')]");
    private final By taglineText = By.xpath(
            "//*[contains(@text,'World-Class Healthcare') or contains(@content-desc,'World-Class Healthcare')]");
    private final By hospitalsStat = By.xpath(
            "//*[contains(@text,'500+ Hospitals') or contains(@content-desc,'500+ Hospitals')]");
    private final By countriesStat = By.xpath(
            "//*[contains(@text,'50+ Countries') or contains(@content-desc,'50+ Countries')]");
    private final By enableFingerprintMsg = By.xpath(
            "//*[contains(@text,'Enable Fingerprint') or contains(@content-desc,'Enable Fingerprint')]");
    private final By promptSubtitleText = By.xpath(
            "//*[contains(@text,'verify once now') or contains(@content-desc,'verify once now')"
                    + " or contains(@text,'instant access') or contains(@content-desc,'instant access')]");
    private final By biometricCenterIcon = By.xpath(
            "//android.widget.ImageView | //*[contains(@content-desc,'fingerprint')"
                    + " or contains(@content-desc,'Fingerprint') or contains(@content-desc,'biometric')]");

    private final By enableButton = By.xpath(
            "//*[@clickable='true' and (contains(@content-desc,'Enable') or @text='Enable')]");
    private final By skipButton = By.xpath(
            "//*[@clickable='true' and (contains(@content-desc,'Skip') or @text='Skip')]");

    // ── System Biometric Prompt & Cancellation ────────────────────────────
    private final By systemPromptContainer = By.xpath(
            "//*[contains(@package,'systemui') or contains(@package,'biometric')"
                    + " or contains(@text,'Touch the fingerprint') or contains(@text,'Confirm fingerprint')]");
    private final By systemPromptCancel = By.xpath(
            "//*[@clickable='true' and (contains(@text,'Cancel') or contains(@content-desc,'Cancel')"
                    + " or contains(@text,'Use password') or contains(@content-desc,'Use password'))]");

    // ── Toast Messages ────────────────────────────────────────────────────
    private final By successToast = By.xpath(
            "//*[contains(@text,'Fingerprint enabled') or contains(@content-desc,'Fingerprint enabled')"
                    + " or contains(@text,'enabled successfully') or contains(@content-desc,'enabled successfully')]");
    private final By errorToast = By.xpath(
            "//*[contains(@text,'failed') or contains(@content-desc,'failed')"
                    + " or contains(@text,'not available') or contains(@content-desc,'not available')"
                    + " or contains(@text,'cancelled') or contains(@content-desc,'cancelled')"
                    + " or contains(@text,'No fingerprint') or contains(@content-desc,'No fingerprint')]");

    public BiometricPage(AndroidDriver driver) {
        super(driver);
    }

    // ── Actions ───────────────────────────────────────────────────────────

    public BiometricPage clickEnable() {
        log.info("Biometric: Clicking Enable button");
        click(enableButton);
        return this;
    }

    public BiometricPage clickSkip() {
        log.info("Biometric: Clicking Skip button");
        click(skipButton);
        return this;
    }

    public boolean cancelSystemBiometricPromptIfOpen() {
        if (isElementDisplayed(systemPromptCancel, 4)) {
            click(systemPromptCancel);
            return true;
        }
        return false;
    }

    public void pressDeviceBack() {
        log.info("Biometric: Pressing Android device back button");
        driver.pressKey(new KeyEvent(AndroidKey.BACK));
    }

    // ── State Checks ──────────────────────────────────────────────────────

    public boolean isBiometricScreenDisplayed() {
        return isElementDisplayed(quickAccessTitle, 6);
    }

    public boolean isBiometricScreenDisplayedNow() {
        return isElementDisplayedNow(quickAccessTitle);
    }

    public boolean isEnableButtonDisplayed() {
        return isElementDisplayed(enableButton, 6);
    }

    public boolean isEnableButtonEnabledAndClickable() {
        WebElement el = waitForVisibility(enableButton);
        return el.isDisplayed() && el.isEnabled() && "true".equals(el.getAttribute("clickable"));
    }

    public boolean isSkipButtonDisplayed() {
        return isElementDisplayed(skipButton, 6);
    }

    public boolean isSkipButtonEnabledAndClickable() {
        WebElement el = waitForVisibility(skipButton);
        return el.isDisplayed() && el.isEnabled() && "true".equals(el.getAttribute("clickable"));
    }

    public boolean isBrandHeaderDisplayed() {
        return isElementDisplayed(brandHeader, 6);
    }

    public boolean isMedicalTourismDisplayed() {
        return isElementDisplayed(medicalTourismSubtitle, 6);
    }

    public boolean isTaglineDisplayed() {
        return isElementDisplayed(taglineText, 6);
    }

    public boolean areStatsDisplayed() {
        return isElementDisplayed(hospitalsStat, 6) && isElementDisplayed(countriesStat, 6);
    }

    public boolean isEnableFingerprintPromptDisplayed() {
        return isElementDisplayed(enableFingerprintMsg, 6);
    }

    public boolean isPromptSubtitleDisplayed() {
        return isElementDisplayed(promptSubtitleText, 6);
    }

    public boolean isCenterIconVisibleAndSized(int minSizePx) {
        if (!isElementDisplayed(biometricCenterIcon, 5)) {
            return false;
        }
        Rectangle r = waitForVisibility(biometricCenterIcon).getRect();
        return r.getWidth() >= minSizePx && r.getHeight() >= minSizePx;
    }

    public Rectangle getEnableButtonRect() {
        return waitForVisibility(enableButton).getRect();
    }

    public Rectangle getSkipButtonRect() {
        return waitForVisibility(skipButton).getRect();
    }

    public boolean areButtonsAlignedAndNonOverlapping(int maxVerticalDeltaPx) {
        Rectangle skipRect = getSkipButtonRect();
        Rectangle enableRect = getEnableButtonRect();
        boolean nonOverlapping = skipRect.getX() + skipRect.getWidth() <= enableRect.getX()
                || enableRect.getX() + enableRect.getWidth() <= skipRect.getX()
                || skipRect.getY() + skipRect.getHeight() <= enableRect.getY()
                || enableRect.getY() + enableRect.getHeight() <= skipRect.getY();
        boolean alignedRowOrCol = Math.abs(skipRect.getY() - enableRect.getY()) <= maxVerticalDeltaPx
                || Math.abs(skipRect.getX() - enableRect.getX()) <= maxVerticalDeltaPx;
        return nonOverlapping && alignedRowOrCol;
    }

    public boolean isSystemBiometricPromptDisplayed() {
        return isElementDisplayed(systemPromptContainer, 5) || isElementDisplayed(systemPromptCancel, 5);
    }

    public boolean isSuccessToastDisplayed() {
        return isElementDisplayed(successToast, 6);
    }

    public boolean isErrorToastDisplayed() {
        return isElementDisplayed(errorToast, 6);
    }

    /**
     * If the biometric setup screen is showing, skip it to proceed to Dashboard.
     */
    public void skipIfPresent() {
        try {
            if (isElementDisplayed(quickAccessTitle, 3)) {
                log.info("Biometric setup screen detected – skipping.");
                clickSkip();
            }
        } catch (Exception e) {
            log.debug("Biometric screen not present or already dismissed: {}", e.getMessage());
        }
    }
}
