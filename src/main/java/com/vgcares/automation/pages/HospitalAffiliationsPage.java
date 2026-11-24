package com.vgcares.automation.pages;

import com.vgcares.automation.utils.GestureUtils;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * ============================================================================
 * Hospital Affiliations Page Object Model
 * ============================================================================
 * Comprehensive SDET page object covering TC_ID_HAF-001 through TC_ID_HAF-330
 * and Currency Conversion TC_ID_CU-051 / 101 / 151:
 *  - Launch & Navigation ("My Affiliations", Back button, Search icon)
 *  - Header search interface (exact/partial/case-insensitive/boundaries)
 *  - Three Primary Tabs: Affiliated, Pending, History
 *  - Active Connections (MGM, Jehangir, Anand Rishi, Full-time, 7/14/2026, Location)
 *  - Affiliation Hours Modal (Sunday Closed, Monday-Saturday, Refresh, Close '✕')
 *  - Deactivation Workflow (Deactivate, Confirm modal, Cancel/Confirm)
 *  - Pending Tab (Aditya Birla Memorial, Pending badge, Cancel request flow)
 *  - History Tab (Cancelled badge, start/end dates, multiple historical records)
 *  - Request Hospital Affiliation Bottom Sheet Modal (Title, Guidance panel,
 *    Hospital dropdown, Type dropdown [Full-time, Teleconsultation, Visiting],
 *    Validation, Submit, Cancel, Close '✕')
 *  - Advanced Dropdown & Search (Filtering, Punctuation, 1000-char queries)
 * ============================================================================
 */
public class HospitalAffiliationsPage extends BasePage {

    // ── Header & Navigation ──────────────────────────────────────────────────
    private final By affiliationsTitle = By.xpath("//*[contains(@text, 'My Affiliations') or contains(@content-desc, 'My Affiliations') or contains(@text, 'Affiliations') or contains(@content-desc, 'Affiliations')]");
    private final By backButton = By.xpath("//*[contains(@content-desc, 'Back') or @content-desc='←' or contains(@class, 'BackButton')]");
    private final By searchIcon = By.xpath("//*[contains(@content-desc, 'Search') or contains(@resource-id, 'search_button') or contains(@class, 'search')]");

    // ── Search Interface ─────────────────────────────────────────────────────
    private final By searchInputField = By.xpath("//android.widget.EditText[contains(@hint, 'Search') or contains(@text, 'Search') or @resource-id='search_src_text']");
    private final By searchClearButton = By.xpath("//*[contains(@content-desc, 'Clear') or contains(@resource-id, 'search_close_btn') or contains(@text, '✕')]");
    private final By noResultsState = By.xpath("//*[contains(@text, 'No') or contains(@content-desc, 'No') or contains(@text, 'not found') or contains(@content-desc, 'not found')]");

    // ── Primary Tabs ─────────────────────────────────────────────────────────
    private final By tabAffiliated = By.xpath("//*[contains(@text, 'Affiliated') or contains(@content-desc, 'Affiliated')]");
    private final By tabPending = By.xpath("//*[contains(@text, 'Pending') or contains(@content-desc, 'Pending')]");
    private final By tabHistory = By.xpath("//*[contains(@text, 'History') or contains(@content-desc, 'History')]");

    // ── Affiliated Tab Content ───────────────────────────────────────────────
    private final By activeConnectionsHeading = By.xpath("//*[contains(@text, 'Active Connections') or contains(@content-desc, 'Active Connections') or contains(@text, 'ACTIVE CONNECTIONS') or contains(@content-desc, 'ACTIVE CONNECTIONS')]");
    private final By affiliationCard = By.xpath("//*[contains(@class, 'Card') or contains(@content-desc, 'Hospital') or contains(@text, 'Hospital')]");
    private final By statusBadgeAffiliated = By.xpath("//*[contains(@text, 'AFFILIATED') or contains(@content-desc, 'AFFILIATED')]");

