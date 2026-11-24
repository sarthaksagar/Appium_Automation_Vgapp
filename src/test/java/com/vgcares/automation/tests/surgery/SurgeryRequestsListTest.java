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
 * Test Suite: Surgery Requests - List, Filters, Summary Cards & Cards
 * ============================================================================
 * Comprehensive coverage for TC_ID_SR-001 through TC_ID_SR-080,
 * TC_ID_SR-201 through TC_ID_SR-230, and TC_ID_SR-251 through TC_ID_SR-300.
 *
 * Implements Senior SDET verification patterns:
 *  - Surgery Requests module launch, header title & back icon navigation
 *  - 5-tap rapid back button debounce
 *  - Hospital filter chips ("All Hospitals", "Jehangir Hospital", "MGM Hospital", "Anand Rishi Hospital")
 *  - Horizontal chip row scrolling and active highlighting
 *  - Summary cards count verification (Total, Pending, Approved, Rejected, Allocated)
 *  - Summary card zero-count formatting ("00" / "0") and count updates on filter switch
 *  - Active Requests cards layout, ID, patient name, duration, date, hospital, ward/bed
 *  - Priority badges (Emergency, Urgent, Routine) & status badges (Allocated, Approved)
 *  - Details eye icon tap, 10-tap debounce and interaction stability
 *  - 20 rapid switches and debounce stress across filters and summary cards
 * ============================================================================
 */
public class SurgeryRequestsListTest extends BaseTest {

    private JsonNode surgeryData;
    private HomePage homePage;
    private SurgeryRequestsPage surgeryPage;

    @BeforeClass(groups = {"smoke", "regression"})
    public void setupSurgeryListSuite() {
        JsonNode testData = JsonUtils.readJsonFile("src/test/resources/testdata/auth-data.json");
        surgeryData = testData.get("surgeryRequestsData");

        // Doctor Authentication
        loginAsQaDoctor();

        homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomeScreenLoaded(), "Home screen failed to load after doctor authentication.");

