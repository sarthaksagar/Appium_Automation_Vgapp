package com.vgcares.automation.pages;

import com.vgcares.automation.utils.GestureUtils;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * ============================================================================
 * Settings Page Object Model
 * ============================================================================
 * Comprehensive SDET page object covering TC_ID_SET-001 through TC_ID_SET-380:
 *  - Header & Navigation (Title "Settings", Back button)
 *  - Appearance Section (Light, System, Dark theme toggles, rapid taps, debouncing)
 *  - Currency Selector (INR, EUR, AUD, JPY, search, toast alerts, default selection)
 *  - Language Selector (English, Portuguese, Hindi, French, Russian, Spanish, German)
 *  - Change Password (Current, New, Confirm, eye toggle, mismatch/weak/same errors)
 *  - Biometric Login (Switch toggle, confirmation prompt, failure simulation)
 *  - My Signature (Draw canvas, Clear, Continue, Upload PNG/JPG <= 2MB, Camera, Info)
 *  - Delete My Account (Destructive warning, confirmation modal, cancel/reopen)
 *  - End-to-End Settings lifecycle and state management
 * ============================================================================
 */
public class SettingsPage extends BasePage {

    // ── Header & Navigation ──────────────────────────────────────────────────
    private final By settingsTitle = By.xpath("//*[contains(@text, 'Settings') or contains(@content-desc, 'Settings')]");
    private final By backButton = By.xpath("//*[contains(@content-desc, 'Back') or @content-desc='<-' or @content-desc='Back' or contains(@class, 'BackButton')]");

    // ── Appearance Section ───────────────────────────────────────────────────
    private final By appearanceHeading = By.xpath("//*[contains(@text, 'Appearance') or contains(@content-desc, 'Appearance')]");
    private final By lightThemeOption = By.xpath("//*[contains(@text, 'Light') or contains(@content-desc, 'Light')]");
    private final By systemThemeOption = By.xpath("//*[contains(@text, 'System') or contains(@content-desc, 'System')]");
    private final By darkThemeOption = By.xpath("//*[contains(@text, 'Dark') or contains(@content-desc, 'Dark')]");

    // ── Currency Section ─────────────────────────────────────────────────────
    private final By currencySectionRow = By.xpath("//*[contains(@text, 'Currency') or contains(@content-desc, 'Currency')]");
    private final By currencySelectorTrigger = By.xpath("//*[contains(@text, 'INR') or contains(@content-desc, 'INR') or contains(@text, 'Currency')]");
    private final By currencyModalTitle = By.xpath("//*[contains(@text, 'Select Currency') or contains(@content-desc, 'Select Currency') or contains(@text, 'Currency')]");
    private final By currencySearchField = By.xpath("//android.widget.EditText[contains(@hint, 'Search') or contains(@text, 'Search') or @resource-id='currency_search']");
    private final By currencyModalClose = By.xpath("//*[contains(@content-desc, 'Close') or contains(@text, 'X') or contains(@content-desc, 'X') or contains(@text, 'Cancel')]");

    // ── Language Section ─────────────────────────────────────────────────────
    private final By languageSectionRow = By.xpath("//*[contains(@text, 'Language') or contains(@content-desc, 'Language')]");
    private final By languageSelectorTrigger = By.xpath("//*[contains(@text, 'English') or contains(@content-desc, 'English') or contains(@text, 'Language')]");
    private final By languageModalTitle = By.xpath("//*[contains(@text, 'Choose Language') or contains(@content-desc, 'Choose Language') or contains(@text, 'Select Language') or contains(@text, 'Language')]");
    private final By languageSearchField = By.xpath("//android.widget.EditText[contains(@hint, 'Search') or contains(@text, 'Search')]");
    private final By languageModalClose = By.xpath("//*[contains(@content-desc, 'Close') or contains(@text, 'X') or contains(@content-desc, 'X') or contains(@text, 'Cancel')]");

