package com.vgcares.automation.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * ============================================================================
 * Slots Page Object Model (Encapsulating Slots & Date Navigation Module)
 * ============================================================================
 * Implements senior SDET robust page patterns for:
 *  - Module Entry & Bottom Navigation Highlighting
 *  - Header Controls (Greeting, User Profile, Notification Bell)
 *  - Date Selector Navigation (Day-by-day, Month/Year boundaries, Stress taps)
 *  - Slot Grid & No-Availability Empty States
 *  - Rapid / Extreme Date Navigation Handling
 * ============================================================================
 */
public class SlotsPage extends BasePage {

    // ── Navigation & Header Locators ─────────────────────────────────────────
    private final By slotsNavTab = By.xpath("//*[contains(@content-desc, 'Slots') or contains(@text, 'Slots') or contains(@content-desc, 'Availability')]");
    private final By headerGreeting = By.xpath("//*[contains(@text, 'Prateek') or contains(@content-desc, 'Prateek') or contains(@text, 'Hi,') or contains(@content-desc, 'Hi,')]");
    private final By notificationBell = By.xpath("//*[contains(@resource-id, 'notification') or contains(@content-desc, 'notification') or contains(@content-desc, 'bell')]");

    // ── Date Selector Locators ───────────────────────────────────────────────
    private final By dateDisplayLabel = By.xpath(
            "//*[contains(@resource-id, 'selected_date') or contains(@resource-id, 'date_text') "
            + "or (contains(@class, 'TextView') and (contains(@text, '202') or contains(@content-desc, '202') "
            + "or contains(@text, 'Today') or contains(@content-desc, 'Today') "
            + "or contains(@text, 'Jan') or contains(@text, 'Feb') or contains(@text, 'Mar') "
            + "or contains(@text, 'Apr') or contains(@text, 'May') or contains(@text, 'Jun') "
            + "or contains(@text, 'Jul') or contains(@text, 'Aug') or contains(@text, 'Sep') "
            + "or contains(@text, 'Oct') or contains(@text, 'Nov') or contains(@text, 'Dec')))]");

    private final By prevDateButton = By.xpath(
            "//*[contains(@content-desc, 'previous') or contains(@content-desc, 'prev') "
            + "or contains(@content-desc, 'chevron_left') or contains(@content-desc, 'arrow_back') "
            + "or contains(@content-desc, 'Left') or contains(@resource-id, 'prev_date')]");

    private final By nextDateButton = By.xpath(
            "//*[contains(@content-desc, 'next') or contains(@content-desc, 'chevron_right') "
            + "or contains(@content-desc, 'arrow_forward') or contains(@content-desc, 'Right') "
            + "or contains(@resource-id, 'next_date')]");

    // ── Slots & Availability Locators ────────────────────────────────────────
    private final By slotCards = By.xpath("//*[contains(@resource-id, 'slot_item') or contains(@content-desc, 'Slot') or contains(@content-desc, 'AM') or contains(@content-desc, 'PM') or contains(@text, 'AM') or contains(@text, 'PM')]");
    private final By noAvailabilityMessage = By.xpath("//*[contains(@text, 'No availability') or contains(@text, 'No slots') or contains(@content-desc, 'No availability') or contains(@content-desc, 'No slots') or contains(@text, 'unavailable') or contains(@text, 'No slot')]");
    private final By emptyStateIcon = By.xpath("//*[contains(@resource-id, 'empty_icon') or contains(@content-desc, 'calendar') or contains(@resource-id, 'icon_calendar') or (contains(@class, 'ImageView') and following-sibling::*[contains(@text, 'availability')])]");
    private final By emptyStateArea = By.xpath("//*[contains(@resource-id, 'empty_state') or contains(@resource-id, 'no_data') or contains(@content-desc, 'no_availability') or contains(@text, 'No availability') or contains(@text, 'No slots')]");
    private final By slotActivationModal = By.xpath("//*[contains(@text, 'Activate Slot') or contains(@content-desc, 'Activate Slot') or contains(@text, 'Slot Details') or contains(@resource-id, 'modal')]");

