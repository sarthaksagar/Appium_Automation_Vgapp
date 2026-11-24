package com.vgcares.automation.pages;

import com.vgcares.automation.utils.GestureUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * ============================================================================
 * Consultation Fees Page Object Model
 * ============================================================================
 * Senior SDET enterprise Page Object encapsulating all UI elements,
 * interactions, and state verifications for:
 *  - Module Header (Title, Back navigation, Search icon, Info icon)
 *  - Primary Tabs (My Fees, Change History)
 *  - Search & Filter bar (Input field, Clear/Reset button, Empty-state results)
 *  - Hospital Cards:
 *      * Hospital Identity (Name, City/State location, Active status badge)
 *      * In-Person Visit Fee Card (Icon, Fee amount, Currency, Enabled status)
 *      * Teleconsultation Fee Card (Icon, Fee amount, Currency, Enabled status)
 *  - Change History Records:
 *      * Timestamp, Hospital name & location
 *      * Fee type badge (In-Person Visit, Teleconsultation)
 *      * Fee change transition (Old Fee -> New Fee)
 *      * Actor badge (hospital_admin)
 *  - Consultation Fees Info Modal:
 *      * Modal title, explanatory content (fees managed by Hospital Admin)
 *      * Got it button
 * ============================================================================
 */
public class ConsultationFeesPage extends BasePage {

    // ── Header & Navigation ──────────────────────────────────────────────────
    private final By screenTitle = By.xpath("//*[contains(@text, 'My Consultation Fees') or contains(@content-desc, 'My Consultation Fees') or contains(@text, 'Consultation Fees') or contains(@content-desc, 'Consultation Fees')]");
    private final By backButton = By.xpath("//*[contains(@content-desc, 'Back') or contains(@content-desc, '<') or contains(@class, 'BackButton')]");
    private final By searchIcon = By.xpath("//*[contains(@content-desc, 'Search') or contains(@content-desc, 'search') or contains(@resource-id, 'search')]");
    private final By infoIcon = By.xpath("//*[contains(@content-desc, 'Info') or contains(@content-desc, 'info') or contains(@text, 'i') or contains(@content-desc, 'Help')]");

    // ── Primary Tabs ─────────────────────────────────────────────────────────
    private final By tabMyFees = By.xpath("//*[contains(@text, 'My Fees') or contains(@content-desc, 'My Fees')]");
    private final By tabChangeHistory = By.xpath("//*[contains(@text, 'Change History') or contains(@content-desc, 'Change History') or contains(@text, 'History') or contains(@content-desc, 'History')]");

    // ── Search Interface ─────────────────────────────────────────────────────
    private final By searchInputField = By.xpath("//android.widget.EditText[contains(@hint, 'Search') or contains(@text, 'Search') or contains(@hint, 'hospital') or @resource-id='search_input']");
    private final By clearSearchButton = By.xpath("//*[contains(@content-desc, 'Clear') or contains(@text, 'Clear') or contains(@content-desc, 'close') or contains(@content-desc, 'cancel')]");
    private final By emptySearchIcon = By.xpath("//*[contains(@resource-id, 'empty') or contains(@content-desc, 'no_result') or contains(@class, 'ImageView')]");
    private final By emptySearchMessage = By.xpath("//*[contains(@text, 'No matching') or contains(@content-desc, 'No matching') or contains(@text, 'No results') or contains(@content-desc, 'No results') or contains(@text, 'not found')]");

    // ── Hospital Cards (My Fees Tab) ─────────────────────────────────────────
    private final By hospitalCards = By.xpath("//*[contains(@resource-id, 'card') or contains(@content-desc, 'Hospital') or contains(@text, 'Hospital')]");
    private final By cardMGM = By.xpath("//*[contains(@text, 'MGM Hospital') or contains(@content-desc, 'MGM Hospital')]");
    private final By cardJehangir = By.xpath("//*[contains(@text, 'Jehangir Hospital') or contains(@content-desc, 'Jehangir Hospital')]");
    private final By cardAnandRishi = By.xpath("//*[contains(@text, 'Anand Rishi Hospital') or contains(@content-desc, 'Anand Rishi Hospital')]");

    // Status Badges
    private final By activeHospitalBadge = By.xpath("//*[contains(@text, 'Active') or contains(@content-desc, 'Active')]");
    private final By enabledFeeBadge = By.xpath("//*[contains(@text, 'Enabled') or contains(@content-desc, 'Enabled')]");