    // ── Change Password Section ──────────────────────────────────────────────
    private final By changePasswordRow = By.xpath("//*[contains(@text, 'Change Password') or contains(@content-desc, 'Change Password')]");
    private final By changePasswordModalTitle = By.xpath("//*[contains(@text, 'Change Password') or contains(@content-desc, 'Change Password')]");
    private final By currentPasswordField = By.xpath("//android.widget.EditText[contains(@hint, 'Current') or contains(@text, 'Current') or contains(@content-desc, 'Current')]");
    private final By newPasswordField = By.xpath("//android.widget.EditText[contains(@hint, 'New Password') or contains(@text, 'New') or contains(@content-desc, 'New')]");
    private final By confirmPasswordField = By.xpath("//android.widget.EditText[contains(@hint, 'Confirm') or contains(@text, 'Confirm') or contains(@content-desc, 'Confirm')]");
    private final By passwordVisibilityToggle = By.xpath("//*[contains(@content-desc, 'eye') or contains(@resource-id, 'password_toggle') or contains(@content-desc, 'Show') or contains(@content-desc, 'Hide')]");
    private final By updatePasswordButton = By.xpath("//android.widget.Button[contains(@text, 'Update Password') or contains(@content-desc, 'Update Password') or contains(@text, 'Update')]");
    private final By changePasswordClose = By.xpath("//*[contains(@content-desc, 'Close') or contains(@text, 'X') or contains(@content-desc, 'X') or contains(@text, 'Cancel')]");
    private final By passwordMismatchError = By.xpath("//*[contains(@text, 'match') or contains(@content-desc, 'match') or contains(@text, 'Mismatch')]");
    private final By weakPasswordError = By.xpath("//*[contains(@text, 'least 8') or contains(@content-desc, 'least 8') or contains(@text, 'character')]");
    private final By samePasswordError = By.xpath("//*[contains(@text, 'different') or contains(@content-desc, 'different') or contains(@text, 'same')]");

    // ── Biometric Login Section ──────────────────────────────────────────────
    private final By biometricRow = By.xpath("//*[contains(@text, 'Biometric') or contains(@content-desc, 'Biometric') or contains(@text, 'Fingerprint')]");
    private final By biometricSwitch = By.xpath("//android.widget.Switch | //*[contains(@text, 'Biometric')]/following::android.widget.Switch[1] | //*[contains(@content-desc, 'Biometric')]/following::android.widget.Switch[1]");
    private final By biometricPrompt = By.xpath("//*[contains(@text, 'Verify') or contains(@content-desc, 'Verify') or contains(@text, 'Touch the fingerprint') or contains(@text, 'Biometric')]");
    private final By biometricPromptCancel = By.xpath("//*[contains(@text, 'Cancel') or contains(@content-desc, 'Cancel')]");

    // ── My Signature Section ─────────────────────────────────────────────────
    private final By mySignatureRow = By.xpath("//*[contains(@text, 'My Signature') or contains(@content-desc, 'My Signature') or contains(@text, 'Signature')]");
    private final By mySignatureScreenTitle = By.xpath("//*[contains(@text, 'My Signature') or contains(@content-desc, 'My Signature')]");
    private final By signatureCanvas = By.xpath("//*[contains(@resource-id, 'signature_canvas') or contains(@content-desc, 'canvas') or contains(@class, 'View')]");
    private final By signatureClearButton = By.xpath("//*[contains(@text, 'Clear') or contains(@content-desc, 'Clear')]");
    private final By signatureContinueButton = By.xpath("//*[contains(@text, 'Continue') or contains(@content-desc, 'Continue') or contains(@text, 'Save')]");
    private final By signatureUploadOption = By.xpath("//*[contains(@text, 'Upload') or contains(@content-desc, 'Upload') or contains(@text, 'Gallery')]");
    private final By signatureCameraOption = By.xpath("//*[contains(@text, 'Camera') or contains(@content-desc, 'Camera') or contains(@text, 'Take Photo')]");
    private final By signatureVersionInfo = By.xpath("//*[contains(@text, 'Version') or contains(@content-desc, 'Version') or contains(@text, 'v1') or contains(@text, 'PNG') or contains(@text, 'JPG')]");

