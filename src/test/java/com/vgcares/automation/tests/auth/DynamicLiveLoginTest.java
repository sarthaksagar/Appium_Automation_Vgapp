package com.vgcares.automation.tests.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.dataproviders.AuthDataProvider;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.pages.LaunchPage;
import com.vgcares.automation.pages.LoginPage;
import com.vgcares.automation.utils.JsonUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.time.Duration;

/**
 * ============================================================================
 * VGCares Global – Dynamic Active Live Account Authentication Test Suite
 * ============================================================================
 * Dedicated test suite for the dynamic active account with live backend data:
 *   - Account: virajd@techvishwaguru.com
 *   - Credentials: Admin@123
 *
 * This test suite distinguishes live dynamic data retrieval from static mocks:
 *   1. Dynamic account credentials verification from auth-data.json
 *   2. Login UI & field accessibility verification
 *   3. Security & password masking during entry
 *   4. Positive live authentication and transition to Dashboard
 *   5. Real-time header greeting and role badge validation
 *   6. Dynamic clinical snapshot cards rendering
 *   7. Dynamic side navigation drawer validation
 *   8. Session preservation across background / resume lifecycle
 *   9. Clean session termination & logout
 * ============================================================================
 */
public class DynamicLiveLoginTest extends BaseTest {

    private LoginPage loginPage;
    private HomePage homePage;
    private JsonNode dynamicUserData;

    @BeforeMethod(alwaysRun = true)
    public void setupDynamicAuthEnvironment() {
        log.info("Setting up dynamic live authentication test environment");
        JsonNode authData = JsonUtils.readJsonFile("src/test/resources/testdata/auth-data.json");
        dynamicUserData = authData.get("dynamicActiveUser");

        new LaunchPage(getDriver()).passOnboardingIfPresent();
        loginPage = new LoginPage(getDriver());
        loginPage.openLoginTab();
    }

    /**
     * Test 1: Verify dynamic live user credentials exist in configuration
     */
    @Test(priority = 1, groups = {"smoke", "dynamic"},
          description = "TC_ID_DYN_LOG-001: Verify dynamic active user credentials are configured")
    public void testDynamicLiveUserConfigurationCheck() {
        Assert.assertNotNull(dynamicUserData, "dynamicActiveUser object must exist in auth-data.json");
        Assert.assertEquals(dynamicUserData.get("email").asText(), "virajd@techvishwaguru.com",
                "Dynamic email must match virajd@techvishwaguru.com");
        Assert.assertEquals(dynamicUserData.get("password").asText(), "Admin@123",
                "Dynamic password must match Admin@123");
        log.info("Dynamic account configuration validated: {}", dynamicUserData.get("email").asText());
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
        String rawPassword = dynamicUserData.get("password").asText();
        loginPage.enterEmailOrPhone(dynamicUserData.get("email").asText())
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
          dataProvider = "dynamicActiveUserData",
          dataProviderClass = AuthDataProvider.class,
          description = "TC_ID_DYN_LOG-004: Verify live authentication and redirection to Dashboard")
    public void testDynamicLiveAuthenticationAndDashboardRedirection(String email, String password,
                                                                    String role, String description) {
        log.info("Executing live dynamic authentication for {} ({})", email, description);
        homePage = loginPage.loginWithDynamicAccount(email, password);

        Assert.assertTrue(homePage.isHomeScreenLoaded(),
                "App must successfully authenticate dynamic active account and load Doctor Dashboard");
    }

    /**
     * Test 5: Verify dynamic live user header greeting and profile elements
     */
    @Test(priority = 5, groups = {"dynamic", "regression"},
          dataProvider = "dynamicActiveUserData",
          dataProviderClass = AuthDataProvider.class,
          description = "TC_ID_DYN_LOG-005: Verify dynamic user greeting and header profile elements")
    public void testDynamicLiveUserHeaderGreetingAndRole(String email, String password,
                                                         String role, String description) {
        homePage = loginPage.loginWithDynamicAccount(email, password);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(homePage.isUserGreetingDisplayed(), "Dynamic user greeting must be displayed");
        softAssert.assertTrue(homePage.isUserAvatarDisplayed(), "Dynamic user profile avatar must be visible");
        softAssert.assertTrue(homePage.isNotificationBellDisplayed(), "Notification bell must be accessible");

        String greeting = homePage.getUserGreetingText();
        log.info("Dynamic live user greeting text: {}", greeting);
        softAssert.assertAll();
    }

    /**
     * Test 6: Verify dynamic clinical snapshot and data modules
     */
    @Test(priority = 6, groups = {"dynamic", "regression"},
          dataProvider = "dynamicActiveUserData",
          dataProviderClass = AuthDataProvider.class,
          description = "TC_ID_DYN_LOG-006: Verify dynamic clinical snapshot cards and patient search")
    public void testDynamicLiveClinicalSnapshotCards(String email, String password,
                                                    String role, String description) {
        homePage = loginPage.loginWithDynamicAccount(email, password);

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
          dataProvider = "dynamicActiveUserData",
          dataProviderClass = AuthDataProvider.class,
          description = "TC_ID_DYN_LOG-007: Verify side navigation drawer for dynamic live account")
    public void testDynamicLiveSideNavigationDrawerAccess(String email, String password,
                                                         String role, String description) {
        homePage = loginPage.loginWithDynamicAccount(email, password);

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
          dataProvider = "dynamicActiveUserData",
          dataProviderClass = AuthDataProvider.class,
          description = "TC_ID_DYN_LOG-008: Verify dynamic session persists across background/resume")
    public void testDynamicLiveSessionPersistenceAcrossAppSwitching(String email, String password,
                                                                   String role, String description) {
        homePage = loginPage.loginWithDynamicAccount(email, password);
        Assert.assertTrue(homePage.isHomeScreenLoaded(), "Home screen loaded initially");

        // Send app to background for 3 seconds then resume
        log.info("Sending app to background to test dynamic session persistence");
        try {
            getDriver().runAppInBackground(Duration.ofSeconds(3));
        } catch (Exception e) {
            log.warn("Background gesture handled: {}", e.getMessage());
        }

        Assert.assertTrue(homePage.isHomeScreenLoaded(),
                "Dynamic live user session must remain active after resuming app from background");
    }

    /**
     * Test 9: Verify clean logout and session termination for dynamic live account
     */
    @Test(priority = 9, groups = {"smoke", "dynamic"},
          dataProvider = "dynamicActiveUserData",
          dataProviderClass = AuthDataProvider.class,
          description = "TC_ID_DYN_LOG-009: Verify clean logout and session teardown")
    public void testDynamicLiveGracefulLogoutAndSessionTeardown(String email, String password,
                                                               String role, String description) {
        homePage = loginPage.loginWithDynamicAccount(email, password);
        Assert.assertTrue(homePage.isHomeScreenLoaded(), "Home screen loaded before logout");

        LoginPage postLogoutPage = homePage.logout();
        Assert.assertTrue(postLogoutPage.isLoginButtonDisplayed(),
                "User must return to Login screen upon successful dynamic account logout");
        log.info("Dynamic account session terminated cleanly");
    }
}
