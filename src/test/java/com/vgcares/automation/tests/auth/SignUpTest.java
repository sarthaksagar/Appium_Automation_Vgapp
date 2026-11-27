package com.vgcares.automation.tests.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.pages.SignUpPage;
import com.vgcares.automation.testdata.AuthFixtures;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Sign Up & Toaster Messages Test Suite (TC_ID_SIGNUP-001 to TC_ID_SIGNUP-008).
 * Configured for Doctor (D) suite execution per user request; loads module data
 * from src/test/resources/testdata/doctor/signup-data.json.
 */
public class SignUpTest extends DoctorBaseTest {

    private SignUpPage signUpPage;
    private JsonNode signupData;

    @BeforeClass(alwaysRun = true)
    public void setupSignUp() {
        signupData = ModuleDataLoader.doctor("signup-data.json").path("testData");
        prepareAuthScreen();
        signUpPage = new SignUpPage(getDriver());
        signUpPage.openSignUpTab();
    }

    @BeforeMethod(alwaysRun = true)
    public void ensureOnSignUpTab() {
        if (signUpPage != null) {
            if (!signUpPage.isOnSignUpFormNow()) {
                prepareAuthScreen();
                signUpPage.openSignUpTab();
            } else {
                signUpPage.scrollToTop();
            }
        }
    }

    @Test(priority = 1, groups = {"doctor", "smoke", "regression"},
          description = "TC_ID_SIGNUP-001: Verify user can create account with valid details")
    public void testValidSignUpCreatesAccount() {
        JsonNode valid = signupData.path("valid");
        String fullName = valid.path("fullName").asText("John Doe");
        String uniqueEmail = AuthFixtures.uniqueEmail(valid.path("emailDomain").asText("example.com"));
        String password = AuthFixtures.newPassword();

        StepLogger.step("Enter valid Full Name, Email, Password, Confirm Password and tap Create Account");
        signUpPage.enterFullName(fullName)
                  .enterEmail(uniqueEmail)
                  .enterPassword(password)
                  .enterConfirmPassword(password)
                  .clickCreateAccount();

        StepLogger.step("Verify account creation confirmation modal or verification/dashboard transition");
        SignUpPage.SignUpOutcome outcome = signUpPage.waitForSignUpOutcome(15);
        if (outcome == SignUpPage.SignUpOutcome.CONFIRM_EMAIL_MODAL) {
            StepLogger.pass("Confirm Your Email modal displayed successfully");
            signUpPage.clickConfirmEmailOk();
            StepLogger.pass("Dismissed Confirm Your Email modal cleanly");
        } else if (outcome == SignUpPage.SignUpOutcome.NAVIGATED) {
            StepLogger.pass("Valid account creation succeeded");
        } else {
            String details = outcome == SignUpPage.SignUpOutcome.ERROR
                    ? " Visible error/toast: " + signUpPage.getToastOrErrorText()
                    : "";
            Assert.fail("Expected confirmation modal or navigation after valid registration, but outcome was "
                    + outcome + "." + details);
        }
    }

    @Test(priority = 2, groups = {"doctor", "regression"},
          description = "TC_ID_SIGNUP-002: Verify account creation fails with empty Full Name")
    public void testEmptyFullNameValidation() {
        JsonNode tc = signupData.path("emptyFullName");
        String password = AuthFixtures.newPassword();

        StepLogger.step("Leave Full Name empty, enter valid Email and matching Passwords, tap Create Account");
        signUpPage.enterFullName(tc.path("fullName").asText(""))
                  .enterEmail(tc.path("email").asText("test@email.com"))
                  .enterPassword(password)
                  .enterConfirmPassword(password)
                  .clickCreateAccount();

        StepLogger.step("Verify Full Name validation error blocks account creation");
        Assert.assertTrue(signUpPage.waitForValidationOrStay(3),
                "Empty Full Name must block account creation and display validation.");
        StepLogger.pass("Empty Full Name rejected as expected");
    }

    @Test(priority = 3, groups = {"doctor", "regression"},
          description = "TC_ID_SIGNUP-003: Verify account creation fails with empty Email")
    public void testEmptyEmailValidation() {
        JsonNode tc = signupData.path("emptyEmail");
        String password = AuthFixtures.newPassword();

        StepLogger.step("Enter Full Name, leave Email empty, enter matching Passwords, tap Create Account");
        signUpPage.enterFullName(tc.path("fullName").asText("John Doe"))
                  .enterEmail(tc.path("email").asText(""))
                  .enterPassword(password)
                  .enterConfirmPassword(password)
                  .clickCreateAccount();

        StepLogger.step("Verify Email validation error blocks account creation");
        Assert.assertTrue(signUpPage.waitForValidationOrStay(3),
                "Empty Email must block account creation and display validation.");
        StepLogger.pass("Empty Email rejected as expected");
    }