    // ── Delete My Account Section ────────────────────────────────────────────
    private final By deleteAccountRow = By.xpath("//*[contains(@text, 'Delete My Account') or contains(@content-desc, 'Delete My Account') or contains(@text, 'Delete Account')]");
    private final By deleteAccountDialog = By.xpath("//*[contains(@text, 'Delete Account') or contains(@content-desc, 'Delete Account') or contains(@text, 'Warning') or contains(@content-desc, 'Warning') or contains(@text, 'permanent')]");
    private final By deleteAccountConfirmButton = By.xpath("//android.widget.Button[contains(@text, 'Delete') or contains(@content-desc, 'Delete')]");
    private final By deleteAccountCancelButton = By.xpath("//*[contains(@text, 'Cancel') or contains(@content-desc, 'Cancel')]");

    public SettingsPage(AndroidDriver driver) {
        super(driver);
    }

    // ── Header & Navigation ──────────────────────────────────────────────────
    public boolean isSettingsPageDisplayed() {
        return isElementDisplayed(settingsTitle, 10) || isElementDisplayed(appearanceHeading, 8);
    }

    public boolean isBackButtonDisplayed() {
        return isElementDisplayed(backButton, 6);
    }

    public void clickBack() {
        log.info("Clicking Settings back button");
        click(backButton);
    }

    // ── Appearance Section ───────────────────────────────────────────────────
    public boolean isAppearanceSectionDisplayed() {
        return isElementDisplayed(appearanceHeading, 8);
    }

    public boolean isLightOptionDisplayed() {
        return isElementDisplayed(lightThemeOption, 6);
    }

    public boolean isSystemOptionDisplayed() {
        return isElementDisplayed(systemThemeOption, 6);
    }

    public boolean isDarkOptionDisplayed() {
        return isElementDisplayed(darkThemeOption, 6);
    }

    public void selectLight() {
        log.info("Selecting Light appearance mode");
        click(lightThemeOption);
    }

    public void selectDark() {
        log.info("Selecting Dark appearance mode");
        click(darkThemeOption);
    }

    public void selectSystem() {
        log.info("Selecting System appearance mode");
        click(systemThemeOption);
    }

    public void tapLightMultipleTimes(int count) {
        log.info("Tapping Light theme option {} times", count);
        for (int i = 0; i < count; i++) {
            click(lightThemeOption);
        }
    }

    public void tapDarkMultipleTimes(int count) {
        log.info("Tapping Dark theme option {} times", count);
        for (int i = 0; i < count; i++) {
            click(darkThemeOption);
        }
    }

    public void tapSystemMultipleTimes(int count) {
        log.info("Tapping System theme option {} times", count);
        for (int i = 0; i < count; i++) {
            click(systemThemeOption);
        }
    }

    // ── Currency Section ─────────────────────────────────────────────────────
    public boolean isCurrencySectionDisplayed() {
        return isElementDisplayed(currencySectionRow, 8);
    }

    public boolean isCurrentCurrencyDisplayed(String currencyCode) {
        By currentCurrencyLocator = By.xpath("//*[contains(@text, '" + currencyCode + "') or contains(@content-desc, '" + currencyCode + "')]");
        return isElementDisplayed(currentCurrencyLocator, 6);
    }

    public void openCurrencyModal() {
        log.info("Opening Currency selection modal");
        click(currencySelectorTrigger);
    }

    public boolean isCurrencyModalDisplayed() {
        return isElementDisplayed(currencyModalTitle, 8);
    }

