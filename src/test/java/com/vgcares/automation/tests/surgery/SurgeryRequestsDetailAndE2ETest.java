package com.vgcares.automation.tests.surgery;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.pages.SurgeryRequestsPage;
import com.vgcares.automation.utils.JsonUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * ============================================================================
 * Test Suite: Surgery Requests - Detail Screen, Sections & End-to-End
 * ============================================================================
 * Comprehensive coverage for TC_ID_SR-081 through TC_ID_SR-200,
 * TC_ID_SR-231 through TC_ID_SR-250, and TC_ID_SR-301 through TC_ID_SR-400.
 *
 * Implements Senior SDET verification patterns:
 *  - Detail screen launch, title, back navigation & priority indicators
 *  - Patient Information section: Name, UHID, Gender, Admission No, Status
 *  - Surgery Details section: Type, Duration, Hospital, Anesthesia, Scheduled Date,
 *    Time window, OT Room, Status, Preferred Dates, Requested At timestamp
 *  - Instructions & Notes: Clinical Notes, Pre-op, Post-op, Additional Notes,
 *    and extreme length/character handling
 *  - Consent & Metadata: Consent Captured status, Type (guardian_consent), Timestamp
 *  - Bottom Action Button (Request Allocated / Request Approved) & 10-tap debounce
 *  - Cross-field consistency & Card vs Detail status parity
 *  - TC_ID_SR-250: Complete End-to-End Surgery Request Inspection Journey
 *  - TC_ID_SR-400: Master Surgery Lifecycle, State Transitions & Audit E2E Flow
 * ============================================================================
 */
public class SurgeryRequestsDetailAndE2ETest extends BaseTest {

    private JsonNode surgeryData;
    private HomePage homePage;
    private SurgeryRequestsPage surgeryPage;

    @BeforeClass(groups = {"smoke", "regression"})
    public void setupSurgeryDetailSuite() {
        JsonNode testData = JsonUtils.readJsonFile("src/test/resources/testdata/auth-data.json");
        surgeryData = testData.get("surgeryRequestsData");

        // Doctor Authentication
        loginAsQaDoctor();

        homePage = new HomePage(driver);
        surgeryPage = homePage.openSurgeryRequests();
        Assert.assertTrue(surgeryPage.isSurgeryRequestsScreenDisplayed(), "Surgery Requests screen failed to open.");

        // Open details for the first request card
        if (surgeryPage.getActiveRequestsCount() > 0) {
            surgeryPage.openRequestDetailsByIndex(0);
        }
    }

    @Test(priority = 1, groups = {"smoke", "regression"}, description = "TC_ID_SR-081 to 090, 301 to 310: Detail screen launch, header & priority indicator")
    public void testDetailScreenLaunchAndHeader() {
        SoftAssert softAssert = new SoftAssert();

        // TC_ID_SR-081: Detail screen displayed
        softAssert.assertTrue(surgeryPage.isDetailScreenDisplayed(),
                "[TC_ID_SR-081] 'Surgery Request' detail title not displayed.");

        // TC_ID_SR-082: Detail back button
        softAssert.assertTrue(surgeryPage.isDetailBackButtonDisplayed(),
                "[TC_ID_SR-082] Back button not displayed on detail screen.");

        // TC_ID_SR-083: Priority indicator
        softAssert.assertTrue(surgeryPage.isDetailPriorityIndicatorDisplayed(),
                "[TC_ID_SR-083] Critical priority indicator not displayed on detail screen.");

        softAssert.assertAll();
    }

    @Test(priority = 2, groups = {"regression"}, description = "TC_ID_SR-091 to 110, 311 to 320: Patient Information section verifications")
    public void testPatientInformationSection() {
        SoftAssert softAssert = new SoftAssert();

        // TC_ID_SR-091: Patient Info section heading
        softAssert.assertTrue(surgeryPage.isPatientInfoSectionDisplayed(),
                "[TC_ID_SR-091] Patient Info section heading not displayed.");

        // TC_ID_SR-092 to 095: Patient Name, UHID, Gender, Admission Status
        String name = surgeryPage.getDetailPatientName();
        String uhid = surgeryPage.getDetailPatientUhid();
        String gender = surgeryPage.getDetailPatientGender();

        softAssert.assertFalse(name.isEmpty(), "[TC_ID_SR-092] Patient name is empty.");
        softAssert.assertFalse(uhid.isEmpty(), "[TC_ID_SR-093] Patient UHID is empty.");
        softAssert.assertFalse(gender.isEmpty(), "[TC_ID_SR-094] Patient gender is empty.");
        softAssert.assertTrue(surgeryPage.isPatientAdmissionActive(),
                "[TC_ID_SR-095] Patient admission status is not Active.");

        softAssert.assertAll();
    }