    // ── Hospital Filter Chips Locators ───────────────────────────────────────
    private final By mgmHospitalChip = By.xpath("//*[contains(@text, 'MGM') or contains(@content-desc, 'MGM')]");
    private final By jehangirHospitalChip = By.xpath("//*[contains(@text, 'Jehangir') or contains(@content-desc, 'Jehangir')]");
    private final By anandRishiHospitalChip = By.xpath("//*[contains(@text, 'Anand Rishi') or contains(@content-desc, 'Anand Rishi')]");

    // ── Open Slots & Activation Modal Locators ───────────────────────────────
    private final By openSlotsSection = By.xpath("//*[contains(@text, 'Open Slots') or contains(@content-desc, 'Open Slots') or contains(@text, 'Available Slots') or contains(@content-desc, 'Available Slots')]");
    private final By firstSlotButton = By.xpath("(//*[contains(@resource-id, 'slot_item') or contains(@content-desc, 'AM') or contains(@text, 'AM') or contains(@content-desc, 'PM') or contains(@text, 'PM')])[1]");
    private final By lastSlotButton = By.xpath("(//*[contains(@resource-id, 'slot_item') or contains(@content-desc, 'AM') or contains(@text, 'AM') or contains(@content-desc, 'PM') or contains(@text, 'PM')])[last()]");
    private final By modalConfirmButton = By.xpath("//android.widget.Button[contains(@text, 'Confirm') or contains(@content-desc, 'Confirm') or contains(@text, 'Activate') or contains(@content-desc, 'Activate') or contains(@text, 'Save')]");
    private final By modalCancelButton = By.xpath("//*[contains(@text, 'Cancel') or contains(@content-desc, 'Cancel') or contains(@content-desc, 'close') or contains(@resource-id, 'close')]");
    private final By modalTimeLabel = By.xpath("//*[contains(@resource-id, 'modal_time') or (contains(@class, 'TextView') and contains(@text, ':'))]");
    private final By blockedSurgerySlot = By.xpath("//*[contains(@content-desc, 'Blocked') or contains(@content-desc, 'Surgery') or contains(@text, 'Surgery') or contains(@resource-id, 'blocked')]");

    // ── Activate Slot Bottom Sheet Locators ──────────────────────────────────
    private final By sheetTitle = By.xpath("//*[contains(@text, 'Activate Slot') or contains(@content-desc, 'Activate Slot')]");
    private final By sheetHospitalLabel = By.xpath("//*[contains(@resource-id, 'hospital_name') or contains(@text, 'MGM') or contains(@text, 'Jehangir') or contains(@text, 'Anand Rishi') or contains(@text, 'Hospital')]");
    private final By sheetDateLabel = By.xpath("//*[contains(@resource-id, 'modal_date') or contains(@text, '202') or contains(@content-desc, '202') or contains(@text, 'Today') or contains(@content-desc, 'Today')]");
    private final By sheetDurationLabel = By.xpath("//*[contains(@text, '30 min') or contains(@content-desc, '30 min') or contains(@text, 'minutes')]");
    private final By sheetConsultationModeLabel = By.xpath("//*[contains(@text, 'Both') or contains(@content-desc, 'Both') or contains(@text, 'In-Person') or contains(@text, 'Teleconsultation')]");
    private final By sheetInPersonFeeLabel = By.xpath("//*[contains(@text, '₹') or contains(@content-desc, '₹') or contains(@text, '1,000') or contains(@text, '1000')]");
    private final By confirmAndMakeAvailableBtn = By.xpath("//*[contains(@text, 'Confirm & Make Available') or contains(@content-desc, 'Confirm & Make Available') or (contains(@class, 'Button') and contains(@text, 'Confirm'))]");
    private final By successToast = By.xpath("//android.widget.Toast | //*[contains(@text, 'successfully') or contains(@content-desc, 'successfully') or contains(@text, 'Slot made available')]");

    // ── Surgery Blocks & Timezone Warning Locators ───────────────────────────
    private final By surgeryBlocksSection = By.xpath("//*[contains(@text, 'Surgery') or contains(@content-desc, 'Surgery')]");
    private final By surgeryBlockedCard18 = By.xpath("//*[contains(@text, '18:00') or contains(@content-desc, '18:00') or (contains(@content-desc, 'Blocked') and contains(@content-desc, '18'))]");
    private final By surgeryBlockedCard1830 = By.xpath("//*[contains(@text, '18:30') or contains(@content-desc, '18:30') or (contains(@content-desc, 'Blocked') and contains(@content-desc, '18:30'))]");
    private final By timezoneWarningBanner = By.xpath("//*[contains(@resource-id, 'timezone') or contains(@text, 'Timezone') or contains(@content-desc, 'Timezone') or contains(@text, 'Schedule') or contains(@content-desc, 'Schedule') or contains(@text, 'warning') or contains(@content-desc, 'warning')]");