    // Hospital Card Details
    private final By mgmHospitalCard = By.xpath("//*[contains(@text, 'MGM Hospital') or contains(@content-desc, 'MGM Hospital')]");
    private final By jehangirHospitalCard = By.xpath("//*[contains(@text, 'Jehangir') or contains(@content-desc, 'Jehangir')]");
    private final By anandRishiHospitalCard = By.xpath("//*[contains(@text, 'Anand Rishi') or contains(@content-desc, 'Anand Rishi')]");
    private final By affiliationTypeFullTime = By.xpath("//*[contains(@text, 'Full-time') or contains(@content-desc, 'Full-time')]");
    private final By affiliationStartDate = By.xpath("//*[contains(@text, '7/14/2026') or contains(@content-desc, '7/14/2026') or contains(@text, '2026') or contains(@content-desc, '2026')]");
    private final By affiliationLocation = By.xpath("//*[contains(@text, 'Pune') or contains(@content-desc, 'Pune') or contains(@text, 'Maharashtra')]");
    private final By hospitalLogo = By.xpath("//*[contains(@class, 'ImageView') or contains(@resource-id, 'logo') or contains(@content-desc, 'logo')]");

    // Actions on Card
    private final By viewHoursButton = By.xpath("//*[contains(@text, 'View Hours') or contains(@content-desc, 'View Hours') or contains(@text, 'Hours') or contains(@content-desc, 'Hours')]");
    private final By deactivateButton = By.xpath("//*[contains(@text, 'Deactivate') or contains(@content-desc, 'Deactivate')]");

    // ── Affiliation Hours Modal ──────────────────────────────────────────────
    private final By hoursModalTitle = By.xpath("//*[contains(@text, 'Affiliation Hours') or contains(@content-desc, 'Affiliation Hours')]");
    private final By hoursClockIcon = By.xpath("//*[contains(@content-desc, 'clock') or contains(@resource-id, 'clock') or contains(@class, 'clock')]");
    private final By sundayHours = By.xpath("//*[contains(@text, 'Sunday') or contains(@content-desc, 'Sunday')]");
    private final By sundayClosedBadge = By.xpath("//*[contains(@text, 'Closed') or contains(@content-desc, 'Closed')]");
    private final By mondayHours = By.xpath("//*[contains(@text, 'Monday') or contains(@content-desc, 'Monday')]");
    private final By wednesdayHours = By.xpath("//*[contains(@text, 'Wednesday') or contains(@content-desc, 'Wednesday')]");
    private final By fridayHours = By.xpath("//*[contains(@text, 'Friday') or contains(@content-desc, 'Friday')]");
    private final By saturdayHours = By.xpath("//*[contains(@text, 'Saturday') or contains(@content-desc, 'Saturday')]");
    private final By workingDayHoursValue = By.xpath("//*[contains(@text, '09:00') or contains(@content-desc, '09:00')]");
    private final By hoursRefreshIcon = By.xpath("//*[contains(@content-desc, 'Refresh') or contains(@resource-id, 'refresh') or contains(@class, 'refresh')]");
    private final By hoursCloseButton = By.xpath("//*[contains(@content-desc, 'Close') or contains(@text, '✕') or contains(@content-desc, '✕') or @content-desc='x']");

    // ── Deactivation Confirmation Modal ──────────────────────────────────────
    private final By deactivateConfirmModal = By.xpath("//*[contains(@text, 'Deactivate') or contains(@content-desc, 'Deactivate') or contains(@text, 'Are you sure') or contains(@content-desc, 'Are you sure')]");
    private final By deactivateConfirmButton = By.xpath("//android.widget.Button[contains(@text, 'Deactivate') or contains(@content-desc, 'Deactivate') or contains(@text, 'Yes') or contains(@content-desc, 'Yes')]");
    private final By deactivateCancelButton = By.xpath("//*[contains(@text, 'Cancel') or contains(@content-desc, 'Cancel')]");

    // ── Pending Tab Elements ─────────────────────────────────────────────────
    private final By pendingConnectionsHeading = By.xpath("//*[contains(@text, 'Pending Connections') or contains(@content-desc, 'Pending Connections') or contains(@text, 'PENDING CONNECTIONS') or contains(@content-desc, 'PENDING CONNECTIONS')]");
    private final By adityaBirlaHospitalCard = By.xpath("//*[contains(@text, 'Aditya Birla') or contains(@content-desc, 'Aditya Birla')]");
    private final By statusBadgePending = By.xpath("//*[contains(@text, 'Pending') or contains(@content-desc, 'Pending')]");
    private final By cancelPendingButton = By.xpath("//*[contains(@text, 'Cancel') or contains(@content-desc, 'Cancel')]");
    private final By emptyPendingMessage = By.xpath("//*[contains(@text, 'No pending') or contains(@content-desc, 'No pending')]");

