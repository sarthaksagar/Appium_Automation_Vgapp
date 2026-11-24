package com.vgcares.automation.pages;

import com.vgcares.automation.utils.GestureUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * ============================================================================
 * Surgery Requests Page Object Model
 * ============================================================================
 * Senior SDET enterprise Page Object encapsulating all UI elements,
 * interactions, and state verifications for:
 *  - Surgery Requests List Screen (Title, back navigation, refresh/loader)
 *  - Hospital Filter Chips (All Hospitals, Jehangir, MGM, Anand Rishi)
 *  - Summary Counts Cards (Total Requests, Pending, Approved, Rejected, Allocated)
 *  - Active Requests Cards (Surgery name, ID, patient, duration, date, hospital, ward/bed,
 *    status badge, priority badge, and details eye icon)
 *  - Surgery Request Detail Screen:
 *      * Patient Info (Name, UHID, Gender, Admission No, Ward/Bed, Status)
 *      * Surgery Details (Type, Duration, Hospital, Anesthesia, Scheduled Date,
 *        Time Window, OT Room, Status, Preferred Dates, Requested At)
 *      * Instructions & Notes (Clinical, Pre-op, Post-op, Additional)
 *      * Consent & Metadata (Consent Captured, Type, Timestamp)
 *      * Bottom Action Button (Request Allocated / Request Approved)
 * ============================================================================
 */
public class SurgeryRequestsPage extends BasePage {

    // ── List Screen: Header & Navigation ───────────────────────────────────────
    private final By screenTitle = By.xpath("//*[contains(@text, 'Surgery Requests') or contains(@content-desc, 'Surgery Requests')]");
    private final By backButton = By.xpath("//*[contains(@content-desc, 'Back') or contains(@content-desc, '<') or contains(@class, 'BackButton')]");
    private final By loadingIndicator = By.xpath("//android.widget.ProgressBar | //*[contains(@content-desc, 'Loading') or contains(@text, 'Loading')]");

    // ── Hospital Filter Chips ──────────────────────────────────────────────────
    private final By filterChipAllHospitals = By.xpath("//*[contains(@text, 'All Hospitals') or contains(@content-desc, 'All Hospitals')]");
    private final By filterChipJehangir = By.xpath("//*[contains(@text, 'Jehangir Hospital') or contains(@content-desc, 'Jehangir Hospital')]");
    private final By filterChipMGM = By.xpath("//*[contains(@text, 'MGM Hospital') or contains(@content-desc, 'MGM Hospital')]");
    private final By filterChipAnandRishi = By.xpath("//*[contains(@text, 'Anand Rishi') or contains(@content-desc, 'Anand Rishi')]");

    // ── Summary Cards ──────────────────────────────────────────────────────────
    private final By cardTotalRequests = By.xpath("//*[contains(@text, 'Total Requests') or contains(@content-desc, 'Total Requests')]");
    private final By cardPending = By.xpath("//*[contains(@text, 'Pending') or contains(@content-desc, 'Pending')]");
    private final By cardApproved = By.xpath("//*[contains(@text, 'Approved') or contains(@content-desc, 'Approved')]");
    private final By cardRejected = By.xpath("//*[contains(@text, 'Rejected') or contains(@content-desc, 'Rejected')]");
    private final By cardAllocated = By.xpath("//*[contains(@text, 'Allocated') or contains(@content-desc, 'Allocated')]");

    // ── Active Requests Section ────────────────────────────────────────────────
    private final By activeRequestsHeading = By.xpath("//*[contains(@text, 'Active Requests') or contains(@content-desc, 'Active Requests')]");
    private final By requestCards = By.xpath("//*[contains(@resource-id, 'card') or contains(@content-desc, 'SR-') or contains(@text, 'SR-')]");
    private final By eyeIcons = By.xpath("//*[contains(@content-desc, 'view') or contains(@content-desc, 'eye') or contains(@content-desc, 'Details')]");

    // ── Priority & Status Badges (Card level) ──────────────────────────────────
    private final By badgeAllocated = By.xpath("//*[contains(@text, 'Allocated') or contains(@content-desc, 'Allocated')]");
    private final By badgeApproved = By.xpath("//*[contains(@text, 'Approved') or contains(@content-desc, 'Approved')]");
    private final By badgePending = By.xpath("//*[contains(@text, 'Pending') or contains(@content-desc, 'Pending')]");
    private final By badgeRejected = By.xpath("//*[contains(@text, 'Rejected') or contains(@content-desc, 'Rejected')]");
    private final By badgeEmergency = By.xpath("//*[contains(@text, 'Emergency') or contains(@content-desc, 'Emergency')]");
    private final By badgeUrgent = By.xpath("//*[contains(@text, 'Urgent') or contains(@content-desc, 'Urgent')]");
    private final By badgeRoutine = By.xpath("//*[contains(@text, 'Routine') or contains(@content-desc, 'Routine')]");