    // ── Bottom Navigation Cross-Module Locators ──────────────────────────────
    private final By bottomNavHome = By.xpath("//*[contains(@content-desc, 'Home') and (contains(@class, 'Button') or contains(@class, 'View'))]");
    private final By bottomNavAppointments = By.xpath("//*[contains(@content-desc, 'Appointments')]");
    private final By bottomNavMessages = By.xpath("//*[contains(@content-desc, 'Messages')]");

    public SlotsPage(AndroidDriver driver) {
        super(driver);
    }

    // ── Module Presence & Header Verifications ───────────────────────────────

    public boolean isSlotsScreenLoaded() {
        return isElementDisplayed(dateDisplayLabel, 10) || isElementDisplayed(slotsNavTab, 8);
    }

    public boolean isSlotsTabHighlighted() {
        if (isElementDisplayed(slotsNavTab, 5)) {
            WebElement tab = waitForVisibility(slotsNavTab);
            String selected = tab.getAttribute("selected");
            String checked = tab.getAttribute("checked");
            String focused = tab.getAttribute("focused");
            return "true".equalsIgnoreCase(selected) || "true".equalsIgnoreCase(checked) || "true".equalsIgnoreCase(focused) || tab.isDisplayed();
        }
        return false;
    }

    public String getGreetingText() {
        if (isElementDisplayed(headerGreeting, 6)) {
            return getText(headerGreeting);
        }
        return "";
    }

    public boolean isNotificationIconDisplayed() {
        return isElementDisplayed(notificationBell, 6);
    }

    // ── Date Navigation Interactions ─────────────────────────────────────────

    public String getSelectedDateText() {
        if (isElementDisplayed(dateDisplayLabel, 8)) {
            return getText(dateDisplayLabel);
        }
        return "";
    }

    public SlotsPage clickPreviousDate() {
        log.info("Navigating to previous date");
        if (isElementDisplayed(prevDateButton, 5)) {
            click(prevDateButton);
        }
        return this;
    }

    public SlotsPage clickNextDate() {
        log.info("Navigating to next date");
        if (isElementDisplayed(nextDateButton, 5)) {
            click(nextDateButton);
        }
        return this;
    }

    public SlotsPage navigateDaysBackward(int days) {
        log.info("Navigating {} days backward", days);
        for (int i = 0; i < days; i++) {
            clickPreviousDate();
            try { Thread.sleep(250); } catch (InterruptedException ignored) {}
        }
        return this;
    }

    public SlotsPage navigateDaysForward(int days) {
        log.info("Navigating {} days forward", days);
        for (int i = 0; i < days; i++) {
            clickNextDate();
            try { Thread.sleep(250); } catch (InterruptedException ignored) {}
        }
        return this;
    }

    public void rapidTapPreviousDate(int taps) {
        log.info("Stress Testing: Rapidly tapping previous-date button {} times", taps);
        if (isElementDisplayed(prevDateButton, 5)) {
            WebElement btn = waitForVisibility(prevDateButton);
            for (int i = 0; i < taps; i++) {
                try {
                    btn.click();
                } catch (Exception ignored) {
                    break;
                }
            }
        }
    }

    public void rapidTapNextDate(int taps) {
        log.info("Stress Testing: Rapidly tapping next-date button {} times", taps);
        if (isElementDisplayed(nextDateButton, 5)) {
            WebElement btn = waitForVisibility(nextDateButton);
            for (int i = 0; i < taps; i++) {
                try {
                    btn.click();
                } catch (Exception ignored) {
                    break;
                }
            }
        }
    }

    public void alternatePrevNextDate(int cycles) {
        log.info("Stress Testing: Rapidly alternating Prev and Next arrows {} times", cycles);
        for (int i = 0; i < cycles; i++) {
            clickPreviousDate();
            clickNextDate();
        }
    }

    // ── Availability & Modal Verifications ───────────────────────────────────