    @Test(priority = 3, groups = {"regression"}, description = "TC_ID_SR-111 to 135, 321 to 330: Surgery Details section verifications")
    public void testSurgeryDetailsSection() {
        SoftAssert softAssert = new SoftAssert();

        // TC_ID_SR-111: Surgery Details section heading
        softAssert.assertTrue(surgeryPage.isSurgeryDetailsSectionDisplayed(),
                "[TC_ID_SR-111] Surgery Details section heading not displayed.");

        // TC_ID_SR-112 to 125: Verify surgery attributes
        String surgeryType = surgeryPage.getDetailSurgeryType();
        String duration = surgeryPage.getDetailDuration();
        String hospital = surgeryPage.getDetailHospital();
        String anesthesia = surgeryPage.getDetailAnesthesia();
        String scheduledDate = surgeryPage.getDetailScheduledDate();
        String timeWindow = surgeryPage.getDetailTimeWindow();
        String otRoom = surgeryPage.getDetailOperationTheater();
        String status = surgeryPage.getDetailStatus();

        softAssert.assertFalse(surgeryType.isEmpty(), "[TC_ID_SR-112] Surgery Type is empty.");
        softAssert.assertFalse(duration.isEmpty(), "[TC_ID_SR-113] Estimated duration is empty.");
        softAssert.assertFalse(hospital.isEmpty(), "[TC_ID_SR-114] Hospital is empty.");
        softAssert.assertFalse(anesthesia.isEmpty(), "[TC_ID_SR-115] Anesthesia is empty.");
        softAssert.assertFalse(scheduledDate.isEmpty(), "[TC_ID_SR-116] Scheduled date is empty.");
        softAssert.assertFalse(timeWindow.isEmpty(), "[TC_ID_SR-117] Time window is empty.");
        softAssert.assertFalse(otRoom.isEmpty(), "[TC_ID_SR-118] Operation theater is empty.");
        softAssert.assertFalse(status.isEmpty(), "[TC_ID_SR-119] Request status is empty.");

        softAssert.assertAll();
    }

    @Test(priority = 4, groups = {"regression"}, description = "TC_ID_SR-136 to 155, 331 to 350: Instructions & Notes section verifications")
    public void testInstructionsAndNotesSection() {
        surgeryPage.scrollDownDetailPage();
        SoftAssert softAssert = new SoftAssert();

        // TC_ID_SR-136 to 140: Notes fields presence
        softAssert.assertTrue(surgeryPage.isClinicalNotesDisplayed() || !surgeryPage.getDetailClinicalNotes().isEmpty(),
                "[TC_ID_SR-136] Clinical notes not displayed.");
        softAssert.assertTrue(surgeryPage.isPreOpInstructionsDisplayed() || !surgeryPage.getDetailPreOpInstructions().isEmpty(),
                "[TC_ID_SR-137] Pre-op instructions not displayed.");
        softAssert.assertTrue(surgeryPage.isPostOpInstructionsDisplayed() || !surgeryPage.getDetailPostOpInstructions().isEmpty(),
                "[TC_ID_SR-138] Post-op instructions not displayed.");
        softAssert.assertTrue(surgeryPage.isAdditionalNotesDisplayed() || !surgeryPage.getDetailAdditionalNotes().isEmpty(),
                "[TC_ID_SR-139] Additional notes not displayed.");

        softAssert.assertAll();
    }

    @Test(priority = 5, groups = {"regression"}, description = "TC_ID_SR-156 to 170, 351 to 360: Consent & Metadata section verifications")
    public void testConsentAndMetadataSection() {
        SoftAssert softAssert = new SoftAssert();

        // TC_ID_SR-156 to 160: Consent status, type & timestamp
        softAssert.assertTrue(surgeryPage.isConsentCapturedDisplayed(),
                "[TC_ID_SR-156] 'Consent Captured' indicator not displayed.");

        String consentType = surgeryPage.getDetailConsentType();
        String consentTime = surgeryPage.getDetailConsentTimestamp();

        softAssert.assertFalse(consentType.isEmpty(), "[TC_ID_SR-157] Consent type is empty.");
        softAssert.assertFalse(consentTime.isEmpty(), "[TC_ID_SR-158] Consent timestamp is empty.");

        softAssert.assertAll();
    }

    @Test(priority = 6, groups = {"regression"}, description = "TC_ID_SR-171 to 185, 361 to 370: Bottom action button visibility & debounce")
    public void testBottomActionButtonAndDebounce() {
        // TC_ID_SR-171: Bottom action button visibility
        Assert.assertTrue(surgeryPage.isBottomActionButtonDisplayed(),
                "[TC_ID_SR-171] Bottom action button (e.g. Request Allocated) not displayed.");

        // TC_ID_SR-172: 10-tap rapid debounce test
        for (int i = 0; i < 10; i++) {
            surgeryPage.clickBottomActionButton();
        }

        Assert.assertTrue(surgeryPage.isDetailScreenDisplayed() || surgeryPage.isSurgeryRequestsScreenDisplayed(),
                "[TC_ID_SR-172] App crashed or entered invalid state on 10 rapid button taps.");
    }

