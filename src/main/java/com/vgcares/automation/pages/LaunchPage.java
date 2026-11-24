package com.vgcares.automation.pages;

import com.vgcares.automation.config.ConfigManager;
import com.vgcares.automation.utils.WaitUtils;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.Arrays;

public class LaunchPage extends BasePage {

    // ── Language Selection Screen ──────────────────────────────────────────
    private static final By CONTINUE_BUTTON = control("Continue");
    private static final By ENGLISH_OPTION = labelContaining("English");

    // ── Splash Screen Branding (TC_ID_SPL-001..120) ───────────────────────
    private final By splashBrandName     = By.xpath(
        "//*[contains(@content-desc,'VGCares') or contains(@text,'VGCares')" +
        " or contains(@content-desc,'VG Cares') or contains(@text,'VG Cares')]");
    private final By splashBrandSubtitle = By.xpath(
        "//*[contains(@content-desc,'GLOBAL') or contains(@text,'GLOBAL')]");
    private final By splashProductBy     = By.xpath(
        "//*[contains(@content-desc,'Vishwaguru') or contains(@text,'Vishwaguru')]");
    private final By splashLogoCenter    = By.xpath(
        "//*[contains(@resource-id,'logo') or contains(@content-desc,'logo')" +
        " or @class='android.widget.ImageView']");
    private final By splashLoadingSpinner = By.xpath(
        "//*[contains(@class,'ProgressBar') or contains(@resource-id,'loading')" +
        " or contains(@content-desc,'loading')]");

    private static final By NEXT_BUTTON = By.xpath(
        "//*[(@text='Next' or @content-desc='Next' or starts-with(@content-desc,'Next\n'))]");
    private static final By SKIP_BUTTON = By.xpath(
        "//*[(@text='Skip' or @content-desc='Skip' or starts-with(@content-desc,'Skip\n'))]");
    private static final By BACK_BUTTON = By.xpath(
        "//*[(@text='Back' or @content-desc='Back' or starts-with(@content-desc,'Back\n'))]");
    private static final By GET_STARTED_BUTTON = By.xpath(
        "//*[contains(@content-desc,'Get Started') or contains(@text,'Get Started')]");
    private static final By LOGIN_TAB = By.xpath(
        "//*[@clickable='true' and starts-with(@content-desc,'Login\nTab ')]");
    private static final By SIGN_UP_TAB = By.xpath(
        "//*[@clickable='true' and starts-with(@content-desc,'Sign Up\nTab ')]");

    private static final By HOME_MARKER = By.xpath(
        "//*[contains(@content-desc,'Hello,') or contains(@text,'Hello,') "
        + "or contains(@content-desc,'Search by patient') or contains(@text,'Search by patient')]");

    public enum Screen {
        LANGUAGE(labelContaining("Choose Your Language")),
        WELCOME(labelContaining("Welcome to VG Care")),
        DOCTORS(labelContaining("Connect with Doctors")),
        HOSPITAL_NETWORK(labelContaining("Hospital Network")),
        GLOBAL_HEALTHCARE(labelContaining("Global Healthcare")),
        AUTH(SIGN_UP_TAB),
        HOME(HOME_MARKER);

        private final By marker;

        Screen(By marker) {
            this.marker = marker;
        }
    }

    public LaunchPage(AndroidDriver driver) {
        super(driver);
    }

    // ── Language Selection ─────────────────────────────────────────────────

    public boolean isLanguageSelectionDisplayed() {
        return isScreenVisible(Screen.LANGUAGE);
    }

    public LaunchPage selectEnglish() {
        acceptPermissionIfPresent();
        log.info("Selecting English language");
        click(ENGLISH_OPTION);
        return this;
    }

    public LaunchPage selectLanguage(String languageName) {
        acceptPermissionIfPresent();
        log.info("Selecting language: {}", languageName);
        click(labelContaining(languageName));
        return this;
    }

    public LaunchPage clickContinue() {
        acceptPermissionIfPresent();
        log.info("Clicking Continue on Language Selection");
        click(CONTINUE_BUTTON);
        acceptPermissionIfPresent();
        return this;
    }

    // ── Onboarding Navigation ──────────────────────────────────────────────

    public boolean isWelcomeScreenDisplayed()          { return isScreenVisible(Screen.WELCOME); }
    public boolean isDoctorsScreenDisplayed()          { return isScreenVisible(Screen.DOCTORS); }
    public boolean isHospitalNetworkScreenDisplayed()  { return isScreenVisible(Screen.HOSPITAL_NETWORK); }
    public boolean isGlobalHealthcareScreenDisplayed() { return isScreenVisible(Screen.GLOBAL_HEALTHCARE); }

    public LaunchPage clickNext() {
        log.info("Clicking 'Next' in onboarding");
        click(NEXT_BUTTON);
        return this;
    }

    public LaunchPage clickBack() {
        log.info("Clicking 'Back' in onboarding");
        click(BACK_BUTTON);
        return this;
    }

    public void clickSkip() {
        log.info("Clicking 'Skip' in onboarding");
        click(SKIP_BUTTON);
    }

    public void clickGetStarted() {
        log.info("Clicking 'Get Started' button");
        click(GET_STARTED_BUTTON);
        waitForAuthTabs();
        log.info("Get Started opened the Login and Sign Up tabs");
    }