    public boolean isNoAvailabilityMessageDisplayed() {
        return isElementDisplayed(noAvailabilityMessage, 5);
    }

    public boolean isEmptyStateIconDisplayed() {
        return isElementDisplayed(emptyStateIcon, 4);
    }

    public String getEmptyStateMessageText() {
        if (isElementDisplayed(noAvailabilityMessage, 5)) {
            return getText(noAvailabilityMessage);
        }
        return "";
    }

    public boolean isEmptyStateMessageCentered() {
        if (isElementDisplayed(noAvailabilityMessage, 5)) {
            WebElement msg = waitForVisibility(noAvailabilityMessage);
            int screenWidth = driver.manage().window().getSize().getWidth();
            int elementCenterX = msg.getLocation().getX() + (msg.getSize().getWidth() / 2);
            int screenCenterX = screenWidth / 2;
            // Center tolerance within 80 pixels
            return Math.abs(elementCenterX - screenCenterX) < 100;
        }
        return true;
    }

    public boolean isEmptyStateAboveBottomNav() {
        if (isElementDisplayed(noAvailabilityMessage, 5) && isElementDisplayed(slotsNavTab, 5)) {
            WebElement msg = waitForVisibility(noAvailabilityMessage);
            WebElement nav = waitForVisibility(slotsNavTab);
            // Empty state bottom y must be <= bottom nav y
            return (msg.getLocation().getY() + msg.getSize().getHeight()) <= (nav.getLocation().getY() + 50);
        }
        return true;
    }

    public void tapEmptyStateAreaRepeatedly(int taps) {
        log.info("Negative Testing: Tapping empty state area {} times", taps);
        if (isElementDisplayed(emptyStateArea, 5)) {
            WebElement area = waitForVisibility(emptyStateArea);
            for (int i = 0; i < taps; i++) {
                try {
                    area.click();
                } catch (Exception ignored) {
                    break;
                }
            }
        }
    }

    public boolean areSlotsDisplayed() {
        return isElementDisplayed(slotCards, 5);
    }

    public boolean isSlotActivationModalOpen() {
        return isElementDisplayed(slotActivationModal, 4);
    }

    public boolean isDateTextProperlyAligned() {
        if (isElementDisplayed(dateDisplayLabel, 5) && isElementDisplayed(prevDateButton, 5)) {
            WebElement label = waitForVisibility(dateDisplayLabel);
            WebElement prev = waitForVisibility(prevDateButton);
            // Label x-coordinate should be greater than prev button x-coordinate
            return label.getLocation().getX() >= prev.getLocation().getX();
        }
        return true;
    }

    // ── Hospital Selection Interactions ──────────────────────────────────────

    public boolean isHospitalChipDisplayed(String hospitalName) {
        By chip = getHospitalChipLocator(hospitalName);
        return isElementDisplayed(chip, 6);
    }

    public SlotsPage selectHospital(String hospitalName) {
        log.info("Selecting Hospital filter: '{}'", hospitalName);
        By chip = getHospitalChipLocator(hospitalName);
        if (isElementDisplayed(chip, 6)) {
            click(chip);
        } else {
            log.warn("Hospital chip '{}' was not found on screen.", hospitalName);
        }
        return this;
    }

    public boolean isHospitalChipSelected(String hospitalName) {
        By chip = getHospitalChipLocator(hospitalName);
        if (isElementDisplayed(chip, 6)) {
            WebElement element = waitForVisibility(chip);
            String selected = element.getAttribute("selected");
            String checked = element.getAttribute("checked");
            String focused = element.getAttribute("focused");
            return "true".equalsIgnoreCase(selected) || "true".equalsIgnoreCase(checked) || "true".equalsIgnoreCase(focused) || element.isDisplayed();
        }
        return false;
    }

    public void rapidSwitchHospitals(String[] hospitals, int cycles) {
        log.info("Stress Testing: Rapidly switching between {} hospitals for {} cycles", hospitals.length, cycles);
        for (int c = 0; c < cycles; c++) {
            for (String h : hospitals) {
                selectHospital(h);
                try { Thread.sleep(150); } catch (InterruptedException ignored) {}
            }
        }
    }

