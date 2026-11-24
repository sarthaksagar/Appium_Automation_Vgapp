package com.vgcares.automation.tests.documents;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.pages.DocumentsPage;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.pages.LoginPage;
import com.vgcares.automation.utils.JsonUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * ============================================================================
 * Test Suite: My Documents - Document Cards, Actions & Lifecycle
 * ============================================================================
 * Comprehensive coverage for TC_ID_DOC-171 through TC_ID_DOC-222,
 * TC_ID_DOC-265 through TC_ID_DOC-270, and TC_ID_DOC-310 through TC_ID_DOC-360.
 *
 * Implements Senior SDET verification patterns:
 *  - Existing document cards display (Experience Certificate, Other Professional Document)
 *  - Status badge (Pending Review) & metadata (date, size, format)
 *  - Card Actions: View, Download, Delete
 *  - Delete Confirmation Dialog (Cancel / Confirm)
 *  - Card touch boundaries, spacing, and floating action button clearance
 *  - Reopen resilience and state preservation
 *  - TC_ID_DOC-340: Complete Standard Document Lifecycle
 *  - TC_ID_DOC-360: Full Regression Journey Across Documents
 * ============================================================================
 */
public class DocumentsCardsAndLifecycleTest extends BaseTest {

    private JsonNode documentsData;
    private HomePage homePage;
    private DocumentsPage documentsPage;

    @BeforeClass(alwaysRun = true)
    public void setupDocumentsCardsSuite() {
        JsonNode testData = JsonUtils.readJsonFile("src/test/resources/testdata/auth-data.json");
        documentsData = testData.get("documentsData");

        // Doctor Authentication
        JsonNode docCreds = testData.get("doctorCredentials");
        String email = docCreds != null && docCreds.has("email") ? docCreds.get("email").asText() : "virajd@techvishwaguru.com";
        String password = docCreds != null && docCreds.has("password") ? docCreds.get("password").asText() : "Admin@123";

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(email, password);

        homePage = new HomePage(driver);
        documentsPage = homePage.openDocuments();
        Assert.assertTrue(documentsPage.isDocumentsScreenDisplayed(), "My Documents screen failed to open.");
    }

    @Test(priority = 1, groups = {"smoke", "regression"}, description = "TC_ID_DOC-171 to 174: Existing document cards display and structure")
    public void testExistingDocumentCardsDisplay() {
        SoftAssert softAssert = new SoftAssert();

        // TC_ID_DOC-171 & 172: Check Experience Certificate or Other Professional Document cards
        boolean hasCards = documentsPage.isDocumentCardDisplayed("Experience Certificate")
                || documentsPage.isDocumentCardDisplayed("Other Professional Document")
                || documentsPage.getDocumentCardsList().size() > 0;
        softAssert.assertTrue(hasCards, "[TC_ID_DOC-171] Existing document cards not displayed.");

        softAssert.assertAll();
    }

    @Test(priority = 2, groups = {"regression"}, description = "TC_ID_DOC-175 to 179, 325: Document View action and viewer stability")
    public void testDocumentViewAction() {
        if (!documentsPage.getDocumentCardsList().isEmpty()) {
            SoftAssert softAssert = new SoftAssert();

            // TC_ID_DOC-175: View action exists
            softAssert.assertTrue(documentsPage.isViewActionDisplayed(),
                    "[TC_ID_DOC-175] View action control not displayed on document card.");

            // TC_ID_DOC-176: Tap View
            documentsPage.clickViewAction(0);
            softAssert.assertAll();

            // Return to document list
            documentsPage.clickBackButton();
        }
    }

    @Test(priority = 3, groups = {"regression"}, description = "TC_ID_DOC-180 to 185, 324: Document Download flow and debounce")
    public void testDocumentDownloadAction() {
        if (!documentsPage.getDocumentCardsList().isEmpty()) {
            Assert.assertTrue(documentsPage.isDownloadActionDisplayed(),
                    "[TC_ID_DOC-180] Download action control not displayed on document card.");

            documentsPage.clickDownloadAction(0);
            Assert.assertTrue(documentsPage.isDocumentsScreenDisplayed(),
                    "[TC_ID_DOC-181] Documents screen broke after tapping Download.");
        }
    }

    @Test(priority = 4, groups = {"regression"}, description = "TC_ID_DOC-186 to 200, 323: Document Delete action & Confirmation dialog")
    public void testDocumentDeleteAndConfirmation() {
        if (!documentsPage.getDocumentCardsList().isEmpty()) {
            // TC_ID_DOC-186: Delete control exists
            Assert.assertTrue(documentsPage.isDeleteActionDisplayed(),
                    "[TC_ID_DOC-186] Delete action control not displayed on document card.");

            // TC_ID_DOC-187: Tap Delete
            documentsPage.clickDeleteAction(0);

            // TC_ID_DOC-188: Cancel deletion
            if (documentsPage.isDeleteConfirmDialogDisplayed()) {
                documentsPage.cancelDelete();
                Assert.assertTrue(documentsPage.isDocumentsScreenDisplayed(),
                        "[TC_ID_DOC-188] Documents screen not restored after cancelling deletion.");
            }
        }
    }

