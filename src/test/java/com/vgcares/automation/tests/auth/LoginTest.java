package com.vgcares.automation.tests.auth;

import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.pages.ForgotPasswordPage;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.pages.LaunchPage;
import com.vgcares.automation.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Login Test Suite — 5 Core High-Value Tests
 * Covers login UI, empty validation, password masking, forgot password navigation,
 * and live dynamic authentication.
 */
public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeClass(alwaysRun = true)
    public void setupLogin() {
        new LaunchPage(getDriver()).passOnboardingIfPresent();
        loginPage = new LoginPage(getDriver());
        loginPage.openLoginTab();
    }

    @Test(description = "TC_ID_LOGIN-001: Verify Login screen controls are displayed",
          groups = {"smoke", "regression"}, priority = 1)
    public void testLoginScreenControlsDisplayed() {
        log.info("Executing TC_ID_LOGIN-001: Login Screen Controls Verification");
        Assert.assertTrue(loginPage.isLoginTabDisplayed(), "Login tab must be visible.");
        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login button must be visible.");
        Assert.assertTrue(loginPage.isForgotPasswordDisplayed(), "Forgot Password link must be visible.");
    }

    @Test(description = "TC_ID_LOGIN-002: Verify validation prevents submission when fields are empty",
          groups = {"regression"}, priority = 2)
    public void testEmptyFieldsValidationOnLogin() {
        log.info("Executing TC_ID_LOGIN-002: Submitting empty login credentials");
        loginPage.clearCredentials();
        loginPage.clickLogin();

        Assert.assertTrue(loginPage.isLoginButtonDisplayed(),
                "User must remain on Login screen when submitting empty credentials.");
    }

    @Test(description = "TC_ID_LOGIN-003: Verify entered password is masked securely for privacy",
          groups = {"security", "regression"}, priority = 3)
    public void testPasswordMaskingSecurity() {
        log.info("Executing TC_ID_LOGIN-003: Password Masking Verification");
        String secretPassword = "SecretPassword@123";
        loginPage.enterPassword(secretPassword);

        String pageSource = getDriver().getPageSource();
        Assert.assertFalse(pageSource.contains(secretPassword),
                "Raw password text should never be exposed in clear text within UI hierarchy.");
        loginPage.clearCredentials();
    }

    @Test(description = "TC_ID_LOGIN-004: Verify navigation to Forgot Password screen from Login",
          groups = {"regression"}, priority = 4)
    public void testForgotPasswordNavigationFromLogin() {
        log.info("Executing TC_ID_LOGIN-004: Navigating to Forgot Password");
        loginPage.clickForgotPassword();

        ForgotPasswordPage fpPage = new ForgotPasswordPage(getDriver());
        Assert.assertTrue(fpPage.isForgotPasswordScreenDisplayed(),
                "Forgot Password screen should be displayed upon clicking link.");

        // Return to login screen via 'Go to Sign In'
        fpPage.clickGoToSignIn();
        Assert.assertTrue(loginPage.isLoginButtonDisplayed(),
                "Should return to Login screen after clicking 'Go to Sign In'.");
    }

    @Test(description = "TC_ID_LOGIN-005: Verify login with dynamic live credentials navigates to Home Dashboard",
          groups = {"smoke", "regression"}, priority = 5)
    public void testDynamicLiveLoginToDashboard() {
        log.info("Executing TC_ID_LOGIN-005: Logging in using dynamic live account");
        HomePage homePage = loginPage.loginWithDynamicAccount("virajd@techvishwaguru.com", "Admin@123");

        Assert.assertTrue(homePage.isHomeScreenLoaded() || homePage.isTodaySnapshotDisplayed()
                || homePage.isBottomNavigationDisplayed(),
                "User must successfully land on Doctor Home Dashboard with live backend data.");
    }
}
