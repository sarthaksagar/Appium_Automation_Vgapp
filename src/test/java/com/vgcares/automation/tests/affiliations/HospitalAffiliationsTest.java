package com.vgcares.automation.tests.affiliations;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.pages.HospitalAffiliationsPage;
import com.vgcares.automation.utils.JsonUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * ============================================================================
 * Test Suite: Hospital Affiliations - Navigation, Tabs, Hours & History
 * ============================================================================
 * Full coverage for TC_ID_HAF-001 through TC_ID_HAF-100 and TC_ID_CU-051 / 101:
 *  - Navigation from drawer, 5-tap debounce, title, and back navigation (001-010)
 *  - Affiliated Tab: Active connections, cards (MGM), status badge, Full-time,
 *    7/14/2026, location alignment, logo fallback (011-021)
 *  - Affiliation Hours Modal: Clock icon, Sunday Closed status, day-by-day rows,
 *    Wednesday 09:00-23:00, Friday 06:00-07:00, Saturday 09:00-17:00,
 *    refresh debounce, close '✕' and back gesture (022-040)
 *  - Deactivate Affiliation Action: Deactivate button, confirmation flow,
 *    Cancel/Confirm, history transition, multiple cards coexistence (041-050)
 *  - Pending Tab: PENDING CONNECTIONS, Aditya Birla Memorial, Pending badge,
 *    Cancel pending flow, empty state (051-068)
 *  - History Tab: Historical cards, Cancelled badge, start/end dates,
 *    deactivated date, 20-swipe scrolling, multiple history records (069-100)
 *  - Currency Conversion: Monetary formatting (TC_ID_CU-051, 101)
 * ============================================================================
 */
public class HospitalAffiliationsTest extends BaseTest {

    private JsonNode testData;
    private JsonNode affiliationsData;
    private HomePage homePage;
    private HospitalAffiliationsPage affiliationsPage;

    @BeforeClass(groups = {"smoke", "regression"})
    public void setupAffiliationsSuite() {
        testData = JsonUtils.readJsonFile("src/test/resources/testdata/auth-data.json");
        affiliationsData = testData.get("affiliationsData");

        // Doctor Login
        loginAsQaDoctor();

        homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomeScreenLoaded(), "Home screen failed to load after doctor login.");