    @Test(priority = 4, groups = {"doctor", "regression"},
          description = "TC_ID_SIGNUP-004: Verify account creation fails with empty Password")
    public void testEmptyPasswordValidation() {
        JsonNode tc = signupData.path("emptyPassword");
        String confirmPass = AuthFixtures.newPassword();

        StepLogger.step("Enter Full Name and Email, leave Password empty, enter Re-enter Password, tap Create Account");
        signUpPage.enterFullName(tc.path("fullName").asText("John Doe"))
                  .enterEmail(tc.path("email").asText("test@email.com"))
                  .enterPassword(tc.path("password").asText(""))
                  .enterConfirmPassword(confirmPass)
                  .clickCreateAccount();

        StepLogger.step("Verify Password validation error blocks account creation");
        Assert.assertTrue(signUpPage.waitForValidationOrStay(3),
                "Empty Password must block account creation.");
        StepLogger.pass("Empty Password rejected as expected");
    }

    @Test(priority = 5, groups = {"doctor", "regression"},
          description = "TC_ID_SIGNUP-005: Verify account creation fails with empty Re-enter Password")
    public void testEmptyConfirmPasswordValidation() {
        JsonNode tc = signupData.path("emptyConfirmPassword");
        String password = AuthFixtures.newPassword();

        StepLogger.step("Enter Full Name, Email, Password, leave Re-enter Password empty, tap Create Account");
        signUpPage.enterFullName(tc.path("fullName").asText("John Doe"))
                  .enterEmail(tc.path("email").asText("test@email.com"))
                  .enterPassword(password)
                  .enterConfirmPassword(tc.path("confirmPassword").asText(""))
                  .clickCreateAccount();

        StepLogger.step("Verify Re-enter Password validation error blocks account creation");
        Assert.assertTrue(signUpPage.waitForValidationOrStay(3),
                "Empty Re-enter Password must block account creation.");
        StepLogger.pass("Empty Re-enter Password rejected as expected");
    }

    @Test(priority = 6, groups = {"doctor", "regression"},
          description = "TC_ID_SIGNUP-006: Verify account creation fails with invalid email format")
    public void testInvalidEmailFormatValidation() {
        JsonNode tc = signupData.path("invalidEmail");
        String fullName = tc.path("fullName").asText("John Doe");
        String password = AuthFixtures.newPassword();

        for (JsonNode badEmailNode : tc.path("invalidEmails")) {
            String invalidEmail = badEmailNode.asText();
            StepLogger.step("Test invalid email format: '" + invalidEmail + "'");
            signUpPage.enterFullName(fullName)
                      .enterEmail(invalidEmail)
                      .enterPassword(password)
                      .enterConfirmPassword(password)
                      .clickCreateAccount();

            Assert.assertTrue(signUpPage.waitForValidationOrStay(3),
                    "Invalid email '" + invalidEmail + "' must be rejected.");
            StepLogger.pass("Invalid email '" + invalidEmail + "' rejected as expected");
        }
    }

    @Test(priority = 7, groups = {"doctor", "regression"},
          description = "TC_ID_SIGNUP-007: Verify account creation fails when passwords do not match")
    public void testPasswordMismatchValidation() {
        JsonNode tc = signupData.path("passwordMismatch");
        String pass1 = AuthFixtures.newPassword();
        String pass2 = AuthFixtures.newPassword();

        StepLogger.step("Enter mismatched Password and Re-enter Password and tap Create Account");
        signUpPage.enterFullName(tc.path("fullName").asText("John Doe"))
                  .enterEmail(tc.path("email").asText("test@email.com"))
                  .enterPassword(pass1)
                  .enterConfirmPassword(pass2)
                  .clickCreateAccount();

        StepLogger.step("Verify 'Passwords do not match' error blocks account creation");
        Assert.assertTrue(signUpPage.waitForValidationOrStay(3),
                "Mismatched passwords must block account creation.");
        StepLogger.pass("Password mismatch rejected as expected");
    }

    @Test(priority = 8, groups = {"doctor", "regression"},
          description = "TC_ID_SIGNUP-008: Verify account creation fails with existing email")
    public void testExistingEmailRejected() {
        JsonNode tc = signupData.path("existingEmail");
        String existingEmail = tc.path("email").asText("already.registered@email.com");
        String password = AuthFixtures.newPassword();

        StepLogger.step("Enter already-registered email and submit Sign Up");
        signUpPage.enterFullName(tc.path("fullName").asText("John Doe"))
                  .enterEmail(existingEmail)
                  .enterPassword(password)
                  .enterConfirmPassword(password)
                  .clickCreateAccount();

        StepLogger.step("Verify duplicate/existing email error or user stays on Sign Up");
        Assert.assertTrue(signUpPage.waitForValidationOrStay(4),
                "Account creation with existing email must fail or remain on Sign Up.");
        StepLogger.pass("Existing email check completed cleanly");
    }
}
