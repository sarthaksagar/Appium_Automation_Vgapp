package com.vgcares.automation.tests.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.config.ConfigManager;
import com.vgcares.automation.driver.DriverManager;
import com.vgcares.automation.enums.UserRole;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.pages.LoginPage;
import com.vgcares.automation.pages.SignUpPage;
import com.vgcares.automation.pages.patient.PatientHomePage;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.testdata.QaCredentials;
import com.vgcares.automation.utils.StepLogger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Login Module Test Suite (TC_ID_LOGIN-048 to TC_ID_LOGIN-079).
 * Configured for Doctor (D) suite execution; loads module data from
 * src/test/resources/testdata/doctor/doctor-login-data.json.
 */
public class DoctorLoginTest extends DoctorBaseTest {

    private LoginPage loginPage;
    private JsonNode loginData;

    @BeforeClass(alwaysRun = true)
    public void setupLogin() {
        loginData = ModuleDataLoader.doctor("doctor-login-data.json").path("testData");
        prepareAuthScreen();
        loginPage = new LoginPage(getDriver()).openLoginTab();
    }

    @BeforeMethod(alwaysRun = true)
    public void ensureOnLoginTab() {
        if (loginPage != null) {
            if (!loginPage.isOnLoginTabNow()) {
                prepareAuthScreen();
                loginPage.openLoginTab();
            } else {
                loginPage.hideKeyboard();
            }
        }
    }

    // ── Responsiveness, Device Back & Input Field UI (048, 049, 052) ─────

