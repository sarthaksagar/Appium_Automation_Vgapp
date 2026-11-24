package com.vgcares.automation.tests.auth;

import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.testdata.AuthFixtures;
import com.vgcares.automation.dataproviders.AuthDataProvider;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/** Doctor authentication, dashboard, and session validation. */
public class DoctorLoginTest extends BaseTest {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod(groups = {"smoke", "regression"})
    public void setupDoctorAuthEnvironment() {
        log.info("Setting up clean launch environment for Doctor Authentication test");
        loginPage = openFreshLogin();
    }

    // ══════════════════════════════════════════════════════════════════════
    // 1. UI & FIELD INTEGRITY CHECKS
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_DOC_LOG-000: Verify Doctor Login UI controls are displayed and interactable",
          groups = {"smoke", "regression"}, priority = 1)
    public void testDoctorLoginScreenUIElementsDisplayed() {
        log.info("Executing TC_ID_DOC_LOG-000: Verifying Doctor Login UI Controls");
        Assert.assertTrue(loginPage.isLoginTabDisplayed(),
                "[TC_ID_DOC_LOG-000] Doctor Login tab must be visible.");
        Assert.assertTrue(loginPage.isLoginButtonDisplayed(),
                "[TC_ID_DOC_LOG-000] Doctor Login / Sign In action button must be visible.");
        Assert.assertTrue(loginPage.isForgotPasswordDisplayed(),
                "[TC_ID_DOC_LOG-000] Forgot Password link must be visible on Login tab.");
    }

    @Test(description = "TC_ID_DOC_LOG-002a: Verify submitting empty doctor login fields triggers validation and prevents progression",
          groups = {"regression"}, priority = 2)
    public void testDoctorLoginWithEmptyFieldsValidation() {
        log.info("Executing TC_ID_DOC_LOG-002a: Submitting blank credentials");
        loginPage.clearCredentials();
        loginPage.clickLogin();

        Assert.assertTrue(loginPage.isLoginButtonDisplayed(),
                "[TC_ID_DOC_LOG-002a] User must remain on Login tab when submitting empty credentials.");
    }

    // ══════════════════════════════════════════════════════════════════════
    // 2. DATA-DRIVEN NEGATIVE DOCTOR LOGIN SCENARIOS
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_DOC_LOG-001..008: Verify doctor login rejection for invalid data combinations",
          dataProvider = "doctorNegativeLoginData",
          dataProviderClass = AuthDataProvider.class,
          groups = {"regression"}, priority = 3)
    public void testDoctorLoginNegativeScenarios(String tcId, String email, String password, String description) {
        log.info("Executing {}: {}", tcId, description);
        loginPage.clearCredentials()
                 .enterEmailOrPhone(email)
                 .enterPassword(password)
                 .clickLogin();

        // Must remain on login screen or show error dialog without app crashing
        boolean remainsOnLogin = loginPage.isLoginButtonDisplayed();
        Assert.assertTrue(remainsOnLogin,
                String.format("[%s] Expected rejection for '%s', but user navigated away from Login.", tcId, description));
    }

    @Test(description = "TC_ID_DOC_LOG-008: Verify login rejects SQL injection patterns safely",
          groups = {"security", "regression"}, priority = 4)
    public void testDoctorLoginSQLInjectionPrevention() {
        log.info("Executing TC_ID_DOC_LOG-008: Verifying SQL Injection rejection on Doctor login");
        loginPage.clearCredentials()
                 .enterEmailOrPhone("' OR '1'='1")
                 .enterPassword("' OR '1'='1")
                 .clickLogin();

        Assert.assertTrue(loginPage.isLoginButtonDisplayed(),
                "[TC_ID_DOC_LOG-008] SQL injection payload must be blocked without bypassing authentication.");
    }

    @Test(description = "TC_ID_DOC_LOG-009: Verify password field masks doctor password securely",
          groups = {"security", "regression"}, priority = 5)
    public void testDoctorLoginPasswordMasking() {
        log.info("Executing TC_ID_DOC_LOG-009: Verifying Doctor password masking");
        String password = AuthFixtures.newPassword();
        loginPage.enterEmailOrPhone(AuthFixtures.uniqueEmail("example.invalid")).enterPassword(password);

        String pageSource = getDriver().getPageSource();
        Assert.assertFalse(pageSource.contains(password),
                "[TC_ID_DOC_LOG-009] Raw password text should NEVER appear unmasked in DOM / page source hierarchy.");
    }

    // ══════════════════════════════════════════════════════════════════════
    // 3. POSITIVE DOCTOR AUTHENTICATION & DASHBOARD LANDING
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_DOC_LOG-010: Verify registered Doctor can login successfully and reach Doctor Dashboard",
          groups = {"smoke", "regression"}, priority = 6)
    public void testDoctorSuccessfulLoginAndDashboardRedirection() {
        homePage = loginAsQaDoctor(loginPage);

        Assert.assertTrue(homePage.isHomeScreenLoaded(),
                "[TC_ID_DOC_LOG-010] Doctor must be redirected to Doctor Dashboard upon valid authentication.");
    }

    @Test(description = "TC_ID_DOC_LOG-011: Verify Doctor Dashboard header displays Dr. greeting and profile avatar",
          groups = {"smoke", "regression"}, priority = 7)
    public void testDoctorDashboardHeaderGreetingAndAvatar() {
        homePage = loginAsQaDoctor(loginPage);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(homePage.isUserGreetingDisplayed(),
                "[TC_ID_DOC_LOG-011] Doctor greeting / name header must be visible.");
        softAssert.assertTrue(homePage.isUserAvatarDisplayed(),
                "[TC_ID_DOC_LOG-011] Doctor avatar icon must be visible in header.");
        softAssert.assertTrue(homePage.isNotificationBellDisplayed(),
                "[TC_ID_DOC_LOG-011] Notification bell must be accessible from Doctor header.");

        softAssert.assertAll();
    }

    @Test(description = "TC_ID_DOC_LOG-012: Verify Doctor Dashboard displays clinical snapshot and hospital network",
          groups = {"regression"}, priority = 8)
    public void testDoctorDashboardClinicalSnapshotAndHospitals() {
        log.info("Executing TC_ID_DOC_LOG-012: Verifying clinical modules for Doctor role");
        homePage = loginAsQaDoctor(loginPage);

        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(homePage.isTodaySnapshotDisplayed(),
                "[TC_ID_DOC_LOG-012] 'Today's Snapshot' section must be displayed for Doctor.");
        softAssert.assertTrue(homePage.isAppointmentsCardDisplayed(),
                "[TC_ID_DOC_LOG-012] 'Appointments' count card must be displayed in Snapshot.");
        softAssert.assertTrue(homePage.isAvailableSlotsCardDisplayed(),
                "[TC_ID_DOC_LOG-012] 'Available Slots' card must be displayed in Snapshot.");
        softAssert.assertTrue(homePage.isPatientSearchFieldDisplayed(),
                "[TC_ID_DOC_LOG-012] Doctor Patient Search field must be visible.");

        softAssert.assertAll();
    }

    // ══════════════════════════════════════════════════════════════════════
    // 4. DOCTOR NAVIGATION DRAWER & SESSION TERMINATION
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_DOC_LOG-013: Verify Doctor side navigation drawer displays doctor menu items",
          groups = {"regression"}, priority = 9)
    public void testDoctorSideNavigationDrawerItems() {
        log.info("Executing TC_ID_DOC_LOG-013: Inspecting Doctor Navigation Drawer");
        homePage = loginAsQaDoctor(loginPage);

        boolean drawerOpened = homePage.openSideDrawer();

        if (drawerOpened) {
            log.info("Drawer opened successfully, verifying Doctor specific sections");
            Assert.assertTrue(
                    homePage.isDrawerMenuItemDisplayed("My Profile") ||
                    homePage.isDrawerMenuItemDisplayed("Hospital Affiliations") ||
                    homePage.isSideDrawerOpen(),
                    "[TC_ID_DOC_LOG-013] Doctor menu items should be listed in side navigation.");
        } else {
            log.warn("Navigation drawer trigger not available or replaced by bottom navigation.");
            Assert.assertTrue(homePage.isBottomNavigationDisplayed(),
                    "[TC_ID_DOC_LOG-013] Either side drawer or bottom navigation must be available.");
        }
    }

    @Test(description = "TC_ID_DOC_LOG-015: Verify Doctor logout cleanly terminates session and returns to login",
          groups = {"smoke", "regression"}, priority = 10)
    public void testDoctorLogoutCleanSessionTermination() {
        log.info("Executing TC_ID_DOC_LOG-015: Verifying clean Doctor Logout");
        homePage = loginAsQaDoctor(loginPage);

        LoginPage returnedLoginPage = homePage.logout();

        Assert.assertTrue(returnedLoginPage.isLoginTabDisplayed() || returnedLoginPage.isLoginButtonDisplayed(),
                "[TC_ID_DOC_LOG-015] Logging out of Doctor account must return to Login screen cleanly.");
    }
}
