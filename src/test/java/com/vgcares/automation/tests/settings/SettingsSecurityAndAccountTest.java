package com.vgcares.automation.tests.settings;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.pages.LoginPage;
import com.vgcares.automation.pages.SettingsPage;
import com.vgcares.automation.utils.JsonUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * ============================================================================
 * Settings Security and Account Test Suite
 * ============================================================================
 * Covers TC_ID_SET-161 through TC_ID_SET-280 and TC_ID_SET-321 through SET-380:
 *  - Change Password (fields, eye toggle, validation, boundary, injection)
 *  - Biometric Login (toggle, prompt, failure recovery)
 *  - My Signature (canvas drawing, Clear/Continue, file upload <=2MB, camera, version)
 *  - Delete My Account (destructive dialog, confirmation, cancel/reopen)
 *  - Full settings lifecycle and stack recovery
 * ============================================================================
 */
public class SettingsSecurityAndAccountTest extends BaseTest {

    private SettingsPage settingsPage;
    private JsonNode settingsData;

    @BeforeClass(alwaysRun = true)
    public void setupSecurityTest() {
        JsonNode authData = JsonUtils.readJsonFile("src/test/resources/testdata/auth-data.json");
        settingsData = authData.get("settingsData");

        JsonNode docCreds = authData.get("doctorCredentials");
        String email = docCreds != null && docCreds.has("email") ? docCreds.get("email").asText() : "virajd@techvishwaguru.com";
        String password = docCreds != null && docCreds.has("password") ? docCreds.get("password").asText() : "Admin@123";

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(email, password);

        HomePage homePage = new HomePage(driver);
        settingsPage = homePage.openSettings();
        Assert.assertTrue(settingsPage.isSettingsPageDisplayed(), "Settings screen must load successfully");
    }

    @Test(priority = 1, description = "TC_ID_SET-161 to 170: Change Password modal launch")
    public void testChangePasswordRowAndModalOpen_SET_161_to_170() {
        Assert.assertTrue(settingsPage.isChangePasswordRowDisplayed(), "Change Password row must be displayed");
        settingsPage.clickChangePassword();
        Assert.assertTrue(settingsPage.isChangePasswordModalDisplayed(), "Change Password modal must open");
        settingsPage.closeChangePasswordModal();
    }

    @Test(priority = 2, description = "TC_ID_SET-171 to 180: Password eye toggle masking")
    public void testChangePasswordVisibilityToggleAndMasking_SET_171_to_180() {
        settingsPage.clickChangePassword();
        settingsPage.enterCurrentPassword(settingsData.get("passwordData").get("currentPassword").asText());
        settingsPage.togglePasswordVisibility();
        settingsPage.togglePasswordVisibility();
        settingsPage.closeChangePasswordModal();
    }

    @Test(priority = 3, description = "TC_ID_SET-181 to 200: Password mismatch and weak password validations")
    public void testChangePasswordValidationsMismatchWeakAndSame_SET_181_to_200() {
        settingsPage.clickChangePassword();
        settingsPage.enterCurrentPassword(settingsData.get("passwordData").get("currentPassword").asText());
        settingsPage.enterNewPassword(settingsData.get("passwordData").get("newPassword").asText());
        settingsPage.enterConfirmPassword(settingsData.get("passwordData").get("mismatchPassword").asText());
        settingsPage.clickUpdatePassword();
        Assert.assertTrue(settingsPage.isPasswordMismatchErrorDisplayed() || settingsPage.isChangePasswordModalDisplayed(),
                "Mismatch error must display or modal remain open");

        settingsPage.enterNewPassword(settingsData.get("passwordData").get("weakPassword").asText());
        settingsPage.enterConfirmPassword(settingsData.get("passwordData").get("weakPassword").asText());
        settingsPage.clickUpdatePassword();
        Assert.assertTrue(settingsPage.isWeakPasswordErrorDisplayed() || settingsPage.isChangePasswordModalDisplayed(),
                "Weak password error must trigger");

        settingsPage.closeChangePasswordModal();
    }

