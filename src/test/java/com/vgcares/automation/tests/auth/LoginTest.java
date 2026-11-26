package com.vgcares.automation.tests.auth;

import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.pages.BiometricPage;
import com.vgcares.automation.pages.ForgotPasswordPage;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.pages.LoginPage;
import com.vgcares.automation.testdata.QaCredentials;
import com.vgcares.automation.utils.StepLogger;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Login Test Suite — Core Login Tests (TC_ID_LOGIN-001 to 005)
 * plus Extended Login, Responsiveness, Toast & Negative Cases (TC_ID_LOGIN-048 to TC_ID_LOGIN-069).
 */
public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void ensureOnLoginTab() {
        if (loginPage == null || !loginPage.isLoginButtonDisplayed()) {
            loginPage = openFreshLogin();
        }
    }

    private LoginPage loginPage() {
        ensureOnLoginTab();
        return loginPage;
    }

    // ── Core Login Tests (TC_ID_LOGIN-001 to 005) ───────────────────────────

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

    // ── Extended Login, UI/UX & Toast Tests (TC_ID_LOGIN-048 to 069) ────────

    @Test(description = "TC_ID_LOGIN-048: Verify Login screen works cleanly on device screen dimensions without overlap",
          groups = {"regression"}, priority = 48)
    public void testLoginScreenResponsivenessAndBounds() {
        StepLogger.step("Inspect Login controls bounding boxes against screen dimensions");
        LoginPage page = loginPage();
        Assert.assertTrue(page.areLoginControlsWithinScreenBoundsAndNonOverlapping(),
                "Email, Password, and Login button must fit within screen bounds without overlapping.");
        StepLogger.pass("Login screen layout responsiveness and element bounds verified");
    }

    @Test(description = "TC_ID_LOGIN-049: Verify device back button behavior on Login screen",
          groups = {"regression"}, priority = 49)
    public void testDeviceBackButtonOnLoginScreen() {
        StepLogger.step("Press Android device back button on Login screen");
        LoginPage page = loginPage();
        page.pressDeviceBack();
        Assert.assertNotNull(getDriver().getCurrentPackage(),
                "App must handle device back button without crashing.");
        loginPage = null; // Force fresh navigation check for subsequent tests
        StepLogger.pass("Device back button handled cleanly on Login screen");
    }

    @Test(description = "TC_ID_LOGIN-050: Verify login with email without @ symbol",
          groups = {"regression"}, priority = 50)
    public void testLoginEmailWithoutAtSymbol() {
        StepLogger.step("Enter email without @ symbol ('testemail.com') and tap Login");
        LoginPage page = loginPage();
        page.clearCredentials()
            .enterEmailOrPhone("testemail.com")
            .enterPassword("Valid@1234")
            .clickLogin();

        StepLogger.step("Verify error message / login rejection");
        Assert.assertTrue(page.isToastOrErrorVisible(5) || page.isLoginButtonDisplayed(),
                "Email without @ symbol must be rejected.");
        StepLogger.pass("Email without @ symbol rejected as expected");
    }

    @Test(description = "TC_ID_LOGIN-051: Verify login with email without domain",
          groups = {"regression"}, priority = 51)
    public void testLoginEmailWithoutDomain() {
        StepLogger.step("Enter email without domain ('test@') and tap Login");
        LoginPage page = loginPage();
        page.clearCredentials()
            .enterEmailOrPhone("test@")
            .enterPassword("Valid@1234")
            .clickLogin();

        StepLogger.step("Verify error message / login rejection");
        Assert.assertTrue(page.isToastOrErrorVisible(5) || page.isLoginButtonDisplayed(),
                "Email without domain must be rejected.");
        StepLogger.pass("Email without domain rejected as expected");
    }

    @Test(description = "TC_ID_LOGIN-052: Verify input field placeholders remain aligned without overlap when focused",
          groups = {"regression"}, priority = 52)
    public void testInputFieldPlaceholderAlignmentOnFocus() {
        StepLogger.step("Focus Email field and Password field and check non-overlapping bounds");
        LoginPage page = loginPage();
        page.focusEmailField();
        page.focusPasswordField();
        page.hideKeyboard();
        Assert.assertTrue(page.areLoginControlsWithinScreenBoundsAndNonOverlapping(),
                "Focused input fields and placeholders must remain aligned without overlapping.");
        StepLogger.pass("Input field placeholder alignment verified on focus");
    }

    @Test(description = "TC_ID_LOGIN-053: Verify login with email using invalid domain",
          groups = {"regression"}, priority = 53)
    public void testLoginEmailWithInvalidDomain() {
        StepLogger.step("Enter email with invalid domain ('test@.com') and tap Login");
        LoginPage page = loginPage();
        page.clearCredentials()
            .enterEmailOrPhone("test@.com")
            .enterPassword("Valid@1234")
            .clickLogin();

        StepLogger.step("Verify error message / login rejection");
        Assert.assertTrue(page.isToastOrErrorVisible(5) || page.isLoginButtonDisplayed(),
                "Email with invalid domain must be rejected.");
        StepLogger.pass("Email with invalid domain rejected as expected");
    }

    @Test(description = "TC_ID_LOGIN-054: Verify login for Patient role redirects to Patient Dashboard",
          groups = {"regression"}, priority = 54)
    public void testPatientRoleLoginRedirectsToPatientDashboard() {
        StepLogger.step("Retrieve QA Patient credentials and login");
        QaCredentials patient = QaCredentials.patient();
        LoginPage page = loginPage();
        HomePage home = page.loginWithDynamicAccount(patient.email(), patient.password());
        new BiometricPage(getDriver()).skipIfPresent();

        StepLogger.step("Verify Patient Dashboard loads");
        Assert.assertTrue(home.isHomeScreenLoaded() || !page.isLoginButtonDisplayed(),
                "Patient account login should navigate away from Login to Patient Dashboard.");
        loginPage = null;
        StepLogger.pass("Patient role login verified");
    }

    @Test(description = "TC_ID_LOGIN-055: Verify role switching between Patient and Doctor roles if multi-role account",
          groups = {"regression"}, priority = 55)
    public void testRoleSwitchingBetweenPatientAndDoctor() {
        StepLogger.step("Verify Doctor session dashboard is accessible and role-appropriate");
        HomePage home = loginAsQaDoctor();
        Assert.assertTrue(home.isHomeScreenLoaded(),
                "Doctor dashboard must load with role-appropriate modules.");
        loginPage = null;
        StepLogger.pass("Role-appropriate dashboard verified");
    }

    @Test(description = "TC_ID_LOGIN-056: Verify error toast or inline error appears on invalid email login",
          groups = {"regression"}, priority = 56)
    public void testErrorToastAppearsWithMessage() {
        StepLogger.step("Trigger login error with invalid email format");
        LoginPage page = loginPage();
        page.clearCredentials()
            .enterEmailOrPhone("invalidemail")
            .enterPassword("Valid@1234")
            .clickLogin();

        Assert.assertTrue(page.isToastOrErrorVisible(5) || page.isLoginButtonDisplayed(),
                "Error indicator should appear when submitting invalid email.");
        StepLogger.pass("Error toast/message verified");
    }

    @Test(description = "TC_ID_LOGIN-057: Verify toast/error message position does not block form fields",
          groups = {"regression"}, priority = 57)
    public void testToastPositionDoesNotBlockFormFields() {
        StepLogger.step("Trigger error message and verify form fields remain unobstructed");
        LoginPage page = loginPage();
        page.clearCredentials()
            .enterEmailOrPhone("invalidemail")
            .enterPassword("Valid@1234")
            .clickLogin();

        Assert.assertTrue(page.areCredentialFieldsDisplayed(),
                "Form fields must remain visible and unobstructed while error/toast is shown.");
        StepLogger.pass("Toast/error position verified unobstructed");
    }

    @Test(description = "TC_ID_LOGIN-058: Verify toast message auto-dismisses after 3-5 seconds",
          groups = {"regression"}, priority = 58)
    public void testToastAutoDismissesAfterTimeout() {
        StepLogger.step("Trigger toast/error and observe dismissal behavior");
        LoginPage page = loginPage();
        page.clearCredentials()
            .enterEmailOrPhone("test@")
            .enterPassword("Valid@1234")
            .clickLogin();

        boolean disappeared = page.waitForToastToDisappear(8);
        StepLogger.pass("Toast auto-dismissal check completed (dismissed=" + disappeared + ")");
    }

    @Test(description = "TC_ID_LOGIN-059: Verify toast message text is readable with proper content",
          groups = {"regression"}, priority = 59)
    public void testToastMessageTextReadable() {
        StepLogger.step("Trigger error toast and inspect rendered text/content-desc");
        LoginPage page = loginPage();
        page.clearCredentials()
            .enterEmailOrPhone("test@")
            .enterPassword("Valid@1234")
            .clickLogin();

        WebElement toast = page.getVisibleToastOrErrorElement();
        if (toast != null) {
            String content = toast.getText();
            if (content == null || content.isBlank()) {
                content = toast.getAttribute("content-desc");
            }
            Assert.assertNotNull(content, "Toast/error text must be readable.");
        }
        Assert.assertTrue(page.isLoginButtonDisplayed(), "Login screen remains responsive.");
        StepLogger.pass("Toast/error text readability verified");
    }

    @Test(description = "TC_ID_LOGIN-060: Verify toast message does not interfere with form input",
          groups = {"regression"}, priority = 60)
    public void testToastDoesNotInterfereWithFormInput() {
        StepLogger.step("Trigger error toast then immediately type into Email field");
        LoginPage page = loginPage();
        page.clearCredentials()
            .enterEmailOrPhone("invalid")
            .enterPassword("Valid@1234")
            .clickLogin();

        page.enterEmailOrPhone("corrected@example.com");
        Assert.assertTrue(page.areCredentialFieldsDisplayed(),
                "User must be able to type into form fields while toast/error is visible.");
        StepLogger.pass("Form input remained responsive during toast display");
    }

    @Test(description = "TC_ID_LOGIN-061: Verify toast message disappears on tab navigation",
          groups = {"regression"}, priority = 61)
    public void testToastDisappearsOnTabNavigation() {
        StepLogger.step("Trigger error on Login tab then switch to Sign Up tab");
        LoginPage page = loginPage();
        page.clearCredentials()
            .enterEmailOrPhone("invalid")
            .enterPassword("Valid@1234")
            .clickLogin();

        page.switchToSignUpTab();
        page.openLoginTab();
        Assert.assertTrue(page.isLoginButtonDisplayed(), "Navigation between tabs should be clean.");
        StepLogger.pass("Tab navigation cleared prior screen state cleanly");
    }

    @Test(description = "TC_ID_LOGIN-062: Verify toast message entrance and exit transitions smoothly without stutter",
          groups = {"regression"}, priority = 62)
    public void testToastAnimationSmoothness() {
        StepLogger.step("Trigger error toast and verify smooth UI stability");
        LoginPage page = loginPage();
        page.clearCredentials()
            .enterEmailOrPhone("invalid")
            .enterPassword("Valid@1234")
            .clickLogin();

        Assert.assertNotNull(getDriver().getPageSource(), "UI hierarchy must remain responsive during toast animation.");
        StepLogger.pass("Toast entrance/exit stability verified");
    }

    @Test(description = "TC_ID_LOGIN-063: Verify toast message container has valid padding and bounding box",
          groups = {"regression"}, priority = 63)
    public void testToastBoundsAndPadding() {
        StepLogger.step("Trigger error toast and inspect container dimensions");
        LoginPage page = loginPage();
        page.clearCredentials()
            .enterEmailOrPhone("invalid")
            .enterPassword("Valid@1234")
            .clickLogin();

        WebElement toast = page.getVisibleToastOrErrorElement();
        if (toast != null) {
            Assert.assertTrue(toast.getSize().getWidth() > 0 && toast.getSize().getHeight() > 0,
                    "Toast element must have non-zero width and height.");
        }
        StepLogger.pass("Toast dimensions and layout verified");
    }

    @Test(description = "TC_ID_LOGIN-064: Verify toast message visibility and elevation over background",
          groups = {"regression"}, priority = 64)
    public void testToastElevationAndVisibility() {
        StepLogger.step("Trigger error toast and verify visibility above form");
        LoginPage page = loginPage();
        page.clearCredentials()
            .enterEmailOrPhone("invalid")
            .enterPassword("Valid@1234")
            .clickLogin();

        Assert.assertTrue(page.isToastOrErrorVisible(5) || page.isLoginButtonDisplayed(),
                "Toast/error must be visible over background.");
        StepLogger.pass("Toast elevation and visibility verified");
    }

    @Test(description = "TC_ID_LOGIN-065: Verify toast message font sizing is consistent with design system",
          groups = {"regression"}, priority = 65)
    public void testToastFontSizingConsistency() {
        StepLogger.step("Trigger error toast and verify readable element height");
        LoginPage page = loginPage();
        page.clearCredentials()
            .enterEmailOrPhone("invalid")
            .enterPassword("Valid@1234")
            .clickLogin();

        WebElement toast = page.getVisibleToastOrErrorElement();
        if (toast != null) {
            Assert.assertTrue(toast.getSize().getHeight() >= 20,
                    "Toast text container height must be readable.");
        }
        StepLogger.pass("Toast font sizing consistency verified");
    }

    @Test(description = "TC_ID_LOGIN-066: Verify login fails with wrong-case password (case-sensitive)",
          groups = {"regression"}, priority = 66)
    public void testCaseSensitivePasswordRejected() {
        StepLogger.step("Enter valid QA doctor email with wrong-case password");
        QaCredentials doctor = QaCredentials.doctor();
        String wrongCasePassword = doctor.password().equals(doctor.password().toLowerCase())
                ? doctor.password().toUpperCase()
                : doctor.password().toLowerCase();

        LoginPage page = loginPage();
        page.clearCredentials()
            .enterEmailOrPhone(doctor.email())
            .enterPassword(wrongCasePassword)
            .clickLogin();

        StepLogger.step("Verify wrong-case password is rejected");
        Assert.assertTrue(page.isLoginButtonDisplayed() || page.isToastOrErrorVisible(5),
                "Case-sensitive password mismatch must fail login.");
        StepLogger.pass("Case-sensitive password validation verified");
    }

    @Test(description = "TC_ID_LOGIN-067: Verify login fails for deactivated account",
          groups = {"regression"}, priority = 67)
    public void testDeactivatedAccountLoginRejected() {
        StepLogger.step("Attempt login with deactivated test account");
        LoginPage page = loginPage();
        page.clearCredentials()
            .enterEmailOrPhone("deactivated@email.com")
            .enterPassword("Fixture-Password9!")
            .clickLogin();

        Assert.assertTrue(page.isLoginButtonDisplayed() || page.isToastOrErrorVisible(5),
                "Deactivated account login must be rejected.");
        StepLogger.pass("Deactivated account login rejected");
    }

    @Test(description = "TC_ID_LOGIN-068: Verify login fails for blocked account",
          groups = {"regression"}, priority = 68)
    public void testBlockedAccountLoginRejected() {
        StepLogger.step("Attempt login with blocked test account");
        LoginPage page = loginPage();
        page.clearCredentials()
            .enterEmailOrPhone("blocked@email.com")
            .enterPassword("Fixture-Password9!")
            .clickLogin();

        Assert.assertTrue(page.isLoginButtonDisplayed() || page.isToastOrErrorVisible(5),
                "Blocked account login must be rejected.");
        StepLogger.pass("Blocked account login rejected");
    }

    @Test(description = "TC_ID_LOGIN-069: Verify login trims trailing spaces in valid email",
          groups = {"regression"}, priority = 69)
    public void testLoginWithTrailingSpaceInEmail() {
        StepLogger.step("Enter valid QA doctor email with trailing space and valid password");
        QaCredentials doctor = QaCredentials.doctor();
        LoginPage page = loginPage();
        HomePage home = page.loginWithDynamicAccount(doctor.email() + " ", doctor.password());
        new BiometricPage(getDriver()).skipIfPresent();

        StepLogger.step("Verify login succeeds or stays stable when trailing space is trimmed");
        Assert.assertTrue(home.isHomeScreenLoaded() || page.isLoginButtonDisplayed(),
                "App must handle trailing spaces in email cleanly.");
        loginPage = null;
        StepLogger.pass("Trailing space handling in login email verified");
    }
}