    // ── History Tab Elements ─────────────────────────────────────────────────
    private final By statusBadgeCancelled = By.xpath("//*[contains(@text, 'Cancelled') or contains(@content-desc, 'Cancelled') or contains(@text, 'Deactivated') or contains(@content-desc, 'Deactivated')]");
    private final By historyDateRange = By.xpath("//*[contains(@text, '3/8/2026') or contains(@content-desc, '3/8/2026') or contains(@text, '→')]");

    // ── Request Affiliation Bottom Sheet / Modal ─────────────────────────────
    private final By requestAffiliationBottomButton = By.xpath("//*[contains(@text, 'Request Affiliation') or contains(@content-desc, 'Request Affiliation')]");
    private final By requestSheetTitle = By.xpath("//*[contains(@text, 'Request Hospital Affiliation') or contains(@content-desc, 'Request Hospital Affiliation') or contains(@text, 'Request Affiliation') or contains(@content-desc, 'Request Affiliation')]");
    private final By requestInfoPanel = By.xpath("//*[contains(@text, 'Credentials') or contains(@content-desc, 'Credentials') or contains(@text, 'approval') or contains(@content-desc, 'approval') or contains(@text, 'guidance')]");
    private final By requestHospitalField = By.xpath("//*[contains(@text, 'Select Hospital') or contains(@content-desc, 'Select Hospital') or contains(@hint, 'hospital') or (contains(@text, 'Hospital') and contains(@class, 'Dropdown'))]");
    private final By requestTypeField = By.xpath("//*[contains(@text, 'Affiliation Type') or contains(@content-desc, 'Affiliation Type') or contains(@hint, 'type') or (contains(@text, 'Type') and contains(@class, 'Dropdown'))]");
    private final By requestSubmitButton = By.xpath("//*[contains(@text, 'Submit') or contains(@content-desc, 'Submit')]");
    private final By requestCancelButton = By.xpath("//*[contains(@text, 'Cancel') or contains(@content-desc, 'Cancel')]");
    private final By requestCloseX = By.xpath("//*[contains(@content-desc, 'Close') or contains(@text, '✕') or contains(@content-desc, '✕') or @content-desc='x']");

    // Dropdown Type Options
    private final By typeOptionFullTime = By.xpath("//*[contains(@text, 'Full-time') or contains(@content-desc, 'Full-time')]");
    private final By typeOptionTeleconsultation = By.xpath("//*[contains(@text, 'Teleconsultation') or contains(@content-desc, 'Teleconsultation')]");
    private final By typeOptionVisiting = By.xpath("//*[contains(@text, 'Visiting') or contains(@content-desc, 'Visiting')]");

    // Dropdown Search Inside Hospital Picker
    private final By dropdownHospitalSearchInput = By.xpath("//android.widget.EditText[contains(@hint, 'Search') or contains(@text, 'Search') or contains(@hint, 'hospital')]");
    private final By validationError = By.xpath("//*[contains(@text, 'required') or contains(@content-desc, 'required') or contains(@text, 'select') or contains(@content-desc, 'select')]");

    public HospitalAffiliationsPage(AndroidDriver driver) {
        super(driver);
    }

    // ── Launch & Navigation ──────────────────────────────────────────────────
    public boolean isAffiliationsPageDisplayed() {
        return isElementDisplayed(affiliationsTitle, 10) || isElementDisplayed(activeConnectionsHeading, 8);
    }

    public boolean isBackButtonDisplayed() {
        return isElementDisplayed(backButton, 6);
    }

    public void clickBack() {
        log.info("Clicking Affiliations back button");
        click(backButton);
    }

    public boolean isSearchIconDisplayed() {
        return isElementDisplayed(searchIcon, 6);
    }

    public void clickSearchIcon() {
        log.info("Opening search input interface");
        click(searchIcon);
    }

    // ── Search Interactions ──────────────────────────────────────────────────
    public boolean isSearchFieldDisplayed() {
        return isElementDisplayed(searchInputField, 6);
    }

    public void enterSearchQuery(String query) {
        log.info("Entering search query: {}", query);
        clearAndSendKeys(searchInputField, query);
    }

    public void clearSearch() {
        log.info("Clearing search input");
        if (isElementDisplayed(searchClearButton, 4)) {
            click(searchClearButton);
        } else {
            clearAndSendKeys(searchInputField, "");
        }
    }

    public boolean isHospitalResultDisplayed(String hospitalName) {
        By hospitalItem = By.xpath("//*[contains(@text, '" + hospitalName + "') or contains(@content-desc, '" + hospitalName + "')]");
        return isElementDisplayed(hospitalItem, 6);
    }

    public boolean isNoResultsDisplayed() {
        return isElementDisplayed(noResultsState, 6);
    }

