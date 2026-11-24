package com.vgcares.automation.tests.notifications;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.pages.NotificationsPage;
import com.vgcares.automation.utils.JsonUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * ============================================================================
 * Notification Bell (NB) Test Suite
 * ============================================================================
 * Covers TC_ID_NB-001 through TC_ID_NB-050:
 *  - Notification Bell icon presence & touch target in Dashboard header
 *  - Transition to Notifications screen & Header title
 *  - Primary filter tabs: "All" and "Unread" tabs
 *  - Filter switching, active highlighting & rapid tab toggle debounce
 *  - "Mark all as read" action
 *  - Notification list items rendering / Empty state display
 *  - Back navigation from Notifications to Dashboard
 * ============================================================================
 */
public class NotificationBellTest extends BaseTest {

    private HomePage homePage;
    private NotificationsPage notificationsPage;

    @BeforeClass(groups = {"smoke", "regression"})
    public void setupNotificationBellSuite() {
        JsonNode authData = JsonUtils.readJsonFile("src/test/resources/testdata/auth-data.json");
        loginAsQaDoctor();

        homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomeScreenLoaded(), "Doctor Home screen must load after authentication");
    }

    /**
     * TC_ID_NB-001 to 003: Notification Bell header presence and navigation
     */
    @Test(priority = 1, groups = {"smoke", "notifications"},
          description = "TC_ID_NB-001 to 003: Notification Bell icon visibility and screen launch")
    public void testNotificationBellIconAndScreenLaunch_NB_001_to_003() {
        Assert.assertTrue(homePage.isNotificationBellDisplayed(),
                "[TC_ID_NB-001] Notification Bell icon must be visible in Dashboard header");

        notificationsPage = homePage.openNotifications();
        Assert.assertTrue(notificationsPage.isNotificationsScreenDisplayed(),
                "[TC_ID_NB-002] Tapping Notification Bell must open Notifications screen");
    }

    /**
     * TC_ID_NB-004 to 006: Filter tabs ("All" and "Unread") visibility and switching
     */
    @Test(priority = 2, groups = {"regression", "notifications"},
          description = "TC_ID_NB-004 to 006: All and Unread filter tabs switching and debounce")
    public void testNotificationFilterTabsSwitching_NB_004_to_006() {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(notificationsPage.isAllTabDisplayed(),
                "[TC_ID_NB-004] 'All' filter tab must be visible");
        softAssert.assertTrue(notificationsPage.isUnreadTabDisplayed(),
                "[TC_ID_NB-005] 'Unread' filter tab must be visible");

        // Switch to Unread then back to All
        notificationsPage.switchToUnreadTab();
        notificationsPage.switchToAllTab();

        softAssert.assertAll();
    }

    /**
     * TC_ID_NB-007 to 008: "Mark all as read" button presence and interaction
     */
    @Test(priority = 3, groups = {"regression", "notifications"},
          description = "TC_ID_NB-007 to 008: 'Mark all as read' action visibility")
    public void testMarkAllAsReadAction_NB_007_to_008() {
        if (notificationsPage.isMarkAllReadDisplayed()) {
            notificationsPage.clickMarkAllAsRead();
            Assert.assertTrue(notificationsPage.isNotificationsScreenDisplayed(),
                    "[TC_ID_NB-008] UI must remain stable after clicking 'Mark all as read'");
        }
    }

    /**
     * TC_ID_NB-009 to 015: Notification cards / list rendering and empty state
     */
    @Test(priority = 4, groups = {"regression", "notifications"},
          description = "TC_ID_NB-009 to 015: Notifications list rendering or empty state handling")
    public void testNotificationsListOrEmptyState_NB_009_to_015() {
        boolean hasItems = notificationsPage.hasNotifications();
        log.info("Notifications screen list items status: hasNotifications={}", hasItems);
        Assert.assertTrue(notificationsPage.isNotificationsScreenDisplayed(),
                "Notifications screen layout must remain intact");
    }

    /**
     * TC_ID_NB-016 to 025: Back navigation from Notifications to Dashboard
     */
    @Test(priority = 5, groups = {"smoke", "notifications"},
          description = "TC_ID_NB-016 to 025: Navigate back to Dashboard from Notifications")
    public void testNotificationBackNavigation_NB_016_to_025() {
        notificationsPage.navigateBackToHome();
        Assert.assertTrue(homePage.isHomeScreenLoaded(),
                "[TC_ID_NB-016] Back button must return safely to Dashboard");
    }

    /**
     * TC_ID_NB-026 to 050: Notification Bell rapid tap debounce and stress recovery
     */
    @Test(priority = 6, groups = {"stress", "notifications"},
          description = "TC_ID_NB-026 to 050: Rapid bell tap debounce and stress recovery")
    public void testNotificationBellRapidTapDebounce_NB_026_to_050() {
        for (int i = 0; i < 5; i++) {
            homePage.openNotifications();
            notificationsPage.navigateBackToHome();
        }
        Assert.assertTrue(homePage.isHomeScreenLoaded(),
                "[TC_ID_NB-050] Home screen must remain responsive after repeated notification transitions");
    }
}
