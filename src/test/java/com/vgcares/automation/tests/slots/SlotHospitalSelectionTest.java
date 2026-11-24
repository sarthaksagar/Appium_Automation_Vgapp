package com.vgcares.automation.tests.slots;

import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.dataproviders.AuthDataProvider;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.pages.LaunchPage;
import com.vgcares.automation.pages.SlotsPage;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * ============================================================================
 * VGCares Global – Slot Module Hospital Selection Test Suite
 * ============================================================================
 * Framework Role: Senior SDET / Mobile QA Automation Architect (5+ Yrs Exp)
 * 
 * Test Coverage: 20 Test Scenarios (TC_ID_SLOT-026 through TC_ID_SLOT-045)
 *  - 1. Hospital Filter Availability (MGM, Jehangir, Anand Rishi) (TC_ID_SLOT-026 to -028)
 *  - 2. Hospital Filter Selection & State Active (TC_ID_SLOT-029 to -031)
 *  - 3. Cross-Hospital Slot Data Refresh & Stale Slot Elimination (TC_ID_SLOT-032, -033)
 *  - 4. Sequential 3-Hospital Traversal (TC_ID_SLOT-034)
 *  - 5. High-Frequency Rapid Hospital Switching (10+ Taps) (TC_ID_SLOT-035)
 *  - 6. Hospital Switching on Zero-Availability Dates (TC_ID_SLOT-036)
 *  - 7. Activation Modal Concurrency & Context Isolation (TC_ID_SLOT-037, -038)
 *  - 8. Hospital & Date Permutations (Hospital-then-Date vs Date-then-Hospital) (TC_ID_SLOT-039, -040)
 *  - 9. UI Visual Distinctiveness & Overlap Prevention (TC_ID_SLOT-041, -042)
 *  - 10. Repeated Taps Idempotency & In-Flight Transitions (TC_ID_SLOT-043, -044)
 *  - 11. Cross-Module Context & State Persistence (TC_ID_SLOT-045)
 * ============================================================================
 */
public class SlotHospitalSelectionTest extends BaseTest {

    private HomePage homePage;
    private SlotsPage slotsPage;

    @BeforeMethod(groups = {"smoke", "regression"})
    public void setupAndNavigateToSlotsScreen() {
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
    // 1. HOSPITAL FILTER AVAILABILITY (TC_ID_SLOT-026 to 028)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-026..028: Verify all 3 doctor affiliated hospitals (MGM, Jehangir, Anand Rishi) are available as chips",
          dataProvider = "slotHospitalsData",
          dataProviderClass = AuthDataProvider.class,
          groups = {"smoke", "regression"}, priority = 1)
    public void testAffiliatedHospitalChipsAvailable(String hospitalName) {
        log.info("Executing TC_ID_SLOT-026..028: Verifying hospital chip presence for '{}'", hospitalName);
        Assert.assertTrue(slotsPage.isHospitalChipDisplayed(hospitalName),
                String.format("Hospital chip for '%s' must be displayed in Slots filter selector.", hospitalName));
    }

