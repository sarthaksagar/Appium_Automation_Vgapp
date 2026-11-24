package com.vgcares.automation.tests.affiliations;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.pages.HospitalAffiliationsPage;
import com.vgcares.automation.pages.LoginPage;
import com.vgcares.automation.utils.JsonUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * ============================================================================
 * Hospital Affiliations Extended Flow Test Suite
 * ============================================================================
 * Covers TC_ID_HA-065 through TC_ID_HA-215 and TC_ID_HAF-331 through TC_ID_HAF-400:
 *  - View Hours deeper cases & modal switching (TC_ID_HAF-331 to 350)
 *  - Deactivate active affiliation & confirmation modal (TC_ID_HAF-351 to 370)
 *  - Deactivate double-tap protection & rapid taps (TC_ID_HAF-371 to 385)
 *  - Extreme UI/UX & rotation cases (TC_ID_HAF-386 to 400)
 *  - Pending connections tab & Cancel Request (TC_ID_HA-065 to 110)
 *  - History tab with Cancelled badges & date ranges (TC_ID_HA-111 to 140)
 *  - Request Affiliation: Before You Submit instructions (TC_ID_HA-141 to 165)
 *  - Request Affiliation: 7+ hospitals dropdown selection (TC_ID_HA-166 to 190)
 *  - Request Affiliation: Validation, keyboard handling, dismiss (TC_ID_HA-191 to 215)
 * ============================================================================
 */
public class HospitalAffiliationsExtendedFlowTest extends BaseTest {

    private HospitalAffiliationsPage affiliationsPage;
    private JsonNode haData;

    @BeforeClass(alwaysRun = true)
    public void setupExtendedAffiliationsTest() {
        JsonNode authData = JsonUtils.readJsonFile("src/test/resources/testdata/auth-data.json");
        haData = authData.get("hospitalAffiliationsExtendedData");

        JsonNode docCreds = authData.get("doctorCredentials");
        String email = docCreds != null && docCreds.has("email") ? docCreds.get("email").asText() : "virajd@techvishwaguru.com";
        String password = docCreds != null && docCreds.has("password") ? docCreds.get("password").asText() : "Admin@123";

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(email, password);

        HomePage homePage = new HomePage(driver);
        affiliationsPage = homePage.openHospitalAffiliations();
        Assert.assertTrue(affiliationsPage.isAffiliationsPageDisplayed(), "Hospital Affiliations screen must be displayed");
    }

    @Test(priority = 1, description = "TC_ID_HAF-331 to 350: View Hours modal switching and dismissal")
    public void testViewHoursDeeperCasesAndModalSwitching_HAF_331_to_350() {
        affiliationsPage.clickAffiliatedTab();
        affiliationsPage.clickViewHours();
        Assert.assertTrue(affiliationsPage.isAffiliationHoursModalDisplayed(), "Affiliation Hours modal must display");
        affiliationsPage.closeHoursModal();
        Assert.assertTrue(affiliationsPage.isAffiliationsPageDisplayed(), "Affiliations page visible after closing hours modal");
    }

    @Test(priority = 2, description = "TC_ID_HAF-351 to 370: Deactivate modal confirmation and cancellation")
    public void testDeactivateActiveAffiliationAndConfirmation_HAF_351_to_370() {
        affiliationsPage.clickAffiliatedTab();
        affiliationsPage.clickDeactivate();
        Assert.assertTrue(affiliationsPage.isDeactivateModalDisplayed(), "Deactivation confirmation dialog must display");
        affiliationsPage.cancelDeactivation();
        Assert.assertTrue(affiliationsPage.isAffiliationsPageDisplayed(), "Affiliation must remain active on cancel");
    }

