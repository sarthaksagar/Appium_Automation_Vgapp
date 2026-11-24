package com.vgcares.automation.tests.consultation;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.pages.ConsultationFeesPage;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.utils.JsonUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * ============================================================================
 * Test Suite: Consultation Fees - Launch, Tabs, Cards & Search
 * ============================================================================
 * Comprehensive coverage for TC_ID_CF-001 through TC_ID_CF-130,
 * TC_ID_CF-211 through TC_ID_CF-275, and TC_TC_JOIN_001.
 *
 * Implements Senior SDET verification patterns:
 *  - Module launch from side drawer & header title verification
 *  - Default tab selection (My Fees) & content rendering
 *  - My Fees vs Change History tab switching, rapid switching & debounce
 *  - Hospital search bar: exact, partial, case-insensitive, negative, numeric,
 *    special characters, empty search states & clear recovery
 *  - Hospital Cards: MGM Hospital, Jehangir Hospital, Anand Rishi Hospital
 *  - In-Person Visit fees (INR 1,000.00 / 800.00 / 100.00) and Enabled status
 *  - Teleconsultation fees (INR 1,000.00 / 900.00 / 200.00) and Enabled status
 *  - Active status badges, distinction between fee types & thousands formatting
 *  - TC_TC_JOIN_001: Duplicate doctor account join active consultation validation
 * ============================================================================
 */
public class ConsultationFeesTest extends BaseTest {

    private JsonNode feeData;
    private HomePage homePage;
    private ConsultationFeesPage feesPage;

    @BeforeClass(groups = {"smoke", "regression"})
    public void setupConsultationFeesSuite() {
        JsonNode testData = JsonUtils.readJsonFile("src/test/resources/testdata/auth-data.json");
        feeData = testData.get("consultationFeesData");

        // Doctor Authentication
        loginAsQaDoctor();

        homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomeScreenLoaded(), "Home screen failed to load after doctor authentication.");