    public void searchCurrency(String query) {
        log.info("Searching Currency with query: {}", query);
        clearAndSendKeys(currencySearchField, query);
    }

    public void selectCurrency(String currencyCode) {
        log.info("Selecting Currency: {}", currencyCode);
        By currencyOption = By.xpath("//*[contains(@text, '" + currencyCode + "') or contains(@content-desc, '" + currencyCode + "')]");
        click(currencyOption);
    }

    public void closeCurrencyModal() {
        log.info("Closing Currency modal");
        if (isElementDisplayed(currencyModalClose, 4)) {
            click(currencyModalClose);
        }
    }

    public boolean isCurrencyOptionDisplayed(String currencyCode) {
        By currencyOption = By.xpath("//*[contains(@text, '" + currencyCode + "') or contains(@content-desc, '" + currencyCode + "')]");
        return isElementDisplayed(currencyOption, 4);
    }

    // ── Language Section ─────────────────────────────────────────────────────
    public boolean isLanguageSectionDisplayed() {
        return isElementDisplayed(languageSectionRow, 8);
    }

    public boolean isCurrentLanguageDisplayed(String langName) {
        By langLocator = By.xpath("//*[contains(@text, '" + langName + "') or contains(@content-desc, '" + langName + "')]");
        return isElementDisplayed(langLocator, 6);
    }

    public void openLanguageModal() {
        log.info("Opening Language selection modal");
        click(languageSelectorTrigger);
    }

    public boolean isLanguageModalDisplayed() {
        return isElementDisplayed(languageModalTitle, 8);
    }

    public void searchLanguage(String query) {
        log.info("Searching Language with query: {}", query);
        clearAndSendKeys(languageSearchField, query);
    }

    public void selectLanguage(String langName) {
        log.info("Selecting Language: {}", langName);
        By langOption = By.xpath("//*[contains(@text, '" + langName + "') or contains(@content-desc, '" + langName + "')]");
        click(langOption);
    }

    public void closeLanguageModal() {
        log.info("Closing Language modal");
        if (isElementDisplayed(languageModalClose, 4)) {
            click(languageModalClose);
        }
    }

    public boolean isLanguageOptionDisplayed(String langName) {
        By langOption = By.xpath("//*[contains(@text, '" + langName + "') or contains(@content-desc, '" + langName + "')]");
        return isElementDisplayed(langOption, 4);
    }

    // ── Change Password Section ──────────────────────────────────────────────
    public boolean isChangePasswordRowDisplayed() {
        return isElementDisplayed(changePasswordRow, 8);
    }

    public void clickChangePassword() {
        log.info("Opening Change Password modal/screen");
        click(changePasswordRow);
    }

    public boolean isChangePasswordModalDisplayed() {
        return isElementDisplayed(changePasswordModalTitle, 8);
    }

    public void enterCurrentPassword(String password) {
        log.info("Entering current password");
        clearAndSendKeys(currentPasswordField, password);
    }

    public void enterNewPassword(String password) {
        log.info("Entering new password");
        clearAndSendKeys(newPasswordField, password);
    }

    public void enterConfirmPassword(String password) {
        log.info("Entering confirm password");
        clearAndSendKeys(confirmPasswordField, password);
    }

    public void togglePasswordVisibility() {
        log.info("Toggling password visibility eye");
        if (isElementDisplayed(passwordVisibilityToggle, 4)) {
            click(passwordVisibilityToggle);
        }
    }

    public void clickUpdatePassword() {
        log.info("Clicking Update Password button");
        click(updatePasswordButton);
    }

    public void closeChangePasswordModal() {
        log.info("Closing Change Password modal");
        if (isElementDisplayed(changePasswordClose, 4)) {
            click(changePasswordClose);
        }
    }

    public boolean isPasswordMismatchErrorDisplayed() {
        return isElementDisplayed(passwordMismatchError, 5);
    }