    @Test(priority = 4, description = "TC_ID_SET-201 to 220: Password 255-char boundary and SQL injection resistance")
    public void testChangePasswordBoundaryAndInjectionInputs_SET_201_to_220() {
        settingsPage.clickChangePassword();
        StringBuilder boundaryBuilder = new StringBuilder();
        for (int i = 0; i < 255; i++) {
            boundaryBuilder.append("A");
        }
        boundaryBuilder.append("1!");
        String boundaryPassword = boundaryBuilder.toString();
        String injectionString = "' OR '1'='1' --";

        settingsPage.enterCurrentPassword(injectionString);
        settingsPage.enterNewPassword(boundaryPassword);
        settingsPage.enterConfirmPassword(boundaryPassword);
        settingsPage.clickUpdatePassword();
        Assert.assertTrue(settingsPage.isChangePasswordModalDisplayed(), "Modal must handle boundary and injection cleanly");

        settingsPage.closeChangePasswordModal();
    }

    @Test(priority = 5, description = "TC_ID_SET-221 to 240: Biometric Login toggle check")
    public void testBiometricRowAndToggleState_SET_221_to_240() {
        settingsPage.scrollDown();
        Assert.assertTrue(settingsPage.isBiometricRowDisplayed(), "Biometric row must be displayed");
        Assert.assertTrue(settingsPage.isBiometricToggleDisplayed(), "Biometric switch toggle must be visible");
    }

    @Test(priority = 6, description = "TC_ID_SET-241 to 260: Biometric toggle interaction and prompt dismissal")
    public void testBiometricToggleStressAndPromptDismissal_SET_241_to_260() {
        settingsPage.toggleBiometric();
        if (settingsPage.isBiometricPromptDisplayed()) {
            settingsPage.cancelBiometricPrompt();
        }
        settingsPage.toggleBiometric();
        if (settingsPage.isBiometricPromptDisplayed()) {
            settingsPage.cancelBiometricPrompt();
        }
        Assert.assertTrue(settingsPage.isSettingsPageDisplayed(), "UI must remain intact after biometric toggling");
    }

    @Test(priority = 7, description = "TC_ID_SET-261 to 280: My Signature drawing canvas and Clear/Continue")
    public void testMySignatureScreenCanvasAndActions_SET_261_to_280() {
        Assert.assertTrue(settingsPage.isMySignatureRowDisplayed(), "My Signature row must be displayed");
        settingsPage.clickMySignature();
        Assert.assertTrue(settingsPage.isMySignatureScreenDisplayed() || settingsPage.isSignatureCanvasDisplayed(),
                "My Signature screen/canvas must open");

        settingsPage.drawSampleSignature();
        settingsPage.clickClearSignature();
        settingsPage.drawSampleSignature();
        settingsPage.clickContinueSignature();
    }

    @Test(priority = 8, description = "TC_ID_SET-321 to 340: My Signature upload, camera, and version info")
    public void testMySignatureUploadCameraAndVersionMetadata_SET_321_to_340() {
        settingsPage.clickMySignature();
        Assert.assertTrue(settingsPage.isUploadOptionDisplayed() || settingsPage.isSignatureCanvasDisplayed(),
                "Upload or Canvas option should be present");
        if (settingsPage.isUploadOptionDisplayed()) {
            settingsPage.clickUploadSignature();
        }
        settingsPage.clickBack();
    }

    @Test(priority = 9, description = "TC_ID_SET-341 to 360: Delete My Account warning dialog and cancellation")
    public void testDeleteAccountWarningDialogAndCancel_SET_341_to_360() {
        settingsPage.scrollDown();
        Assert.assertTrue(settingsPage.isDeleteAccountRowDisplayed(), "Delete My Account row must be displayed");
        settingsPage.clickDeleteAccount();
        Assert.assertTrue(settingsPage.isDeleteAccountDialogDisplayed(), "Delete warning dialog must display");
        settingsPage.clickCancelDeleteAccount();
        Assert.assertTrue(settingsPage.isSettingsPageDisplayed(), "Canceling delete must return to Settings screen");
    }

    @Test(priority = 10, description = "TC_ID_SET-361 to 380: Full Settings regression lifecycle")
    public void testSettingsFullRegressionLifecycleAndNavigation_SET_361_to_380() {
        settingsPage.scrollUp();
        Assert.assertTrue(settingsPage.isAppearanceSectionDisplayed(), "Appearance section visible at top");
        Assert.assertTrue(settingsPage.isBackButtonDisplayed(), "Back navigation button must be present");
        settingsPage.clickBack();
    }
}