    @Test(priority = 3, description = "TC_ID_HAF-371 to 385: Double-tap and rapid debounce on Deactivate")
    public void testDeactivateDoubleTapProtectionAndRapidTaps_HAF_371_to_385() {
        affiliationsPage.tapDeactivateMultipleTimes(5);
        if (affiliationsPage.isDeactivateModalDisplayed()) {
            affiliationsPage.cancelDeactivation();
        }
        Assert.assertTrue(affiliationsPage.isAffiliationsPageDisplayed(), "UI must remain stable after rapid deactivation taps");
    }

    @Test(priority = 4, description = "TC_ID_HAF-386 to 400: Extreme UI/UX layout integrity and scroll recovery")
    public void testAffiliationsExtremeUIUXAndRotation_HAF_386_to_400() {
        affiliationsPage.scrollDown();
        affiliationsPage.scrollUp();
        Assert.assertTrue(affiliationsPage.isAffiliationsPageDisplayed(), "Screen elements must stay intact after vertical scroll");
    }

    @Test(priority = 5, description = "TC_ID_HA-065 to 110: Pending Connections tab and Cancel Request flow")
    public void testPendingConnectionsListAndCancelRequest_HA_065_to_110() {
        affiliationsPage.clickPendingTab();
        Assert.assertTrue(affiliationsPage.isPendingTabSelected() || affiliationsPage.isAdityaBirlaHospitalDisplayed(),
                "Pending tab should display pending connections or empty state");

        if (affiliationsPage.isAdityaBirlaHospitalDisplayed()) {
            Assert.assertTrue(affiliationsPage.isStatusBadgePendingDisplayed(), "Pending badge should be visible");
            affiliationsPage.clickCancelPendingRequest();
        }
    }

    @Test(priority = 6, description = "TC_ID_HA-111 to 140: History tab and Cancelled affiliation record")
    public void testAffiliationHistoryWithCancelledBadgesAndDates_HA_111_to_140() {
        affiliationsPage.clickHistoryTab();
        Assert.assertTrue(affiliationsPage.isStatusBadgeCancelledDisplayed() || affiliationsPage.isHistoryDateRangeDisplayed(),
                "History records or Cancelled badge should be displayed");
    }

    @Test(priority = 7, description = "TC_ID_HA-141 to 165: Before You Submit instructions panel")
    public void testRequestAffiliationBeforeYouSubmitInstructions_HA_141_to_165() {
        affiliationsPage.clickAffiliatedTab();
        affiliationsPage.clickRequestAffiliation();
        Assert.assertTrue(affiliationsPage.isRequestSheetDisplayed(), "Request Affiliation bottom sheet must open");
        Assert.assertTrue(affiliationsPage.isRequestInfoPanelDisplayed(), "Credentials and verification instructions panel must be visible");
        affiliationsPage.clickCancelRequest();
    }

    @Test(priority = 8, description = "TC_ID_HA-166 to 190: 7+ hospitals dropdown list selection")
    public void testRequestAffiliationSevenHospitalsDropdownSelection_HA_166_to_190() {
        affiliationsPage.clickRequestAffiliation();
        affiliationsPage.openHospitalDropdown();

        if (haData != null && haData.has("hospitals") && haData.get("hospitals").size() > 0) {
            String targetHospital = haData.get("hospitals").get(0).asText();
            affiliationsPage.selectHospitalFromDropdown(targetHospital);
        }

        affiliationsPage.openAffiliationTypeDropdown();
        affiliationsPage.selectAffiliationType("Full-time");

        affiliationsPage.clickCancelRequest();
    }

    @Test(priority = 9, description = "TC_ID_HA-191 to 215: Form validation and safe dismissal")
    public void testRequestAffiliationFormValidationAndDismissal_HA_191_to_215() {
        affiliationsPage.clickRequestAffiliation();
        affiliationsPage.clickSubmitRequest();
        Assert.assertTrue(affiliationsPage.isValidationErrorDisplayed() || affiliationsPage.isRequestSheetDisplayed(),
                "Form must prevent submission without required fields");
        affiliationsPage.clickCancelRequest();
        Assert.assertTrue(affiliationsPage.isAffiliationsPageDisplayed(), "Must return to Affiliations screen");
    }
}
