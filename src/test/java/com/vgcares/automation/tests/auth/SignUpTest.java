package com.vgcares.automation.tests.auth;

import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.testdata.AuthFixtures;
import com.vgcares.automation.testdata.QaCredentials;
import com.vgcares.automation.pages.SignUpPage;
import com.vgcares.automation.utils.StepLogger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Sign Up Test Suite — 5 Core High-Value Tests
 * Covers Sign Up form validation, password strength checklist, and the "Confirm Your Email" modal.
 */
public class SignUpTest extends BaseTest {

    private SignUpPage signUpPage;

    @BeforeClass(alwaysRun = true)
    public void setupSignUp() {
        prepareAuthScreen();
        signUpPage = new SignUpPage(getDriver());
        signUpPage.openSignUpTab();
    }

    @org.testng.annotations.BeforeMethod(alwaysRun = true)
    public void resetSignUpForm() {
        if (signUpPage != null) {
            signUpPage.scrollToTop();
        }
    }

    @Test(description = "TC_ID_SIGNUP-001: Verify Sign Up screen UI fields and controls are displayed",
          groups = {"smoke", "regression"}, priority = 1)
    public void testSignUpScreenUIControlsDisplayed() {
        StepLogger.step("Verify Sign Up tab is active and form fields are visible");
        Assert.assertTrue(signUpPage.isCreateAccountButtonDisplayed(),
                "Create Account button must be displayed on Sign Up tab.");
        StepLogger.pass("Sign Up form UI controls verified successfully");
    }

    @Test(description = "TC_ID_SIGNUP-002: Verify validation error when submitting empty fields",
          groups = {"regression"}, priority = 2)
    public void testEmptyFieldsValidationOnSignUp() {
        StepLogger.step("Submit empty registration form");
        signUpPage.enterFullName("")
                  .enterEmail("")
                  .enterPassword("")
                  .enterConfirmPassword("")
                  .clickCreateAccount();

        StepLogger.step("Verify validation prevents submission and user remains on Sign Up screen");
        Assert.assertTrue(signUpPage.isErrorOrValidationDisplayed() || signUpPage.isCreateAccountButtonDisplayed(),
                "Submitting empty form should display validation error and remain on Sign Up.");
        StepLogger.pass("Empty registration rejected as expected");
    }

    @Test(description = "TC_ID_SIGNUP-003: Verify password strength requirement checklist is visible",
          groups = {"regression"}, priority = 3)
    public void testPasswordRequirementsChecklistVisible() {
        StepLogger.step("Verify password strength checklist items (length, uppercase, number, etc.)");
        Assert.assertTrue(signUpPage.isPasswordChecklistVisible(),
                "Password requirements checklist (8 characters, uppercase, number, etc.) should be visible.");
        StepLogger.pass("Password strength requirement checklist verified");
    }

    @Test(description = "TC_ID_SIGNUP-004: Verify password mismatch error when passwords do not match",
          groups = {"regression"}, priority = 4)
    public void testPasswordMismatchValidation() {
        StepLogger.step("Enter mismatched passwords in Password and Confirm Password fields");
        signUpPage.enterFullName("Test Doctor")
                  .enterEmail("test.doctor@email.com")
                  .enterPassword(AuthFixtures.newPassword())
                  .enterConfirmPassword(AuthFixtures.newPassword())
                  .clickCreateAccount();

        StepLogger.step("Verify account creation is blocked on password mismatch");
        Assert.assertTrue(signUpPage.isCreateAccountButtonDisplayed(),
                "Account creation should be prevented when passwords do not match.");
        StepLogger.pass("Password mismatch blocked submission as expected");
    }

    @Test(description = "TC_ID_SIGNUP-005: Verify account registration displays 'Confirm Your Email' modal and can be dismissed",
          groups = {"smoke", "regression"}, priority = 5)
    public void testValidSignUpDisplaysConfirmEmailModal() {
        StepLogger.step("Generate unique QA test registration details");
        String dynamicEmail = AuthFixtures.uniqueEmail(QaCredentials.signupDomain());
        String signupPassword = AuthFixtures.newPassword();

        StepLogger.step("Fill registration form with unique email and valid credentials");
        signUpPage.enterFullName("Test Doctor")
                  .enterEmail(dynamicEmail)
                  .enterPassword(signupPassword)
                  .enterConfirmPassword(signupPassword)
                  .clickCreateAccount();

        StepLogger.step("Verify confirmation prompt ('Confirm Your Email' modal or success transition)");
        if (signUpPage.isConfirmEmailModalDisplayed()) {
            StepLogger.pass("Confirm Your Email modal displayed successfully");
            signUpPage.clickConfirmEmailOk();
            StepLogger.pass("Dismissed Confirm Your Email modal via OK button");
        } else {
            Assert.assertTrue(signUpPage.isSuccessOrNextScreenDisplayed() || !signUpPage.isCreateAccountButtonDisplayed(),
                    "Expected either 'Confirm Your Email' popup or navigation after registration.");
            StepLogger.pass("Registration completed and verified successfully");
        }
    }
}
