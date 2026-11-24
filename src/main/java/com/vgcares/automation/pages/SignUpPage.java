package com.vgcares.automation.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

/**
 * Page Object for the Sign-Up screen of VGCares Global Doctor (Flutter app).
 *
 * <p><b>Locator strategy for Flutter + UiAutomator2:</b>
 * Flutter does NOT expose {@code @hint} attributes via UiAutomator2.
 * All fields are found via index-based XPath or {@code content-desc}
 * (set by Flutter's {@code Semantics(label: ...)} widget).
 *
 * <p>Field order on the Sign-Up form (from screenshot):
 * <ol>
 *   <li>Full Name  — EditText index 1</li>
 *   <li>Email      — EditText index 2</li>
 *   <li>Password   — EditText index 3</li>
 *   <li>Confirm Password — EditText index 4</li>
 * </ol>
 */
public class SignUpPage extends BasePage {

    // ── Tab Selectors ──────────────────────────────────────────────────────
    // Flutter renders tabs as android.view.View with content-desc from Semantics
    private final By signUpTab = By.xpath(
        "//*[contains(@content-desc,'Sign Up') or contains(@text,'Sign Up')]");
    private final By loginTab  = By.xpath(
        "//*[contains(@content-desc,'Login') or contains(@text,'Login')]");

    // ── Form Fields (index-based — works reliably in Flutter + UiAutomator2) ─
    // The @hint attribute is NOT exposed by Flutter's UiAutomator2 bridge.
    // Index positions are stable as long as the form layout doesn't change.
    private final By fullNameField       = By.xpath("(//android.widget.EditText)[1]");
    private final By emailField          = By.xpath("(//android.widget.EditText)[2]");
    private final By passwordField       = By.xpath("(//android.widget.EditText)[3]");
    private final By confirmPasswordField = By.xpath("(//android.widget.EditText)[4]");

    // ── Create Account Button ──────────────────────────────────────────────
    // Try accessibilityId first; fall back to text/content-desc XPath
    private final By createAccountButton = By.xpath(
        "//*[contains(@content-desc,'Create Account') or contains(@text,'Create Account')]");

    // ── Password Strength Checklist ────────────────────────────────────────
    private final By requirement8Chars    = By.xpath(
        "//*[contains(@text,'8 characters') or contains(@content-desc,'8 characters')]");
    private final By requirementUppercase = By.xpath(
        "//*[contains(@text,'uppercase') or contains(@content-desc,'uppercase')]");
    private final By requirementLowercase = By.xpath(
        "//*[contains(@text,'lowercase') or contains(@content-desc,'lowercase')]");
    private final By requirementNumber    = By.xpath(
        "//*[contains(@text,'number') or contains(@content-desc,'number')]");
    private final By requirementSpecial   = By.xpath(
        "//*[contains(@text,'special') or contains(@content-desc,'special')]");

    // ── Eye-toggle Buttons (sibling approach; no @hint dependency) ─────────
    private final By passwordToggleBtn        = By.xpath(
        "(//android.widget.EditText)[3]/following-sibling::*[@clickable='true']");
    private final By confirmPasswordToggleBtn = By.xpath(
        "(//android.widget.EditText)[4]/following-sibling::*[@clickable='true']");

    // ── Validation / Error Indicators ─────────────────────────────────────
    // Covers Flutter inline error text rendered via Text widgets
    private final By errorIndicator = By.xpath(
        "//*[contains(@text,'required') or contains(@text,'match') or contains(@text,'already')" +
        " or contains(@text,'Invalid') or contains(@text,'must contain') or contains(@text,'enter')" +
        " or contains(@content-desc,'required') or contains(@content-desc,'match')" +
        " or contains(@content-desc,'already') or contains(@content-desc,'Invalid')" +
        " or contains(@content-desc,'enter')]");

    // ── Success / Next-screen Indicators ──────────────────────────────────
    private final By successIndicator = By.xpath(
        "//*[contains(@text,'Success') or contains(@text,'Dashboard') or contains(@text,'Verification')" +
        " or contains(@text,'OTP') or contains(@text,'Welcome')" +
        " or contains(@content-desc,'Dashboard') or contains(@content-desc,'Verification')" +
        " or contains(@content-desc,'Success') or contains(@content-desc,'Welcome')" +
        " or contains(@content-desc,'OTP')]");

    // ── Toast ──────────────────────────────────────────────────────────────
    private final By toastMessage = By.xpath(
        "//android.widget.Toast | //*[contains(@resource-id,'toast') or contains(@class,'Toast')]");