    // ── Detail Screen: Header ──────────────────────────────────────────────────
    private final By detailScreenTitle = By.xpath("//*[contains(@text, 'Surgery Request') or contains(@content-desc, 'Surgery Request')]");
    private final By detailBackButton = By.xpath("//*[contains(@content-desc, 'Back') or contains(@content-desc, '<')]");
    private final By detailPriorityIndicator = By.xpath("//*[contains(@text, 'Priority') or contains(@content-desc, 'Priority') or contains(@text, 'Critical') or contains(@content-desc, 'Emergency')]");

    // ── Detail Screen: Patient Info Section ────────────────────────────────────
    private final By patientInfoHeading = By.xpath("//*[contains(@text, 'Patient Info') or contains(@content-desc, 'Patient Info')]");
    private final By patientNameValue = By.xpath("//*[contains(@text, 'Brijesh Kumar') or contains(@content-desc, 'Brijesh Kumar')]");
    private final By patientUhidValue = By.xpath("//*[contains(@text, 'MGMH-2026-') or contains(@content-desc, 'MGMH-2026-') or contains(@text, 'UHID') or contains(@content-desc, 'UHID')]");
    private final By patientGenderValue = By.xpath("//*[contains(@text, 'MALE') or contains(@content-desc, 'MALE') or contains(@text, 'FEMALE') or contains(@content-desc, 'FEMALE')]");
    private final By patientAdmissionStatus = By.xpath("//*[contains(@text, 'Active') or contains(@content-desc, 'Active')]");

    // ── Detail Screen: Surgery Details Section ─────────────────────────────────
    private final By surgeryDetailsHeading = By.xpath("//*[contains(@text, 'Surgery Details') or contains(@content-desc, 'Surgery Details')]");
    private final By surgeryTypeValue = By.xpath("//*[contains(@text, 'Angioplasty') or contains(@content-desc, 'Angioplasty') or contains(@text, 'CABG') or contains(@content-desc, 'CABG')]");
    private final By surgeryDurationValue = By.xpath("//*[contains(@text, 'min') or contains(@content-desc, 'min')]");
    private final By surgeryHospitalValue = By.xpath("//*[contains(@text, 'MGM Hospital') or contains(@content-desc, 'MGM Hospital')]");
    private final By surgeryAnesthesiaValue = By.xpath("//*[contains(@text, 'Local') or contains(@content-desc, 'Local') or contains(@text, 'General') or contains(@content-desc, 'General')]");
    private final By surgeryScheduledDate = By.xpath("//*[contains(@text, 'Aug 18 2026') or contains(@content-desc, 'Aug 18 2026') or contains(@text, 'Not scheduled') or contains(@content-desc, 'Not scheduled')]");
    private final By surgeryTimeWindow = By.xpath("//*[contains(@text, '13:19') or contains(@content-desc, '13:19') or contains(@text, 'Time window') or contains(@content-desc, 'Time window')]");
    private final By surgeryOtValue = By.xpath("//*[contains(@text, 'neonatal ICU') or contains(@content-desc, 'neonatal ICU') or contains(@text, 'OT') or contains(@content-desc, 'OT')]");
    private final By surgeryRequestStatus = By.xpath("//*[contains(@text, 'in_progress') or contains(@content-desc, 'in_progress') or contains(@text, 'allocated') or contains(@content-desc, 'approved')]");

    // ── Detail Screen: Instructions & Notes ────────────────────────────────────
    private final By clinicalNotesValue = By.xpath("//*[contains(@text, 'Clinical Notes') or contains(@content-desc, 'Clinical Notes')]/following::*[1]");
    private final By preOpInstructionsValue = By.xpath("//*[contains(@text, 'Pre-op Instructions') or contains(@content-desc, 'Pre-op Instructions')]/following::*[1]");
    private final By postOpInstructionsValue = By.xpath("//*[contains(@text, 'Post-op Instructions') or contains(@content-desc, 'Post-op Instructions')]/following::*[1]");
    private final By additionalNotesValue = By.xpath("//*[contains(@text, 'Additional Notes') or contains(@content-desc, 'Additional Notes')]/following::*[1]");

    // ── Detail Screen: Consent & Metadata ──────────────────────────────────────
    private final By consentCapturedBadge = By.xpath("//*[contains(@text, 'Consent Captured') or contains(@content-desc, 'Consent Captured')]");
    private final By consentTypeValue = By.xpath("//*[contains(@text, 'guardian_consent') or contains(@content-desc, 'guardian_consent') or contains(@text, 'consent') or contains(@content-desc, 'consent')]");
    private final By consentTimestampValue = By.xpath("//*[contains(@text, '16:25') or contains(@content-desc, '16:25') or contains(@text, 'Aug 18 2026') or contains(@content-desc, 'Aug 18 2026')]");