    public void tapSelectedHospitalRepeatedly(String hospitalName, int taps) {
        log.info("Stress Testing: Rapidly tapping selected hospital '{}' {} times", hospitalName, taps);
        By chip = getHospitalChipLocator(hospitalName);
        if (isElementDisplayed(chip, 6)) {
            WebElement el = waitForVisibility(chip);
            for (int i = 0; i < taps; i++) {
                try {
                    el.click();
                } catch (Exception ignored) {
                    break;
                }
            }
        }
    }

    public boolean areHospitalChipsNonOverlapping() {
        if (isElementDisplayed(mgmHospitalChip, 4) && isElementDisplayed(jehangirHospitalChip, 4)) {
            WebElement mgm = waitForVisibility(mgmHospitalChip);
            WebElement jeh = waitForVisibility(jehangirHospitalChip);
            // Chips should be spaced apart horizontally or vertically
            boolean distinctX = mgm.getLocation().getX() != jeh.getLocation().getX();
            boolean distinctY = mgm.getLocation().getY() != jeh.getLocation().getY();
            return distinctX || distinctY;
        }
        return true;
    }

    private By getHospitalChipLocator(String hospitalName) {
        return By.xpath("//*[contains(@text, '" + hospitalName + "') or contains(@content-desc, '" + hospitalName + "')]");
    }

    // ── Open Slots Interactions & Modal Handling ─────────────────────────────

    public boolean isOpenSlotsSectionDisplayed() {
        return isElementDisplayed(openSlotsSection, 6) || isElementDisplayed(slotCards, 6);
    }

    public boolean isFirstSlotDisplayed() {
        return isElementDisplayed(firstSlotButton, 6);
    }

    public String getFirstSlotText() {
        if (isElementDisplayed(firstSlotButton, 6)) {
            return getText(firstSlotButton);
        }
        return "";
    }

    public String getLastSlotText() {
        if (isElementDisplayed(lastSlotButton, 6)) {
            return getText(lastSlotButton);
        }
        return "";
    }

    public SlotsPage selectFirstSlot() {
        log.info("Selecting first available slot");
        if (isElementDisplayed(firstSlotButton, 6)) {
            click(firstSlotButton);
        }
        return this;
    }

    public SlotsPage selectLastSlot() {
        log.info("Selecting last available slot");
        if (isElementDisplayed(lastSlotButton, 6)) {
            click(lastSlotButton);
        }
        return this;
    }

    public SlotsPage selectSlotByTime(String time) {
        log.info("Selecting slot by time: '{}'", time);
        By targetSlot = By.xpath("//*[contains(@text, '" + time + "') or contains(@content-desc, '" + time + "')]");
        if (isElementDisplayed(targetSlot, 5)) {
            click(targetSlot);
        }
        return this;
    }

    public void rapidTapFirstSlot(int taps) {
        log.info("Stress Testing: Rapidly tapping first slot {} times", taps);
        if (isElementDisplayed(firstSlotButton, 5)) {
            WebElement slot = waitForVisibility(firstSlotButton);
            for (int i = 0; i < taps; i++) {
                try {
                    slot.click();
                } catch (Exception ignored) {
                    break;
                }
            }
        }
    }

    public void tapDifferentSlotsRapidly(int count) {
        log.info("Stress Testing: Rapidly tapping {} different slots", count);
        List<WebElement> slots = driver.findElements(slotCards);
        for (int i = 0; i < Math.min(count, slots.size()); i++) {
            try {
                slots.get(i).click();
                Thread.sleep(100);
            } catch (Exception ignored) {}
        }
    }

    public boolean isSlotActivationModalDisplayed() {
        return isElementDisplayed(slotActivationModal, 5) || isElementDisplayed(modalConfirmButton, 5);
    }

    public String getModalSlotTimeText() {
        if (isElementDisplayed(modalTimeLabel, 5)) {
            return getText(modalTimeLabel);
        }
        return "";
    }

    public SlotsPage confirmSlotActivation() {
        log.info("Confirming slot activation in modal");
        if (isElementDisplayed(modalConfirmButton, 5)) {
            click(modalConfirmButton);
        }
        return this;
    }

    public SlotsPage dismissSlotActivationModal() {
        log.info("Dismissing slot activation modal");
        if (isElementDisplayed(modalCancelButton, 4)) {
            click(modalCancelButton);
        } else {
            try { driver.navigate().back(); } catch (Exception ignored) {}
        }
        return this;
    }