    // ── "Confirm Your Email" Popup Modal (Image 1) ──────────────────────────
    private final By confirmEmailModalTitle = By.xpath(
        "//*[contains(@content-desc,'Confirm Your Email') or contains(@text,'Confirm Your Email')]");
    private final By confirmEmailModalBody = By.xpath(
        "//*[contains(@content-desc,'Account created') or contains(@text,'Account created')" +
        " or contains(@content-desc,'confirmation email') or contains(@text,'confirmation email')]");
    private final By confirmEmailOkButton = By.xpath(
        "//*[contains(@content-desc,'OK') or contains(@text,'OK')]");
    private final By confirmEmailResendText = By.xpath(
        "//*[contains(@content-desc,'Resend') or contains(@text,'Resend')]");

    public SignUpPage(AndroidDriver driver) {
        super(driver);
    }

    // ── Tab Navigation ─────────────────────────────────────────────────────

    public SignUpPage openSignUpTab() {
        log.info("Opening Sign Up tab");
        click(signUpTab);
        // Wait for first EditText (Full Name) to be visible
        waitForVisibility(fullNameField, 10);
        return this;
    }

    public void openLoginTab() {
        log.info("Navigating back to Login tab from Sign Up screen");
        click(loginTab);
    }

    // ── Field Interactions ──────────────────────────────────────────────────

    public SignUpPage enterFullName(String fullName) {
        log.info("Entering Full Name: {}", fullName);
        clearAndSendKeys(fullNameField, fullName);
        return this;
    }

    public SignUpPage enterEmail(String email) {
        log.info("Entering Email: {}", email);
        clearAndSendKeys(emailField, email);
        return this;
    }

    public SignUpPage enterPassword(String password) {
        log.info("Entering Password");
        clearAndSendKeys(passwordField, password);
        return this;
    }

    public SignUpPage enterConfirmPassword(String confirmPassword) {
        log.info("Entering Confirm Password");
        clearAndSendKeys(confirmPasswordField, confirmPassword);
        return this;
    }

    public SignUpPage clickCreateAccount() {
        log.info("Clicking 'Create Account' button");
        hideKeyboard();
        click(createAccountButton);
        return this;
    }

    public void togglePasswordVisibility() {
        log.info("Toggling password visibility");
        if (isElementDisplayed(passwordToggleBtn, 3)) {
            click(passwordToggleBtn);
        }
    }

    public void toggleConfirmPasswordVisibility() {
        log.info("Toggling confirm-password visibility");
        if (isElementDisplayed(confirmPasswordToggleBtn, 3)) {
            click(confirmPasswordToggleBtn);
        }
    }

    // ── State Checks ───────────────────────────────────────────────────────

    public boolean isCreateAccountButtonDisplayed() {
        return isElementDisplayed(createAccountButton, 10);
    }

    public boolean isPasswordChecklistVisible() {
        return isElementDisplayed(requirement8Chars, 5);
    }

    public boolean isRequirement8CharsVisible()    { return isElementDisplayed(requirement8Chars,    5); }
    public boolean isRequirementUppercaseVisible() { return isElementDisplayed(requirementUppercase, 5); }
    public boolean isRequirementLowercaseVisible() { return isElementDisplayed(requirementLowercase, 5); }
    public boolean isRequirementNumberVisible()    { return isElementDisplayed(requirementNumber,    5); }
    public boolean isRequirementSpecialVisible()   { return isElementDisplayed(requirementSpecial,   5); }

    public boolean isSuccessOrNextScreenDisplayed() {
        return isElementDisplayed(successIndicator, 15);
    }

    public boolean isErrorOrValidationDisplayed() {
        return isElementDisplayed(errorIndicator, 8);
    }

    public boolean isConfirmEmailModalDisplayed() {
        return isElementDisplayed(confirmEmailModalTitle, 8) || isElementDisplayed(confirmEmailModalBody, 8);
    }

    public void clickConfirmEmailOk() {
        log.info("Clicking 'OK' on Confirm Your Email popup modal");
        click(confirmEmailOkButton);
    }

    public String getConfirmEmailModalText() {
        if (isElementDisplayed(confirmEmailModalBody, 5)) {
            return getText(confirmEmailModalBody);
        }
        return "";
    }

    public String getToastOrErrorText() {
        if (isElementDisplayed(toastMessage, 4)) {
            return getText(toastMessage);
        }
        if (isErrorOrValidationDisplayed()) {
            return getText(errorIndicator);
        }
        return "";
    }
}