    // ── Primary Tabs Navigation ──────────────────────────────────────────────
    public boolean isAffiliatedTabSelected() {
        return isElementDisplayed(tabAffiliated, 6);
    }

    public void clickAffiliatedTab() {
        switchToAffiliatedTab();
    }

    public void clickPendingTab() {
        switchToPendingTab();
    }

    public void clickHistoryTab() {
        switchToHistoryTab();
    }

    public boolean isPendingTabSelected() {
        return isPendingConnectionsHeadingDisplayed();
    }

    public void clickViewHours() {
        click(viewHoursButton);
    }

    public boolean isAffiliationHoursModalDisplayed() {
        return isHoursModalDisplayed();
    }

    public boolean isDeactivateModalDisplayed() {
        return isDeactivateConfirmationModalDisplayed();
    }

    public void clickCancelPendingRequest() {
        clickCancelPending();
    }

    public void switchToAffiliatedTab() {
        log.info("Switching to 'Affiliated' tab");
        click(tabAffiliated);
    }

    public void switchToPendingTab() {
        log.info("Switching to 'Pending' tab");
        click(tabPending);
    }

    public void switchToHistoryTab() {
        log.info("Switching to 'History' tab");
        click(tabHistory);
    }

    // ── Affiliated Tab Content ───────────────────────────────────────────────
    public boolean isActiveConnectionsHeadingDisplayed() {
        return isElementDisplayed(activeConnectionsHeading, 6);
    }

    public boolean isStatusBadgeAffiliatedDisplayed() {
        return isElementDisplayed(statusBadgeAffiliated, 6);
    }

    public boolean isMgmHospitalDisplayed() {
        return isElementDisplayed(mgmHospitalCard, 6);
    }

    public boolean isJehangirHospitalDisplayed() {
        return isElementDisplayed(jehangirHospitalCard, 6);
    }

    public boolean isAnandRishiHospitalDisplayed() {
        return isElementDisplayed(anandRishiHospitalCard, 6);
    }

    public boolean isAffiliationTypeDisplayed() {
        return isElementDisplayed(affiliationTypeFullTime, 6);
    }

    public boolean isAffiliationStartDateDisplayed() {
        return isElementDisplayed(affiliationStartDate, 6);
    }

    public boolean isAffiliationLocationDisplayed() {
        return isElementDisplayed(affiliationLocation, 6);
    }

    public boolean isHospitalLogoDisplayed() {
        return isElementDisplayed(hospitalLogo, 6);
    }

    public boolean isViewHoursButtonDisplayed() {
        return isElementDisplayed(viewHoursButton, 6);
    }

    public void openHoursForHospital(String hospitalName) {
        log.info("Opening Affiliation Hours for: {}", hospitalName);
        By specificViewHours = By.xpath("//*[contains(@text, '" + hospitalName + "') or contains(@content-desc, '" + hospitalName + "')]/ancestor::*[contains(@class, 'Card') or contains(@class, 'View')]//*[contains(@text, 'Hours') or contains(@content-desc, 'Hours')]");
        if (isElementDisplayed(specificViewHours, 4)) {
            click(specificViewHours);
        } else {
            click(viewHoursButton);
        }
    }

    // ── Affiliation Hours Modal ──────────────────────────────────────────────
    public boolean isHoursModalDisplayed() {
        return isElementDisplayed(hoursModalTitle, 8);
    }

    public boolean isHoursClockIconDisplayed() {
        return isElementDisplayed(hoursClockIcon, 4);
    }

    public boolean isSundayHoursDisplayed() {
        return isElementDisplayed(sundayHours, 6);
    }

    public boolean isSundayClosedBadgeDisplayed() {
        return isElementDisplayed(sundayClosedBadge, 6);
    }

    public boolean isMondayHoursDisplayed() {
        return isElementDisplayed(mondayHours, 6);
    }

    public boolean isWednesdayHoursDisplayed() {
        return isElementDisplayed(wednesdayHours, 6);
    }

    public boolean isFridayHoursDisplayed() {
        return isElementDisplayed(fridayHours, 6);
    }

    public boolean isSaturdayHoursDisplayed() {
        return isElementDisplayed(saturdayHours, 6);
    }

    public boolean isWorkingDayHoursValueDisplayed() {
        return isElementDisplayed(workingDayHoursValue, 6);
    }

    public boolean isHoursRefreshIconDisplayed() {
        return isElementDisplayed(hoursRefreshIcon, 6);
    }

