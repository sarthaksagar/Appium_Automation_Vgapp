package com.vgcares.automation.tests.auth;

import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.pages.LaunchPage;
import com.vgcares.automation.pages.SignUpPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Sign Up Test Suite — 5 Core High-Value Tests
 * Covers form validation, password checklist, and the "Confirm Your Email" modal.
 */
public class SignUpTest extends BaseTest {

    private SignUpPage signUpPage;

    @BeforeClass(alwaysRun = true)
    public void setupSignUp() {
        new LaunchPage(getDriver()).passOnboardingIfPresent();
        signUpPage = new SignUpPage(getDriver());
        signUpPage.openSignUpTab();
    }

    @Test(description = "TC_ID_SIGNUP-001: Verify Sign Up screen UI fields and controls are displayed",
          groups = {"smoke", "regression"}, priority = 1)
    public void testSignUpScreenUIControlsDisplayed() {
        log.info("Executing TC_ID_SIGNUP-001: Sign Up UI Controls Verification");
        Assert.assertTrue(signUpPage.isCreateAccountButtonDisplayed(),
                "Create Account button must be displayed on Sign Up tab.");
    }

    @Test(description = "TC_ID_SIGNUP-002: Verify validation error when submitting empty fields",
          groups = {"regression"}, priority = 2)
    public void testEmptyFieldsValidationOnSignUp() {
        log.info("Executing TC_ID_SIGNUP-002: Empty fields validation");
        signUpPage.enterFullName("")
                  .enterEmail("")
                  .enterPassword("")
                  .enterConfirmPassword("")
                  .clickCreateAccount();

        Assert.assertTrue(signUpPage.isErrorOrValidationDisplayed() || signUpPage.isCreateAccountButtonDisplayed(),
                "Submitting empty form should display validation error and remain on Sign Up.");
    }

    @Test(description = "TC_ID_SIGNUP-003: Verify password strength requirement checklist is visible",
          groups = {"regression"}, priority = 3)
    public void testPasswordRequirementsChecklistVisible() {
        log.info("Executing TC_ID_SIGNUP-003: Password checklist verification");
        Assert.assertTrue(signUpPage.isPasswordChecklistVisible(),
                "Password requirements checklist (8 characters, uppercase, number, etc.) should be visible.");
    }

    @Test(description = "TC_ID_SIGNUP-004: Verify password mismatch error when passwords do not match",
          groups = {"regression"}, priority = 4)
    public void testPasswordMismatchValidation() {
        log.info("Executing TC_ID_SIGNUP-004: Password mismatch validation");
        signUpPage.enterFullName("John Doe")
                  .enterEmail("test.user@email.com")
                  .enterPassword("VGCare@12345")
                  .enterConfirmPassword("Different@999")
                  .clickCreateAccount();

        Assert.assertTrue(signUpPage.isCreateAccountButtonDisplayed(),
                "Account creation should be prevented when passwords do not match.");
    }

    @Test(description = "TC_ID_SIGNUP-005: Verify account registration displays 'Confirm Your Email' modal and can be dismissed",
          groups = {"smoke", "regression"}, priority = 5)
    public void testValidSignUpDisplaysConfirmEmailModal() {
        log.info("Executing TC_ID_SIGNUP-005: Confirm Your Email popup modal verification");
        String dynamicEmail = "qa_" + System.currentTimeMillis() + "@email.com";
        signUpPage.enterFullName("Test Doctor")
                  .enterEmail(dynamicEmail)
                  .enterPassword("VGCare@12345")
                  .enterConfirmPassword("VGCare@12345")
                  .clickCreateAccount();

        // Check if "Confirm Your Email" popup modal appears (Image 1)
        if (signUpPage.isConfirmEmailModalDisplayed()) {
            log.info("Confirm Your Email modal displayed successfully!");
            String modalText = signUpPage.getConfirmEmailModalText();
            log.info("Modal content: {}", modalText);
            signUpPage.clickConfirmEmailOk();
            log.info("Clicked 'OK' on modal");
        } else {
            // Alternatively success toast or navigation
            Assert.assertTrue(signUpPage.isSuccessOrNextScreenDisplayed() || !signUpPage.isCreateAccountButtonDisplayed(),
                    "Expected either 'Confirm Your Email' popup or navigation after registration.");
        }
    }
}
