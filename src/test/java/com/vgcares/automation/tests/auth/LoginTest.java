package com.vgcares.automation.tests.auth;

import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.pages.ForgotPasswordPage;
import com.vgcares.automation.pages.LoginPage;
import com.vgcares.automation.utils.StepLogger;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Login Test Suite — 5 Core High-Value Tests
 * Covers Login UI controls, empty submission validation, password masking security,
 * forgot password navigation, and authorized QA Doctor authentication.
 */
public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    private LoginPage loginPage() {
        if (loginPage == null) {
            loginPage = openFreshLogin();
        }
        return loginPage;
    }

    @Test(description = "TC_ID_LOGIN-001: Verify Login screen controls are displayed",
          groups = {"smoke", "regression"}, priority = 1)
    public void testLoginScreenControlsDisplayed() {
        StepLogger.step("Navigate to and open fresh Login screen");
        LoginPage page = loginPage();
        
        StepLogger.step("Verify visibility of Login tab, fields, and action buttons");
        Assert.assertTrue(page.isLoginTabDisplayed(), "Login tab must be visible.");
        Assert.assertTrue(page.areCredentialFieldsDisplayed(), "Email and password fields must be visible.");
        Assert.assertTrue(page.isLoginButtonDisplayed(), "Login button must be visible.");
        Assert.assertTrue(page.isForgotPasswordDisplayed(), "Forgot Password link must be visible.");
        StepLogger.pass("All core Login screen UI controls verified successfully");
    }

    @Test(description = "TC_ID_LOGIN-002: Verify validation prevents submission when fields are empty",
          groups = {"regression"}, priority = 2)
    public void testEmptyFieldsValidationOnLogin() {
        StepLogger.step("Clear all input fields and tap Login");
        LoginPage page = loginPage();
        page.clearCredentials();
        page.clickLogin();

        StepLogger.step("Verify user remains on Login screen with submission rejected");
        Assert.assertTrue(page.isLoginButtonDisplayed(),
                "User must remain on Login screen when submitting empty credentials.");
        StepLogger.pass("Empty submission rejected as expected");
    }

    @Test(description = "TC_ID_LOGIN-003: Verify entered password is masked securely for privacy",
          groups = {"security", "regression"}, priority = 3)
    public void testPasswordMaskingSecurity() {
        StepLogger.step("Enter test password into password field");
        LoginPage page = loginPage();
        String secretPassword = "SecretPassword@123";
        page.enterPassword(secretPassword);

        StepLogger.step("Inspect UI page source to verify raw password is not exposed in clear text");
        String pageSource = getDriver().getPageSource();
        Assert.assertFalse(pageSource.contains(secretPassword),
                "Raw password text should never be exposed in clear text within UI hierarchy.");
        page.clearCredentials();
        StepLogger.pass("Password masking confirmed secure; no clear text found in page source");
    }

    @Test(description = "TC_ID_LOGIN-004: Verify navigation to Forgot Password screen from Login",
          groups = {"regression"}, priority = 4)
    public void testForgotPasswordNavigationFromLogin() {
        StepLogger.step("Tap 'Forgot Password?' link on Login screen");
        LoginPage page = loginPage();
        page.clickForgotPassword();

        StepLogger.step("Verify navigation to Forgot Password screen");
        ForgotPasswordPage fpPage = new ForgotPasswordPage(getDriver());
        Assert.assertTrue(fpPage.isForgotPasswordScreenDisplayed(),
                "Forgot Password screen should be displayed upon clicking link.");
        StepLogger.pass("Navigated to Forgot Password screen successfully");

        StepLogger.step("Tap 'Go to Sign In' to return to Login screen");
        fpPage.clickGoToSignIn();
        Assert.assertTrue(page.isLoginButtonDisplayed(),
                "Should return to Login screen after clicking 'Go to Sign In'.");
        StepLogger.pass("Returned to Login screen successfully");
    }

    @Test(description = "TC_ID_LOGIN-005: Verify login with dynamic live credentials navigates to Home Dashboard",
          groups = {"smoke", "regression"}, priority = 5)
    public void testDynamicLiveLoginToDashboard() {
        StepLogger.step("Authenticate using authorized QA Doctor account");
        loginAsQaDoctor();
        StepLogger.pass("Doctor logged in and Home Dashboard reached successfully");
    }
}