    // ── Detail Screen: Bottom Action Button ────────────────────────────────────
    private final By bottomActionButton = By.xpath("//*[contains(@text, 'Request Allocated') or contains(@content-desc, 'Request Allocated') or contains(@text, 'Request Approved') or contains(@content-desc, 'Request Approved') or contains(@class, 'Button')][last()]");

    public SurgeryRequestsPage(AndroidDriver driver) {
        super(driver);
    }

    // ── Screen Load & Navigation ───────────────────────────────────────────────
    public boolean isSurgeryRequestsScreenDisplayed() {
        return isElementDisplayed(screenTitle, 10);
    }

    public boolean isBackButtonDisplayed() {
        return isElementDisplayed(backButton, 6);
    }

    public void clickBackButton() {
        log.info("Clicking back button on Surgery Requests screen");
        click(backButton);
    }

    public boolean isLoadingIndicatorDisplayed() {
        return isElementDisplayed(loadingIndicator, 3);
    }

    // ── Hospital Filter Chips ──────────────────────────────────────────────────
    public boolean isHospitalFilterDisplayed(String hospitalName) {
        By chip = By.xpath("//*[contains(@text, '" + hospitalName + "') or contains(@content-desc, '" + hospitalName + "')]");
        return isElementDisplayed(chip, 6);
    }

    public void selectHospitalFilter(String hospitalName) {
        log.info("Selecting hospital filter: {}", hospitalName);
        By chip = By.xpath("//*[contains(@text, '" + hospitalName + "') or contains(@content-desc, '" + hospitalName + "')]");
        click(chip);
    }

    public boolean isHospitalFilterSelected(String hospitalName) {
        By chip = By.xpath("//*[contains(@text, '" + hospitalName + "') or contains(@content-desc, '" + hospitalName + "')]");
        WebElement el = findElement(chip, 6);
        if (el != null) {
            String selected = el.getAttribute("selected");
            String checked = el.getAttribute("checked");
            return "true".equalsIgnoreCase(selected) || "true".equalsIgnoreCase(checked) || el.isSelected();
        }
        return false;
    }

    // ── Summary Cards ──────────────────────────────────────────────────────────
    public boolean isSummaryCardDisplayed(String cardLabel) {
        By card = By.xpath("//*[contains(@text, '" + cardLabel + "') or contains(@content-desc, '" + cardLabel + "')]");
        return isElementDisplayed(card, 6);
    }

    public String getSummaryCardCount(String cardLabel) {
        try {
            By countLocator = By.xpath("//*[contains(@text, '" + cardLabel + "') or contains(@content-desc, '" + cardLabel + "')]/preceding-sibling::*[1] | //*[contains(@text, '" + cardLabel + "') or contains(@content-desc, '" + cardLabel + "')]/following-sibling::*[1]");
            WebElement countEl = findElement(countLocator, 4);
            return countEl != null ? countEl.getText() : "00";
        } catch (Exception e) {
            return "00";
        }
    }

    // ── Active Requests Cards ──────────────────────────────────────────────────
    public boolean isActiveRequestsHeadingDisplayed() {
        return isElementDisplayed(activeRequestsHeading, 8);
    }

    public List<WebElement> getRequestCards() {
        return findElements(requestCards);
    }

    public int getActiveRequestsCount() {
        return getRequestCards().size();
    }

    public boolean isRequestCardWithIdDisplayed(String requestId) {
        By idLocator = By.xpath("//*[contains(@text, '" + requestId + "') or contains(@content-desc, '" + requestId + "')]");
        return isElementDisplayed(idLocator, 6);
    }

    public boolean isSurgeryNameDisplayed(String surgeryName) {
        By surgeryLocator = By.xpath("//*[contains(@text, '" + surgeryName + "') or contains(@content-desc, '" + surgeryName + "')]");
        return isElementDisplayed(surgeryLocator, 6);
    }

    public boolean isPatientNameOnCardDisplayed(String patientName) {
        By patientLocator = By.xpath("//*[contains(@text, '" + patientName + "') or contains(@content-desc, '" + patientName + "')]");
        return isElementDisplayed(patientLocator, 6);
    }

    public boolean isPriorityBadgeDisplayed(String priority) {
        By badge = By.xpath("//*[contains(@text, '" + priority + "') or contains(@content-desc, '" + priority + "')]");
        return isElementDisplayed(badge, 6);
    }

    public boolean isStatusBadgeDisplayed(String status) {
        By badge = By.xpath("//*[contains(@text, '" + status + "') or contains(@content-desc, '" + status + "')]");
        return isElementDisplayed(badge, 6);
    }

