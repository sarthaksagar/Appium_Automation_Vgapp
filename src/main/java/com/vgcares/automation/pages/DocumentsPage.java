package com.vgcares.automation.pages;

import com.vgcares.automation.utils.GestureUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * ============================================================================
 * Documents Page Object Model (My Documents Module)
 * ============================================================================
 * Senior SDET enterprise Page Object encapsulating all UI elements,
 * interactions, and state verifications for:
 *  - Header & Navigation (Title "My Documents", Back button)
 *  - Floating Action Button (+ / Add Document)
 *  - Upload New Document Modal:
 *      * Modal title, close X, Cancel button
 *      * Document Type dropdown (8 types) & required validation
 *      * Document Number input (Optional, boundaries & validation)
 *      * File Selection bottom sheet (Take Photo, Upload from Gallery, Upload PDF, Cancel)
 *      * Upload action button & form validations
 *  - Existing Document Cards List:
 *      * Card display (Experience Certificate, Other Professional Document, etc.)
 *      * Status badge (Pending Review)
 *      * Metadata: Date, File size, Format
 *      * Card Actions: View, Download, Delete & Confirmation dialog
 *  - Gestures and Scrolling
 * ============================================================================
 */
public class DocumentsPage extends BasePage {

    // ── Header & Navigation ──────────────────────────────────────────────────
    private final By screenTitle = By.xpath("//*[contains(@text, 'My Documents') or contains(@content-desc, 'My Documents') or contains(@text, 'Documents') or contains(@content-desc, 'Documents')]");
    private final By backButton = By.xpath("//*[contains(@content-desc, 'Back') or contains(@content-desc, '<') or contains(@class, 'BackButton')]");

    // ── Floating Action Button (FAB) ─────────────────────────────────────────
    private final By floatingAddButton = By.xpath("//*[contains(@resource-id, 'fab') or contains(@content-desc, 'Add') or contains(@content-desc, '+') or contains(@text, '+')]");

    // ── Upload New Document Modal ────────────────────────────────────────────
    private final By uploadModalTitle = By.xpath("//*[contains(@text, 'Upload New Document') or contains(@content-desc, 'Upload New Document')]");
    private final By modalCloseX = By.xpath("//*[contains(@content-desc, 'Close') or contains(@content-desc, 'close') or contains(@text, 'X') or contains(@content-desc, 'X') or contains(@text, 'x')]");
    private final By modalCancelButton = By.xpath("//*[contains(@text, 'Cancel') or contains(@content-desc, 'Cancel')]");
    private final By modalUploadButton = By.xpath("//android.widget.Button[contains(@text, 'Upload') or contains(@content-desc, 'Upload')] | //*[contains(@text, 'Upload') or contains(@content-desc, 'Upload')][last()]");

    // Document Type Dropdown & Field
    private final By documentTypeDropdown = By.xpath("//*[contains(@hint, 'Document Type') or contains(@text, 'Document Type') or contains(@content-desc, 'Document Type') or contains(@text, 'Select document type') or contains(@content-desc, 'Select document type')]");
    private final By documentTypeRequiredAsterisk = By.xpath("//*[contains(@text, 'Document Type') or contains(@content-desc, 'Document Type')]/following-sibling::*[contains(@text, '*') or contains(@content-desc, '*')]");
    private final By typeSelectionPanel = By.xpath("//*[contains(@resource-id, 'select_dialog') or contains(@class, 'ListView') or contains(@class, 'BottomSheet')]");

    // Document Number Input (Optional)
    private final By documentNumberField = By.xpath("//android.widget.EditText[contains(@hint, 'Document Number') or contains(@text, 'Document Number') or contains(@hint, 'Number')]");

    // File Selection
    private final By chooseFileButton = By.xpath("//*[contains(@text, 'Choose File') or contains(@content-desc, 'Choose File') or contains(@text, 'Browse') or contains(@content-desc, 'Browse') or contains(@text, 'Select File')]");
    private final By selectedFileNameText = By.xpath("//*[contains(@resource-id, 'file_name') or contains(@text, '.pdf') or contains(@text, '.jpg') or contains(@text, '.png') or contains(@content-desc, '.pdf')]");

    // File Source Bottom Sheet Options
    private final By sourceTakePhoto = By.xpath("//*[contains(@text, 'Take Photo') or contains(@content-desc, 'Take Photo') or contains(@text, 'Camera') or contains(@content-desc, 'Camera')]");
    private final By sourceGallery = By.xpath("//*[contains(@text, 'Gallery') or contains(@content-desc, 'Gallery') or contains(@content-desc, 'Photos')]");
    private final By sourcePdf = By.xpath("//*[contains(@text, 'PDF') or contains(@content-desc, 'PDF') or contains(@content-desc, 'Document') or contains(@text, 'Files')]");
    private final By sourceCancel = By.xpath("//*[contains(@text, 'Cancel') or contains(@content-desc, 'Cancel')]");

