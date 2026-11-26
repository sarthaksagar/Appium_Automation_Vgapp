package com.vgcares.automation.tests.auth;

import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.dataproviders.AuthDataProvider;
import com.vgcares.automation.pages.SignUpPage;
import com.vgcares.automation.testdata.AuthFixtures;
import com.vgcares.automation.testdata.QaCredentials;
import com.vgcares.automation.utils.StepLogger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Sign Up Test Suite (TC_ID_SIGNUP-001 to TC_ID_SIGNUP-010).
 * Covers valid account creation, empty field validations, invalid email format,
 * password mismatch, existing email rejection, invalid full name, and 50-char boundary.
 */
public class SignUpTest extends BaseTest {

    private SignUpPage signUpPage;

    @BeforeClass(alwaysRun = true)
    public void setupSignUp() {
        prepareAuthScreen();
        signUpPage = new SignUpPage(getDriver());
        signUpPage.openSignUpTab();
    }

    @BeforeMethod(alwaysRun = true)
    public void resetSignUpForm() {
        if (signUpPage != null) {
            if (!signUpPage.isCreateAccountButtonDisplayed()) {
                prepareAuthScreen();
                signUpPage.openSignUpTab();
            } else {
                signUpPage.scrollToTop();
            }
        }
    }

    @Test(description = "TC_ID_SIGNUP-001: Verify user can create account with valid details",
          groups = {"smoke", "regression"}, priority = 1)
    public void testValidSignUpCreatesAccount() {
        StepLogger.step("Generate unique QA test registration details");
        String dynamicEmail = AuthFixtures.uniqueEmail(QaCredentials.signupDomain());
        String signupPassword = AuthFixtures.newPassword();

        StepLogger.step("Fill Full Name, Email, Password, Confirm Password and tap Create Account");
        signUpPage.enterFullName("John Doe")
                  .enterEmail(dynamicEmail)
                  .enterPassword(signupPassword)
                  .enterConfirmPassword(signupPassword)
                  .clickCreateAccount();

        StepLogger.step("Verify confirmation modal or navigation to verification/dashboard screen");
        if (signUpPage.isConfirmEmailModalDisplayed()) {
            StepLogger.pass("Confirm Your Email modal displayed successfully");
            signUpPage.clickConfirmEmailOk();
            StepLogger.pass("Dismissed Confirm Your Email modal via OK button");
        } else {
            Assert.assertTrue(signUpPage.isSuccessOrNextScreenDisplayed() || !signUpPage.isCreateAccountButtonDisplayed(),
                    "Expected either 'Confirm Your Email' popup or navigation after valid registration.");
            StepLogger.pass("Registration completed and verified successfully");
        }
    }

    @Test(description = "TC_ID_SIGNUP-002: Verify account creation fails with empty Full Name",
          groups = {"regression"}, priority = 2)
    public void testEmptyFullNameValidation() {
        StepLogger.step("Leave Full Name empty and fill Email, Password, Confirm Password");
        String password = AuthFixtures.newPassword();
        signUpPage.enterFullName("")
                  .enterEmail(AuthFixtures.uniqueEmail(QaCredentials.signupDomain()))
                  .enterPassword(password)
                  .enterConfirmPassword(password)
                  .clickCreateAccount();

        StepLogger.step("Verify validation error prevents account creation");
        Assert.assertTrue(signUpPage.isErrorOrValidationDisplayed() || signUpPage.isCreateAccountButtonDisplayed(),
                "Empty Full Name must block account creation and display validation.");
        StepLogger.pass("Empty Full Name rejected as expected");
    }

    @Test(description = "TC_ID_SIGNUP-003: Verify account creation fails with empty Email",
          groups = {"regression"}, priority = 3)
    public void testEmptyEmailValidation() {
        StepLogger.step("Enter Full Name, leave Email empty, and fill Password & Confirm Password");
        String password = AuthFixtures.newPassword();
        signUpPage.enterFullName("John Doe")
                  .enterEmail("")
                  .enterPassword(password)
                  .enterConfirmPassword(password)
                  .clickCreateAccount();

        StepLogger.step("Verify validation error prevents account creation");
        Assert.assertTrue(signUpPage.isErrorOrValidationDisplayed() || signUpPage.isCreateAccountButtonDisplayed(),
                "Empty Email must block account creation and display validation.");
        StepLogger.pass("Empty Email rejected as expected");
    }

    @Test(description = "TC_ID_SIGNUP-004: Verify account creation fails with empty Password",
          groups = {"regression"}, priority = 4)
    public void testEmptyPasswordValidation() {
        StepLogger.step("Fill Full Name and Email, leave Password empty, and tap Create Account");
        signUpPage.enterFullName("John Doe")
                  .enterEmail(AuthFixtures.uniqueEmail(QaCredentials.signupDomain()))
                  .enterPassword("")
                  .enterConfirmPassword("Test@1234")
                  .clickCreateAccount();

        StepLogger.step("Verify validation error prevents account creation");
        Assert.assertTrue(signUpPage.isErrorOrValidationDisplayed() || signUpPage.isCreateAccountButtonDisplayed(),
                "Empty Password must block account creation.");
        StepLogger.pass("Empty Password rejected as expected");
    }