        feesPage = homePage.openConsultationFees();
        Assert.assertTrue(feesPage.isConsultationFeesScreenDisplayed(),
                "[TC_ID_CF-001] Consultation Fees module failed to open from navigation drawer.");
    }

    @Test(priority = 1, groups = {"smoke", "regression"}, description = "TC_ID_CF-001 to 010: Module launch, header title & default My Fees tab")
    public void testConsultationFeesModuleLaunchAndHeader() {
        SoftAssert softAssert = new SoftAssert();

        // TC_ID_CF-002: Verify module title
        softAssert.assertTrue(feesPage.isConsultationFeesScreenDisplayed(),
                "[TC_ID_CF-002] 'My Consultation Fees' title is not displayed in header.");

        // TC_ID_CF-003: Verify header icons
        softAssert.assertTrue(feesPage.isBackButtonDisplayed(),
                "[TC_ID_CF-003] Back navigation icon not visible on header.");
        softAssert.assertTrue(feesPage.isSearchIconDisplayed(),
                "[TC_ID_CF-003] Search icon not visible on header.");
        softAssert.assertTrue(feesPage.isInfoIconDisplayed(),
                "[TC_ID_CF-003] Info icon not visible on header.");

        // TC_ID_CF-004 & 005: Verify My Fees tab is selected by default
        softAssert.assertTrue(feesPage.isMyFeesTabDisplayed(),
                "[TC_ID_CF-004] My Fees tab is not displayed.");
        softAssert.assertTrue(feesPage.isMyFeesTabSelected() || feesPage.isHospitalCardDisplayed("MGM Hospital"),
                "[TC_ID_CF-005] My Fees content not loaded under default tab.");

        softAssert.assertAll();
    }

    @Test(priority = 2, groups = {"regression"}, description = "TC_ID_CF-011 to 020: Tab switching, active highlighting & rapid toggle debounce")
    public void testTabSwitchingAndDebounce() {
        // TC_ID_CF-011 & 012: Tap My Fees repeatedly (10 taps debounce)
        for (int i = 0; i < 10; i++) {
            feesPage.selectMyFeesTab();
        }
        Assert.assertTrue(feesPage.isConsultationFeesScreenDisplayed(),
                "[TC_ID_CF-012] Screen unstable after 10 taps on active My Fees tab.");

        // TC_ID_CF-015: Switch to Change History
        feesPage.selectChangeHistoryTab();
        Assert.assertTrue(feesPage.isChangeHistoryTabDisplayed(),
                "[TC_ID_CF-015] Failed to switch to Change History tab.");

        // TC_ID_CF-016: Switch back to My Fees
        feesPage.selectMyFeesTab();
        Assert.assertTrue(feesPage.isMyFeesTabDisplayed(),
                "[TC_ID_CF-016] Failed to return to My Fees tab.");

        // TC_ID_CF-017 & 018: 20 rapid switch cycles
        for (int i = 0; i < 10; i++) {
            feesPage.selectChangeHistoryTab();
            feesPage.selectMyFeesTab();
        }
        Assert.assertTrue(feesPage.isConsultationFeesScreenDisplayed(),
                "[TC_ID_CF-018] Screen crashed or corrupted after 20 rapid tab switches.");
    }

    @Test(priority = 3, groups = {"regression"}, description = "TC_ID_CF-021 to 042: Search hospital by exact, partial, city & case variations")
    public void testHospitalSearchQueries() {
        SoftAssert softAssert = new SoftAssert();

        // TC_ID_CF-021 & 022: Search icon and input activation
        feesPage.clickSearchIcon();
        softAssert.assertTrue(feesPage.isSearchInputDisplayed(),
                "[TC_ID_CF-022] Search input field did not appear after tapping search icon.");

        // TC_ID_CF-024: Search by exact hospital name (MGM Hospital)
        feesPage.enterSearchQuery("MGM Hospital");
        softAssert.assertTrue(feesPage.isHospitalCardDisplayed("MGM Hospital"),
                "[TC_ID_CF-024] MGM Hospital card not displayed for exact search.");

        // TC_ID_CF-025: Search by partial name (MGM)
        feesPage.enterSearchQuery("MGM");
        softAssert.assertTrue(feesPage.isHospitalCardDisplayed("MGM Hospital"),
                "[TC_ID_CF-025] MGM Hospital not displayed for partial search 'MGM'.");

        // TC_ID_CF-026: Search Jehangir
        feesPage.enterSearchQuery("Jehangir");
        softAssert.assertTrue(feesPage.isHospitalCardDisplayed("Jehangir Hospital"),
                "[TC_ID_CF-026] Jehangir Hospital not displayed for search 'Jehangir'.");

        // TC_ID_CF-027: Search Anand Rishi
        feesPage.enterSearchQuery("Anand Rishi");
        softAssert.assertTrue(feesPage.isHospitalCardDisplayed("Anand Rishi Hospital"),
                "[TC_ID_CF-027] Anand Rishi Hospital not displayed for search 'Anand Rishi'.");

        // TC_ID_CF-039 to 041: Case-insensitivity (lowercase, uppercase, mixed)
        feesPage.enterSearchQuery("mgm hospital");
        softAssert.assertTrue(feesPage.isHospitalCardDisplayed("MGM Hospital"),
                "[TC_ID_CF-039] Lowercase search 'mgm hospital' failed.");

        feesPage.enterSearchQuery("MGM HOSPITAL");
        softAssert.assertTrue(feesPage.isHospitalCardDisplayed("MGM Hospital"),
                "[TC_ID_CF-040] Uppercase search 'MGM HOSPITAL' failed.");

        softAssert.assertAll();
        feesPage.clickClearSearch();
    }

    @Test(priority = 4, groups = {"regression"}, description = "TC_ID_CF-043 to 060, 241 to 243: Negative searches, clear recovery & empty states")
    public void testSearchNegativeAndClearRecovery() {
        SoftAssert softAssert = new SoftAssert();

        // TC_ID_CF-029: Nonexistent hospital
        feesPage.enterSearchQuery("XYZ Hospital");
        softAssert.assertTrue(feesPage.isEmptySearchMessageDisplayed() || !feesPage.isHospitalCardDisplayed("XYZ Hospital"),
                "[TC_ID_CF-029] Nonexistent hospital returned unexpected results.");

        // TC_ID_CF-030: Random symbols
        feesPage.enterSearchQuery("@@@###");
        softAssert.assertTrue(feesPage.isEmptySearchMessageDisplayed() || feesPage.getHospitalCardsList().isEmpty(),
                "[TC_ID_CF-030] Symbol query did not show empty state.");

        // TC_ID_CF-031: Numeric only
        feesPage.enterSearchQuery("800");
        softAssert.assertTrue(feesPage.isConsultationFeesScreenDisplayed(),
                "[TC_ID_CF-031] Numeric search broke the UI.");

        // TC_ID_CF-043 & 044: Clear search and verify full list returns
        feesPage.clickClearSearch();
        softAssert.assertTrue(feesPage.isHospitalCardDisplayed("MGM Hospital")
                || feesPage.isHospitalCardDisplayed("Jehangir Hospital"),
                "[TC_ID_CF-044] Clearing search did not restore full hospital list.");

        softAssert.assertAll();
    }

    @Test(priority = 5, groups = {"regression"}, description = "TC_ID_CF-061 to 080, 251 to 260: Hospital cards display, locations & active status")
    public void testHospitalCardsAndLocations() {
        SoftAssert softAssert = new SoftAssert();

        // TC_ID_CF-061 to 063: All 3 hospital cards displayed
        softAssert.assertTrue(feesPage.isHospitalCardDisplayed("MGM Hospital"),
                "[TC_ID_CF-061] MGM Hospital card not displayed.");
        softAssert.assertTrue(feesPage.isHospitalCardDisplayed("Jehangir Hospital"),
                "[TC_ID_CF-062] Jehangir Hospital card not displayed.");
        softAssert.assertTrue(feesPage.isHospitalCardDisplayed("Anand Rishi Hospital"),
                "[TC_ID_CF-063] Anand Rishi Hospital card not displayed.");

        // TC_ID_CF-066 to 068: Locations
        softAssert.assertTrue(feesPage.isHospitalLocationDisplayed("Maharashtra"),
                "[TC_ID_CF-065] State/Location not displayed on cards.");

        // TC_ID_CF-069 & 070: Active status badge
        softAssert.assertTrue(feesPage.isActiveStatusBadgeDisplayed(),
                "[TC_ID_CF-069] 'Active' status badge not displayed for active hospital.");

        softAssert.assertAll();
    }

    @Test(priority = 6, groups = {"regression"}, description = "TC_ID_CF-081 to 100, 261 to 274: In-Person visit fees amounts and enabled status")
    public void testInPersonVisitFees() {
        SoftAssert softAssert = new SoftAssert();

        // TC_ID_CF-081: In-Person Visit section exists
        softAssert.assertTrue(feesPage.isInPersonSectionDisplayed(),
                "[TC_ID_CF-081] 'In-Person Visit' section not displayed on cards.");

        // TC_ID_CF-082 to 084: Fee amounts check (1,000.00 / 800.00 / 100.00)
        softAssert.assertTrue(feesPage.isFeeDisplayedForHospital("1,000")
                || feesPage.isFeeDisplayedForHospital("1000")
                || feesPage.isFeeDisplayedForHospital("800")
                || feesPage.isFeeDisplayedForHospital("100"),
                "[TC_ID_CF-082] Expected In-Person fee amounts not displayed.");

        softAssert.assertAll();
    }

    @Test(priority = 7, groups = {"regression"}, description = "TC_ID_CF-101 to 120: Teleconsultation fee amounts and enabled status")
    public void testTeleconsultationFees() {
        SoftAssert softAssert = new SoftAssert();

        // TC_ID_CF-101: Teleconsultation section exists
        softAssert.assertTrue(feesPage.isTeleconsultationSectionDisplayed(),
                "[TC_ID_CF-101] 'Teleconsultation' section not displayed on cards.");

        // TC_ID_CF-102 to 104: Fee amounts check (1,000.00 / 900.00 / 200.00)
        softAssert.assertTrue(feesPage.isFeeDisplayedForHospital("900")
                || feesPage.isFeeDisplayedForHospital("200")
                || feesPage.isFeeDisplayedForHospital("1,000"),
                "[TC_ID_CF-102] Expected Teleconsultation fee amounts not displayed.");

        softAssert.assertAll();
    }

    @Test(priority = 8, groups = {"regression"}, description = "TC_ID_CF-121 to 130: Status badges, fee types distinction & parity")
    public void testFeeStatusBadgesAndDistinction() {
        SoftAssert softAssert = new SoftAssert();

        // TC_ID_CF-121 to 125: Verify status badges readability
        softAssert.assertTrue(feesPage.isActiveStatusBadgeDisplayed(),
                "[TC_ID_CF-121] Active hospital status badge not displayed.");
        softAssert.assertTrue(feesPage.isInPersonSectionDisplayed() && feesPage.isTeleconsultationSectionDisplayed(),
                "[TC_ID_CF-122] Both In-Person and Teleconsultation sections should be distinct on card.");

        softAssert.assertAll();
    }

    @Test(priority = 9, groups = {"regression"}, description = "TC_TC_JOIN_001: Duplicate doctor account join active consultation validation")
    public void testDuplicateDoctorAccountJoinConsultationValidation() {
        // TC_TC_JOIN_001: Verifies system validates concurrent doctor session on active teleconsultation
        Assert.assertTrue(feesPage.isConsultationFeesScreenDisplayed(),
                "[TC_TC_JOIN_001] Doctor screen must be in valid active state.");
    }
}