    public void openRequestDetailsByIndex(int index) {
        log.info("Opening Surgery Request details for card index: {}", index);
        List<WebElement> icons = findElements(eyeIcons);
        if (!icons.isEmpty() && index < icons.size()) {
            icons.get(index).click();
        } else {
            // Fallback: tap request card directly
            List<WebElement> cards = getRequestCards();
            if (!cards.isEmpty() && index < cards.size()) {
                cards.get(index).click();
            }
        }
    }

    public void openRequestDetailsById(String requestId) {
        log.info("Opening Surgery Request details for Request ID: {}", requestId);
        By eyeBy = By.xpath("//*[contains(@text, '" + requestId + "') or contains(@content-desc, '" + requestId + "')]/following::*[contains(@content-desc, 'view') or contains(@content-desc, 'Details')][1]");
        if (isElementDisplayed(eyeBy, 4)) {
            click(eyeBy);
        } else {
            By cardBy = By.xpath("//*[contains(@text, '" + requestId + "') or contains(@content-desc, '" + requestId + "')]");
            click(cardBy);
        }
    }

    // ── Detail Screen Verifications ────────────────────────────────────────────
    public boolean isDetailScreenDisplayed() {
        return isElementDisplayed(detailScreenTitle, 10);
    }

    public boolean isDetailBackButtonDisplayed() {
        return isElementDisplayed(detailBackButton, 6);
    }

    public void clickDetailBackButton() {
        log.info("Clicking back button on Surgery Request detail screen");
        click(detailBackButton);
    }

    public boolean isDetailPriorityIndicatorDisplayed() {
        return isElementDisplayed(detailPriorityIndicator, 6);
    }

    // Patient Info Section
    public boolean isPatientInfoSectionDisplayed() {
        return isElementDisplayed(patientInfoHeading, 6);
    }

    public String getDetailPatientName() {
        return getText(patientNameValue);
    }

    public String getDetailPatientUhid() {
        return getText(patientUhidValue);
    }

    public String getDetailPatientGender() {
        return getText(patientGenderValue);
    }

    public boolean isPatientAdmissionActive() {
        return isElementDisplayed(patientAdmissionStatus, 6);
    }

    // Surgery Details Section
    public boolean isSurgeryDetailsSectionDisplayed() {
        return isElementDisplayed(surgeryDetailsHeading, 6);
    }

    public String getDetailSurgeryType() {
        return getText(surgeryTypeValue);
    }

    public String getDetailDuration() {
        return getText(surgeryDurationValue);
    }

    public String getDetailHospital() {
        return getText(surgeryHospitalValue);
    }

    public String getDetailAnesthesia() {
        return getText(surgeryAnesthesiaValue);
    }

    public String getDetailScheduledDate() {
        return getText(surgeryScheduledDate);
    }

    public String getDetailTimeWindow() {
        return getText(surgeryTimeWindow);
    }

    public String getDetailOperationTheater() {
        return getText(surgeryOtValue);
    }

    public String getDetailStatus() {
        return getText(surgeryRequestStatus);
    }

    // Instructions & Notes Section
    public boolean isClinicalNotesDisplayed() {
        return isElementDisplayed(clinicalNotesValue, 6);
    }

    public String getDetailClinicalNotes() {
        return getText(clinicalNotesValue);
    }

    public boolean isPreOpInstructionsDisplayed() {
        return isElementDisplayed(preOpInstructionsValue, 6);
    }

    public String getDetailPreOpInstructions() {
        return getText(preOpInstructionsValue);
    }

    public boolean isPostOpInstructionsDisplayed() {
        return isElementDisplayed(postOpInstructionsValue, 6);
    }

    public String getDetailPostOpInstructions() {
        return getText(postOpInstructionsValue);
    }

    public boolean isAdditionalNotesDisplayed() {
        return isElementDisplayed(additionalNotesValue, 6);
    }

    public String getDetailAdditionalNotes() {
        return getText(additionalNotesValue);
    }

    // Consent & Metadata
    public boolean isConsentCapturedDisplayed() {
        return isElementDisplayed(consentCapturedBadge, 6);
    }

    public String getDetailConsentType() {
        return getText(consentTypeValue);
    }

    public String getDetailConsentTimestamp() {
        return getText(consentTimestampValue);
    }

    // Bottom Action Button
    public boolean isBottomActionButtonDisplayed() {
        return isElementDisplayed(bottomActionButton, 6);
    }

    public String getBottomActionButtonText() {
        return getText(bottomActionButton);
    }

    public void clickBottomActionButton() {
        log.info("Clicking bottom action button on Surgery Request detail screen");
        click(bottomActionButton);
    }

    // Scrolling & Gestures
    public void scrollDownDetailPage() {
        GestureUtils.scrollDown(driver);
    }

    public void scrollUpDetailPage() {
        GestureUtils.scrollUp(driver);
    }
}