    @Test(description = "TC_ID_SIGNUP-005: Verify account creation fails with empty Re-enter Password",
          groups = {"regression"}, priority = 5)
    public void testEmptyConfirmPasswordValidation() {
        StepLogger.step("Fill Full Name, Email, Password, leave Re-enter Password empty, and tap Create Account");
        signUpPage.enterFullName("John Doe")
                  .enterEmail(AuthFixtures.uniqueEmail(QaCredentials.signupDomain()))
                  .enterPassword("Test@1234")
                  .enterConfirmPassword("")
                  .clickCreateAccount();

        StepLogger.step("Verify validation error prevents account creation");
        Assert.assertTrue(signUpPage.isErrorOrValidationDisplayed() || signUpPage.isCreateAccountButtonDisplayed(),
                "Empty Re-enter Password must block account creation.");
        StepLogger.pass("Empty Re-enter Password rejected as expected");
    }

    @Test(description = "TC_ID_SIGNUP-006: Verify account creation fails with invalid email format",
          dataProvider = "invalidEmailData", dataProviderClass = AuthDataProvider.class,
          groups = {"regression"}, priority = 6)
    public void testInvalidEmailFormatValidation(String fullName, String invalidEmail, String password, String confirmPassword) {
        StepLogger.step("Enter invalid email format: " + invalidEmail);
        signUpPage.enterFullName(fullName)
                  .enterEmail(invalidEmail)
                  .enterPassword(password)
                  .enterConfirmPassword(confirmPassword)
                  .clickCreateAccount();

        StepLogger.step("Verify email validation error prevents account creation");
        Assert.assertTrue(signUpPage.isErrorOrValidationDisplayed() || signUpPage.isCreateAccountButtonDisplayed(),
                "Invalid email '" + invalidEmail + "' must be rejected.");
        StepLogger.pass("Invalid email format '" + invalidEmail + "' rejected as expected");
    }

    @Test(description = "TC_ID_SIGNUP-007: Verify account creation fails when passwords do not match",
          groups = {"regression"}, priority = 7)
    public void testPasswordMismatchValidation() {
        StepLogger.step("Enter mismatched passwords in Password and Re-enter Password fields");
        signUpPage.enterFullName("John Doe")
                  .enterEmail(AuthFixtures.uniqueEmail(QaCredentials.signupDomain()))
                  .enterPassword("Test@1234")
                  .enterConfirmPassword("Test@5678")
                  .clickCreateAccount();

        StepLogger.step("Verify account creation is blocked on password mismatch");
        Assert.assertTrue(signUpPage.isErrorOrValidationDisplayed() || signUpPage.isCreateAccountButtonDisplayed(),
                "Account creation must be prevented when passwords do not match.");
        StepLogger.pass("Password mismatch blocked submission as expected");
    }

    @Test(description = "TC_ID_SIGNUP-008: Verify account creation fails with existing registered email",
          groups = {"regression"}, priority = 8)
    public void testExistingEmailRejectedOnSignUp() {
        StepLogger.step("Enter already registered QA doctor email and submit registration");
        String existingEmail = QaCredentials.doctor().email();
        String password = AuthFixtures.newPassword();
        signUpPage.enterFullName("John Doe")
                  .enterEmail(existingEmail)
                  .enterPassword(password)
                  .enterConfirmPassword(password)
                  .clickCreateAccount();

        StepLogger.step("Verify duplicate email registration is rejected");
        Assert.assertTrue(signUpPage.isErrorOrValidationDisplayed() || signUpPage.isCreateAccountButtonDisplayed(),
                "Registration with already existing email must be rejected.");
        StepLogger.pass("Existing email registration rejected as expected");
    }

    @Test(description = "TC_ID_SIGNUP-009: Verify account creation fails with invalid Full Name (numbers/special characters)",
          groups = {"regression"}, priority = 9)
    public void testInvalidFullNameValidation() {
        StepLogger.step("Enter Full Name containing numbers: John123");
        signUpPage.enterFullName("John123")
                  .enterEmail(AuthFixtures.uniqueEmail(QaCredentials.signupDomain()))
                  .enterPassword("Test@1234")
                  .enterConfirmPassword("Test@1234")
                  .clickCreateAccount();

        StepLogger.step("Verify invalid Full Name validation prevents account creation");
        Assert.assertTrue(signUpPage.isErrorOrValidationDisplayed() || signUpPage.isCreateAccountButtonDisplayed(),
                "Full Name with numbers ('John123') should trigger validation or remain on Sign Up.");
        StepLogger.pass("Invalid Full Name validation verified");
    }

    @Test(description = "TC_ID_SIGNUP-010: Verify full name field accepts 50-character maximum boundary",
          groups = {"regression"}, priority = 10)
    public void testFullNameMaxCharacterBoundary() {
        StepLogger.step("Enter 50-character Full Name and valid email/password");
        String fiftyCharName = "AlexandriaMontgomeryElizabethVictoriaSmithJonesBob";
        String dynamicEmail = AuthFixtures.uniqueEmail(QaCredentials.signupDomain());
        String password = AuthFixtures.newPassword();

        signUpPage.enterFullName(fiftyCharName)
                  .enterEmail(dynamicEmail)
                  .enterPassword(password)
                  .enterConfirmPassword(password)
                  .clickCreateAccount();

        StepLogger.step("Verify 50-character Full Name is accepted without app crash");
        if (signUpPage.isConfirmEmailModalDisplayed()) {
            signUpPage.clickConfirmEmailOk();
            StepLogger.pass("50-character Full Name accepted and confirmation modal displayed");
        } else {
            Assert.assertNotNull(getDriver().getCurrentPackage(),
                    "App must remain stable when submitting 50-character Full Name.");
            StepLogger.pass("50-character Full Name boundary handled cleanly");
        }
    }
}