    public boolean isWeakPasswordErrorDisplayed() {
        return isElementDisplayed(weakPasswordError, 5);
    }

    public boolean isSamePasswordErrorDisplayed() {
        return isElementDisplayed(samePasswordError, 5);
    }

    // ── Biometric Login Section ──────────────────────────────────────────────
    public boolean isBiometricRowDisplayed() {
        return isElementDisplayed(biometricRow, 8);
    }

    public boolean isBiometricToggleDisplayed() {
        return isElementDisplayed(biometricSwitch, 6);
    }

    public boolean isBiometricEnabled() {
        if (isElementDisplayed(biometricSwitch, 4)) {
            WebElement el = findElement(biometricSwitch, 4);
            String checked = el.getAttribute("checked");
            return "true".equalsIgnoreCase(checked);
        }
        return false;
    }

    public void toggleBiometric() {
        log.info("Toggling Biometric switch");
        click(biometricSwitch);
    }

    public boolean isBiometricPromptDisplayed() {
        return isElementDisplayed(biometricPrompt, 5);
    }

    public void cancelBiometricPrompt() {
        log.info("Canceling biometric prompt if open");
        if (isElementDisplayed(biometricPromptCancel, 4)) {
            click(biometricPromptCancel);
        }
    }

    // ── My Signature Section ─────────────────────────────────────────────────
    public boolean isMySignatureRowDisplayed() {
        return isElementDisplayed(mySignatureRow, 8);
    }

    public void clickMySignature() {
        log.info("Opening My Signature screen");
        click(mySignatureRow);
    }

    public boolean isMySignatureScreenDisplayed() {
        return isElementDisplayed(mySignatureScreenTitle, 8);
    }

    public boolean isSignatureCanvasDisplayed() {
        return isElementDisplayed(signatureCanvas, 6);
    }

    public void drawSampleSignature() {
        log.info("Simulating drawing stroke on signature canvas");
        if (isElementDisplayed(signatureCanvas, 4)) {
            click(signatureCanvas);
        }
    }

    public void clickClearSignature() {
        log.info("Clearing signature pad");
        click(signatureClearButton);
    }

    public void clickContinueSignature() {
        log.info("Confirming signature with Continue");
        click(signatureContinueButton);
    }

    public boolean isUploadOptionDisplayed() {
        return isElementDisplayed(signatureUploadOption, 6);
    }

    public void clickUploadSignature() {
        log.info("Clicking Upload Signature from Gallery");
        click(signatureUploadOption);
    }

    public boolean isCameraOptionDisplayed() {
        return isElementDisplayed(signatureCameraOption, 6);
    }

    public void clickCameraSignature() {
        log.info("Clicking Camera capture for Signature");
        click(signatureCameraOption);
    }

    public boolean isSignatureVersionInfoDisplayed() {
        return isElementDisplayed(signatureVersionInfo, 6);
    }

    // ── Delete My Account Section ────────────────────────────────────────────
    public boolean isDeleteAccountRowDisplayed() {
        return isElementDisplayed(deleteAccountRow, 8);
    }

    public void clickDeleteAccount() {
        log.info("Clicking Delete My Account");
        click(deleteAccountRow);
    }

    public boolean isDeleteAccountDialogDisplayed() {
        return isElementDisplayed(deleteAccountDialog, 8);
    }

    public void clickConfirmDeleteAccount() {
        log.info("Confirming Delete Account");
        click(deleteAccountConfirmButton);
    }

    public void clickCancelDeleteAccount() {
        log.info("Canceling Delete Account");
        if (isElementDisplayed(deleteAccountCancelButton, 4)) {
            click(deleteAccountCancelButton);
        }
    }

    // ── Gestures ─────────────────────────────────────────────────────────────
    public void scrollDown() {
        GestureUtils.scrollDown(driver);
    }

    public void scrollUp() {
        GestureUtils.scrollUp(driver);
    }
}