    public void clickHoursRefresh() {
        log.info("Refreshing hospital hours in modal");
        click(hoursRefreshIcon);
    }

    public void closeHoursModal() {
        log.info("Closing Affiliation Hours modal");
        if (isElementDisplayed(hoursCloseButton, 4)) {
            click(hoursCloseButton);
        } else {
            GestureUtils.tapByCoordinates(driver, 100, 200);
        }
    }

    public boolean isHospitalHoursPopupShowingFor(String hospitalName) {
        By modalHospitalTitle = By.xpath("//*[contains(@text, '" + hospitalName + "') or contains(@content-desc, '" + hospitalName + "')]");
        return isElementDisplayed(modalHospitalTitle, 6);
    }

    public void dismissHoursModalByBackdrop() {
        log.info("Dismissing hours modal by tapping backdrop outside container");
        GestureUtils.tapByCoordinates(driver, 100, 200);
    }

    public void pressDeviceBack() {
        log.info("Triggering device back navigation");
        try {
            driver.navigate().back();
        } catch (Exception e) {
            log.warn("Device back failed: {}", e.getMessage());
        }
    }

    public boolean isSpecificTimeRangeDisplayed(String timeRange) {
        By timeLocator = By.xpath("//*[contains(@text, '" + timeRange + "') or contains(@content-desc, '" + timeRange + "')]");
        return isElementDisplayed(timeLocator, 6);
    }

    public boolean areAllSevenDaysDisplayed() {
        String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        for (String day : days) {
            By dayLocator = By.xpath("//*[contains(@text, '" + day + "') or contains(@content-desc, '" + day + "')]");
            if (!isElementDisplayed(dayLocator, 3)) {
                return false;
            }
        }
        return true;
    }

    // ── Deactivation Workflow ────────────────────────────────────────────────
    public boolean isDeactivateButtonDisplayed() {
        return isElementDisplayed(deactivateButton, 4);
    }

    public void clickDeactivate() {
        log.info("Clicking Deactivate on active affiliation");
        click(deactivateButton);
    }

    public boolean isDeactivateConfirmationModalDisplayed() {
        return isElementDisplayed(deactivateConfirmModal, 6);
    }

    public void cancelDeactivation() {
        log.info("Canceling affiliation deactivation");
        if (isElementDisplayed(deactivateCancelButton, 4)) {
            click(deactivateCancelButton);
        }
    }

    public void confirmDeactivation() {
        log.info("Confirming affiliation deactivation");
        if (isElementDisplayed(deactivateConfirmButton, 4)) {
            click(deactivateConfirmButton);
        }
    }

    // ── Pending Tab ──────────────────────────────────────────────────────────
    public boolean isPendingConnectionsHeadingDisplayed() {
        return isElementDisplayed(pendingConnectionsHeading, 6);
    }

    public boolean isAdityaBirlaHospitalDisplayed() {
        return isElementDisplayed(adityaBirlaHospitalCard, 6);
    }

    public boolean isStatusBadgePendingDisplayed() {
        return isElementDisplayed(statusBadgePending, 6);
    }

    public boolean isCancelPendingButtonDisplayed() {
        return isElementDisplayed(cancelPendingButton, 6);
    }

    public void clickCancelPending() {
        log.info("Tapping Cancel on pending affiliation");
        click(cancelPendingButton);
    }

    public boolean isCancelPendingConfirmationDisplayed() {
        return isElementDisplayed(deactivateConfirmModal, 6);
    }

    public void rejectPendingCancellation() {
        log.info("Rejecting pending cancellation (tapping Cancel/No)");
        if (isElementDisplayed(deactivateCancelButton, 4)) {
            click(deactivateCancelButton);
        }
    }

    public void confirmPendingCancellation() {
        log.info("Confirming pending cancellation");
        if (isElementDisplayed(deactivateConfirmButton, 4)) {
            click(deactivateConfirmButton);
        }
    }

    public boolean isDeactivateButtonAvailableOnCard() {
        return isElementDisplayed(deactivateButton, 3);
    }

    public boolean isEmptyPendingStateDisplayed() {
        return isElementDisplayed(emptyPendingMessage, 6);
    }

    // ── History Tab ──────────────────────────────────────────────────────────
    public boolean isStatusBadgeCancelledDisplayed() {
        return isElementDisplayed(statusBadgeCancelled, 6);
    }

    public boolean isHistoryDateRangeDisplayed() {
        return isElementDisplayed(historyDateRange, 6);
    }