    // Form Validations
    private final By typeRequiredError = By.xpath("//*[contains(@text, 'Select document type') or contains(@text, 'Document type is required') or contains(@content-desc, 'required')]");
    private final By fileRequiredError = By.xpath("//*[contains(@text, 'File is required') or contains(@text, 'Please select a file') or contains(@content-desc, 'file is required')]");

    // ── Existing Document Cards ──────────────────────────────────────────────
    private final By documentCards = By.xpath("//*[contains(@resource-id, 'card') or contains(@content-desc, 'Certificate') or contains(@text, 'Certificate') or contains(@content-desc, 'Document') or contains(@text, 'Document')]");
    private final By statusPendingReview = By.xpath("//*[contains(@text, 'Pending Review') or contains(@content-desc, 'Pending Review') or contains(@text, 'Pending') or contains(@content-desc, 'Pending')]");

    // Card Actions
    private final By viewAction = By.xpath("//*[contains(@content-desc, 'View') or contains(@text, 'View') or contains(@content-desc, 'view') or contains(@content-desc, 'eye')]");
    private final By downloadAction = By.xpath("//*[contains(@content-desc, 'Download') or contains(@text, 'Download') or contains(@content-desc, 'download')]");
    private final By deleteAction = By.xpath("//*[contains(@content-desc, 'Delete') or contains(@text, 'Delete') or contains(@content-desc, 'trash') or contains(@content-desc, 'remove')]");

    // Delete Confirmation Dialog
    private final By deleteConfirmDialog = By.xpath("//*[contains(@text, 'Delete Document') or contains(@content-desc, 'Delete Document') or contains(@text, 'Are you sure') or contains(@content-desc, 'Are you sure')]");
    private final By deleteConfirmButton = By.xpath("//android.widget.Button[contains(@text, 'Delete') or contains(@content-desc, 'Delete') or contains(@text, 'Yes') or contains(@content-desc, 'Yes')]");
    private final By deleteCancelButton = By.xpath("//android.widget.Button[contains(@text, 'Cancel') or contains(@content-desc, 'Cancel') or contains(@text, 'No') or contains(@content-desc, 'No')]");

    public DocumentsPage(AndroidDriver driver) {
        super(driver);
    }

    // ── Screen Load & Navigation ─────────────────────────────────────────────
    public boolean isDocumentsScreenDisplayed() {
        return isElementDisplayed(screenTitle, 10);
    }

    public boolean isBackButtonDisplayed() {
        return isElementDisplayed(backButton, 6);
    }

    public void clickBackButton() {
        log.info("Clicking back navigation button on My Documents screen");
        click(backButton);
    }

    // ── Floating Action Button & Modal Launch ─────────────────────────────────
    public boolean isFloatingAddButtonDisplayed() {
        return isElementDisplayed(floatingAddButton, 6);
    }

    public void clickFloatingAddButton() {
        log.info("Clicking floating Add Document button");
        click(floatingAddButton);
    }

    public boolean isUploadModalDisplayed() {
        return isElementDisplayed(uploadModalTitle, 8);
    }

    public void clickModalCloseX() {
        log.info("Clicking modal close X icon");
        click(modalCloseX);
    }

    public void clickModalCancel() {
        log.info("Clicking modal Cancel button");
        click(modalCancelButton);
    }

    // ── Document Type Dropdown ───────────────────────────────────────────────
    public boolean isDocumentTypeDropdownDisplayed() {
        return isElementDisplayed(documentTypeDropdown, 6);
    }

    public void clickDocumentTypeDropdown() {
        log.info("Opening Document Type dropdown");
        click(documentTypeDropdown);
    }

    public void selectDocumentType(String typeName) {
        log.info("Selecting Document Type: '{}'", typeName);
        if (!isElementDisplayed(By.xpath("//*[contains(@text, '" + typeName + "') or contains(@content-desc, '" + typeName + "')]"), 3)) {
            clickDocumentTypeDropdown();
        }
        By typeItem = By.xpath("//*[contains(@text, '" + typeName + "') or contains(@content-desc, '" + typeName + "')]");
        click(typeItem);
    }

    public boolean isDocumentTypeSelected(String typeName) {
        By typeBy = By.xpath("//*[contains(@text, '" + typeName + "') or contains(@content-desc, '" + typeName + "')]");
        return isElementDisplayed(typeBy, 4);
    }

