package com.vgcares.automation.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

/**
 * Page Object for Forgot Password screens (Images 2 & 3).
 * Covers: Enter email -> Continue -> Go to Sign In.
 */
public class ForgotPasswordPage extends BasePage {

    // ── Forgot Password Screen Controls (matches Flutter UI in Images 2 & 3) ──
    // Single EditText on screen (Flutter does not expose @hint reliably)
    private final By emailField = By.xpath("(//android.widget.EditText)[1]");

    // Blue "Continue" action button
    private final By continueButton = By.xpath(
        "//*[contains(@content-desc,'Continue') or contains(@text,'Continue')]");

    // Link back to Login: "Go to Sign In"
    private final By goToSignInLink = By.xpath(
        "//*[contains(@content-desc,'Go to Sign In') or contains(@text,'Go to Sign In')]");

    // Header / Subtitle indicators
    private final By enterEmailHeader = By.xpath(
        "//*[contains(@content-desc,'Enter your email') or contains(@text,'Enter your email')" +
        " or contains(@content-desc,'VGCares Global') or contains(@text,'VGCares Global')]");

    // ── Confirmation Screen ────────────────────────────────────────────────
    private final By resetLinkSentMsg = By.xpath(
        "//*[contains(@content-desc,'Reset link sent') or contains(@text,'Reset link sent')" +
        " or contains(@content-desc,'sent') or contains(@text,'sent')]");
    private final By resendEmailLink = By.xpath(
        "//*[contains(@content-desc,'Resend') or contains(@text,'Resend')]");

    // ── Toast / Error messages ─────────────────────────────────────────────
    private final By toastSuccess = By.xpath(
        "//*[contains(@text,'Reset link sent') or contains(@content-desc,'Reset link sent')" +
        " or contains(@text,'Password reset successfully') or contains(@content-desc,'Password reset successfully')]");

    private final By toastError = By.xpath(
        "//*[contains(@text,'valid email') or contains(@text,'required')" +
        " or contains(@text,'not found') or contains(@content-desc,'valid email')" +
        " or contains(@content-desc,'required') or contains(@content-desc,'not found')]");

    public ForgotPasswordPage(AndroidDriver driver) {
        super(driver);
    }

    // ── Actions ─────────────────────────────────────────────────────────────

    public ForgotPasswordPage enterEmail(String email) {
        log.info("ForgotPassword: Entering email: {}", email);
        clearAndSendKeys(emailField, email);
        return this;
    }

    public ForgotPasswordPage clickContinue() {
        log.info("ForgotPassword: Clicking 'Continue' button");
        hideKeyboard();
        click(continueButton);
        return this;
    }

    public LoginPage clickGoToSignIn() {
        log.info("ForgotPassword: Clicking 'Go to Sign In' link");
        click(goToSignInLink);
        return new LoginPage(driver);
    }

    public void clickResendEmail() {
        log.info("ForgotPassword: Clicking 'Resend Email'");
        click(resendEmailLink);
    }

    // ── State Checks ────────────────────────────────────────────────────────

    public boolean isForgotPasswordScreenDisplayed() {
        return isElementDisplayed(enterEmailHeader, 10) || isElementDisplayed(emailField, 10);
    }

    public boolean isContinueButtonDisplayed() {
        return isElementDisplayed(continueButton, 8);
    }

    public boolean isGoToSignInLinkDisplayed() {
        return isElementDisplayed(goToSignInLink, 8);
    }

    public boolean isResetLinkSentConfirmationDisplayed() {
        return isElementDisplayed(resetLinkSentMsg, 10);
    }

    public boolean isResendEmailLinkDisplayed() {
        return isElementDisplayed(resendEmailLink, 8);
    }

    public boolean isSuccessToastDisplayed() {
        return isElementDisplayed(toastSuccess, 8);
    }

    public boolean isErrorToastDisplayed() {
        return isElementDisplayed(toastError, 8);
    }

    public String getEnteredEmail() {
        return getText(emailField);
    }
}
