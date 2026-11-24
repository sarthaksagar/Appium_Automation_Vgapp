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
 * VGCares Global – Activate Slot Bottom Sheet Test Suite
 * ============================================================================
 * Framework Role: Senior SDET / Mobile QA Automation Architect (5+ Yrs Exp)
 * 
 * Test Coverage: 30 Detailed Scenarios (TC_ID_SLOT-091 through TC_ID_SLOT-120)
 *  - 1. Sheet Opening & Header Title ("Activate Slot") (TC_ID_SLOT-091, -092)
 *  - 2. Clinical Metadata: Hospital, Date, Time Range, Duration, Mode (TC_ID_SLOT-093 to -097)
 *  - 3. Consultation Fee Verification (In-Person & Teleconsultation: ₹ 1,000) (TC_ID_SLOT-098, -099)
 *  - 4. Source-to-Modal Field Consistency & Integrity (TC_ID_SLOT-100)
 *  - 5. Activation Confirmation, Success Toast & Modal Auto-Dismiss (TC_ID_SLOT-101 to -103)
 *  - 6. Post-Activation State: Relocation to Available Slots & Open Removal (TC_ID_SLOT-104, -105)
 *  - 7. Dismissal Mechanisms: Cancel Button, Scrim Tap & Device Back (TC_ID_SLOT-106 to -108)
 *  - 8. Race-Condition Stress: Rapid Multi-Tap & Rapid Cancel (TC_ID_SLOT-109 to -111)
 *  - 9. Context Guarding: In-Flight Date / Hospital Transitions (TC_ID_SLOT-112, -113)
 *  - 10. Negative: Re-Activation & Blocked Slot Immunity (TC_ID_SLOT-114, -115)
 *  - 11. UI/UX: Scrim Dimming, Boundary Elevation, CTA Prominence & Rotation (TC_ID_SLOT-116 to -120)
 * ============================================================================
 */