    // ── Document Number Input ────────────────────────────────────────────────
    public boolean isDocumentNumberFieldDisplayed() {
        return isElementDisplayed(documentNumberField, 6);
    }

    public void enterDocumentNumber(String docNumber) {
        log.info("Entering Document Number: '{}'", docNumber);
        clearAndSendKeys(documentNumberField, docNumber);
    }

    public void clearDocumentNumber() {
        log.info("Clearing Document Number field");
        clearAndSendKeys(documentNumberField, "");
    }

    // ── File Selection ───────────────────────────────────────────────────────
    public boolean isChooseFileButtonDisplayed() {
        return isElementDisplayed(chooseFileButton, 6);
    }

    public void clickChooseFile() {
        log.info("Clicking Choose File button");
        click(chooseFileButton);
    }

    public boolean isTakePhotoOptionDisplayed() {
        return isElementDisplayed(sourceTakePhoto, 5);
    }

    public boolean isGalleryOptionDisplayed() {
        return isElementDisplayed(sourceGallery, 5);
    }

    public boolean isPdfOptionDisplayed() {
        return isElementDisplayed(sourcePdf, 5);
    }

    public void selectSourceOption(String sourceName) {
        log.info("Selecting file source: '{}'", sourceName);
        By sourceBy = By.xpath("//*[contains(@text, '" + sourceName + "') or contains(@content-desc, '" + sourceName + "')]");
        click(sourceBy);
    }

    public void cancelFileSourceSelection() {
        log.info("Cancelling file source selection");
        if (isElementDisplayed(sourceCancel, 3)) {
            click(sourceCancel);
        }
    }

    // ── Upload Submission & Validations ──────────────────────────────────────
    public boolean isUploadButtonDisplayed() {
        return isElementDisplayed(modalUploadButton, 6);
    }

    public void clickUploadButton() {
        log.info("Clicking Upload button in modal");
        click(modalUploadButton);
    }

    public boolean isTypeRequiredErrorDisplayed() {
        return isElementDisplayed(typeRequiredError, 5);
    }

    public boolean isFileRequiredErrorDisplayed() {
        return isElementDisplayed(fileRequiredError, 5);
    }

    // ── Existing Document Cards ──────────────────────────────────────────────
    public List<WebElement> getDocumentCardsList() {
        return findElements(documentCards);
    }

    public boolean isDocumentCardDisplayed(String documentType) {
        By cardBy = By.xpath("//*[contains(@text, '" + documentType + "') or contains(@content-desc, '" + documentType + "')]");
        return isElementDisplayed(cardBy, 6);
    }

    public boolean isPendingReviewStatusDisplayed() {
        return isElementDisplayed(statusPendingReview, 6);
    }

    public boolean isViewActionDisplayed() {
        return isElementDisplayed(viewAction, 5);
    }

    public void clickViewAction(int cardIndex) {
        log.info("Clicking View action on card index: {}", cardIndex);
        List<WebElement> views = findElements(viewAction);
        if (!views.isEmpty() && cardIndex < views.size()) {
            views.get(cardIndex).click();
        }
    }

    public boolean isDownloadActionDisplayed() {
        return isElementDisplayed(downloadAction, 5);
    }

    public void clickDownloadAction(int cardIndex) {
        log.info("Clicking Download action on card index: {}", cardIndex);
        List<WebElement> downloads = findElements(downloadAction);
        if (!downloads.isEmpty() && cardIndex < downloads.size()) {
            downloads.get(cardIndex).click();
        }
    }

    public boolean isDeleteActionDisplayed() {
        return isElementDisplayed(deleteAction, 5);
    }

    public void clickDeleteAction(int cardIndex) {
        log.info("Clicking Delete action on card index: {}", cardIndex);
        List<WebElement> deletes = findElements(deleteAction);
        if (!deletes.isEmpty() && cardIndex < deletes.size()) {
            deletes.get(cardIndex).click();
        }
    }

    public boolean isDeleteConfirmDialogDisplayed() {
        return isElementDisplayed(deleteConfirmDialog, 5) || isElementDisplayed(deleteConfirmButton, 5);
    }

    public void confirmDelete() {
        log.info("Confirming document deletion");
        click(deleteConfirmButton);
    }

    public void cancelDelete() {
        log.info("Cancelling document deletion");
        click(deleteCancelButton);
    }

    // ── Gestures & Scrolling ─────────────────────────────────────────────────
    public void scrollDownPage() {
        GestureUtils.scrollDown(driver);
    }

    public void scrollUpPage() {
        GestureUtils.scrollUp(driver);
    }
}