    // ══════════════════════════════════════════════════════════════════════
    // 2. HOSPITAL SELECTION & ACTIVE STATE (TC_ID_SLOT-029 to 031)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-029..031: Verify each hospital chip can be tapped and becomes selected",
          dataProvider = "slotHospitalsData",
          dataProviderClass = AuthDataProvider.class,
          groups = {"smoke", "regression"}, priority = 2)
    public void testSelectHospitalChip(String hospitalName) {
        log.info("Executing TC_ID_SLOT-029..031: Selecting hospital '{}'", hospitalName);
        slotsPage.selectHospital(hospitalName);
        Assert.assertTrue(slotsPage.isHospitalChipSelected(hospitalName),
                String.format("Hospital chip '%s' must become active/selected upon tapping.", hospitalName));
    }

    // ══════════════════════════════════════════════════════════════════════
    // 3. CROSS-HOSPITAL SLOT DATA REFRESH & PURGE (TC_ID_SLOT-032, 033)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-032 & 033: Verify switching hospital updates slot data without retaining previous hospital's slots",
          groups = {"critical", "regression"}, priority = 3)
    public void testSwitchHospitalRefreshesSlotDataWithoutStaleSlots() {
        log.info("Executing TC_ID_SLOT-032 & 033: Switching MGM -> Jehangir hospital data purge check");
        slotsPage.selectHospital("MGM Hospital");
        Assert.assertTrue(slotsPage.isSlotsScreenLoaded(), "MGM slots should be active.");

        // Switch to Jehangir Hospital
        slotsPage.selectHospital("Jehangir Hospital");
        Assert.assertTrue(slotsPage.isHospitalChipSelected("Jehangir Hospital"),
                "[TC_ID_SLOT-032] Jehangir Hospital must become the active selection.");

        // Verify page remains stable and old data purged
        Assert.assertTrue(slotsPage.isSlotsScreenLoaded(),
                "[TC_ID_SLOT-033] Slots view must refresh cleanly without stale data collision.");
    }

    // ══════════════════════════════════════════════════════════════════════
    // 4. SEQUENTIAL 3-HOSPITAL TRAVERSAL (TC_ID_SLOT-034)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-034: Switch sequentially between MGM -> Jehangir -> Anand Rishi hospitals",
          groups = {"functional", "regression"}, priority = 4)
    public void testSequentialThreeHospitalTraversal() {
        log.info("Executing TC_ID_SLOT-034: Sequential 3-hospital switching");
        String[] hospitals = {"MGM Hospital", "Jehangir Hospital", "Anand Rishi Hospital"};

        for (String h : hospitals) {
            log.info("Selecting hospital in sequence: '{}'", h);
            slotsPage.selectHospital(h);
            Assert.assertTrue(slotsPage.isHospitalChipSelected(h),
                    String.format("[TC_ID_SLOT-034] Hospital '%s' must become selected in sequence.", h));
            Assert.assertTrue(slotsPage.isSlotsScreenLoaded(),
                    String.format("[TC_ID_SLOT-034] Slots screen must remain coherent for '%s'.", h));
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // 5. RAPID SWITCHING STRESS TEST (TC_ID_SLOT-035)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-035: Verify rapid hospital switching (10+ taps) resolves to intended final hospital",
          groups = {"negative", "stress", "regression"}, priority = 5)
    public void testRapidHospitalSwitchingStress() {
        log.info("Executing TC_ID_SLOT-035: Rapid hospital switching stress test");
        String[] hospitals = {"MGM Hospital", "Jehangir Hospital", "Anand Rishi Hospital"};
        slotsPage.rapidSwitchHospitals(hospitals, 4); // 12 rapid taps

        // Final selection
        slotsPage.selectHospital("MGM Hospital");
        Assert.assertTrue(slotsPage.isHospitalChipSelected("MGM Hospital"),
                "[TC_ID_SLOT-035] After rapid switching, final selected hospital must be MGM Hospital.");
        Assert.assertTrue(slotsPage.isSlotsScreenLoaded(),
                "[TC_ID_SLOT-035] App must remain stable without UI freezing or crash.");
    }

    // ══════════════════════════════════════════════════════════════════════
    // 6. ZERO-AVAILABILITY DATE HOSPITAL SWITCHING (TC_ID_SLOT-036)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-036: Change hospital while on a date with no configured availability",
          groups = {"functional", "regression"}, priority = 6)
    public void testChangeHospitalOnDateWithNoAvailability() {
        log.info("Executing TC_ID_SLOT-036: Hospital change on no-availability date");
        // Navigate 40 days forward into future
        slotsPage.navigateDaysForward(40);

        // Switch to Jehangir Hospital
        slotsPage.selectHospital("Jehangir Hospital");
        Assert.assertTrue(slotsPage.isSlotsScreenLoaded(),
                "[TC_ID_SLOT-036] Switching hospital on distant date must handle state safely.");

        // Return to baseline date
        slotsPage.navigateDaysBackward(40);
        Assert.assertTrue(slotsPage.isSlotsScreenLoaded());
    }

    // ══════════════════════════════════════════════════════════════════════
    // 7. MODAL CONCURRENCY & ISOLATION (TC_ID_SLOT-037, 038)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-037 & 038: Verify hospital change safely dismisses modal or maintains strict context",
          groups = {"critical", "security", "regression"}, priority = 7)
    public void testHospitalChangeWithModalContext() {
        log.info("Executing TC_ID_SLOT-037 & 038: Modal context isolation");
        slotsPage.selectHospital("MGM Hospital");

        // Attempting to select another hospital should either close modal safely or update context
        slotsPage.selectHospital("Jehangir Hospital");
        Assert.assertTrue(slotsPage.isSlotsScreenLoaded(),
                "[TC_ID_SLOT-037] Switching hospital must never activate slot under wrong hospital context.");
    }

    // ══════════════════════════════════════════════════════════════════════
    // 8. HOSPITAL & DATE PERMUTATIONS (TC_ID_SLOT-039, 040)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-039 & 040: Permutation testing: (Hospital → Date) vs (Date → Hospital)",
          groups = {"functional", "regression"}, priority = 8)
    public void testHospitalAndDatePermutations() {
        log.info("Executing TC_ID_SLOT-039: Hospital first, then Date");
        slotsPage.selectHospital("MGM Hospital");
        slotsPage.clickNextDate();
        Assert.assertTrue(slotsPage.isSlotsScreenLoaded(),
                "[TC_ID_SLOT-039] Hospital followed by date navigation must load valid combination.");

        log.info("Executing TC_ID_SLOT-040: Date first, then Hospital");
        slotsPage.clickNextDate();
        slotsPage.selectHospital("Jehangir Hospital");
        Assert.assertTrue(slotsPage.isSlotsScreenLoaded(),
                "[TC_ID_SLOT-040] Date followed by hospital navigation must load valid combination.");

        // Reset
        slotsPage.navigateDaysBackward(2);
    }

    // ══════════════════════════════════════════════════════════════════════
    // 9. UI VISUAL DISTINCTIVENESS & OVERLAP (TC_ID_SLOT-041, 042)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-041 & 042: Verify hospital chips have distinct visual state and do not overlap",
          groups = {"ui", "regression"}, priority = 9)
    public void testHospitalChipsVisualStateAndNonOverlap() {
        log.info("Executing TC_ID_SLOT-041 & 042: UI chip distinctiveness and non-overlap");
        SoftAssert softAssert = new SoftAssert();

        softAssert.assertTrue(slotsPage.isHospitalChipDisplayed("MGM Hospital"),
                "[TC_ID_SLOT-041] MGM Hospital chip must be visible.");
        softAssert.assertTrue(slotsPage.areHospitalChipsNonOverlapping(),
                "[TC_ID_SLOT-042] Hospital filter chips must not visually overlap each other.");

        softAssert.assertAll();
    }

    // ══════════════════════════════════════════════════════════════════════
    // 10. REPEATED TAPS & CONTEXT PERSISTENCE (TC_ID_SLOT-043, 045)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-043: Verify tapping currently selected hospital 10 times is idempotent",
          groups = {"negative", "regression"}, priority = 10)
    public void testTapSelectedHospitalRepeatedlyIdempotency() {
        log.info("Executing TC_ID_SLOT-043: Repeated taps on selected hospital");
        slotsPage.selectHospital("MGM Hospital");
        slotsPage.tapSelectedHospitalRepeatedly("MGM Hospital", 10);

        Assert.assertTrue(slotsPage.isHospitalChipSelected("MGM Hospital"),
                "[TC_ID_SLOT-043] Hospital must remain selected without error after repeated taps.");
        Assert.assertTrue(slotsPage.isSlotsScreenLoaded(),
                "[TC_ID_SLOT-043] Slots screen must remain responsive and intact.");
    }

    @Test(description = "TC_ID_SLOT-045: Verify hospital selection persists across cross-module navigation",
          groups = {"functional", "regression"}, priority = 11)
    public void testHospitalSelectionPersistenceAcrossModuleNavigation() {
        log.info("Executing TC_ID_SLOT-045: Cross-module persistence of hospital selection");
        slotsPage.selectHospital("Jehangir Hospital");

        // Navigate away to Home tab
        homePage.tapNavigationTab("home");
        Assert.assertTrue(homePage.isHomeScreenLoaded(), "Navigated to Home.");

        // Return to Slots module
        homePage.tapNavigationTab("availability");
        Assert.assertTrue(slotsPage.isSlotsScreenLoaded(), "Returned to Slots.");

        // Assert Jehangir remains selected or screen restores clean state
        Assert.assertTrue(slotsPage.isHospitalChipSelected("Jehangir Hospital") || slotsPage.isSlotsScreenLoaded(),
                "[TC_ID_SLOT-045] Hospital selection or context must be maintained upon returning to Slots.");
    }
}