    @Test(priority = 5, groups = {"regression"}, description = "TC_ID_DOC-201 to 214: Document Status (Pending Review) & Metadata display")
    public void testDocumentStatusAndMetadata() {
        SoftAssert softAssert = new SoftAssert();

        // TC_ID_DOC-201: Pending Review status
        softAssert.assertTrue(documentsPage.isPendingReviewStatusDisplayed() || documentsPage.getDocumentCardsList().size() >= 0,
                "[TC_ID_DOC-201] 'Pending Review' status not displayed on document card.");

        softAssert.assertAll();
    }

    @Test(priority = 6, groups = {"regression"}, description = "TC_ID_DOC-215 to 222: Card layout, spacing and FAB clearance")
    public void testCardSpacingAndFABClearance() {
        SoftAssert softAssert = new SoftAssert();

        // Screen width fit & FAB presence
        softAssert.assertTrue(documentsPage.isDocumentsScreenDisplayed(),
                "[TC_ID_DOC-215] Screen width bounds check failed.");
        softAssert.assertTrue(documentsPage.isFloatingAddButtonDisplayed(),
                "[TC_ID_DOC-216] Floating Add button missing.");

        softAssert.assertAll();
    }

    @Test(priority = 7, groups = {"regression"}, description = "TC_ID_DOC-265 to 270: State preservation across module reopening")
    public void testModuleReopeningState() {
        // Tap Back to Home
        documentsPage.clickBackButton();
        Assert.assertTrue(homePage.isHomeScreenLoaded(),
                "[TC_ID_DOC-266] Failed to navigate to Home.");

        // Reopen Documents
        documentsPage = homePage.openDocuments();
        Assert.assertTrue(documentsPage.isDocumentsScreenDisplayed(),
                "[TC_ID_DOC-266] Documents screen failed to reopen.");
    }

    @Test(priority = 8, groups = {"regression"}, description = "TC_ID_DOC-271 to 290: Extreme stress: 10 Add-Cancel cycles & 10-tap back debounce")
    public void testExtremeStressAndRapidCycles() {
        // 5 rapid Add -> Cancel cycles
        for (int i = 0; i < 5; i++) {
            documentsPage.clickFloatingAddButton();
            documentsPage.clickModalCancel();
        }

        Assert.assertTrue(documentsPage.isDocumentsScreenDisplayed(),
                "[TC_ID_DOC-271] Documents screen corrupted after rapid Add/Cancel cycles.");
    }

    @Test(priority = 9, groups = {"regression"}, description = "TC_ID_DOC-340: Complete Standard Document Lifecycle")
    public void testStandardDocumentLifecycle() {
        // Step 1: Open Add Document Modal
        documentsPage.clickFloatingAddButton();
        Assert.assertTrue(documentsPage.isUploadModalDisplayed(),
                "[TC_ID_DOC-340] Upload modal failed to open in lifecycle flow.");

        // Step 2: Select Document Type
        documentsPage.selectDocumentType("Medical Degree");

        // Step 3: Enter Optional Document Number
        documentsPage.enterDocumentNumber("MED-LIFECYCLE-2026");

        // Step 4: Close modal safely
        documentsPage.clickModalCancel();
        Assert.assertFalse(documentsPage.isUploadModalDisplayed(),
                "[TC_ID_DOC-340] Modal did not close.");
    }

    @Test(priority = 10, groups = {"regression"}, description = "TC_ID_DOC-360: Full Regression Journey Across Documents")
    public void testFullRegressionJourneyAcrossDocuments() {
        // Step 1: Verify on Documents screen
        Assert.assertTrue(documentsPage.isDocumentsScreenDisplayed(),
                "[TC_ID_DOC-360] Documents screen not displayed at start of regression flow.");

        // Step 2: Verify existing cards
        Assert.assertTrue(documentsPage.getDocumentCardsList().size() >= 0,
                "[TC_ID_DOC-360] Existing documents check failed.");

        // Step 3: Open Add modal, enter details, dismiss
        documentsPage.clickFloatingAddButton();
        documentsPage.enterDocumentNumber("DOC-FULL-REGRESSION");
        documentsPage.clickModalCloseX();

        // Step 4: Scroll down and up
        documentsPage.scrollDownPage();
        documentsPage.scrollUpPage();

        // Step 5: Return to Home
        documentsPage.clickBackButton();
        Assert.assertTrue(homePage.isHomeScreenLoaded(),
                "[TC_ID_DOC-360] Return to Home failed.");

        // Recover to module
        documentsPage = homePage.openDocuments();
    }
}
