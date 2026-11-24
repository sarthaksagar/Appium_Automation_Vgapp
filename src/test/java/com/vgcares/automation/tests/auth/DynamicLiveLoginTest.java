package com.vgcares.automation.tests.auth;

import com.vgcares.automation.testdata.QaCredentials;
import com.vgcares.automation.testdata.AuthFixtures;
import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;

/** Exercises live doctor authentication using an authorized QA account. */
public class DynamicLiveLoginTest extends BaseTest {

    private LoginPage loginPage;
    private HomePage homePage;

    @BeforeMethod(groups = {"smoke", "regression"})
    public void setupDynamicAuthEnvironment() {
        log.info("Setting up dynamic live authentication test environment");
        loginPage = openFreshLogin();
    }

    /**
     * Test 1: Verify dynamic live user credentials exist in configuration
     */
    @Test(priority = 1, groups = {"smoke", "dynamic"},
          description = "TC_ID_DYN_LOG-001: Verify dynamic active user credentials are configured")
    public void testDynamicLiveUserConfigurationCheck() {
        QaCredentials.doctor();
    }

    /**
     * Test 2: Verify Login UI elements are interactable for dynamic credentials entry
     */
    @Test(priority = 2, groups = {"smoke", "dynamic"},
          description = "TC_ID_DYN_LOG-002: Verify Login UI is ready for dynamic credential input")
    public void testDynamicLiveLoginScreenUIElements() {
        Assert.assertTrue(loginPage.isLoginTabDisplayed(), "Login tab must be visible");
        Assert.assertTrue(loginPage.isLoginButtonDisplayed(), "Login / Sign In button must be interactable");
        Assert.assertTrue(loginPage.isForgotPasswordDisplayed(), "Forgot Password link must be present");
    }

    /**
     * Test 3: Verify dynamic password is masked securely in the DOM
     */
    @Test(priority = 3, groups = {"security", "dynamic"},
          description = "TC_ID_DYN_LOG-003: Verify dynamic password field masks input securely")
    public void testDynamicLivePasswordMaskingVerification() {
        String rawPassword = AuthFixtures.newPassword();
        loginPage.enterEmailOrPhone(AuthFixtures.uniqueEmail("example.invalid"))
                 .enterPassword(rawPassword);

        String pageSource = getDriver().getPageSource();
        Assert.assertFalse(pageSource.contains(rawPassword),
                "Raw dynamic password text must NEVER appear in cleartext inside page hierarchy");
        log.info("Dynamic password masking verified successfully");
    }

    /**
     * Test 4: Positive authentication using dynamic live account
     */
    @Test(priority = 4, groups = {"smoke", "dynamic"},
          description = "TC_ID_DYN_LOG-004: Verify live authentication and redirection to Dashboard")
    public void testDynamicLiveAuthenticationAndDashboardRedirection() {
        homePage = loginAsQaDoctor(loginPage);

        Assert.assertTrue(homePage.isHomeScreenLoaded(),
                "App must successfully authenticate dynamic active account and load Doctor Dashboard");
    }

    /**
     * Test 5: Verify dynamic live user header greeting and profile elements
     */
    @Test(priority = 5, groups = {"dynamic", "regression"},
          description = "TC_ID_DYN_LOG-005: Verify dynamic user greeting and header profile elements")
    public void testDynamicLiveUserHeaderGreetingAndRole() {
        homePage = loginAsQaDoctor(loginPage);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(homePage.isUserGreetingDisplayed(), "Dynamic user greeting must be displayed");
        softAssert.assertTrue(homePage.isUserAvatarDisplayed(), "Dynamic user profile avatar must be visible");
        softAssert.assertTrue(homePage.isNotificationBellDisplayed(), "Notification bell must be accessible");

        softAssert.assertAll();
    }

    /**
     * Test 6: Verify dynamic clinical snapshot and data modules
     */
    @Test(priority = 6, groups = {"dynamic", "regression"},
          description = "TC_ID_DYN_LOG-006: Verify dynamic clinical snapshot cards and patient search")
    public void testDynamicLiveClinicalSnapshotCards() {
        homePage = loginAsQaDoctor(loginPage);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(homePage.isTodaySnapshotDisplayed(), "Snapshot section must load for live user");
        softAssert.assertTrue(homePage.isAppointmentsCardDisplayed(), "Appointments card must be rendered");
        softAssert.assertTrue(homePage.isAvailableSlotsCardDisplayed(), "Available Slots card must be rendered");
        softAssert.assertTrue(homePage.isPatientSearchFieldDisplayed(), "Patient search must be accessible");
        softAssert.assertAll();
    }

    /**
     * Test 7: Verify dynamic side navigation drawer access
     */
    @Test(priority = 7, groups = {"dynamic", "regression"},
          description = "TC_ID_DYN_LOG-007: Verify side navigation drawer for dynamic live account")
    public void testDynamicLiveSideNavigationDrawerAccess() {
        homePage = loginAsQaDoctor(loginPage);

        boolean drawerOpened = homePage.openSideDrawer();
        Assert.assertTrue(drawerOpened || homePage.isSideDrawerOpen(), "Side drawer must open for dynamic account");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(homePage.isMyProfileMenuItemDisplayed(), "My Profile must be in drawer");
        softAssert.assertTrue(homePage.isHospitalAffiliationsMenuItemDisplayed(), "Hospital Affiliations in drawer");
        softAssert.assertTrue(homePage.isSurgeryRequestsMenuItemDisplayed(), "Surgery Requests in drawer");
        softAssert.assertTrue(homePage.isConsultationFeesMenuItemDisplayed(), "Consultation Fees in drawer");
        softAssert.assertTrue(homePage.isReviewsMenuItemDisplayed(), "Reviews in drawer");
        softAssert.assertTrue(homePage.isDocumentsMenuItemDisplayed(), "Documents in drawer");
        softAssert.assertTrue(homePage.isSettingsMenuItemDisplayed(), "Settings in drawer");
        softAssert.assertAll();

        homePage.closeSideDrawer();
    }

    /**
     * Test 8: Verify dynamic session persistence across app background and resume
     */
    @Test(priority = 8, groups = {"dynamic", "regression"},
          description = "TC_ID_DYN_LOG-008: Verify dynamic session persists across background/resume")
    public void testDynamicLiveSessionPersistenceAcrossAppSwitching() {
        homePage = loginAsQaDoctor(loginPage);
        Assert.assertTrue(homePage.isHomeScreenLoaded(), "Home screen loaded initially");

        // Send app to background for 3 seconds then resume
        log.info("Sending app to background to test dynamic session persistence");
        getDriver().runAppInBackground(Duration.ofSeconds(3));

        Assert.assertTrue(homePage.isHomeScreenLoaded(),
                "Dynamic live user session must remain active after resuming app from background");
    }

    /**
     * Test 9: Verify clean logout and session termination for dynamic live account
     */
    @Test(priority = 9, groups = {"smoke", "dynamic"},
          description = "TC_ID_DYN_LOG-009: Verify clean logout and session teardown")
    public void testDynamicLiveGracefulLogoutAndSessionTeardown() {
        homePage = loginAsQaDoctor(loginPage);
        Assert.assertTrue(homePage.isHomeScreenLoaded(), "Home screen loaded before logout");

        LoginPage postLogoutPage = homePage.logout();
        Assert.assertTrue(postLogoutPage.isLoginButtonDisplayed(),
                "User must return to Login screen upon successful dynamic account logout");
        log.info("Dynamic account session terminated cleanly");
    }
}
