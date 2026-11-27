package com.vgcares.automation.pages;

import com.vgcares.automation.utils.WaitUtils;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.TimeoutException;
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
    private final By signUpTab = By.xpath(
            "//*[@clickable='true' and starts-with(@content-desc,'Sign Up\nTab ')]");

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

    // ── Optional Biometric Trigger on Login Screen ────────────────────────
    private final By biometricLoginOption = By.xpath(
            "//*[contains(@content-desc,'Fingerprint') or contains(@text,'Fingerprint')"
                    + " or contains(@content-desc,'Biometric') or contains(@text,'Biometric')"
                    + " or contains(@content-desc,'Face ID') or contains(@text,'Face ID')]");

    // ── Error & Toast Indicators ───────────────────────────────────────────
    // Exclude EditText hints so field placeholders never produce false error matches
    private final By errorMessage = By.xpath(
            "//*[not(self::android.widget.EditText) and ("
                    + "contains(@text,'Invalid') or contains(@text,'incorrect') or contains(@text,'required')"
                    + " or contains(@text,'wrong') or contains(@text,'not found')"
                    + " or contains(@text,'valid email') or contains(@text,'Please enter')"
                    + " or contains(@text,'deactivated') or contains(@text,'blocked')"
                    + " or contains(@text,'changed') or contains(@text,'domain')"
                    + " or contains(@content-desc,'Invalid') or contains(@content-desc,'incorrect')"
                    + " or contains(@content-desc,'required') or contains(@content-desc,'wrong')"
                    + " or contains(@content-desc,'not found') or contains(@content-desc,'valid email')"
                    + " or contains(@content-desc,'Please enter') or contains(@content-desc,'deactivated')"
                    + " or contains(@content-desc,'blocked') or contains(@content-desc,'changed')"
                    + " or contains(@content-desc,'domain'))]");

    private final By toastMessage = By.xpath(
            "//android.widget.Toast | //*[contains(@resource-id,'toast') or contains(@class,'Toast')"
                    + " or contains(@resource-id,'snackbar')]");

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

    public LoginPage openSignUpTab() {
        log.info("Switching to Sign Up tab");
        click(signUpTab);
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

    public LoginPage tapEmailField() {
        click(emailOrPhoneField);
        return this;
    }

    public LoginPage tapPasswordField() {
        click(passwordField);
        return this;
    }

    public LoginPage clickLogin() {
        log.info("Clicking Login button");
        if (!isElementDisplayedNow(loginButton)) {
            hideKeyboard();
        }
        click(loginButton);
        return this;
    }

    public LoginPage tapLoginRapidly(int tapCount) {
        log.info("Rapidly tapping Login button {} times", tapCount);
        if (!isElementDisplayedNow(loginButton)) {
            hideKeyboard();
        }
        WebElement btn = waitForVisibility(loginButton);
        for (int i = 0; i < tapCount; i++) {
            try {
                btn.click();
            } catch (Exception ignored) {
                break;
            }
        }
        return this;
    }

    public void pressDeviceBack() {
        log.info("Pressing Android device back button from Login screen");
        driver.pressKey(new KeyEvent(AndroidKey.BACK));
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

    public boolean isOnLoginTabNow() {
        return isElementDisplayedNow(loginButton) && isElementDisplayedNow(emailOrPhoneField);
    }

    public boolean isLoginButtonEnabledAndClickable() {
        WebElement btn = waitForVisibility(loginButton);
        return btn.isDisplayed() && btn.isEnabled() && "true".equals(btn.getAttribute("clickable"));
    }

    public boolean hasBiometricOptionOnLogin() {
        return isElementDisplayedNow(biometricLoginOption);
    }

    public boolean areLoginElementsWithinBoundsAndNonOverlapping() {
        Dimension screen = driver.manage().window().getSize();
        Rectangle emailRect = waitForVisibility(emailOrPhoneField).getRect();
        Rectangle passRect = waitForVisibility(passwordField).getRect();
        Rectangle btnRect = waitForVisibility(loginButton).getRect();

        return isInsideViewport(emailRect, screen)
                && isInsideViewport(passRect, screen)
                && isInsideViewport(btnRect, screen)
                && emailRect.getY() + emailRect.getHeight() <= passRect.getY()
                && passRect.getY() + passRect.getHeight() <= btnRect.getY();
    }

    private boolean isInsideViewport(Rectangle rect, Dimension screen) {
        return rect.getX() >= 0
                && rect.getY() >= 0
                && rect.getWidth() > 0
                && rect.getHeight() > 0
                && rect.getX() + rect.getWidth() <= screen.getWidth()
                && rect.getY() + rect.getHeight() <= screen.getHeight();
    }

    public boolean waitForValidationOrStay(int timeoutSeconds) {
        try {
            Boolean found = WaitUtils.getWait(driver, timeoutSeconds).until(d ->
                    (isElementDisplayedNow(errorMessage) || isElementDisplayedNow(toastMessage))
                            ? Boolean.TRUE
                            : null);
            return Boolean.TRUE.equals(found);
        } catch (TimeoutException e) {
            return isElementDisplayedNow(loginButton);
        }
    }

    public WebElement findVisibleErrorOrToast(int timeoutSeconds) {
        try {
            return WaitUtils.getWait(driver, timeoutSeconds).until(d -> {
                for (By locator : new By[]{toastMessage, errorMessage}) {
                    for (WebElement el : driver.findElements(locator)) {
                        if (el.isDisplayed()) {
                            return el;
                        }
                    }
                }
                return null;
            });
        } catch (TimeoutException e) {
            return null;
        }
    }

    public boolean waitForTransientToastToDismiss(int timeoutSeconds) {
        try {
            return WaitUtils.getWait(driver, timeoutSeconds).until(d ->
                    !isElementDisplayedNow(toastMessage));
        } catch (TimeoutException e) {
            return false;
        }
    }

    public Rectangle getEmailFieldRect() {
        return waitForVisibility(emailOrPhoneField).getRect();
    }

    public Rectangle getPasswordFieldRect() {
        return waitForVisibility(passwordField).getRect();
    }

    public String getErrorMessageText() {
        WebElement el = findVisibleErrorOrToast(3);
        if (el == null) {
            return "";
        }
        String text = el.getText();
        if (text == null || text.isBlank()) {
            text = el.getAttribute("content-desc");
        }
        return text != null ? text.trim() : "";
    }
}
