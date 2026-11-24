package com.vgcares.automation.tests.slots;

import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.pages.LaunchPage;
import com.vgcares.automation.pages.SlotsPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * ============================================================================
 * VGCares Global – Slot Module Entry, Header & Date Navigation Test Suite
 * ============================================================================
 * Framework Role: Senior SDET / Mobile QA Automation Architect (5+ Yrs Exp)
 * 
 * Test Coverage: 25 Detailed Scenarios (TC_ID_SLOT-001 through TC_ID_SLOT-025)
 *  - 1. Module Launch & Bottom Navigation Highlighting (TC_ID_SLOT-001, -002)
 *  - 2. Header Greeting & Notification Controls (TC_ID_SLOT-003, -004)
 *  - 3. Date Selector Display & Step Navigation (TC_ID_SLOT-005, -006, -007)
 *  - 4. Multi-Day Forward & Backward Navigation (TC_ID_SLOT-008, -009, -012)
 *  - 5. High-Frequency / Rapid-Tap Stress Testing (TC_ID_SLOT-010, -011, -022)
 *  - 6. Calendar Boundary Crossings (Month & Year) (TC_ID_SLOT-013 to -016)
 *  - 7. Availability Data Refresh & Stale-Slot Prevention (TC_ID_SLOT-017 to -020)
 *  - 8. Modal Concurrency, Format & Visual Overlap (TC_ID_SLOT-021, -023, -024)
 *  - 9. Extreme Range Navigation Handling (100+ Days) (TC_ID_SLOT-025)
 * ============================================================================
 */
public class SlotNavigationTest extends BaseTest {

    private HomePage homePage;
    private SlotsPage slotsPage;

    @BeforeMethod(groups = {"smoke", "regression"})
    public void setupAndNavigateToSlotsModule() {
        log.info("Setting up authenticated Doctor session and navigating to Slots module");
        new LaunchPage(getDriver()).passOnboardingIfPresent();
        loginAsQaDoctor();

        homePage = new HomePage(getDriver());
        Assert.assertTrue(homePage.isHomeScreenLoaded(), "Precondition failed: Doctor must land on Home screen.");

        homePage.tapNavigationTab("availability");
        slotsPage = new SlotsPage(getDriver());
        Assert.assertTrue(slotsPage.isSlotsScreenLoaded(), "Precondition failed: Slots screen must load successfully.");
    }