    public Screen waitForLaunchScreen() {
        return waitForScreen(ConfigManager.getInt("appWaitTimeout", 20),
            Screen.AUTH, Screen.LANGUAGE, Screen.WELCOME, Screen.DOCTORS,
            Screen.HOSPITAL_NETWORK, Screen.GLOBAL_HEALTHCARE);
    }

    public Screen waitForAuthEntryScreen() {
        return waitForScreen(ConfigManager.getInt("appWaitTimeout", 20),
            Screen.AUTH, Screen.HOME, Screen.LANGUAGE, Screen.WELCOME, Screen.DOCTORS,
            Screen.HOSPITAL_NETWORK, Screen.GLOBAL_HEALTHCARE);
    }

    public Screen waitForScreen(Screen... expectedScreens) {
        return waitForScreen(ConfigManager.getInt("explicitWait", 10), expectedScreens);
    }

    private Screen waitForScreen(int timeoutSeconds, Screen... expectedScreens) {
        return WaitUtils.getWait(driver, timeoutSeconds)
                .withMessage("Waiting for VG Cares screen " + Arrays.toString(expectedScreens)).until(d -> {
            if (acceptPermissionIfPresent()) {
                return null;
            }
            for (Screen screen : expectedScreens) {
                if (screen == Screen.AUTH ? isAuthScreenVisible() : hasDisplayedElement(screen.marker)) {
                    return screen;
                }
            }
            return null;
        });
    }

    public Screen advanceToNextSlide(Screen current) {
        Screen next;
        switch (current) {
            case WELCOME:
                next = Screen.DOCTORS;
                break;
            case DOCTORS:
                next = Screen.HOSPITAL_NETWORK;
                break;
            case HOSPITAL_NETWORK:
                next = Screen.GLOBAL_HEALTHCARE;
                break;
            default:
                throw new IllegalArgumentException("No Next slide after " + current);
        }
        clickNext();
        return waitForScreen(next);
    }

    public void waitForGetStartedButton() {
        WaitUtils.waitForClickable(driver, GET_STARTED_BUTTON, 8);
    }

    public void waitForAuthTabs() {
        WaitUtils.getWait(driver).withMessage("Waiting for the Login and Sign Up tabs").until(d -> {
            acceptPermissionIfPresent();
            return isAuthScreenVisible();
        });
    }

    /** Advances onboarding until authentication is available. */
    public void passOnboardingIfPresent() {
        passOnboardingIfPresent(waitForAuthEntryScreen());
    }

    public void passOnboardingIfPresent(Screen screen) {
        if (screen == Screen.HOME || screen == Screen.AUTH) {
            return;
        }
        if (screen == Screen.LANGUAGE) {
            selectEnglish().clickContinue();
            screen = waitForScreen(Screen.WELCOME, Screen.AUTH);
        }

        if (screen != Screen.AUTH && screen != Screen.GLOBAL_HEALTHCARE
                && WaitUtils.isElementDisplayed(driver, SKIP_BUTTON, 2)) {
            click(SKIP_BUTTON);
            screen = waitForScreen(Screen.AUTH, Screen.GLOBAL_HEALTHCARE);
        }
        while (screen != Screen.AUTH && screen != Screen.GLOBAL_HEALTHCARE) {
            screen = advanceToNextSlide(screen);
        }
        if (screen == Screen.GLOBAL_HEALTHCARE) {
            clickGetStarted();
        }
    }

    public boolean isHomeScreenVisible() {
        return hasDisplayedElement(HOME_MARKER);
    }

    public boolean isAuthScreenVisible() {
        return hasDisplayedElement(SIGN_UP_TAB) && hasDisplayedElement(LOGIN_TAB);
    }

    private boolean hasDisplayedElement(By locator) {
        try {
            return driver.findElements(locator).stream().anyMatch(WebElement::isDisplayed);
        } catch (StaleElementReferenceException e) {
            return false;
        }
    }

    private boolean isScreenVisible(Screen screen) {
        try {
            waitForScreen(screen);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private static By labelContaining(String label) {
        return By.xpath("//*[contains(@text,'" + label + "') or contains(@content-desc,'" + label + "')]");
    }

    private static By control(String label) {
        return By.xpath("//*[@clickable='true' and (@text='" + label + "' or @content-desc='" + label +
            "' or starts-with(@content-desc,'" + label + "\n'))]");
    }

    // ── Splash Screen Methods (TC_ID_SPL-001..120) ────────────────────────

    public boolean isSplashBrandNameDisplayed()      { return isElementDisplayed(splashBrandName, 6); }
    public boolean isSplashBrandSubtitleDisplayed()  { return isElementDisplayed(splashBrandSubtitle, 6); }
    public boolean isSplashProductByDisplayed()      { return isElementDisplayed(splashProductBy, 6); }
    public boolean isSplashLogoDisplayed()           { return isElementDisplayed(splashLogoCenter, 6); }
    public boolean isSplashLoadingSpinnerDisplayed() { return isElementDisplayed(splashLoadingSpinner, 6); }

    public boolean isSplashScreenDisplayed() {
        return isSplashBrandNameDisplayed() || isSplashLogoDisplayed() || isSplashBrandSubtitleDisplayed();
    }

    public void waitForSplashToDismiss(int timeoutSeconds) {
        log.info("Waiting for splash screen to dismiss (up to {}s)", timeoutSeconds);
        waitForInvisibility(splashLogoCenter, timeoutSeconds);
    }

}