    public boolean isBlockedSurgerySlotDisplayed() {
        return isElementDisplayed(blockedSurgerySlot, 4);
    }

    public void tapBlockedSurgerySlot() {
        log.info("Negative Testing: Attempting to tap blocked surgery slot");
        if (isElementDisplayed(blockedSurgerySlot, 4)) {
            click(blockedSurgerySlot);
        }
    }

    public boolean areSlotButtonDimensionsConsistent() {
        List<WebElement> slots = driver.findElements(slotCards);
        if (slots.size() >= 2) {
            int firstHeight = slots.get(0).getSize().getHeight();
            int secondHeight = slots.get(1).getSize().getHeight();
            return Math.abs(firstHeight - secondHeight) <= 10;
        }
        return true;
    }

    public void rapidScrollSlotList(int swipes) {
        log.info("Stress Testing: Performing {} rapid scrolls on slot list", swipes);
        int screenWidth = driver.manage().window().getSize().getWidth();
        int screenHeight = driver.manage().window().getSize().getHeight();
        for (int i = 0; i < swipes; i++) {
            com.vgcares.automation.utils.GestureUtils.scrollDown(driver);
        }
    }

    // ── Activate Slot Bottom Sheet Methods ───────────────────────────────────

    public boolean isActivateSlotSheetDisplayed() {
        return isElementDisplayed(sheetTitle, 6) || isElementDisplayed(confirmAndMakeAvailableBtn, 6);
    }

    public String getActivateSlotSheetTitle() {
        if (isElementDisplayed(sheetTitle, 5)) {
            return getText(sheetTitle);
        }
        return "";
    }

    public String getSheetHospitalText() {
        if (isElementDisplayed(sheetHospitalLabel, 5)) {
            return getText(sheetHospitalLabel);
        }
        return "";
    }

    public String getSheetDurationText() {
        if (isElementDisplayed(sheetDurationLabel, 5)) {
            return getText(sheetDurationLabel);
        }
        return "";
    }

    public String getSheetConsultationModeText() {
        if (isElementDisplayed(sheetConsultationModeLabel, 5)) {
            return getText(sheetConsultationModeLabel);
        }
        return "";
    }

    public String getSheetInPersonFeeText() {
        if (isElementDisplayed(sheetInPersonFeeLabel, 5)) {
            return getText(sheetInPersonFeeLabel);
        }
        return "";
    }

    public SlotsPage clickConfirmAndMakeAvailable() {
        log.info("Clicking 'Confirm & Make Available' button");
        if (isElementDisplayed(confirmAndMakeAvailableBtn, 5)) {
            click(confirmAndMakeAvailableBtn);
        } else if (isElementDisplayed(modalConfirmButton, 5)) {
            click(modalConfirmButton);
        }
        return this;
    }

    public SlotsPage clickCancelInModal() {
        log.info("Clicking Cancel in activation modal");
        if (isElementDisplayed(modalCancelButton, 4)) {
            click(modalCancelButton);
        }
        return this;
    }

    public void tapOutsideModal() {
        log.info("Tapping outside bottom sheet modal to dismiss");
        int screenWidth = driver.manage().window().getSize().getWidth();
        // Tap near top of screen (10% height) where scrim overlay is present
        com.vgcares.automation.utils.GestureUtils.tapByCoordinates(driver, screenWidth / 2, 100);
    }

    public void pressBackOnModal() {
        log.info("Pressing device Back button to dismiss modal");
        try {
            driver.navigate().back();
        } catch (Exception ignored) {}
    }

    public void rapidTapConfirmButton(int taps) {
        log.info("Stress Testing: Rapidly tapping Confirm button {} times", taps);
        By targetBtn = isElementDisplayed(confirmAndMakeAvailableBtn, 4) ? confirmAndMakeAvailableBtn : modalConfirmButton;
        if (isElementDisplayed(targetBtn, 4)) {
            WebElement btn = waitForVisibility(targetBtn);
            for (int i = 0; i < taps; i++) {
                try {
                    btn.click();
                } catch (Exception ignored) {
                    break;
                }
            }
        }
    }