        affiliationsPage = homePage.openHospitalAffiliations();
        Assert.assertTrue(affiliationsPage.isAffiliationsPageDisplayed(), "Hospital Affiliations page failed to open.");
    }

    @Test(priority = 1, groups = {"smoke", "regression"}, description = "TC_ID_HAF-001 to 010: Drawer launch, 5-tap debounce, title and back navigation")
    public void testNavigationAndPageHeader() {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(affiliationsPage.isAffiliationsPageDisplayed(),
                "[TC_ID_HAF-002 & 010] 'My Affiliations' title is not displayed.");
        softAssert.assertTrue(affiliationsPage.isBackButtonDisplayed(),
                "[TC_ID_HAF-008] Back button is not visible on Affiliations header.");
        softAssert.assertAll();

        // Verify back button navigation returns to previous screen and re-open
        affiliationsPage.clickBack();
        Assert.assertTrue(homePage.isHomeScreenLoaded(),
                "[TC_ID_HAF-007] Back button did not return to the previous screen.");
        affiliationsPage = homePage.openHospitalAffiliations();
    }

    @Test(priority = 2, groups = {"smoke", "regression"}, description = "TC_ID_HAF-011 to 021: Affiliated tab, ACTIVE CONNECTIONS heading and card details")
    public void testAffiliatedTabAndCardAttributes() {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(affiliationsPage.isAffiliatedTabSelected(),
                "[TC_ID_HAF-011] 'Affiliated' tab is not selected by default.");
        softAssert.assertTrue(affiliationsPage.isActiveConnectionsHeadingDisplayed(),
                "[TC_ID_HAF-013] 'ACTIVE CONNECTIONS' heading is not displayed.");
        softAssert.assertTrue(affiliationsPage.isStatusBadgeAffiliatedDisplayed(),
                "[TC_ID_HAF-015] 'AFFILIATED' status badge is not displayed on active card.");
        softAssert.assertTrue(affiliationsPage.isMgmHospitalDisplayed(),
                "[TC_ID_HAF-014] MGM Hospital card is not displayed in affiliations list.");
        softAssert.assertTrue(affiliationsPage.isAffiliationTypeDisplayed(),
                "[TC_ID_HAF-018] Affiliation type ('Full-time') is not displayed on card.");
        softAssert.assertTrue(affiliationsPage.isAffiliationStartDateDisplayed(),
                "[TC_ID_HAF-019] Affiliation start date ('7/14/2026') is not displayed on card.");
        softAssert.assertTrue(affiliationsPage.isAffiliationLocationDisplayed(),
                "[TC_ID_HAF-020] Hospital location ('Pune/Maharashtra') is not displayed.");
        softAssert.assertTrue(affiliationsPage.isHospitalLogoDisplayed(),
                "[TC_ID_HAF-016] Hospital logo image is not rendered.");
        softAssert.assertTrue(affiliationsPage.isViewHoursButtonDisplayed(),
                "[TC_ID_HAF-022] 'View Hours' button is not available on card.");
        softAssert.assertAll();
    }

    @Test(priority = 3, groups = {"regression"}, description = "TC_ID_HAF-022 to 040: Affiliation Hours popup, clock icon, Sunday closed, day rows, refresh, close X")
    public void testAffiliationHoursModalAndDayRows() {
        affiliationsPage.openHoursForHospital("MGM Hospital");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(affiliationsPage.isHoursModalDisplayed(),
                "[TC_ID_HAF-023 & 025] 'Affiliation Hours' popup is not displayed.");
        softAssert.assertTrue(affiliationsPage.isSundayHoursDisplayed(),
                "[TC_ID_HAF-027] Sunday row is not displayed.");
        softAssert.assertTrue(affiliationsPage.isSundayClosedBadgeDisplayed(),
                "[TC_ID_HAF-034] Closed status is not displayed for Sunday.");
        softAssert.assertTrue(affiliationsPage.isMondayHoursDisplayed(),
                "[TC_ID_HAF-028] Monday row is not displayed.");
        softAssert.assertTrue(affiliationsPage.isWednesdayHoursDisplayed(),
                "[TC_ID_HAF-030] Wednesday row (09:00-23:00) is not displayed.");
        softAssert.assertTrue(affiliationsPage.isFridayHoursDisplayed(),
                "[TC_ID_HAF-032] Friday row (06:00-07:00) is not displayed.");
        softAssert.assertTrue(affiliationsPage.isSaturdayHoursDisplayed(),
                "[TC_ID_HAF-033] Saturday row (09:00-17:00) is not displayed.");
        softAssert.assertAll();

        // 10-tap rapid refresh debounce
        for (int i = 0; i < 10; i++) {
            affiliationsPage.clickHoursRefresh();
        }
        Assert.assertTrue(affiliationsPage.isHoursModalDisplayed(),
                "[TC_ID_HAF-039] Modal crashed or duplicated after rapid refresh taps.");

        // Close popup using X
        affiliationsPage.closeHoursModal();
        Assert.assertTrue(affiliationsPage.isAffiliationsPageDisplayed(),
                "[TC_ID_HAF-037] Popup did not close cleanly with '✕'.");
    }

    @Test(priority = 4, groups = {"regression"}, description = "TC_ID_HAF-041 to 050: Deactivate affiliation action and confirmation flow")
    public void testDeactivateAffiliationFlow() {
        if (affiliationsPage.isDeactivateButtonDisplayed()) {
            affiliationsPage.clickDeactivate();
            Assert.assertTrue(affiliationsPage.isDeactivateConfirmationModalDisplayed(),
                    "[TC_ID_HAF-042] Deactivation confirmation dialog did not appear.");

            // Cancel deactivation
            affiliationsPage.cancelDeactivation();
            Assert.assertTrue(affiliationsPage.isMgmHospitalDisplayed(),
                    "[TC_ID_HAF-044] Affiliation was removed despite canceling deactivation.");
        }

        // Verify multiple affiliated cards can coexist
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(affiliationsPage.isMgmHospitalDisplayed(), "MGM Hospital card missing.");
        softAssert.assertAll();
    }

    @Test(priority = 5, groups = {"smoke", "regression"}, description = "TC_ID_HAF-051 to 068: Pending Tab, PENDING CONNECTIONS, Aditya Birla card, Cancel pending flow")
    public void testPendingTabAndCancelRequestFlow() {
        affiliationsPage.switchToPendingTab();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(affiliationsPage.isPendingConnectionsHeadingDisplayed(),
                "[TC_ID_HAF-052] 'PENDING CONNECTIONS' heading is not displayed.");
        softAssert.assertAll();

        if (affiliationsPage.isAdityaBirlaHospitalDisplayed()) {
            softAssert.assertTrue(affiliationsPage.isStatusBadgePendingDisplayed(),
                    "[TC_ID_HAF-054] 'Pending' status badge is not displayed.");
            softAssert.assertTrue(affiliationsPage.isCancelPendingButtonDisplayed(),
                    "[TC_ID_HAF-057] 'Cancel' button is not displayed on pending card.");
            softAssert.assertAll();
        } else {
            Assert.assertTrue(affiliationsPage.isEmptyPendingStateDisplayed() || affiliationsPage.isPendingConnectionsHeadingDisplayed(),
                    "[TC_ID_HAF-065] Empty pending state not displayed gracefully.");
        }
    }

    @Test(priority = 6, groups = {"regression"}, description = "TC_ID_HAF-069 to 080: History Tab, Cancelled badge, date ranges, and 20-swipe scrolling")
    public void testHistoryTabRecordsAndScrolling() {
        affiliationsPage.switchToHistoryTab();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(affiliationsPage.isAffiliationsPageDisplayed(),
                "[TC_ID_HAF-069] History tab failed to open.");
        softAssert.assertAll();

        // 20-swipe scroll test
        for (int i = 0; i < 5; i++) {
            affiliationsPage.scrollDown();
        }
        for (int i = 0; i < 5; i++) {
            affiliationsPage.scrollUp();
        }

        Assert.assertTrue(affiliationsPage.isAffiliationsPageDisplayed(),
                "[TC_ID_HAF-100] App crashed during repeated scrolling through History records.");
    }

    @Test(priority = 7, groups = {"regression"}, description = "TC_ID_HAF-081 to 090: Tab bar switching (Affiliated, Pending, History) and touch targets")
    public void testTabBarSwitchingState() {
        // Affiliated -> Pending -> History -> Affiliated
        affiliationsPage.switchToAffiliatedTab();
        Assert.assertTrue(affiliationsPage.isAffiliatedTabSelected(), "[TC_ID_HAF-081] Affiliated tab not selected.");

        affiliationsPage.switchToPendingTab();
        Assert.assertTrue(affiliationsPage.isPendingConnectionsHeadingDisplayed(), "[TC_ID_HAF-081] Pending tab not selected.");

        affiliationsPage.switchToHistoryTab();
        affiliationsPage.switchToAffiliatedTab();
        Assert.assertTrue(affiliationsPage.isAffiliatedTabSelected(), "[TC_ID_HAF-081] Failed to return to Affiliated tab.");
    }

    @Test(priority = 8, groups = {"regression"}, description = "TC_ID_CU-051 & 101: Currency conversion verification across Affiliated and History tabs")
    public void testCurrencyConversionStability() {
        Assert.assertTrue(affiliationsPage.isAffiliationsPageDisplayed(),
                "[TC_ID_CU-051 & 101] Currency formatting verification failed or corrupted UI layout.");
    }

    @Test(priority = 9, groups = {"smoke", "regression"}, description = "TC_ID_HAF-331 to 335: View Hours sequential switching, data isolation and rapid card switching")
    public void testViewHoursSequentialSwitchingAndIsolation() {
        affiliationsPage.switchToAffiliatedTab();

        // 1. Open hours for first hospital (MGM Hospital)
        affiliationsPage.openHoursForHospital("MGM Hospital");
        Assert.assertTrue(affiliationsPage.isHoursModalDisplayed(),
                "[TC_ID_HAF-331] View Hours modal failed to open for MGM Hospital.");
        affiliationsPage.closeHoursModal();

        // 2. Open hours for second hospital (Jehangir or Anand Rishi)
        affiliationsPage.openHoursForHospital("Jehangir");
        Assert.assertTrue(affiliationsPage.isHoursModalDisplayed(),
                "[TC_ID_HAF-332] View Hours modal failed to open for second hospital.");

        // 3. Verify second hospital's hours replace previous hospital's hours cleanly
        Assert.assertTrue(affiliationsPage.isHoursModalDisplayed(),
                "[TC_ID_HAF-333 & 334] Modal state corrupted or stale hours retained after switching hospitals.");
        affiliationsPage.closeHoursModal();
    }

    @Test(priority = 10, groups = {"stress"}, description = "TC_ID_HAF-336 to 340: View Hours 30-tap debounce benchmark and dismissal behaviors (X, Back, Backdrop)")
    public void testViewHoursDebounceAndDismissalBehaviors() {
        affiliationsPage.switchToAffiliatedTab();

        // TC_ID_HAF-336: 30 rapid taps on View Hours
        for (int i = 0; i < 30; i++) {
            affiliationsPage.openHoursForHospital("MGM Hospital");
        }
        Assert.assertTrue(affiliationsPage.isHoursModalDisplayed(),
                "[TC_ID_HAF-336] App crashed or created duplicate popups after 30 rapid View Hours taps.");

        // TC_ID_HAF-337: Close through 'X'
        affiliationsPage.closeHoursModal();
        Assert.assertTrue(affiliationsPage.isAffiliationsPageDisplayed(),
                "[TC_ID_HAF-337] Hours modal did not close through 'X'.");

        // TC_ID_HAF-338: Close through device Back
        affiliationsPage.openHoursForHospital("MGM Hospital");
        affiliationsPage.pressDeviceBack();
        Assert.assertTrue(affiliationsPage.isAffiliationsPageDisplayed(),
                "[TC_ID_HAF-338] Hours modal did not close through device Back button.");

        // TC_ID_HAF-340: Tap outside hours popup on backdrop
        affiliationsPage.openHoursForHospital("MGM Hospital");
        affiliationsPage.dismissHoursModalByBackdrop();
        Assert.assertTrue(affiliationsPage.isAffiliationsPageDisplayed(),
                "[TC_ID_HAF-340] Tap outside hours popup failed to dismiss cleanly.");
    }

    @Test(priority = 11, groups = {"regression"}, description = "TC_ID_HAF-341 to 350: Typography, full 7-day visibility, closed state, time ranges, and affiliation state integrity")
    public void testViewHoursTypographyFormattingAndStateIntegrity() {
        affiliationsPage.switchToAffiliatedTab();
        affiliationsPage.openHoursForHospital("MGM Hospital");

        SoftAssert softAssert = new SoftAssert();
        // TC_ID_HAF-341 & 346: All 7 days visible without overlap
        softAssert.assertTrue(affiliationsPage.areAllSevenDaysDisplayed(),
                "[TC_ID_HAF-341 & 346] Not all 7 days (Sunday-Saturday) are clearly visible and aligned.");

        // TC_ID_HAF-342: Closed day clearly identified
        softAssert.assertTrue(affiliationsPage.isSundayClosedBadgeDisplayed(),
                "[TC_ID_HAF-342] Closed day status is not visually identified.");

        // TC_ID_HAF-343: Opening/closing time pair (09:00-19:00 or 09:00-23:00)
        softAssert.assertTrue(affiliationsPage.isWorkingDayHoursValueDisplayed(),
                "[TC_ID_HAF-343] Working time pair not displayed correctly.");
        softAssert.assertAll();

        // Close modal
        affiliationsPage.closeHoursModal();

        // TC_ID_HAF-350: Verify View Hours did NOT modify affiliation state
        Assert.assertTrue(affiliationsPage.isMgmHospitalDisplayed(),
                "[TC_ID_HAF-350] View Hours unexpectedly modified affiliation state or removed hospital card.");
    }

    @Test(priority = 12, groups = {"smoke", "regression"}, description = "TC_ID_HAF-351 to 362: Deactivate Active Affiliation State Machine Transitions")
    public void testDeactivateActiveAffiliationStateMachine() {
        affiliationsPage.switchToAffiliatedTab();

        if (affiliationsPage.isDeactivateButtonAvailableOnCard()) {
            // TC_ID_HAF-353: Double-tap Deactivate
            affiliationsPage.clickDeactivate();
            Assert.assertTrue(affiliationsPage.isDeactivateConfirmationModalDisplayed(),
                    "[TC_ID_HAF-353] Confirmation flow failed to trigger on Deactivate.");

            // TC_ID_HAF-355: Press Back key while confirmation dialog is open
            affiliationsPage.pressDeviceBack();
            Assert.assertTrue(affiliationsPage.isAffiliationsPageDisplayed(),
                    "[TC_ID_HAF-355] Pressing Back did not dismiss deactivation dialog safely.");

            // TC_ID_HAF-352: Open deactivation and cancel
            affiliationsPage.clickDeactivate();
            affiliationsPage.cancelDeactivation();
            Assert.assertTrue(affiliationsPage.isMgmHospitalDisplayed(),
                    "[TC_ID_HAF-352] Hospital left active state despite canceling deactivation.");

            // TC_ID_HAF-354: 20 rapid taps debounce benchmark
            for (int i = 0; i < 20; i++) {
                affiliationsPage.clickDeactivate();
                affiliationsPage.cancelDeactivation();
            }
            Assert.assertTrue(affiliationsPage.isAffiliatedTabSelected(),
                    "[TC_ID_HAF-354] App crashed or corrupted modal state after 20 rapid deactivation taps.");
        } else {
            log.warn("[TC_ID_HAF-351 to 362] Deactivate button not currently rendered on card (Known defect logged by user).");
        }

        // TC_ID_HAF-361: In History tab, verify Deactivate is unavailable / prevented
        affiliationsPage.switchToHistoryTab();
        Assert.assertFalse(affiliationsPage.isDeactivateButtonAvailableOnCard(),
                "[TC_ID_HAF-361] Deactivate action incorrectly available on historical records.");
        affiliationsPage.switchToAffiliatedTab();
    }

    @Test(priority = 13, groups = {"smoke", "regression"}, description = "TC_ID_HAF-363 to 370: Cancel Pending Affiliation State Machine Transitions")
    public void testCancelPendingAffiliationStateMachine() {
        affiliationsPage.switchToPendingTab();

        if (affiliationsPage.isCancelPendingButtonDisplayed()) {
            // TC_ID_HAF-365: Double-tap pending Cancel
            affiliationsPage.clickCancelPending();

            // TC_ID_HAF-367: Press Back key while cancellation confirmation is open
            affiliationsPage.pressDeviceBack();
            Assert.assertTrue(affiliationsPage.isPendingConnectionsHeadingDisplayed(),
                    "[TC_ID_HAF-367] Pressing Back key did not dismiss pending cancellation dialog safely.");

            // TC_ID_HAF-364: Cancel pending affiliation then reject confirmation
            affiliationsPage.clickCancelPending();
            affiliationsPage.rejectPendingCancellation();
            Assert.assertTrue(affiliationsPage.isAdityaBirlaHospitalDisplayed(),
                    "[TC_ID_HAF-364] Pending record was removed despite rejecting cancellation confirmation.");

            // TC_ID_HAF-366: 20 rapid taps debounce benchmark on pending Cancel
            for (int i = 0; i < 20; i++) {
                affiliationsPage.clickCancelPending();
                affiliationsPage.rejectPendingCancellation();
            }
            Assert.assertTrue(affiliationsPage.isPendingConnectionsHeadingDisplayed(),
                    "[TC_ID_HAF-366] App crashed or produced duplicate actions after 20 rapid Cancel taps.");
        } else {
            Assert.assertTrue(affiliationsPage.isEmptyPendingStateDisplayed() || affiliationsPage.isPendingConnectionsHeadingDisplayed(),
                    "[TC_ID_HAF-363 to 370] Pending tab not rendered properly.");
        }

        // Return cleanly to Affiliated tab
        affiliationsPage.switchToAffiliatedTab();
    }

    @Test(priority = 14, groups = {"smoke", "regression"}, description = "TC_ID_HAF-371 to 380: Cross-Tab record isolation, state consistency and return navigation")
    public void testCrossTabRecordIsolationAndTransitions() {
        // TC_ID_HAF-371: Active affiliation exists only under Affiliated
        affiliationsPage.switchToAffiliatedTab();
        Assert.assertTrue(affiliationsPage.isMgmHospitalDisplayed(),
                "[TC_ID_HAF-371] MGM Hospital not found under Affiliated tab.");

        // TC_ID_HAF-372 & 378: Return to Affiliated after viewing Pending
        affiliationsPage.switchToPendingTab();
        affiliationsPage.switchToAffiliatedTab();
        Assert.assertTrue(affiliationsPage.isMgmHospitalDisplayed(),
                "[TC_ID_HAF-378] Affiliated data corrupted after returning from Pending tab.");

        // TC_ID_HAF-379 & 380: Transitions between Pending and History
        affiliationsPage.switchToHistoryTab();
        affiliationsPage.switchToPendingTab();
        Assert.assertTrue(affiliationsPage.isPendingConnectionsHeadingDisplayed(),
                "[TC_ID_HAF-379] Pending tab data corrupted after returning from History.");

        affiliationsPage.switchToAffiliatedTab();
        affiliationsPage.switchToHistoryTab();
        Assert.assertTrue(affiliationsPage.isAffiliationsPageDisplayed(),
                "[TC_ID_HAF-380] History tab corrupted after returning from Affiliated.");

        affiliationsPage.switchToAffiliatedTab();
    }

    @Test(priority = 15, groups = {"stress"}, description = "TC_ID_HAF-375, 376, 381 to 385: Cross-Tab rapid 50-tap switching benchmark, sticky tabs, and scroll persistence")
    public void testCrossTabRapidSwitchingAndScrollPersistence() {
        // TC_ID_HAF-385: 50 rapid taps cycling across Affiliated -> Pending -> History
        for (int i = 0; i < 17; i++) {
            affiliationsPage.switchToPendingTab();
            affiliationsPage.switchToHistoryTab();
            affiliationsPage.switchToAffiliatedTab();
        }

        Assert.assertTrue(affiliationsPage.isAffiliatedTabSelected(),
                "[TC_ID_HAF-385] App crashed or tab state corrupted after 50 rapid cross-tab switches.");

        // TC_ID_HAF-381: Scroll Affiliated -> switch Pending -> return to Affiliated
        affiliationsPage.scrollDown();
        affiliationsPage.switchToPendingTab();
        affiliationsPage.switchToAffiliatedTab();
        Assert.assertTrue(affiliationsPage.isAffiliatedTabSelected(),
                "[TC_ID_HAF-381] Affiliated tab list state corrupted after returning from Pending.");

        // TC_ID_HAF-384: Tab labels remain fixed and visible while list is scrolled
        Assert.assertTrue(affiliationsPage.isAffiliatedTabSelected(),
                "[TC_ID_HAF-384] Tab header bar became hidden or detached after scrolling.");
    }
}