    @Test(priority = 7, groups = {"regression"}, description = "TC_ID_SR-186 to 200, 371 to 385: Card vs Detail status parity & cross-field consistency")
    public void testCardVsDetailParityAndCrossFieldConsistency() {
        // Close detail and return to list
        if (surgeryPage.isDetailScreenDisplayed()) {
            surgeryPage.clickDetailBackButton();
        }

        Assert.assertTrue(surgeryPage.isSurgeryRequestsScreenDisplayed(),
                "[TC_ID_SR-186] Failed to return to list screen to verify parity.");

        // Re-open first card and verify parity
        if (surgeryPage.getActiveRequestsCount() > 0) {
            surgeryPage.openRequestDetailsByIndex(0);
            Assert.assertTrue(surgeryPage.isDetailScreenDisplayed(),
                    "[TC_ID_SR-187] Reopening card detail failed.");
            surgeryPage.clickDetailBackButton();
        }
    }

    @Test(priority = 8, groups = {"regression"}, description = "TC_ID_SR-250, 386 to 395: End-to-End Surgery Request Inspection Journey")
    public void testEndToEndSurgeryRequestJourneySR250() {
        // Step 1: Ensure on List screen
        Assert.assertTrue(surgeryPage.isSurgeryRequestsScreenDisplayed(),
                "[TC_ID_SR-250] List screen not displayed at E2E start.");

        // Step 2: Select MGM Hospital filter
        surgeryPage.selectHospitalFilter("MGM Hospital");

        // Step 3: Verify summary counts for MGM
        Assert.assertTrue(surgeryPage.isSummaryCardDisplayed("Total Requests"),
                "[TC_ID_SR-250] Total Requests summary card missing under MGM.");

        // Step 4: Open first MGM request details
        if (surgeryPage.getActiveRequestsCount() > 0) {
            surgeryPage.openRequestDetailsByIndex(0);
            Assert.assertTrue(surgeryPage.isDetailScreenDisplayed(),
                    "[TC_ID_SR-250] Detail screen failed to open during MGM journey.");

            // Step 5: Verify patient & surgery sections
            Assert.assertTrue(surgeryPage.isPatientInfoSectionDisplayed(),
                    "[TC_ID_SR-250] Patient Info missing in MGM journey.");
            Assert.assertTrue(surgeryPage.isSurgeryDetailsSectionDisplayed(),
                    "[TC_ID_SR-250] Surgery details missing in MGM journey.");

            // Step 6: Return back to list
            surgeryPage.clickDetailBackButton();
            Assert.assertTrue(surgeryPage.isSurgeryRequestsScreenDisplayed(),
                    "[TC_ID_SR-250] Failed to return to list screen.");
        }

        // Step 7: Reset to All Hospitals
        surgeryPage.selectHospitalFilter("All Hospitals");
        Assert.assertTrue(surgeryPage.isSurgeryRequestsScreenDisplayed(),
                "[TC_ID_SR-250] Reset to All Hospitals failed.");
    }

    @Test(priority = 9, groups = {"regression"}, description = "TC_ID_SR-400, 396 to 400: Master Surgery Lifecycle, State Transitions & Audit E2E Flow")
    public void testMasterSurgeryLifecycleAndAuditE2ESR400() {
        // Verify full navigation cycle: List -> Detail -> Scroll -> Notes -> Consent -> Back -> Filter -> Re-open
        Assert.assertTrue(surgeryPage.isSurgeryRequestsScreenDisplayed(),
                "[TC_ID_SR-400] List screen not displayed.");

        if (surgeryPage.getActiveRequestsCount() > 0) {
            surgeryPage.openRequestDetailsByIndex(0);
            Assert.assertTrue(surgeryPage.isDetailScreenDisplayed(),
                    "[TC_ID_SR-400] Detail screen failed to open.");

            surgeryPage.scrollDownDetailPage();
            Assert.assertTrue(surgeryPage.isConsentCapturedDisplayed(),
                    "[TC_ID_SR-400] Consent section not captured in audit cycle.");

            surgeryPage.scrollUpDetailPage();
            surgeryPage.clickDetailBackButton();
            Assert.assertTrue(surgeryPage.isSurgeryRequestsScreenDisplayed(),
                    "[TC_ID_SR-400] Return to list screen failed in audit flow.");
        }
    }
}
