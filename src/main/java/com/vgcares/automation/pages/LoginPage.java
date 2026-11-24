package com.vgcares.automation.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

/**
 * Page Object for the Login screen of VGCares Global Doctor (Flutter app).
 *
 * <p>
 * <b>Locator strategy for Flutter + UiAutomator2:</b>
 * Flutter does NOT expose {@code @hint} attributes via UiAutomator2.
 * Fields are located by index-based XPath.
 *
 * <p>
 * Login form field order:
 * <ol>
 * <li>Email / Phone — EditText index 1</li>
 * <li>Password — EditText index 2</li>
 * </ol>
 */
public class LoginPage extends BasePage {

    // ── Tab Selectors ──────────────────────────────────────────────────────
    private final By loginTab = By.xpath(
            "//*[contains(@content-desc,'Login') or contains(@text,'Login')]");
    private final By signUpTab = By.xpath(
            "//*[contains(@content-desc,'Sign Up') or contains(@text,'Sign Up')]");

    // ── Form Fields ────────────────────────────────────────────────────────
    // Index-based: works reliably for Flutter apps under UiAutomator2
    private final By emailOrPhoneField = By.xpath("(//android.widget.EditText)[1]");
    private final By passwordField = By.xpath("(//android.widget.EditText)[2]");

    // ── Login Button ───────────────────────────────────────────────────────
    private final By loginButton = By.xpath(
            "//*[contains(@content-desc,'Login') and not(contains(@content-desc,'Tab'))" +
                    " and not(contains(@content-desc,'Sign Up'))]" +
                    " | //android.widget.Button[contains(@text,'Login') or contains(@text,'Sign In')]");

    // A simpler fallback for the Login action button specifically:
    private final By loginActionButton = By.xpath(
            "(//*[contains(@content-desc,'Login') or contains(@text,'Login')])[last()]");

    // ── Forgot Password ────────────────────────────────────────────────────
    private final By forgotPasswordLink = By.xpath(
            "//*[contains(@content-desc,'Forgot Password') or contains(@text,'Forgot Password')" +
                    " or contains(@content-desc,'Forgot') or contains(@text,'Forgot')]");

    // ── Error Indicators ───────────────────────────────────────────────────
    private final By errorMessage = By.xpath(
            "//*[contains(@text,'Invalid') or contains(@text,'incorrect') or contains(@text,'required')" +
                    " or contains(@text,'wrong') or contains(@text,'not found')" +
                    " or contains(@content-desc,'Invalid') or contains(@content-desc,'incorrect')" +
                    " or contains(@content-desc,'required')]");

    public LoginPage(AndroidDriver driver) {
        super(driver);
    }

    // ── Tab Navigation ─────────────────────────────────────────────────────

    public LoginPage openLoginTab() {
        log.info("Switching to Login tab");
        // Only click tab if not already on Login (avoid double-click)
        if (!isElementDisplayed(emailOrPhoneField, 2)) {
            click(loginTab);
            waitForVisibility(emailOrPhoneField, 10);
        }
        return this;
    }

    // ── Field Interactions ─────────────────────────────────────────────────

    public LoginPage enterEmailOrPhone(String credential) {
        log.info("Entering login credential: {}", credential);
        clearAndSendKeys(emailOrPhoneField, credential);
        return this;
    }

    public LoginPage enterPassword(String password) {
        log.info("Entering login password");
        clearAndSendKeys(passwordField, password);
        return this;
    }

    public LoginPage clickLogin() {
        log.info("Clicking Login button");
        hideKeyboard();
        // Try the specific login action button
        if (isElementDisplayed(loginActionButton, 3)) {
            click(loginActionButton);
        } else {
            click(loginButton);
        }
        return this;
    }

    public LoginPage clearCredentials() {
        log.info("Clearing email and password fields");
        clearAndSendKeys(emailOrPhoneField, "");
        clearAndSendKeys(passwordField, "");
        return this;
    }

    // ── Convenience Composite Methods ──────────────────────────────────────

    /**
     * Full login flow: open tab → enter credentials → click Login.
     */
    public void login(String email, String password) {
        openLoginTab();
        enterEmailOrPhone(email);
        enterPassword(password);
        clickLogin();
    }

    /**
     * Login with a live dynamic account and return the resulting HomePage.
     */
    public HomePage loginWithDynamicAccount(String email, String password) {
        log.info("Logging in with dynamic account: {}", email);
        login(email, password);
        return new HomePage(driver);
    }

    /**
     * Alias used by DoctorLoginTest — logs in as a static doctor credential.
     */
    public HomePage loginAsDoctor(String email, String password) {
        log.info("Doctor login: {}", email);
        login(email, password);
        return new HomePage(driver);
    }

    // ── State Checks ───────────────────────────────────────────────────────

    public boolean isLoginTabDisplayed() {
        return isElementDisplayed(loginTab, 10);
    }

    public boolean isLoginButtonDisplayed() {
        return isElementDisplayed(loginActionButton, 8) || isElementDisplayed(loginButton, 8);
    }

    public boolean isForgotPasswordDisplayed() {
        return isElementDisplayed(forgotPasswordLink, 8);
    }

    public boolean isErrorMessageDisplayed() {
        return isElementDisplayed(errorMessage, 8);
    }

    public void clickForgotPassword() {
        log.info("Clicking Forgot Password link");
        click(forgotPasswordLink);
    }

    public String getEnteredEmail() {
        return getText(emailOrPhoneField);
    }

    public String getErrorMessageText() {
        return isErrorMessageDisplayed() ? getText(errorMessage) : "";
    }
}