    public boolean isHospitalInHistory(String hospitalName) {
        By historyRecord = By.xpath("//*[contains(@text, '" + hospitalName + "') or contains(@content-desc, '" + hospitalName + "')]");
        return isElementDisplayed(historyRecord, 6);
    }

    // ── Request Affiliation Bottom Sheet ─────────────────────────────────────
    public boolean isRequestAffiliationButtonDisplayed() {
        return isElementDisplayed(requestAffiliationBottomButton, 6);
    }

    public void clickRequestAffiliation() {
        log.info("Opening Request Hospital Affiliation bottom sheet");
        click(requestAffiliationBottomButton);
    }

    public boolean isRequestSheetDisplayed() {
        return isElementDisplayed(requestSheetTitle, 8);
    }

    public boolean isRequestInfoPanelDisplayed() {
        return isElementDisplayed(requestInfoPanel, 6);
    }

    public boolean isSelectHospitalFieldDisplayed() {
        return isElementDisplayed(requestHospitalField, 6);
    }

    public boolean isAffiliationTypeFieldDisplayed() {
        return isElementDisplayed(requestTypeField, 6);
    }

    public boolean isSubmitRequestButtonDisplayed() {
        return isElementDisplayed(requestSubmitButton, 6);
    }

    public boolean isCancelRequestButtonDisplayed() {
        return isElementDisplayed(requestCancelButton, 6);
    }

    public void openHospitalDropdown() {
        log.info("Opening hospital dropdown in request form");
        click(requestHospitalField);
    }

    public void selectHospitalFromDropdown(String hospitalName) {
        log.info("Selecting hospital from dropdown: {}", hospitalName);
        By hospitalOption = By.xpath("//*[contains(@text, '" + hospitalName + "') or contains(@content-desc, '" + hospitalName + "')]");
        if (!isElementDisplayed(hospitalOption, 3) && isElementDisplayed(dropdownHospitalSearchInput, 3)) {
            clearAndSendKeys(dropdownHospitalSearchInput, hospitalName);
        }
        click(hospitalOption);
    }

    public void openAffiliationTypeDropdown() {
        log.info("Opening Affiliation Type dropdown");
        click(requestTypeField);
    }

    public void selectAffiliationType(String type) {
        log.info("Selecting Affiliation Type: {}", type);
        switch (type.toLowerCase()) {
            case "full-time":
            case "full time":
                click(typeOptionFullTime);
                break;
            case "teleconsultation":
                click(typeOptionTeleconsultation);
                break;
            case "visiting":
                click(typeOptionVisiting);
                break;
            default:
                By dynamicType = By.xpath("//*[contains(@text, '" + type + "') or contains(@content-desc, '" + type + "')]");
                click(dynamicType);
                break;
        }
    }

    public void clickSubmitRequest() {
        log.info("Submitting affiliation request");
        click(requestSubmitButton);
    }

    public void clickCancelRequest() {
        log.info("Canceling affiliation request form");
        if (isElementDisplayed(requestCancelButton, 4)) {
            click(requestCancelButton);
        } else if (isElementDisplayed(requestCloseX, 4)) {
            click(requestCloseX);
        }
    }

    public boolean isValidationErrorDisplayed() {
        return isElementDisplayed(validationError, 4);
    }

    public boolean isRequestGuidancePointDisplayed(String pointText) {
        By guidanceLocator = By.xpath("//*[contains(@text, '" + pointText + "') or contains(@content-desc, '" + pointText + "')]");
        return isElementDisplayed(guidanceLocator, 5);
    }

    public void tapDeactivateMultipleTimes(int taps) {
        log.info("Rapidly tapping Deactivate button {} times", taps);
        for (int i = 0; i < taps; i++) {
            click(deactivateButton);
        }
    }

    public void tapCancelRequestMultipleTimes(int taps) {
        log.info("Rapidly tapping Cancel Request button {} times", taps);
        for (int i = 0; i < taps; i++) {
            if (isElementDisplayed(cancelPendingButton, 2)) {
                click(cancelPendingButton);
            }
        }
    }

    public void tapViewHoursMultipleTimes(int taps) {
        log.info("Rapidly tapping View Hours button {} times", taps);
        for (int i = 0; i < taps; i++) {
            click(viewHoursButton);
        }
    }

    // ── Gestures ─────────────────────────────────────────────────────────────
    public void scrollDown() {
        GestureUtils.scrollDown(driver);
    }

    public void scrollUp() {
        GestureUtils.scrollUp(driver);
    }
}