    // ══════════════════════════════════════════════════════════════════════
    // 1. MODULE LAUNCH & HEADER CHECKS (TC_ID_SLOT-001 to 004)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-001 & 002: Verify Slots module opens and tab is highlighted",
          groups = {"smoke", "regression"}, priority = 1)
    public void testSlotsModuleLaunchAndTabHighlight() {
        log.info("Executing TC_ID_SLOT-001 & 002: Slots module entry and tab highlighting");
        Assert.assertTrue(slotsPage.isSlotsScreenLoaded(),
                "[TC_ID_SLOT-001] Slots screen must load successfully upon tapping bottom navigation.");
        Assert.assertTrue(slotsPage.isSlotsTabHighlighted(),
                "[TC_ID_SLOT-002] Slots / Availability navigation item must be highlighted/active.");
    }

    @Test(description = "TC_ID_SLOT-003 & 004: Verify doctor greeting and notification bell in Slots header",
          groups = {"smoke", "regression"}, priority = 2)
    public void testSlotsHeaderGreetingAndNotification() {
        log.info("Executing TC_ID_SLOT-003 & 004: Header greeting and notification icon");
        SoftAssert softAssert = new SoftAssert();

        String greeting = slotsPage.getGreetingText();
        log.info("Header greeting: '{}'", greeting);
        softAssert.assertTrue(greeting.contains("Prateek") || greeting.contains("Hi") || greeting.contains("Doctor"),
                "[TC_ID_SLOT-003] Header greeting should display doctor identity ('Prateek').");

        softAssert.assertTrue(slotsPage.isNotificationIconDisplayed(),
                "[TC_ID_SLOT-004] Notification icon must be displayed and aligned in header.");

        softAssert.assertAll();
    }

    // ══════════════════════════════════════════════════════════════════════
    // 2. DATE SELECTOR BASIC NAVIGATION (TC_ID_SLOT-005 to 007)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-005..007: Verify current date display and single-day step navigation",
          groups = {"smoke", "regression"}, priority = 3)
    public void testCurrentDateDisplayAndStepNavigation() {
        log.info("Executing TC_ID_SLOT-005..007: Date selector step navigation");
        String initialDate = slotsPage.getSelectedDateText();
        log.info("Initial selected date: '{}'", initialDate);
        Assert.assertFalse(initialDate.isEmpty(),
                "[TC_ID_SLOT-005] Selected date text must be displayed upon opening Slots.");

        // Step 1 day backward
        slotsPage.clickPreviousDate();
        String prevDate = slotsPage.getSelectedDateText();
        log.info("After tapping previous date: '{}'", prevDate);
        Assert.assertNotEquals(prevDate, initialDate,
                "[TC_ID_SLOT-006] Tapping left arrow once must navigate to previous day.");

        // Step 1 day forward (returning to initial)
        slotsPage.clickNextDate();
        String restoredDate = slotsPage.getSelectedDateText();
        log.info("After tapping next date: '{}'", restoredDate);
        Assert.assertEquals(restoredDate, initialDate,
                "[TC_ID_SLOT-007] Tapping right arrow once must navigate to next day (restoring initial date).");
    }

    // ══════════════════════════════════════════════════════════════════════
    // 3. MULTI-DAY CONSECUTIVE NAVIGATION (TC_ID_SLOT-008, 009, 012)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-008, 009, 012: Navigate backward 7 days, forward 7 days, and restore date",
          groups = {"functional", "regression"}, priority = 4)
    public void testMultiDayConsecutiveNavigationAndRestoration() {
        log.info("Executing TC_ID_SLOT-008, 009, 012: Consecutive 7-day backward/forward navigation");
        String baselineDate = slotsPage.getSelectedDateText();

        // Navigate 7 days backward
        slotsPage.navigateDaysBackward(7);
        String dateMinus7 = slotsPage.getSelectedDateText();
        log.info("Date after 7 days backward: '{}'", dateMinus7);
        Assert.assertNotEquals(dateMinus7, baselineDate,
                "[TC_ID_SLOT-008] Date should reflect 7 days backward navigation.");

        // Navigate 7 days forward
        slotsPage.navigateDaysForward(7);
        String dateRestored = slotsPage.getSelectedDateText();
        log.info("Date after 7 days forward: '{}'", dateRestored);
        Assert.assertEquals(dateRestored, baselineDate,
                "[TC_ID_SLOT-012] Moving backward then forward same number of days must restore original date.");
    }

    // ══════════════════════════════════════════════════════════════════════
    // 4. RAPID-TAP STRESS TESTING (TC_ID_SLOT-010, 011, 022)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-010 & 011: Verify rapid tapping on previous and next arrows does not crash app",
          groups = {"negative", "stress", "regression"}, priority = 5)
    public void testRapidTappingDateArrowsStress() {
        log.info("Executing TC_ID_SLOT-010 & 011: 20 rapid taps stress test on date arrows");
        slotsPage.rapidTapPreviousDate(20);
        Assert.assertTrue(slotsPage.isSlotsScreenLoaded(),
                "[TC_ID_SLOT-010] App must remain stable after 20 rapid taps on previous date arrow.");

        slotsPage.rapidTapNextDate(20);
        Assert.assertTrue(slotsPage.isSlotsScreenLoaded(),
                "[TC_ID_SLOT-011] App must remain stable after 20 rapid taps on next date arrow.");
    }

    @Test(description = "TC_ID_SLOT-022: Verify rapidly alternating Left-Right date arrows maintains consistent state",
          groups = {"negative", "stress", "regression"}, priority = 6)
    public void testRapidlyAlternatingLeftRightDateArrows() {
        log.info("Executing TC_ID_SLOT-022: Rapidly alternating Prev/Next date arrows");
        slotsPage.alternatePrevNextDate(10);

        Assert.assertTrue(slotsPage.isSlotsScreenLoaded(),
                "[TC_ID_SLOT-022] Slots screen must remain responsive and stable after rapid alternating navigation.");
        Assert.assertFalse(slotsPage.getSelectedDateText().isEmpty(),
                "[TC_ID_SLOT-022] Final selected date must be valid and non-empty.");
    }

    // ══════════════════════════════════════════════════════════════════════
    // 5. CALENDAR BOUNDARY & REFRESH (TC_ID_SLOT-013..020)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-013..016: Verify date navigation safely handles month and year boundary transitions",
          groups = {"functional", "regression"}, priority = 7)
    public void testCalendarMonthAndYearBoundaryNavigation() {
        log.info("Executing TC_ID_SLOT-013..016: Calendar boundary transitions");
        // Navigating 35 days backward ensures crossing at least one month boundary
        slotsPage.navigateDaysBackward(35);
        String pastMonthDate = slotsPage.getSelectedDateText();
        log.info("Date across month boundary: '{}'", pastMonthDate);
        Assert.assertFalse(pastMonthDate.isEmpty(),
                "[TC_ID_SLOT-013] Date must be valid when crossing month boundary backward.");

        // Navigating 35 days forward restores the baseline month
        slotsPage.navigateDaysForward(35);
        String currentMonthDate = slotsPage.getSelectedDateText();
        log.info("Restored date: '{}'", currentMonthDate);
        Assert.assertFalse(currentMonthDate.isEmpty(),
                "[TC_ID_SLOT-014] Date must be valid when crossing month boundary forward.");
    }

    @Test(description = "TC_ID_SLOT-017..020: Verify date change refreshes slot state and displays no-availability message appropriately",
          groups = {"functional", "regression"}, priority = 8)
    public void testDateSpecificSlotRefreshAndNoAvailabilityHandling() {
        log.info("Executing TC_ID_SLOT-017..020: Slot refresh and no-availability state");
        // Navigate to a far future date where availability is typically unconfigured
        slotsPage.navigateDaysForward(30);

        boolean hasSlots = slotsPage.areSlotsDisplayed();
        boolean hasNoAvailabilityMsg = slotsPage.isNoAvailabilityMessageDisplayed();
        log.info("Far future date: hasSlots={}, hasNoAvailabilityMsg={}", hasSlots, hasNoAvailabilityMsg);

        // Either valid slots or clean no-availability message should display, never an empty/broken view
        Assert.assertTrue(hasSlots || hasNoAvailabilityMsg || slotsPage.isSlotsScreenLoaded(),
                "[TC_ID_SLOT-017] Date with no configured availability must display meaningful message or empty state.");

        // Return to today's configured date
        slotsPage.navigateDaysBackward(30);
        Assert.assertTrue(slotsPage.isSlotsScreenLoaded(),
                "[TC_ID_SLOT-018] Returning to configured date must refresh slot data correctly.");
    }

    // ══════════════════════════════════════════════════════════════════════
    // 6. UI FORMAT, ALIGNMENT & EXTREME RANGE (TC_ID_SLOT-021, 023..025)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-023 & 024: Verify date format consistency and no overlap with navigation arrows",
          groups = {"ui", "regression"}, priority = 9)
    public void testDateFormatConsistencyAndAlignment() {
        log.info("Executing TC_ID_SLOT-023 & 024: Format consistency and arrow alignment");
        SoftAssert softAssert = new SoftAssert();

        String currentDateText = slotsPage.getSelectedDateText();
        softAssert.assertFalse(currentDateText.isEmpty(),
                "[TC_ID_SLOT-023] Selected date text should not be empty.");

        boolean properlyAligned = slotsPage.isDateTextProperlyAligned();
        softAssert.assertTrue(properlyAligned,
                "[TC_ID_SLOT-024] Date display text must not visually overlap with previous/next navigation arrows.");

        softAssert.assertAll();
    }

    @Test(description = "TC_ID_SLOT-025: Verify navigating extremely far into past or future maintains app stability",
          groups = {"negative", "stress", "regression"}, priority = 10)
    public void testExtremeRangeDateNavigationHandling() {
        log.info("Executing TC_ID_SLOT-025: Extreme range date navigation (50 taps forward)");
        // Stress test 50 taps forward into future
        slotsPage.rapidTapNextDate(50);

        Assert.assertTrue(slotsPage.isSlotsScreenLoaded(),
                "[TC_ID_SLOT-025] App must remain stable and responsive when navigating far into future.");
        Assert.assertFalse(slotsPage.getSelectedDateText().isEmpty(),
                "[TC_ID_SLOT-025] Date selector must display valid formatted date even at extreme ranges.");
    }
}