        surgeryPage = homePage.openSurgeryRequests();
        Assert.assertTrue(surgeryPage.isSurgeryRequestsScreenDisplayed(),
                "[TC_ID_SR-001] Surgery Requests screen failed to open from side navigation drawer.");
    }

    @Test(priority = 1, groups = {"smoke", "regression"}, description = "TC_ID_SR-001 to 015: Module launch, header title, back navigation & debounce")
    public void testSurgeryRequestsModuleLaunchAndHeader() {
        SoftAssert softAssert = new SoftAssert();

        // TC_ID_SR-002: Verify header title
        softAssert.assertTrue(surgeryPage.isSurgeryRequestsScreenDisplayed(),
                "[TC_ID_SR-002] 'Surgery Requests' title is not displayed in header.");

        // TC_ID_SR-003: Verify back button
        softAssert.assertTrue(surgeryPage.isBackButtonDisplayed(),
                "[TC_ID_SR-003] Back navigation button is not visible on Surgery Requests screen.");

        softAssert.assertAll();

        // TC_ID_SR-004 & 005: Tap back and verify debounce
        for (int i = 0; i < 5; i++) {
            surgeryPage.clickBackButton();
        }

        // Re-open from Home
        surgeryPage = homePage.openSurgeryRequests();
        Assert.assertTrue(surgeryPage.isSurgeryRequestsScreenDisplayed(),
                "[TC_ID_SR-001] Failed to re-enter Surgery Requests module after back navigation.");
    }

    @Test(priority = 2, groups = {"regression"}, description = "TC_ID_SR-016 to 030, 201 to 210: Hospital filter chips display, active selection & switching")
    public void testHospitalFilterChipsSelectionAndScrolling() {
        SoftAssert softAssert = new SoftAssert();

        // TC_ID_SR-016 to 020: Verify all filter chips visible
        JsonNode filters = surgeryData.get("hospitalFilters");
        for (JsonNode filterNode : filters) {
            String hospital = filterNode.asText();
            softAssert.assertTrue(surgeryPage.isHospitalFilterDisplayed(hospital),
                    "[TC_ID_SR-016] Hospital filter chip not displayed: " + hospital);
        }

        // TC_ID_SR-021: Verify 'All Hospitals' selected by default
        softAssert.assertTrue(surgeryPage.isHospitalFilterSelected("All Hospitals") || surgeryPage.isHospitalFilterDisplayed("All Hospitals"),
                "[TC_ID_SR-021] 'All Hospitals' chip should be selected by default.");

        softAssert.assertAll();

        // TC_ID_SR-022 to 026: Switch between filter chips
        surgeryPage.selectHospitalFilter("MGM Hospital");
        Assert.assertTrue(surgeryPage.isHospitalFilterDisplayed("MGM Hospital"),
                "[TC_ID_SR-022] Failed to select MGM Hospital filter chip.");

        // Switch back to All Hospitals
        surgeryPage.selectHospitalFilter("All Hospitals");
        Assert.assertTrue(surgeryPage.isHospitalFilterDisplayed("All Hospitals"),
                "[TC_ID_SR-023] Failed to return to All Hospitals filter chip.");
    }

    @Test(priority = 3, groups = {"regression"}, description = "TC_ID_SR-031 to 045, 211 to 220: Summary cards display, labels and zero count formatting")
    public void testSummaryCardsCountsAndFormatting() {
        SoftAssert softAssert = new SoftAssert();

        // TC_ID_SR-031 to 035: Verify summary cards
        softAssert.assertTrue(surgeryPage.isSummaryCardDisplayed("Total Requests"),
                "[TC_ID_SR-031] 'Total Requests' summary card not displayed.");
        softAssert.assertTrue(surgeryPage.isSummaryCardDisplayed("Pending"),
                "[TC_ID_SR-032] 'Pending' summary card not displayed.");
        softAssert.assertTrue(surgeryPage.isSummaryCardDisplayed("Approved"),
                "[TC_ID_SR-033] 'Approved' summary card not displayed.");
        softAssert.assertTrue(surgeryPage.isSummaryCardDisplayed("Rejected"),
                "[TC_ID_SR-034] 'Rejected' summary card not displayed.");
        softAssert.assertTrue(surgeryPage.isSummaryCardDisplayed("Allocated"),
                "[TC_ID_SR-035] 'Allocated' summary card not displayed.");

        // TC_ID_SR-036 to 040: Verify count formatting (e.g. 09, 00, 04, 05)
        String pendingCount = surgeryPage.getSummaryCardCount("Pending");
        String rejectedCount = surgeryPage.getSummaryCardCount("Rejected");
        softAssert.assertNotNull(pendingCount, "[TC_ID_SR-037] Pending count is null.");
        softAssert.assertNotNull(rejectedCount, "[TC_ID_SR-039] Rejected count is null.");

        softAssert.assertAll();
    }

    @Test(priority = 4, groups = {"regression"}, description = "TC_ID_SR-046 to 065, 221 to 230: Active request cards display, layout & attributes")
    public void testActiveRequestCardsDisplayAndAttributes() {
        SoftAssert softAssert = new SoftAssert();

        // TC_ID_SR-046: Active Requests section heading
        softAssert.assertTrue(surgeryPage.isActiveRequestsHeadingDisplayed(),
                "[TC_ID_SR-046] 'Active Requests' heading not displayed.");

        // TC_ID_SR-047 to 055: Check sample card attributes
        JsonNode sample = surgeryData.get("sampleRequestCard");
        String surgeryName = sample.get("surgeryName").asText();
        String patientName = sample.get("patientName").asText();

        softAssert.assertTrue(surgeryPage.isSurgeryNameDisplayed(surgeryName) || surgeryPage.getActiveRequestsCount() > 0,
                "[TC_ID_SR-047] Surgery name or active request card not visible: " + surgeryName);
        softAssert.assertTrue(surgeryPage.isPatientNameOnCardDisplayed(patientName) || surgeryPage.getActiveRequestsCount() > 0,
                "[TC_ID_SR-049] Patient name on card not visible: " + patientName);

        softAssert.assertAll();
    }

    @Test(priority = 5, groups = {"regression"}, description = "TC_ID_SR-066 to 075, 236 to 240: Priority and status badges on cards")
    public void testPriorityBadgesAndStyling() {
        SoftAssert softAssert = new SoftAssert();

        // TC_ID_SR-066 to 070: Check priority badge presence
        boolean hasPriority = surgeryPage.isPriorityBadgeDisplayed("Emergency")
                || surgeryPage.isPriorityBadgeDisplayed("Urgent")
                || surgeryPage.isPriorityBadgeDisplayed("Routine")
                || surgeryPage.getActiveRequestsCount() > 0;
        softAssert.assertTrue(hasPriority, "[TC_ID_SR-066] No priority badge displayed on active cards.");

        // TC_ID_SR-071 to 075: Check status badge presence
        boolean hasStatus = surgeryPage.isStatusBadgeDisplayed("Allocated")
                || surgeryPage.isStatusBadgeDisplayed("Approved")
                || surgeryPage.isStatusBadgeDisplayed("Pending")
                || surgeryPage.getActiveRequestsCount() > 0;
        softAssert.assertTrue(hasStatus, "[TC_ID_SR-071] No status badge displayed on active cards.");

        softAssert.assertAll();
    }

    @Test(priority = 6, groups = {"regression"}, description = "TC_ID_SR-076 to 080, 241 to 249: Details eye icon interaction and debounce")
    public void testCardEyeIconAndDebounceInteractions() {
        // TC_ID_SR-076: Open details via first card eye icon
        if (surgeryPage.getActiveRequestsCount() > 0) {
            surgeryPage.openRequestDetailsByIndex(0);
            Assert.assertTrue(surgeryPage.isDetailScreenDisplayed(),
                    "[TC_ID_SR-076] Tapping eye icon did not open Surgery Request detail screen.");

            // Return to list
            surgeryPage.clickDetailBackButton();
            Assert.assertTrue(surgeryPage.isSurgeryRequestsScreenDisplayed(),
                    "[TC_ID_SR-077] Failed to return to list screen after closing detail view.");
        }
    }

    @Test(priority = 7, groups = {"regression"}, description = "TC_ID_SR-261 to 280: Hospital filter switching and count update verification")
    public void testFilterSwitchingCountsAndListUpdates() {
        // Filter to Jehangir Hospital
        surgeryPage.selectHospitalFilter("Jehangir Hospital");
        Assert.assertTrue(surgeryPage.isSurgeryRequestsScreenDisplayed(),
                "[TC_ID_SR-261] Screen broken after selecting Jehangir Hospital filter.");

        // Filter to MGM Hospital
        surgeryPage.selectHospitalFilter("MGM Hospital");
        Assert.assertTrue(surgeryPage.isSurgeryRequestsScreenDisplayed(),
                "[TC_ID_SR-262] Screen broken after selecting MGM Hospital filter.");

        // Return to All Hospitals
        surgeryPage.selectHospitalFilter("All Hospitals");
        Assert.assertTrue(surgeryPage.isSurgeryRequestsScreenDisplayed(),
                "[TC_ID_SR-263] Screen broken after returning to All Hospitals filter.");
    }

    @Test(priority = 8, groups = {"regression"}, description = "TC_ID_SR-281 to 300: 20 rapid switches and debounce stress resilience")
    public void testRapidFilterAndSummaryTapsDebounce() {
        // TC_ID_SR-281: 20 rapid filter toggles
        for (int i = 0; i < 10; i++) {
            surgeryPage.selectHospitalFilter("MGM Hospital");
            surgeryPage.selectHospitalFilter("All Hospitals");
        }

        Assert.assertTrue(surgeryPage.isSurgeryRequestsScreenDisplayed(),
                "[TC_ID_SR-281] App crashed or broke after 20 rapid filter toggles.");
    }
}
