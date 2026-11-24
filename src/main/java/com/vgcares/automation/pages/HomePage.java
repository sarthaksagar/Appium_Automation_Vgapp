package com.vgcares.automation.pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class HomePage extends BasePage {

    // Navigation Drawer & Menu Locators
    private final By sideMenuButton = By.xpath("//*[contains(@content-desc, 'Open navigation drawer') or contains(@content-desc, 'drawer') or contains(@content-desc, 'Menu') or contains(@resource-id, 'menu')]");
    private final By sideDrawerHeader = By.xpath("//*[contains(@text, 'Prateek') or contains(@content-desc, 'Prateek') or contains(@text, 'Doctor') or contains(@content-desc, 'Doctor')]");
    private final By myProfileMenuItem = By.xpath("//*[contains(@text, 'My Profile') or contains(@content-desc, 'My Profile') or contains(@text, 'Profile') or contains(@content-desc, 'Profile')]");
    private final By hospitalAffiliationsMenuItem = By.xpath("//*[contains(@text, 'Affiliation') or contains(@content-desc, 'Affiliation') or contains(@text, 'Hospital') or contains(@content-desc, 'Hospital')]");
    private final By surgeryRequestsMenuItem = By.xpath("//*[contains(@text, 'Surgery Request') or contains(@content-desc, 'Surgery Request') or contains(@text, 'Surgery') or contains(@content-desc, 'Surgery')]");
    private final By consultationFeesMenuItem = By.xpath("//*[contains(@text, 'Consultation Fee') or contains(@content-desc, 'Consultation Fee') or contains(@text, 'Fee') or contains(@content-desc, 'Fee')]");
    private final By reviewsMenuItem = By.xpath("//*[contains(@text, 'My Reviews') or contains(@content-desc, 'My Reviews') or contains(@text, 'Reviews') or contains(@content-desc, 'Reviews')]");
    private final By documentsMenuItem = By.xpath("//*[contains(@text, 'My Documents') or contains(@content-desc, 'My Documents') or contains(@text, 'Documents') or contains(@content-desc, 'Documents')]");
    private final By settingsMenuItem = By.xpath("//*[contains(@text, 'Settings') or contains(@content-desc, 'Settings')]");
    private final By drawerBrandingFooter = By.xpath("//*[contains(@text, 'Powered by VG Cares Global') or contains(@content-desc, 'Powered by VG Cares Global') or contains(@text, 'VG Cares Global') or contains(@content-desc, 'VG Cares Global')]");
    private final By drawerOverlayBackdrop = By.xpath("//*[contains(@resource-id, 'navigation_bar_view') or contains(@class, 'Scrim') or contains(@resource-id, 'touch_outside')]");
    private final By drawerProfileHeader = By.xpath("//*[contains(@resource-id, 'profile') or contains(@content-desc, 'Profile') or contains(@text, 'Dr.') or contains(@content-desc, 'Dr.') or contains(@text, 'Prateek') or contains(@content-desc, 'Prateek')]");
    private final By doctorRoleBadge = By.xpath("//*[contains(@text, 'Doctor') or contains(@content-desc, 'Doctor') or contains(@text, 'Dentistry') or contains(@content-desc, 'Dentistry')]");
    private final By logoutButton = By.xpath("//*[contains(@text, 'Logout') or contains(@content-desc, 'Logout') or contains(@text, 'Sign Out') or contains(@content-desc, 'Sign Out')]");
    private final By logoutConfirmButton = By.xpath("//android.widget.Button[contains(@text, 'Yes') or contains(@content-desc, 'Yes') or contains(@text, 'Logout') or contains(@content-desc, 'Logout')]");

    // Header
    private final By userNameHeading = By.xpath(
        "//*[contains(@content-desc, 'Hello') or contains(@text, 'Hello')" +
        " or contains(@content-desc, 'Hi') or contains(@text, 'Hi')" +
        " or contains(@content-desc, 'Gautam') or contains(@text, 'Gautam')" +
        " or contains(@content-desc, 'Prateek') or contains(@text, 'Prateek')" +
        " or contains(@resource-id, 'user_name')]");
    private final By userAvatar = By.xpath(
        "(//android.widget.ImageView)[1]" +
        " | //*[contains(@resource-id, 'avatar') or contains(@content-desc, 'profile') or contains(@content-desc, 'avatar')]");
    private final By notificationBell = By.xpath(
        "//*[contains(@content-desc, 'notification') or contains(@content-desc, 'bell')" +
        " or contains(@resource-id, 'notification') or contains(@resource-id, 'bell')]");

    // Patient Search
    private final By patientSearchField = By.xpath(
        "(//android.widget.EditText)[1]" +
        " | //*[contains(@content-desc, 'Search by patient') or contains(@text, 'Search by patient')" +
        " or contains(@content-desc, 'Search') or contains(@text, 'Search')]");
    private final By searchCancelButton = By.xpath("//*[contains(@text, 'Cancel') or contains(@content-desc, 'Cancel')]");
    private final By patientSearchResult = By.xpath("//*[contains(@text, 'Tony Deshpande') or contains(@content-desc, 'Tony Deshpande')]");

    // Today's Snapshot
    private final By snapshotSection = By.xpath("//*[contains(@text, \"Today's Snapshot\") or contains(@content-desc, \"Today's Snapshot\")]");
    private final By appointmentsCard = By.xpath("//*[contains(@text, 'Appointments') or contains(@content-desc, 'Appointments')]");
    private final By availableSlotsCard = By.xpath("//*[contains(@text, 'Available Slots') or contains(@content-desc, 'Available Slots')]");

    // Unified Timeline
    private final By unifiedTimelineSection = By.xpath("//*[contains(@text, 'Unified Timeline') or contains(@content-desc, 'Unified Timeline')]");
    private final By timelineViewAll = By.xpath("//*[contains(@text, 'View All') or contains(@content-desc, 'View All')]");

    // Hospital Selector Chips
    private final By allHospitalsChip = By.xpath("//*[contains(@text, 'All Hospitals') or contains(@content-desc, 'All Hospitals')]");
    private final By mgmHospitalChip = By.xpath("//*[contains(@text, 'MGM Hospital') or contains(@content-desc, 'MGM Hospital')]");
    private final By jehangirHospitalChip = By.xpath("//*[contains(@text, 'Jehangir') or contains(@content-desc, 'Jehangir')]");

    // Bottom Navigation Bar
    private final By navHome = By.xpath("//*[contains(@content-desc, 'Home') and (contains(@class, 'Button') or contains(@class, 'View'))]");
    private final By navAvailability = By.xpath("//*[contains(@content-desc, 'Availability')]");
    private final By navAppointments = By.xpath("//*[contains(@content-desc, 'Appointments')]");
    private final By navSlots = By.xpath("//*[contains(@content-desc, 'Slots')]");
    private final By navMessages = By.xpath("//*[contains(@content-desc, 'Messages')]");

    public HomePage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isHomeScreenLoaded() {
        return isElementDisplayed(snapshotSection, 15) || isElementDisplayed(navHome, 10);
    }

    public boolean isUserGreetingDisplayed() {
        return isElementDisplayed(userNameHeading, 8);
    }

    public boolean isUserAvatarDisplayed() {
        return isElementDisplayed(userAvatar, 8);
    }

    public boolean isNotificationBellDisplayed() {
        return isElementDisplayed(notificationBell, 8);
    }

    public NotificationsPage openNotifications() {
        log.info("Tapping Notification Bell in Header");
        click(notificationBell);
        return new NotificationsPage(driver);
    }

    public boolean isPatientSearchFieldDisplayed() {
        return isElementDisplayed(patientSearchField, 10);
    }

    public HomePage enterPatientSearch(String patientQuery) {
        log.info("Searching for patient: {}", patientQuery);
        clearAndSendKeys(patientSearchField, patientQuery);
        return this;
    }

    public boolean isPatientResultDisplayed(String patientName) {
        By dynamicResult = By.xpath("//*[contains(@text, '" + patientName + "') or contains(@content-desc, '" + patientName + "')]");
        return isElementDisplayed(dynamicResult, 10);
    }

    public void selectPatientResult(String patientName) {
        log.info("Selecting patient from search results: {}", patientName);
        By dynamicResult = By.xpath("//*[contains(@text, '" + patientName + "') or contains(@content-desc, '" + patientName + "')]");
        click(dynamicResult);
    }

    public void cancelSearch() {
        log.info("Canceling patient search");
        if (isElementDisplayed(searchCancelButton, 4)) {
            click(searchCancelButton);
        }
    }

    public boolean isTodaySnapshotDisplayed() {
        return isElementDisplayed(snapshotSection, 10);
    }

    public boolean isAppointmentsCardDisplayed() {
        return isElementDisplayed(appointmentsCard, 8);
    }

    public boolean isAvailableSlotsCardDisplayed() {
        return isElementDisplayed(availableSlotsCard, 8);
    }

    public boolean isUnifiedTimelineDisplayed() {
        return isElementDisplayed(unifiedTimelineSection, 8);
    }

    public void clickViewAllTimeline() {
        log.info("Clicking View All on Unified Timeline");
        click(timelineViewAll);
    }

    public void selectHospital(String hospitalName) {
        log.info("Selecting Hospital: {}", hospitalName);
        By hospitalChip = By.xpath("//*[contains(@text, '" + hospitalName + "') or contains(@content-desc, '" + hospitalName + "')]");
        click(hospitalChip);
    }

    public void tapNavigationTab(String tabName) {
        log.info("Tapping bottom navigation tab: {}", tabName);
        switch (tabName.toLowerCase()) {
            case "home": click(navHome); break;
            case "availability": click(navAvailability); break;
            case "appointments": click(navAppointments); break;
            case "slots": click(navSlots); break;
            case "messages": click(navMessages); break;
            default: throw new IllegalArgumentException("Unknown navigation tab: " + tabName);
        }
    }

    public boolean isBottomNavigationDisplayed() {
        return isElementDisplayed(navHome, 8);
    }

    public boolean openSideDrawer() {
        log.info("Opening side navigation drawer");
        if (isElementDisplayed(sideMenuButton, 4)) {
            click(sideMenuButton);
            return true;
        }
        if (isElementDisplayed(userAvatar, 4)) {
            click(userAvatar);
            return true;
        }
        return false;
    }

    public boolean isSideDrawerOpen() {
        return isElementDisplayed(sideDrawerHeader, 6) || isElementDisplayed(logoutButton, 6);
    }

    public boolean isMyProfileMenuItemDisplayed() {
        if (!isSideDrawerOpen()) {
            openSideDrawer();
        }
        return isElementDisplayed(myProfileMenuItem, 6);
    }

    public ProfilePage openMyProfile() {
        log.info("Opening My Profile from Side Navigation Drawer");
        if (!isSideDrawerOpen()) {
            openSideDrawer();
        }
        click(myProfileMenuItem);
        return new ProfilePage(driver);
    }

    public boolean isHospitalAffiliationsMenuItemDisplayed() {
        if (!isSideDrawerOpen()) {
            openSideDrawer();
        }
        return isElementDisplayed(hospitalAffiliationsMenuItem, 6);
    }

    public HospitalAffiliationsPage openHospitalAffiliations() {
        log.info("Opening Hospital Affiliations from Side Navigation Drawer");
        if (!isSideDrawerOpen()) {
            openSideDrawer();
        }
        click(hospitalAffiliationsMenuItem);
        return new HospitalAffiliationsPage(driver);
    }

    public boolean isSurgeryRequestsMenuItemDisplayed() {
        if (!isSideDrawerOpen()) {
            openSideDrawer();
        }
        return isElementDisplayed(surgeryRequestsMenuItem, 6);
    }

    public SurgeryRequestsPage openSurgeryRequests() {
        log.info("Opening Surgery Requests from Side Navigation Drawer");
        if (!isSideDrawerOpen()) {
            openSideDrawer();
        }
        click(surgeryRequestsMenuItem);
        return new SurgeryRequestsPage(driver);
    }

    public boolean isConsultationFeesMenuItemDisplayed() {
        if (!isSideDrawerOpen()) {
            openSideDrawer();
        }
        return isElementDisplayed(consultationFeesMenuItem, 6);
    }

    public ConsultationFeesPage openConsultationFees() {
        log.info("Opening Consultation Fees from Side Navigation Drawer");
        if (!isSideDrawerOpen()) {
            openSideDrawer();
        }
        click(consultationFeesMenuItem);
        return new ConsultationFeesPage(driver);
    }

    public boolean isReviewsMenuItemDisplayed() {
        if (!isSideDrawerOpen()) {
            openSideDrawer();
        }
        return isElementDisplayed(reviewsMenuItem, 6);
    }

    public ReviewsPage openReviews() {
        log.info("Opening My Reviews from Side Navigation Drawer");
        if (!isSideDrawerOpen()) {
            openSideDrawer();
        }
        click(reviewsMenuItem);
        return new ReviewsPage(driver);
    }

    public boolean isDocumentsMenuItemDisplayed() {
        if (!isSideDrawerOpen()) {
            openSideDrawer();
        }
        return isElementDisplayed(documentsMenuItem, 6);
    }

    public DocumentsPage openDocuments() {
        log.info("Opening My Documents from Side Navigation Drawer");
        if (!isSideDrawerOpen()) {
            openSideDrawer();
        }
        click(documentsMenuItem);
        return new DocumentsPage(driver);
    }

    public boolean isSettingsMenuItemDisplayed() {
        if (!isSideDrawerOpen()) {
            openSideDrawer();
        }
        return isElementDisplayed(settingsMenuItem, 6);
    }

    public SettingsPage openSettings() {
        log.info("Opening Settings from Side Navigation Drawer");
        if (!isSideDrawerOpen()) {
            openSideDrawer();
        }
        click(settingsMenuItem);
        return new SettingsPage(driver);
    }

    public boolean isDrawerBrandingFooterDisplayed() {
        return isElementDisplayed(drawerBrandingFooter, 6);
    }

    public String getDrawerBrandingFooterText() {
        if (isDrawerBrandingFooterDisplayed()) {
            return getText(drawerBrandingFooter);
        }
        return "";
    }

    public void clickDrawerProfileHeader() {
        log.info("Clicking Profile Header inside drawer");
        click(drawerProfileHeader);
    }

    public boolean isDrawerOverlayDisplayed() {
        return isElementDisplayed(drawerOverlayBackdrop, 4);
    }

    public void clickDrawerOverlay() {
        log.info("Tapping drawer backdrop overlay to dismiss");
        if (isDrawerOverlayDisplayed()) {
            click(drawerOverlayBackdrop);
        }
    }

    public void closeSideDrawer() {
        log.info("Closing Side Navigation Drawer");
        if (isSideDrawerOpen()) {
            try {
                driver.navigate().back();
            } catch (Exception e) {
                clickDrawerOverlay();
            }
        }
    }

    public boolean isDrawerMenuItemDisplayed(String itemName) {
        By menuItem = By.xpath("//*[contains(@text, '" + itemName + "') or contains(@content-desc, '" + itemName + "')]");
        return isElementDisplayed(menuItem, 6);
    }

    public void clickDrawerMenuItem(String itemName) {
        log.info("Clicking drawer menu item: {}", itemName);
        By menuItem = By.xpath("//*[contains(@text, '" + itemName + "') or contains(@content-desc, '" + itemName + "')]");
        click(menuItem);
    }

    public boolean isDoctorRoleDisplayed() {
        return isElementDisplayed(doctorRoleBadge, 6);
    }

    public String getUserGreetingText() {
        if (isUserGreetingDisplayed()) {
            return getText(userNameHeading);
        }
        return "";
    }

    public LoginPage logout() {
        log.info("Performing Doctor Logout");
        if (!isSideDrawerOpen()) {
            openSideDrawer();
        }
        if (isElementDisplayed(logoutButton, 6)) {
            click(logoutButton);
            if (isElementDisplayed(logoutConfirmButton, 4)) {
                click(logoutConfirmButton);
            }
        }
        return new LoginPage(driver);
    }
}