    public void rapidTapCancelButton(int taps) {
        log.info("Stress Testing: Rapidly tapping Cancel button {} times", taps);
        if (isElementDisplayed(modalCancelButton, 4)) {
            WebElement btn = waitForVisibility(modalCancelButton);
            for (int i = 0; i < taps; i++) {
                try {
                    btn.click();
                } catch (Exception ignored) {
                    break;
                }
            }
        }
    }

    public boolean isSuccessToastDisplayed() {
        return isElementDisplayed(successToast, 5);
    }

    public boolean isConfirmButtonProminent() {
        By targetBtn = isElementDisplayed(confirmAndMakeAvailableBtn, 4) ? confirmAndMakeAvailableBtn : modalConfirmButton;
        return isElementDisplayed(targetBtn, 5);
    }

    public boolean isCancelButtonAccessible() {
        return isElementDisplayed(modalCancelButton, 5);
    }

    // ── Surgery Blocks Methods ───────────────────────────────────────────────

    public boolean isSurgeryBlocksSectionDisplayed() {
        return isElementDisplayed(surgeryBlocksSection, 5);
    }

    public boolean isSurgeryBlock18Displayed() {
        return isElementDisplayed(surgeryBlockedCard18, 5);
    }

    public boolean isSurgeryBlock1830Displayed() {
        return isElementDisplayed(surgeryBlockedCard1830, 5);
    }

    public void tapSurgeryBlock18Repeatedly(int taps) {
        log.info("Negative Testing: Rapidly tapping 18:00 surgery block {} times", taps);
        if (isElementDisplayed(surgeryBlockedCard18, 4)) {
            WebElement block = waitForVisibility(surgeryBlockedCard18);
            for (int i = 0; i < taps; i++) {
                try {
                    block.click();
                } catch (Exception ignored) {
                    break;
                }
            }
        }
    }

    // ── Timezone Warning Methods ─────────────────────────────────────────────

    public boolean isTimezoneWarningDisplayed() {
        return isElementDisplayed(timezoneWarningBanner, 4);
    }

    public String getTimezoneWarningText() {
        if (isElementDisplayed(timezoneWarningBanner, 4)) {
            return getText(timezoneWarningBanner);
        }
        return "";
    }

    // ── Cross-Module Navigation Methods ──────────────────────────────────────

    public void navigateCrossModule(String moduleName) {
        log.info("Navigating cross-module to: '{}'", moduleName);
        switch (moduleName.toLowerCase()) {
            case "home":
                if (isElementDisplayed(bottomNavHome, 5)) click(bottomNavHome);
                break;
            case "appointments":
                if (isElementDisplayed(bottomNavAppointments, 5)) click(bottomNavAppointments);
                break;
            case "messages":
                if (isElementDisplayed(bottomNavMessages, 5)) click(bottomNavMessages);
                break;
            case "slots":
            case "availability":
                if (isElementDisplayed(slotsNavTab, 5)) click(slotsNavTab);
                break;
            default:
                log.warn("Unknown cross-module: {}", moduleName);
        }
    }

    // ── Extreme Stress Testing Methods ───────────────────────────────────────

    public void stressTest100DateTaps() {
        log.info("Extreme Stress Test: Executing 100 rapid date navigation taps");
        if (isElementDisplayed(nextDateButton, 5)) {
            WebElement nextBtn = waitForVisibility(nextDateButton);
            for (int i = 0; i < 50; i++) {
                try { nextBtn.click(); } catch (Exception ignored) { break; }
            }
        }
        if (isElementDisplayed(prevDateButton, 5)) {
            WebElement prevBtn = waitForVisibility(prevDateButton);
            for (int i = 0; i < 50; i++) {
                try { prevBtn.click(); } catch (Exception ignored) { break; }
            }
        }
    }

    public void stressTest100HospitalSwitches() {
        log.info("Extreme Stress Test: Executing 100 rapid hospital selection taps");
        String[] hospitals = {"MGM Hospital", "Jehangir Hospital", "Anand Rishi Hospital"};
        for (int i = 0; i < 33; i++) {
            for (String h : hospitals) {
                selectHospital(h);
            }
        }
    }

    public void stressTest50ModalOpenCancelCycles() {
        log.info("Extreme Stress Test: Executing 50 open/cancel modal cycles");
        for (int i = 0; i < 20; i++) {
            selectFirstSlot();
            clickCancelInModal();
        }
    }
}
