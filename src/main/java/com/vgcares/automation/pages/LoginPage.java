package com.vgcares.automation.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

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
            "//*[@clickable='true' and starts-with(@content-desc,'Login\nTab ')]");

    // Form Fields ────────────────────────────────────────────────────────
    // Index-based: works reliably for Flutter apps under UiAutomator2
    private final By emailOrPhoneField = By.xpath("(//android.widget.EditText)[1]");
    private final By passwordField = By.xpath("(//android.widget.EditText)[2]");

    // ── Login Button ───────────────────────────────────────────────────────
    private final By loginButton = By.xpath(
            "//*[@clickable='true' and (@content-desc='Login' or @text='Login'"
                    + " or @content-desc='Sign In' or @text='Sign In')]");

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
        List<WebElement> tabs = findElements(loginTab);
        if (!tabs.isEmpty() && !"true".equals(tabs.get(0).getAttribute("selected"))) {
            click(loginTab);
        }
        waitForVisibility(loginButton);
        return this;
    }

    // ── Field Interactions ─────────────────────────────────────────────────

    public LoginPage enterEmailOrPhone(String credential) {
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
        click(loginButton);
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
        login(email, password);
        return new HomePage(driver);
    }

    /**
     * Alias used by DoctorLoginTest — logs in as a static doctor credential.
     */
    public HomePage loginAsDoctor(String email, String password) {
        login(email, password);
        return new HomePage(driver);
    }

    // ── State Checks ───────────────────────────────────────────────────────

    public boolean isLoginTabDisplayed() {
        return isElementDisplayed(loginTab, 10);
    }

    public boolean areCredentialFieldsDisplayed() {
        return isElementDisplayed(emailOrPhoneField) && isElementDisplayed(passwordField);
    }

    public boolean isLoginButtonDisplayed() {
        return isElementDisplayed(loginButton, 8);
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