    // Fee Sections
    private final By inPersonSection = By.xpath("//*[contains(@text, 'In-Person Visit') or contains(@content-desc, 'In-Person Visit')]");
    private final By teleconsultationSection = By.xpath("//*[contains(@text, 'Teleconsultation') or contains(@content-desc, 'Teleconsultation')]");

    // ── Change History Tab Elements ──────────────────────────────────────────
    private final By historyRecords = By.xpath("//*[contains(@resource-id, 'history_item') or contains(@content-desc, 'Old Fee') or contains(@text, 'Old Fee')]");
    private final By oldFeeLabel = By.xpath("//*[contains(@text, 'Old Fee') or contains(@content-desc, 'Old Fee')]");
    private final By newFeeLabel = By.xpath("//*[contains(@text, 'New Fee') or contains(@content-desc, 'New Fee')]");
    private final By changedByBadge = By.xpath("//*[contains(@text, 'hospital_admin') or contains(@content-desc, 'hospital_admin') or contains(@text, 'Admin') or contains(@content-desc, 'Admin')]");

    // ── Consultation Fees Info Modal ─────────────────────────────────────────
    private final By infoModalTitle = By.xpath("//*[contains(@text, 'Consultation Fees Info') or contains(@content-desc, 'Consultation Fees Info')]");
    private final By infoModalBody = By.xpath("//*[contains(@text, 'managed by Hospital Admin') or contains(@content-desc, 'managed by Hospital Admin') or contains(@text, 'Hospital Admin') or contains(@content-desc, 'Hospital Admin')]");
    private final By gotItButton = By.xpath("//*[contains(@text, 'Got it') or contains(@content-desc, 'Got it') or contains(@text, 'GOT IT') or contains(@content-desc, 'GOT IT')]");

    public ConsultationFeesPage(AndroidDriver driver) {
        super(driver);
    }

    // ── Screen Load & Navigation ─────────────────────────────────────────────
    public boolean isConsultationFeesScreenDisplayed() {
        return isElementDisplayed(screenTitle, 10);
    }

    public boolean isBackButtonDisplayed() {
        return isElementDisplayed(backButton, 6);
    }

    public void clickBackButton() {
        log.info("Clicking back navigation button on Consultation Fees screen");
        click(backButton);
    }

    public boolean isSearchIconDisplayed() {
        return isElementDisplayed(searchIcon, 6);
    }

    public void clickSearchIcon() {
        log.info("Clicking search icon on Consultation Fees header");
        click(searchIcon);
    }

    public boolean isInfoIconDisplayed() {
        return isElementDisplayed(infoIcon, 6);
    }

    public void clickInfoIcon() {
        log.info("Clicking info icon on Consultation Fees header");
        click(infoIcon);
    }

    // ── Primary Tabs ─────────────────────────────────────────────────────────
    public boolean isMyFeesTabDisplayed() {
        return isElementDisplayed(tabMyFees, 6);
    }

    public boolean isChangeHistoryTabDisplayed() {
        return isElementDisplayed(tabChangeHistory, 6);
    }

    public void selectMyFeesTab() {
        log.info("Selecting My Fees tab");
        click(tabMyFees);
    }

    public void selectChangeHistoryTab() {
        log.info("Selecting Change History tab");
        click(tabChangeHistory);
    }

    public boolean isMyFeesTabSelected() {
        WebElement el = findElement(tabMyFees, 6);
        if (el != null) {
            String selected = el.getAttribute("selected");
            return "true".equalsIgnoreCase(selected) || el.isSelected();
        }
        return false;
    }

    public boolean isChangeHistoryTabSelected() {
        WebElement el = findElement(tabChangeHistory, 6);
        if (el != null) {
            String selected = el.getAttribute("selected");
            return "true".equalsIgnoreCase(selected) || el.isSelected();
        }
        return false;
    }

    // ── Search & Filter ──────────────────────────────────────────────────────
    public boolean isSearchInputDisplayed() {
        return isElementDisplayed(searchInputField, 6);
    }

    public void enterSearchQuery(String query) {
        log.info("Entering search query: '{}'", query);
        if (!isSearchInputDisplayed()) {
            clickSearchIcon();
        }
        clearAndSendKeys(searchInputField, query);
    }