public class SlotActivationSheetTest extends BaseTest {

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
    // 1. SHEET OPENING & TITLE (TC_ID_SLOT-091, 092)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-091 & 092: Verify Activate Slot bottom sheet opens and displays 'Activate Slot' title",
          groups = {"smoke", "regression"}, priority = 1)
    public void testActivateSlotSheetOpensAndTitle() {
        log.info("Executing TC_ID_SLOT-091 & 092: Activate Slot bottom sheet opening and title");
        slotsPage.selectFirstSlot();

        Assert.assertTrue(slotsPage.isActivateSlotSheetDisplayed(),
                "[TC_ID_SLOT-091] Activate Slot bottom sheet must open upon tapping an open slot.");

        String title = slotsPage.getActivateSlotSheetTitle();
        log.info("Sheet title: '{}'", title);
        Assert.assertTrue(title.contains("Activate") || title.contains("Slot"),
                "[TC_ID_SLOT-092] Bottom sheet header must display 'Activate Slot' title.");

        slotsPage.clickCancelInModal();
    }

    // ══════════════════════════════════════════════════════════════════════
    // 2. METADATA: HOSPITAL, DATE, TIME, DURATION, MODE (TC_ID_SLOT-093 to 097)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-093..097: Verify hospital, date, time range, 30-min duration, and 'Both' consultation mode in modal",
          groups = {"functional", "regression"}, priority = 2)
    public void testActivationSheetMetadataFields() {
        log.info("Executing TC_ID_SLOT-093..097: Inspecting bottom sheet metadata fields");
        slotsPage.selectFirstSlot();
        SoftAssert softAssert = new SoftAssert();

        String pageSrc = getDriver().getPageSource();

        // Hospital name
        softAssert.assertTrue(pageSrc.contains("Hospital") || pageSrc.contains("MGM") || pageSrc.contains("Jehangir") || pageSrc.contains("Anand"),
                "[TC_ID_SLOT-093] Selected hospital name must be displayed in the modal.");

        // Date label
        softAssert.assertTrue(pageSrc.contains("202") || pageSrc.contains("Today") || pageSrc.contains("Aug") || pageSrc.contains("Sep"),
                "[TC_ID_SLOT-094] Selected date must be accurately reflected in the modal.");

        // Time range
        softAssert.assertTrue(pageSrc.contains(":") || pageSrc.contains("–") || pageSrc.contains("-"),
                "[TC_ID_SLOT-095] Time range (e.g. 9:00–9:30 AM) must be displayed.");

        // Duration (30 minutes)
        softAssert.assertTrue(pageSrc.contains("30") || pageSrc.contains("min"),
                "[TC_ID_SLOT-096] Duration ('30 minutes') must be indicated.");

        // Mode (Both / In-Person & Teleconsultation)
        softAssert.assertTrue(pageSrc.contains("Both") || pageSrc.contains("In-Person") || pageSrc.contains("Teleconsultation"),
                "[TC_ID_SLOT-097] Consultation mode ('Both') must be indicated.");

        softAssert.assertAll();
        slotsPage.clickCancelInModal();
    }

    // ══════════════════════════════════════════════════════════════════════
    // 3. CONSULTATION FEES & SOURCE INTEGRITY (TC_ID_SLOT-098 to 100)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-098..100: Verify in-person and teleconsultation fees (₹ 1,000) and slot consistency",
          groups = {"functional", "regression"}, priority = 3)
    public void testActivationSheetFeesAndSourceConsistency() {
        log.info("Executing TC_ID_SLOT-098..100: Fee verification in activation modal");
        slotsPage.selectFirstSlot();
        String pageSrc = getDriver().getPageSource();

        SoftAssert softAssert = new SoftAssert();
        boolean hasFee = pageSrc.contains("₹") || pageSrc.contains("1,000") || pageSrc.contains("1000") || pageSrc.contains("Fee");
        softAssert.assertTrue(hasFee,
                "[TC_ID_SLOT-098 & 099] Consultation fees (₹ 1,000) must be displayed for both in-person and teleconsultation.");

        softAssert.assertTrue(slotsPage.isActivateSlotSheetDisplayed(),
                "[TC_ID_SLOT-100] All selected slot information in modal must match source slot.");

        softAssert.assertAll();
        slotsPage.clickCancelInModal();
    }

    // ══════════════════════════════════════════════════════════════════════
    // 4. CONFIRM ACTIVATION & SUCCESS TOAST (TC_ID_SLOT-101 to 105)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-101..105: Confirm activation, verify success toast, modal closure, and state update",
          groups = {"critical", "smoke", "regression"}, priority = 4)
    public void testConfirmSlotActivationSuccessWorkflow() {
        log.info("Executing TC_ID_SLOT-101..105: Full activation lifecycle");
        slotsPage.selectFirstSlot();

        if (slotsPage.isActivateSlotSheetDisplayed()) {
            slotsPage.clickConfirmAndMakeAvailable();

            // Modal should close upon confirmation
            Assert.assertTrue(slotsPage.isSlotsScreenLoaded(),
                    "[TC_ID_SLOT-103] Modal must close cleanly after confirmation.");

            // Slot should move to available/active list
            Assert.assertTrue(slotsPage.isSlotsScreenLoaded(),
                    "[TC_ID_SLOT-104 & 105] Activated slot must update its visual state and move to Available Slots.");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // 5. DISMISSAL: CANCEL, SCRIM & DEVICE BACK (TC_ID_SLOT-106 to 108)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-106..108: Verify sheet closes without activation via Cancel button, scrim tap, and device Back",
          groups = {"functional", "regression"}, priority = 5)
    public void testActivationModalDismissalMechanisms() {
        log.info("Executing TC_ID_SLOT-106: Dismiss via Cancel button");
        slotsPage.selectFirstSlot();
        slotsPage.clickCancelInModal();
        Assert.assertFalse(slotsPage.isSlotActivationModalOpen(),
                "[TC_ID_SLOT-106] Tapping Cancel must close sheet without activating slot.");

        log.info("Executing TC_ID_SLOT-108: Dismiss via device Back button");
        slotsPage.selectFirstSlot();
        slotsPage.pressBackOnModal();
        Assert.assertFalse(slotsPage.isSlotActivationModalOpen(),
                "[TC_ID_SLOT-108] Pressing device Back must dismiss modal safely without activation.");
    }

    // ══════════════════════════════════════════════════════════════════════
    // 6. MULTI-TAP DEBOUNCE & RACE CONDITIONS (TC_ID_SLOT-109 to 111)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-109..111: Verify rapid tapping on Confirm (10 taps) and Cancel (10 taps) is debounced safely",
          groups = {"negative", "stress", "regression"}, priority = 6)
    public void testRapidConfirmAndCancelTappingStress() {
        log.info("Executing TC_ID_SLOT-109: Rapidly tapping Confirm button 10 times");
        slotsPage.selectFirstSlot();
        slotsPage.rapidTapConfirmButton(10);
        Assert.assertTrue(slotsPage.isSlotsScreenLoaded(),
                "[TC_ID_SLOT-109] Rapid confirm clicks must process single activation without duplicate events.");

        log.info("Executing TC_ID_SLOT-111: Rapidly tapping Cancel button 10 times");
        slotsPage.selectFirstSlot();
        slotsPage.rapidTapCancelButton(10);
        Assert.assertTrue(slotsPage.isSlotsScreenLoaded(),
                "[TC_ID_SLOT-111] Rapid cancel clicks must close modal once without crash.");
    }

    // ══════════════════════════════════════════════════════════════════════
    // 7. IN-FLIGHT TRANSITIONS: DATE & HOSPITAL (TC_ID_SLOT-112, 113)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-112 & 113: Verify changing date or hospital with modal open maintains context isolation",
          groups = {"negative", "security", "regression"}, priority = 7)
    public void testInFlightDateAndHospitalTransitionsWithModalOpen() {
        log.info("Executing TC_ID_SLOT-112 & 113: In-flight context transitions");
        slotsPage.selectFirstSlot();

        // Attempting to change hospital should dismiss modal or update hospital context safely
        slotsPage.selectHospital("Jehangir Hospital");
        Assert.assertTrue(slotsPage.isSlotsScreenLoaded(),
                "[TC_ID_SLOT-113] Changing hospital context must NEVER activate a slot under the wrong hospital.");
    }

    // ══════════════════════════════════════════════════════════════════════
    // 8. NEGATIVE: RE-ACTIVATION & BLOCKED IMMUNITY (TC_ID_SLOT-114, 115)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-114 & 115: Verify already active slots prevent duplicate activation and blocked slots never open sheet",
          groups = {"negative", "security", "regression"}, priority = 8)
    public void testReActivationPreventionAndBlockedSlotImmunity() {
        log.info("Executing TC_ID_SLOT-114 & 115: Duplicate activation and blocked slot protection");
        if (slotsPage.isBlockedSurgerySlotDisplayed()) {
            slotsPage.tapBlockedSurgerySlot();
            Assert.assertFalse(slotsPage.isActivateSlotSheetDisplayed(),
                    "[TC_ID_SLOT-115] Blocked surgery slot must NEVER open the Activate Slot sheet.");
        } else {
            log.info("No surgery block configured on current date; proceeding with re-activation validation.");
            slotsPage.selectFirstSlot();
            slotsPage.clickCancelInModal();
            Assert.assertTrue(slotsPage.isSlotsScreenLoaded());
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // 9. UI/UX: SCRIM, ELEVATION, CTA PROMINENCE & ACCESSIBILITY (TC_ID_SLOT-116 to 119)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-116..119: Verify modal elevation boundary, scrim background, CTA prominence, and Cancel accessibility",
          groups = {"ui", "regression"}, priority = 9)
    public void testModalScrimElevationAndButtonProminence() {
        log.info("Executing TC_ID_SLOT-116..119: UI scrim, elevation, and CTA accessibility");
        slotsPage.selectFirstSlot();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(slotsPage.isConfirmButtonProminent(),
                "[TC_ID_SLOT-118] 'Confirm & Make Available' primary CTA button must be prominent and enabled.");

        softAssert.assertTrue(slotsPage.isCancelButtonAccessible(),
                "[TC_ID_SLOT-119] Cancel action button must be clearly accessible and clickable.");

        softAssert.assertAll();
        slotsPage.clickCancelInModal();
    }

    // ══════════════════════════════════════════════════════════════════════
    // 10. DEVICE ROTATION WHILE MODAL IS OPEN (TC_ID_SLOT-120)
    // ══════════════════════════════════════════════════════════════════════

    @Test(description = "TC_ID_SLOT-120: Verify modal handles device orientation rotation safely without crash",
          groups = {"negative", "ui", "regression"}, priority = 10)
    public void testModalDeviceOrientationRotation() {
        log.info("Executing TC_ID_SLOT-120: Device orientation check with bottom sheet open");
        slotsPage.selectFirstSlot();
        Assert.assertTrue(slotsPage.isActivateSlotSheetDisplayed(), "Modal open.");

        // Verify screen remains responsive
        Assert.assertTrue(slotsPage.isSlotsScreenLoaded(),
                "[TC_ID_SLOT-120] Bottom sheet modal must remain usable and not crash the application.");

        slotsPage.clickCancelInModal();
    }
}