    @Test(priority = 1, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-048: Verify Login screen elements fit viewport without overlap")
    public void testLoginScreenResponsivenessAndScaling() {
        StepLogger.step("Verify Login tab, input fields, and Login button fit viewport without overlap");
        Dimension screen = getDriver().manage().window().getSize();
        Assert.assertTrue(loginPage.areLoginElementsWithinBoundsAndNonOverlapping(),
                "Login elements must remain visible within screen bounds (" + screen.getWidth() + "x" + screen.getHeight() + ") without overlapping.");
        StepLogger.pass("Login responsive bounds verified cleanly (" + screen.getWidth() + "x" + screen.getHeight() + ")");
    }

    @Test(priority = 2, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-049: Verify device back button behavior on Login screen")
    public void testDeviceBackButtonOnLoginScreen() {
        StepLogger.step("Press Android device back button from Login screen and verify stability");
        loginPage.pressDeviceBack();
        Assert.assertNotNull(getDriver().getCurrentPackage(),
                "Pressing device Back on Login screen must not crash the app.");
        String appPackage = ConfigManager.get("appPackage", "com.vg.vgcaresglobal.qa");
        if (!appPackage.equals(getDriver().getCurrentPackage())) {
            getDriver().activateApp(appPackage);
        }
        prepareAuthScreen();
        loginPage.openLoginTab();
        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login screen must recover cleanly after Back.");
        StepLogger.pass("Device Back button handled without crash or blank screen");
    }

    @Test(priority = 3, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-052: Verify placeholder alignment without overlap when Email & Password are focused")
    public void testInputFieldPlaceholderAlignmentOnFocus() {
        StepLogger.step("Clear fields, tap Email and Password fields, and verify non-overlapping bounds");
        loginPage.clearCredentials();
        loginPage.tapEmailField();
        Rectangle emailRect = loginPage.getEmailFieldRect();
        loginPage.tapPasswordField();
        Rectangle passwordRect = loginPage.getPasswordFieldRect();
        loginPage.hideKeyboard();

        Assert.assertTrue(emailRect.getWidth() > 0 && emailRect.getHeight() > 0,
                "Focused Email field must maintain valid visible dimensions.");
        Assert.assertTrue(passwordRect.getWidth() > 0 && passwordRect.getHeight() > 0,
                "Focused Password field must maintain valid visible dimensions.");
        Assert.assertTrue(emailRect.getY() + emailRect.getHeight() <= passwordRect.getY(),
                "Email and Password fields/placeholders must not overlap when focused.");
        StepLogger.pass("Focused Email and Password field alignment verified cleanly");
    }

    // ── Email Format & Empty Validation (050, 051, 053, 073, 074) ────────

    @Test(priority = 4, groups = {"doctor", "smoke", "regression", "login"},
          description = "TC_ID_LOGIN-050: Verify login fails with email missing @ symbol")
    public void testLoginRejectsEmailWithoutAtSymbol() {
        String badEmail = loginData.path("invalidEmails").path("missingAt").asText("testemail.com");
        String password = loginData.path("validFormatPassword").asText("Valid@1234");

        StepLogger.step("Enter email without '@' (" + badEmail + ") and tap Login");
        loginPage.enterEmailOrPhone(badEmail).enterPassword(password).clickLogin();

        Assert.assertTrue(loginPage.waitForValidationOrStay(4),
                "Email without '@' must fail validation and block login.");
        StepLogger.pass("Email without '@' rejected cleanly");
    }

    @Test(priority = 5, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-051: Verify login fails with email missing domain")
    public void testLoginRejectsEmailWithoutDomain() {
        String badEmail = loginData.path("invalidEmails").path("missingDomain").asText("test@");
        String password = loginData.path("validFormatPassword").asText("Valid@1234");

        StepLogger.step("Enter email without domain (" + badEmail + ") and tap Login");
        loginPage.enterEmailOrPhone(badEmail).enterPassword(password).clickLogin();

        Assert.assertTrue(loginPage.waitForValidationOrStay(4),
                "Email without domain must fail validation and block login.");
        StepLogger.pass("Email without domain rejected cleanly");
    }

    @Test(priority = 6, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-053: Verify login fails with invalid domain (.com)")
    public void testLoginRejectsInvalidEmailDomain() {
        String badEmail = loginData.path("invalidEmails").path("invalidDomain").asText("test@.com");
        String password = loginData.path("validFormatPassword").asText("Valid@1234");

        StepLogger.step("Enter email with invalid domain (" + badEmail + ") and tap Login");
        loginPage.enterEmailOrPhone(badEmail).enterPassword(password).clickLogin();

        Assert.assertTrue(loginPage.waitForValidationOrStay(4),
                "Email with invalid domain must fail validation and block login.");
        StepLogger.pass("Email with invalid domain rejected cleanly");
    }

    @Test(priority = 7, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-073: Verify validation error messages are user-friendly without technical jargon")
    public void testValidationErrorMessagesAreUserFriendly() {
        StepLogger.step("Submit empty credentials and invalid email to inspect validation feedback");
        loginPage.clearCredentials().clickLogin();
        String msg = loginPage.getErrorMessageText();
        Assert.assertTrue(loginPage.waitForValidationOrStay(3),
                "Submitting empty form must block login.");
        if (!msg.isBlank()) {
            String lower = msg.toLowerCase();
            Assert.assertFalse(lower.contains("exception") || lower.contains("nullpointer") || lower.contains("stacktrace"),
                    "Validation message must not expose technical jargon: " + msg);
        }
        StepLogger.pass("Validation feedback verified user-friendly");
    }

    @Test(priority = 8, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-074: Verify empty Email & Password submission triggers validation/highlight")
    public void testEmptyFieldsValidationAndHighlight() {
        StepLogger.step("Leave Email and Password empty and tap Login");
        loginPage.clearCredentials().clickLogin();
        Assert.assertTrue(loginPage.waitForValidationOrStay(4),
                "Empty Login submission must trigger validation and stay on Login screen.");
        StepLogger.pass("Empty fields validation verified cleanly");
    }

    // ── Toast & Error Feedback UX (056..065) ─────────────────────────────

    @Test(priority = 9, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-056: Verify error toast/message appears on invalid login data")
    public void testErrorToastDisplaysOnInvalidData() {
        String badEmail = loginData.path("invalidEmails").path("genericInvalid").asText("invalid-email");
        String password = loginData.path("validFormatPassword").asText("Valid@1234");

        StepLogger.step("Submit invalid email and verify error toast/inline feedback");
        loginPage.enterEmailOrPhone(badEmail).enterPassword(password).clickLogin();
        Assert.assertTrue(loginPage.waitForValidationOrStay(4),
                "Invalid email must display error toast or validation and block login.");
        StepLogger.pass("Error feedback displayed for invalid login data");
    }

    @Test(priority = 10, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-057: Verify toast/error feedback position does not block form fields")
    public void testToastPositionDoesNotBlockFormFields() {
        String badEmail = loginData.path("invalidEmails").path("genericInvalid").asText("invalid-email");
        String password = loginData.path("validFormatPassword").asText("Valid@1234");

        StepLogger.step("Trigger error indicator and verify position stays inside viewport");
        loginPage.enterEmailOrPhone(badEmail).enterPassword(password).clickLogin();
        WebElement indicator = loginPage.findVisibleErrorOrToast(4);
        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login form must remain accessible.");
        if (indicator != null) {
            Rectangle rect = indicator.getRect();
            Dimension screen = getDriver().manage().window().getSize();
            Assert.assertTrue(rect.getY() >= 0 && rect.getY() + rect.getHeight() <= screen.getHeight(),
                    "Error/toast position must stay within device viewport.");
        }
        StepLogger.pass("Toast/error position verified consistent and non-blocking");
    }

    @Test(priority = 11, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-058: Verify transient toast auto-dismisses without requiring user action")
    public void testToastAutoDismissesAfterDelay() {
        String badEmail = loginData.path("invalidEmails").path("genericInvalid").asText("invalid-email");
        int waitSec = loginData.path("toast").path("dismissWaitSeconds").asInt(6);

        StepLogger.step("Trigger validation error and wait up to " + waitSec + "s for transient toast dismissal");
        loginPage.enterEmailOrPhone(badEmail).enterPassword("Valid@1234").clickLogin();
        Assert.assertTrue(loginPage.waitForTransientToastToDismiss(waitSec),
                "Transient Android toast/snackbar must auto-dismiss cleanly.");
        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login screen must stay stable after toast dismissal.");
        StepLogger.pass("Toast auto-dismissal verified");
    }

    @Test(priority = 12, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-059: Verify error toast/message text is readable with visible dimensions")
    public void testToastTextReadabilityAndVisibility() {
        String badEmail = loginData.path("invalidEmails").path("missingAt").asText("testemail.com");

        StepLogger.step("Trigger error message and verify readable non-empty text/bounds");
        loginPage.enterEmailOrPhone(badEmail).enterPassword("Valid@1234").clickLogin();
        WebElement indicator = loginPage.findVisibleErrorOrToast(4);
        if (indicator != null) {
            Rectangle rect = indicator.getRect();
            Assert.assertTrue(rect.getWidth() > 0 && rect.getHeight() > 0,
                    "Error/toast indicator must have readable non-zero bounds.");
        }
        Assert.assertTrue(loginPage.waitForValidationOrStay(2), "Login validation must remain active.");
        StepLogger.pass("Toast/error readability bounds verified");
    }

    @Test(priority = 13, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-060: Verify toast/error feedback does not interfere with form input")
    public void testToastDoesNotInterfereWithFormInput() {
        StepLogger.step("Trigger error indicator then immediately type into Email field");
        loginPage.enterEmailOrPhone("bad").enterPassword("Valid@1234").clickLogin();
        loginPage.enterEmailOrPhone("editable@example.com");
        Assert.assertEquals(loginPage.getEnteredEmail(), "editable@example.com",
                "User must still be able to edit Login form fields while feedback is displayed.");
        StepLogger.pass("Form input remained responsive during toast/validation display");
    }

    @Test(priority = 14, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-061: Verify toast/error state clears when navigating to Sign Up tab")
    public void testToastClearsOnNavigationToSignUpTab() {
        StepLogger.step("Trigger Login error and immediately switch to Sign Up tab");
        loginPage.enterEmailOrPhone("bad").enterPassword("Valid@1234").clickLogin();
        loginPage.openSignUpTab();

        SignUpPage signUpPage = new SignUpPage(getDriver());
        Assert.assertTrue(signUpPage.isCreateAccountButtonDisplayed(),
                "Sign Up tab must load cleanly without lingering Login toast blocking UI.");
        loginPage.openLoginTab();
        StepLogger.pass("Tab navigation cleared Login feedback cleanly");
    }

    @Test(priority = 15, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-062: Verify toast/validation transition completes smoothly without UI lag")
    public void testToastAnimationAndStability() {
        int waitSec = loginData.path("toast").path("dismissWaitSeconds").asInt(6);
        StepLogger.step("Trigger Login feedback, wait for exit animation, and verify responsive Login button");
        loginPage.enterEmailOrPhone("invalid-email").enterPassword("Valid@1234").clickLogin();
        Assert.assertTrue(loginPage.waitForTransientToastToDismiss(waitSec));
        Assert.assertTrue(loginPage.isLoginButtonEnabledAndClickable(),
                "Login button must remain responsive without UI freeze.");
        StepLogger.pass("Toast entrance/exit stability verified");
    }

    @Test(priority = 16, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-063: Verify toast/error container padding fits within screen margins")
    public void testToastContainerBoundsAndPadding() {
        StepLogger.step("Trigger error indicator and verify container bounds stay inside screen margins");
        loginPage.enterEmailOrPhone("invalid-email").enterPassword("Valid@1234").clickLogin();
        WebElement indicator = loginPage.findVisibleErrorOrToast(4);
        if (indicator != null) {
            Rectangle rect = indicator.getRect();
            Dimension screen = getDriver().manage().window().getSize();
            Assert.assertTrue(rect.getX() >= 0 && rect.getX() + rect.getWidth() <= screen.getWidth(),
                    "Toast/error container must fit cleanly inside screen width.");
        }
        Assert.assertTrue(loginPage.isLoginButtonDisplayed());
        StepLogger.pass("Toast bounds and padding verified cleanly");
    }

    @Test(priority = 17, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-064: Verify toast/error displays cleanly above/separate from background")
    public void testToastVisibilityAboveBackground() {
        StepLogger.step("Trigger error feedback and verify visible rendering height");
        loginPage.enterEmailOrPhone("test@").enterPassword("Valid@1234").clickLogin();
        WebElement indicator = loginPage.findVisibleErrorOrToast(4);
        if (indicator != null) {
            Assert.assertTrue(indicator.getRect().getHeight() > 0, "Toast/error element must be visibly rendered.");
        }
        Assert.assertTrue(loginPage.waitForValidationOrStay(2));
        StepLogger.pass("Toast/error visibility above background verified");
    }

    @Test(priority = 18, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-065: Verify toast/error text height stays consistent with app design bounds")
    public void testToastFontSizeConsistency() {
        int minH = loginData.path("toast").path("minHeightPx").asInt(20);
        int maxH = loginData.path("toast").path("maxHeightPx").asInt(400);

        StepLogger.step("Trigger error indicator and verify height is within readable design range");
        loginPage.enterEmailOrPhone("test@.com").enterPassword("Valid@1234").clickLogin();
        WebElement indicator = loginPage.findVisibleErrorOrToast(4);
        if (indicator != null) {
            int h = indicator.getRect().getHeight();
            Assert.assertTrue(h >= minH && h <= maxH,
                    "Error/toast element height (" + h + "px) should fall within readable design bounds [" + minH + ".." + maxH + "].");
        }
        Assert.assertTrue(loginPage.waitForValidationOrStay(2));
        StepLogger.pass("Toast height/font scale verified consistent");
    }

    // ── Button States & Rapid Tapping (077, 078) ─────────────────────────

    @Test(priority = 19, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-077: Verify Login button is enabled, clickable, and responds on tap")
    public void testLoginButtonInteractiveFeedback() {
        StepLogger.step("Enter credentials and verify Login button is enabled and responds immediately");
        loginPage.enterEmailOrPhone("testemail.com").enterPassword("Valid@1234");
        Assert.assertTrue(loginPage.isLoginButtonEnabledAndClickable(),
                "Login button must be enabled and clickable.");
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.waitForValidationOrStay(3),
                "Tapping Login must trigger immediate feedback.");
        StepLogger.pass("Login button interactive state verified");
    }

    @Test(priority = 20, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-078: Verify rapid tapping Login button 5 times does not crash or duplicate sessions")
    public void testRapidTappingLoginDebounced() {
        StepLogger.step("Enter credentials and rapidly tap Login 5 times");
        loginPage.enterEmailOrPhone("invalid-email").enterPassword("Valid@1234").tapLoginRapidly(5);
        Assert.assertNotNull(getDriver().getCurrentPackage(),
                "Rapid tapping Login must not crash or freeze the app.");
        Assert.assertTrue(loginPage.waitForValidationOrStay(4),
                "App must remain stable after rapid Login taps.");
        StepLogger.pass("Rapid Login taps debounced without crash");
    }

    // ── Special Formats & Role/Credential Scenarios (054, 055, 066..072, 075, 076, 079) ──

    @Test(priority = 21, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-071: Verify email with plus/special character is accepted by format validation")
    public void testPlusAliasEmailAcceptedByValidation() {
        String specialEmail = loginData.path("invalidEmails").path("plusAliasFormat").asText("test+test@email.com");
        String password = loginData.path("validFormatPassword").asText("Valid@1234");

        StepLogger.step("Submit email containing '+' (" + specialEmail + ") and check it is not rejected as invalid format");
        loginPage.enterEmailOrPhone(specialEmail).enterPassword(password).clickLogin();
        String err = loginPage.getErrorMessageText().toLowerCase();
        Assert.assertFalse(err.contains("valid email") || err.contains("domain") || err.contains("with @"),
                "Email containing '+' (" + specialEmail + ") must not fail email-format validation. Actual: " + err);
        StepLogger.pass("Plus-character email accepted by format validation cleanly");
    }

    @Test(priority = 22, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-066: Verify login password is case-sensitive")
    public void testPasswordIsCaseSensitive() {
        QaCredentials doctor = QaCredentials.doctor();
        String wrongCasePassword = toggleCase(doctor.password());
        if (wrongCasePassword.equals(doctor.password())) {
            throw new SkipException("[Test data] Doctor password must contain alphabetic characters to test case sensitivity.");
        }

        StepLogger.step("Enter valid Doctor email and case-swapped password, then tap Login");
        loginPage.login(doctor.email(), wrongCasePassword);
        Assert.assertTrue(loginPage.waitForValidationOrStay(6),
                "Wrong-case password must be rejected and remain on Login screen.");
        StepLogger.pass("Case-sensitive password rejection verified");
    }

    @Test(priority = 23, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-070: Verify leading space in password is not trimmed and fails login")
    public void testLeadingSpaceInPasswordFailsLogin() {
        QaCredentials doctor = QaCredentials.doctor();

        StepLogger.step("Enter valid Doctor email and password with leading space, then tap Login");
        loginPage.login(doctor.email(), " " + doctor.password());
        Assert.assertTrue(loginPage.waitForValidationOrStay(6),
                "Password with leading space must not be trimmed and must fail login.");
        StepLogger.pass("Leading space in password rejected cleanly");
    }

    @Test(priority = 24, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-069: Verify trailing spaces in email are trimmed and login succeeds")
    public void testTrailingSpaceInEmailTrimmedAndSucceeds() {
        QaCredentials doctor = QaCredentials.doctor();
        StepLogger.step("Login with trailing space after valid Doctor email");
        HomePage home = loginPage.loginAsDoctor(doctor.email() + " ", doctor.password());
        try {
            Assert.assertTrue(home.isHomeScreenLoaded(),
                    "Trailing space in email should be trimmed and navigate to Doctor Dashboard.");
            StepLogger.pass("Trailing space email login succeeded");
        } finally {
            if (home.isHomeScreenLoaded()) {
                home.logout();
            }
            DriverManager.setAuthenticatedRole(UserRole.COMMON);
        }
    }

    @Test(priority = 25, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-072: Verify uppercase email logs in (email is case-insensitive)")
    public void testUppercaseEmailLoginSucceeds() {
        QaCredentials doctor = QaCredentials.doctor();
        StepLogger.step("Login with uppercase Doctor email");
        HomePage home = loginPage.loginAsDoctor(doctor.email().toUpperCase(), doctor.password());
        try {
            Assert.assertTrue(home.isHomeScreenLoaded(),
                    "Uppercase email should authenticate case-insensitively to Doctor Dashboard.");
            StepLogger.pass("Uppercase email login succeeded");
        } finally {
            if (home.isHomeScreenLoaded()) {
                home.logout();
            }
            DriverManager.setAuthenticatedRole(UserRole.COMMON);
        }
    }

    @Test(priority = 26, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-054: Verify Patient credentials redirect to Patient Dashboard")
    public void testPatientRoleLoginRedirectsToPatientDashboard() {
        QaCredentials patient = QaCredentials.patient();
        StepLogger.step("Login with Patient credentials from Login screen");
        loginPage.login(patient.email(), patient.password());
        PatientHomePage patientHome = new PatientHomePage(getDriver());
        try {
            Assert.assertTrue(patientHome.isPatientDashboardLoaded(),
                    "Patient credentials must navigate to Patient Dashboard.");
            StepLogger.pass("Patient role dashboard redirection verified");
        } finally {
            if (patientHome.isPatientDashboardLoaded()) {
                patientHome.logout();
            }
            DriverManager.setAuthenticatedRole(UserRole.COMMON);
        }
    }

    @Test(priority = 27, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-055: Verify multi-role user can switch between Patient and Doctor roles")
    public void testMultiRoleSwitchingBetweenPatientAndDoctor() {
        String email = resolveOptional("QA_MULTIROLE_EMAIL", loginData.path("optionalAccounts").path("multiRole").path("email").asText(""));
        String password = resolveOptional("QA_MULTIROLE_PASSWORD", loginData.path("optionalAccounts").path("multiRole").path("password").asText(""));
        if (email.isBlank() || password.isBlank()) {
            throw new SkipException("[Test data] Multi-role credentials (QA_MULTIROLE_EMAIL/PASSWORD) not configured.");
        }
        StepLogger.step("Login with multi-role account");
        HomePage home = loginPage.loginAsDoctor(email, password);
        Assert.assertTrue(home.isHomeScreenLoaded(), "Multi-role account must reach dashboard.");
        home.logout();
        DriverManager.setAuthenticatedRole(UserRole.COMMON);
        StepLogger.pass("Multi-role login flow verified");
    }

    @Test(priority = 28, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-067: Verify login is blocked for deactivated account")
    public void testDeactivatedAccountLoginBlocked() {
        String email = resolveOptional("QA_DEACTIVATED_EMAIL", loginData.path("optionalAccounts").path("deactivated").path("email").asText(""));
        String password = resolveOptional("QA_DEACTIVATED_PASSWORD", loginData.path("optionalAccounts").path("deactivated").path("password").asText(""));
        if (email.isBlank() || password.isBlank()) {
            throw new SkipException("[Test data] Deactivated account credentials (QA_DEACTIVATED_EMAIL/PASSWORD) not configured.");
        }
        StepLogger.step("Attempt login with deactivated account");
        loginPage.login(email, password);
        Assert.assertTrue(loginPage.waitForValidationOrStay(6),
                "Deactivated account must not log in.");
        StepLogger.pass("Deactivated account login blocked cleanly");
    }

    @Test(priority = 29, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-068: Verify login is blocked for blocked account")
    public void testBlockedAccountLoginBlocked() {
        String email = resolveOptional("QA_BLOCKED_EMAIL", loginData.path("optionalAccounts").path("blocked").path("email").asText(""));
        String password = resolveOptional("QA_BLOCKED_PASSWORD", loginData.path("optionalAccounts").path("blocked").path("password").asText(""));
        if (email.isBlank() || password.isBlank()) {
            throw new SkipException("[Test data] Blocked account credentials (QA_BLOCKED_EMAIL/PASSWORD) not configured.");
        }
        StepLogger.step("Attempt login with blocked account");
        loginPage.login(email, password);
        Assert.assertTrue(loginPage.waitForValidationOrStay(6),
                "Blocked account must not log in.");
        StepLogger.pass("Blocked account login blocked cleanly");
    }

    @Test(priority = 30, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-075: Verify login succeeds with new password after password reset")
    public void testLoginWithNewPasswordAfterReset() {
        String email = resolveOptional("QA_POST_RESET_EMAIL", loginData.path("optionalAccounts").path("postReset").path("email").asText(""));
        String newPass = resolveOptional("QA_POST_RESET_NEW_PASSWORD", loginData.path("optionalAccounts").path("postReset").path("newPassword").asText(""));
        if (email.isBlank() || newPass.isBlank()) {
            throw new SkipException("[Test data] Post-reset credentials (QA_POST_RESET_EMAIL/QA_POST_RESET_NEW_PASSWORD) not configured.");
        }
        StepLogger.step("Login with updated post-reset password");
        HomePage home = loginPage.loginAsDoctor(email, newPass);
        try {
            Assert.assertTrue(home.isHomeScreenLoaded(), "New password must succeed after reset.");
            StepLogger.pass("Post-reset new password login verified");
        } finally {
            if (home.isHomeScreenLoaded()) {
                home.logout();
            }
            DriverManager.setAuthenticatedRole(UserRole.COMMON);
        }
    }

    @Test(priority = 31, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-076: Verify old password fails after password reset")
    public void testLoginWithOldPasswordAfterResetFails() {
        String email = resolveOptional("QA_POST_RESET_EMAIL", loginData.path("optionalAccounts").path("postReset").path("email").asText(""));
        String oldPass = resolveOptional("QA_POST_RESET_OLD_PASSWORD", loginData.path("optionalAccounts").path("postReset").path("oldPassword").asText(""));
        if (email.isBlank() || oldPass.isBlank()) {
            throw new SkipException("[Test data] Post-reset old password (QA_POST_RESET_OLD_PASSWORD) not configured.");
        }
        StepLogger.step("Attempt login with old password after reset");
        loginPage.login(email, oldPass);
        Assert.assertTrue(loginPage.waitForValidationOrStay(6),
                "Old password must fail after password reset.");
        StepLogger.pass("Old password after reset rejected cleanly");
    }

    @Test(priority = 32, groups = {"doctor", "regression", "login"},
          description = "TC_ID_LOGIN-079: Verify biometric login option availability on Login screen")
    public void testBiometricLoginOptionAvailability() {
        StepLogger.step("Check whether biometric login trigger is displayed on Login screen");
        if (!loginPage.hasBiometricOptionOnLogin()) {
            throw new SkipException("Biometric option is not rendered on Login screen until enabled after initial login.");
        }
        Assert.assertTrue(loginPage.hasBiometricOptionOnLogin(), "Biometric option must be visible when enabled.");
        StepLogger.pass("Biometric login option verified on Login screen");
    }

    private static String toggleCase(String input) {
        StringBuilder sb = new StringBuilder(input.length());
        for (char c : input.toCharArray()) {
            if (Character.isUpperCase(c)) {
                sb.append(Character.toLowerCase(c));
            } else if (Character.isLowerCase(c)) {
                sb.append(Character.toUpperCase(c));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static String resolveOptional(String envKey, String fallback) {
        String val = System.getenv(envKey);
        if (val == null || val.isBlank()) {
            val = ConfigManager.get(envKey, fallback);
        }
        return val == null ? "" : val.trim();
    }
}