    public boolean isClearSearchDisplayed() {
        return isElementDisplayed(clearSearchButton, 4);
    }

    public void clickClearSearch() {
        log.info("Clearing search input");
        if (isClearSearchDisplayed()) {
            click(clearSearchButton);
        } else if (isSearchInputDisplayed()) {
            clearAndSendKeys(searchInputField, "");
        }
    }

    public boolean isEmptySearchMessageDisplayed() {
        return isElementDisplayed(emptySearchMessage, 5);
    }

    public String getEmptySearchMessageText() {
        return getText(emptySearchMessage);
    }

    // ── Hospital Cards & Content ─────────────────────────────────────────────
    public boolean isHospitalCardDisplayed(String hospitalName) {
        By card = By.xpath("//*[contains(@text, '" + hospitalName + "') or contains(@content-desc, '" + hospitalName + "')]");
        return isElementDisplayed(card, 6);
    }

    public boolean isHospitalLocationDisplayed(String location) {
        By loc = By.xpath("//*[contains(@text, '" + location + "') or contains(@content-desc, '" + location + "')]");
        return isElementDisplayed(loc, 6);
    }

    public boolean isActiveStatusBadgeDisplayed() {
        return isElementDisplayed(activeHospitalBadge, 6);
    }

    public boolean isInPersonSectionDisplayed() {
        return isElementDisplayed(inPersonSection, 6);
    }

    public boolean isTeleconsultationSectionDisplayed() {
        return isElementDisplayed(teleconsultationSection, 6);
    }

    public boolean isFeeDisplayedForHospital(String feeAmount) {
        By feeLocator = By.xpath("//*[contains(@text, '" + feeAmount + "') or contains(@content-desc, '" + feeAmount + "')]");
        return isElementDisplayed(feeLocator, 6);
    }

    public String getFeeText(String hospitalName, String feeType) {
        try {
            By feeBy = By.xpath("//*[contains(@text, '" + hospitalName + "') or contains(@content-desc, '" + hospitalName + "')]/following::*[contains(@text, '" + feeType + "') or contains(@content-desc, '" + feeType + "')][1]/following::*[contains(@text, 'INR') or contains(@text, 'Rs') or contains(@text, 'USD') or contains(@text, 'EUR') or contains(@text, '0') or contains(@text, '1') or contains(@text, '2') or contains(@text, '8')][1]");
            WebElement el = findElement(feeBy, 4);
            return el != null ? el.getText() : "";
        } catch (Exception e) {
            return "";
        }
    }

    public List<WebElement> getHospitalCardsList() {
        return findElements(hospitalCards);
    }

    // ── Change History Tab Elements ──────────────────────────────────────────
    public List<WebElement> getHistoryRecordsList() {
        return findElements(historyRecords);
    }

    public boolean isHistoryRecordDisplayed(String hospitalName, String feeTransition) {
        By historyBy = By.xpath("//*[contains(@text, '" + hospitalName + "') or contains(@content-desc, '" + hospitalName + "')]/following::*[contains(@text, '" + feeTransition + "') or contains(@content-desc, '" + feeTransition + "') or contains(@content-desc, 'Fee')][1]");
        return isElementDisplayed(historyBy, 6);
    }

    public boolean isOldFeeLabelDisplayed() {
        return isElementDisplayed(oldFeeLabel, 6);
    }

    public boolean isNewFeeLabelDisplayed() {
        return isElementDisplayed(newFeeLabel, 6);
    }

    public boolean isChangedByBadgeDisplayed() {
        return isElementDisplayed(changedByBadge, 6);
    }

    // ── Consultation Fees Info Modal ─────────────────────────────────────────
    public boolean isInfoModalDisplayed() {
        return isElementDisplayed(infoModalTitle, 6);
    }

    public String getInfoModalBodyText() {
        return getText(infoModalBody);
    }

    public boolean isGotItButtonDisplayed() {
        return isElementDisplayed(gotItButton, 6);
    }

    public void clickGotItButton() {
        log.info("Clicking Got it button to dismiss Consultation Fees Info modal");
        click(gotItButton);
    }

    // Gestures & Scrolling
    public void scrollDownPage() {
        GestureUtils.scrollDown(driver);
    }

    public void scrollUpPage() {
        GestureUtils.scrollUp(driver);
    }
}
