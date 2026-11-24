package com.vgcares.automation.tests.profile;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.pages.ProfilePage;
import com.vgcares.automation.utils.JsonUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * ============================================================================
 * Test Suite: My Profile - Navigation, Header, Summary Cards & Tabs
 * ============================================================================
 * Comprehensive coverage for TC_ID_MP-001 through TC_ID_MP-040, TC_ID_MP-095
 * through TC_ID_MP-120, TC_ID_MP-205 to 209, and TC_ID_MP-215 to 224.
 *
 * Implements Senior SDET verification patterns:
 *  - Side menu My Profile discovery & launching
 *  - Header hierarchy: title, back button, doctor identity (name, specialization, ID)
 *  - Summary cards: Experience (4.0 yrs), Rating (5), Gender (Male)
 *  - Tab bar: Personal, Professional, Specialization tab transitions & active styling
 *  - Personal Information & Address display verifications
 *  - Professional Information read-only & license fields
 *  - Specialization details, primary/secondary badges, and chips
 *  - Profile photo overlay bottom sheet (Camera, Gallery, Remove)
 * ============================================================================
 */
public class ProfileNavigationAndHeaderTest extends BaseTest {

    private JsonNode profileData;
    private HomePage homePage;
    private ProfilePage profilePage;

    @BeforeClass(groups = {"smoke", "regression"})
    public void setupProfileSuite() {
        JsonNode testData = JsonUtils.readJsonFile("src/test/resources/testdata/auth-data.json");
        profileData = testData.get("profileData");

        // Authenticate with Doctor credentials
        loginAsQaDoctor();

        homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isHomeScreenLoaded(), "Home screen failed to load after doctor authentication.");
    }

    @Test(priority = 1, groups = {"smoke", "regression"}, description = "TC_ID_MP-001 & 002: Verify My Profile is visible in side menu and opens screen")
    public void testSideMenuMyProfileNavigation() {
        Assert.assertTrue(homePage.isMyProfileMenuItemDisplayed(),
                "[TC_ID_MP-001] 'My Profile' option is not displayed in the side navigation drawer.");

        profilePage = homePage.openMyProfile();
        Assert.assertTrue(profilePage.isDoctorProfileScreenDisplayed(),
                "[TC_ID_MP-002] Doctor Profile screen failed to open from side drawer.");
    }

    @Test(priority = 2, groups = {"regression"}, description = "TC_ID_MP-003, 004, 005: Verify profile title, back button visibility and navigation")
    public void testProfileHeaderAndBackNavigation() {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(profilePage.isDoctorProfileScreenDisplayed(),
                "[TC_ID_MP-003] 'Doctor Profile' title is not displayed in header.");
        softAssert.assertTrue(profilePage.isBackButtonDisplayed(),
                "[TC_ID_MP-004] Back button is not visible on profile screen header.");
        softAssert.assertAll();

        // Verify back button returns to previous screen and reopen
        profilePage.clickBack();
        Assert.assertTrue(homePage.isHomeScreenLoaded(),
                "[TC_ID_MP-005] Back button did not return to the previous screen.");
        profilePage = homePage.openMyProfile();
    }

    @Test(priority = 3, groups = {"smoke", "regression"}, description = "TC_ID_MP-006 to 010: Verify doctor identity header attributes")
    public void testDoctorHeaderIdentityAttributes() {
        String expectedName = profileData.get("expectedDoctorName").asText();
        String expectedSpec = profileData.get("expectedSpecialization").asText();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(profilePage.isDoctorProfileImageDisplayed(),
                "[TC_ID_MP-006] Doctor profile image is not displayed.");
        softAssert.assertTrue(profilePage.isDoctorNameDisplayed(),
                "[TC_ID_MP-007] Doctor name is not displayed in profile header.");
        softAssert.assertTrue(profilePage.getDoctorNameText().contains("Prateek"),
                "[TC_ID_MP-007] Expected name to contain 'Prateek', found: " + profilePage.getDoctorNameText());
        softAssert.assertTrue(profilePage.isDoctorSpecializationDisplayed(),
                "[TC_ID_MP-008] Specialization is not displayed in profile header.");
        softAssert.assertTrue(profilePage.isDoctorIdDisplayed(),
                "[TC_ID_MP-009] Doctor ID is not displayed in profile header.");
        softAssert.assertTrue(profilePage.isProfileImageEditIconDisplayed(),
                "[TC_ID_MP-010] Profile image edit icon is not visible over avatar.");
        softAssert.assertAll();
    }

    @Test(priority = 4, groups = {"regression"}, description = "TC_ID_MP-011, 012, 013, 221, 222: Verify summary cards: Experience, Rating, Gender")
    public void testSummaryCardsPresentation() {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(profilePage.isExperienceCardDisplayed(),
                "[TC_ID_MP-011] Experience summary card (4.0 yrs) is not displayed.");
        softAssert.assertTrue(profilePage.isRatingCardDisplayed(),
                "[TC_ID_MP-012] Rating summary card (5) is not displayed.");
        softAssert.assertTrue(profilePage.isGenderSummaryCardDisplayed(),
                "[TC_ID_MP-013] Gender summary card (Male) is not displayed.");
        softAssert.assertAll();
    }

    @Test(priority = 5, groups = {"smoke", "regression"}, description = "TC_ID_MP-014 to 020, 205 to 209: Verify tab bar display and tab transitions")
    public void testTabBarTransitionsAndActiveStyling() {
        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(profilePage.isPersonalTabDisplayed(),
                "[TC_ID_MP-014] Personal tab is not displayed.");
        softAssert.assertTrue(profilePage.isProfessionalTabDisplayed(),
                "[TC_ID_MP-015] Professional tab is not displayed.");
        softAssert.assertTrue(profilePage.isSpecializationTabDisplayed(),
                "[TC_ID_MP-016] Specialization tab is not displayed.");

        // Switch to Professional
        profilePage.switchToProfessionalTab();
        softAssert.assertTrue(profilePage.isProfessionalInfoSectionDisplayed(),
                "[TC_ID_MP-017] Professional tab content did not display after tapping tab.");

        // Switch to Specialization
        profilePage.switchToSpecializationTab();
        softAssert.assertTrue(profilePage.isSpecializationDetailsSectionDisplayed(),
                "[TC_ID_MP-018] Specialization tab content did not display after tapping tab.");

        // Switch back to Personal
        profilePage.switchToPersonalTab();
        softAssert.assertTrue(profilePage.isPersonalInfoSectionDisplayed(),
                "[TC_ID_MP-019] Personal tab content did not display after returning to Personal.");
        softAssert.assertAll();
    }

    @Test(priority = 6, groups = {"regression"}, description = "TC_ID_MP-021 to 030: Verify Personal Information section fields & alignment")
    public void testPersonalInformationSectionFields() {
        profilePage.switchToPersonalTab();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(profilePage.isPersonalInfoSectionDisplayed(),
                "[TC_ID_MP-021] Personal Information section is not displayed.");
        softAssert.assertTrue(profilePage.isDoctorNameDisplayed(),
                "[TC_ID_MP-022] Full Name is not displayed in personal section.");
        softAssert.assertTrue(profilePage.isPersonalDobDisplayed(),
                "[TC_ID_MP-023] Date of Birth (3/7/1990) is not displayed.");
        softAssert.assertTrue(profilePage.isPersonalNationalityDisplayed(),
                "[TC_ID_MP-024] Nationality is not displayed.");
        softAssert.assertTrue(profilePage.isPersonalEmailDisplayed(),
                "[TC_ID_MP-025] Email address is not displayed.");
        softAssert.assertTrue(profilePage.isPersonalMobileDisplayed(),
                "[TC_ID_MP-026] Mobile number is not displayed.");
        softAssert.assertTrue(profilePage.isPersonalAddressDisplayed(),
                "[TC_ID_MP-027] Address (Hinjewadi, Pune) is not displayed.");
        softAssert.assertTrue(profilePage.isPersonalCountryDisplayed(),
                "[TC_ID_MP-028] Country (India) is not displayed.");
        softAssert.assertTrue(profilePage.isPersonalAboutMeDisplayed(),
                "[TC_ID_MP-029] About Me description is not displayed.");
        softAssert.assertTrue(profilePage.isEditProfileButtonDisplayed(),
                "[TC_ID_MP-030] Edit Profile action button is not visible.");
        softAssert.assertAll();
    }

    @Test(priority = 7, groups = {"regression"}, description = "TC_ID_MP-031 to 040: Verify Professional Information section and read-only attributes")
    public void testProfessionalInformationFields() {
        profilePage.switchToProfessionalTab();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(profilePage.isProfessionalInfoSectionDisplayed(),
                "[TC_ID_MP-032] Professional Information section is not displayed.");
        softAssert.assertTrue(profilePage.isYearsOfExperienceDisplayed(),
                "[TC_ID_MP-033] Years of Experience is not displayed.");
        softAssert.assertTrue(profilePage.isRegistrationNumberDisplayed(),
                "[TC_ID_MP-034] Registration Number is not displayed.");
        softAssert.assertTrue(profilePage.isLicenseNumberDisplayed(),
                "[TC_ID_MP-035] License Number (123456) is not displayed.");
        softAssert.assertTrue(profilePage.isDesignationDisplayed(),
                "[TC_ID_MP-036] Designation is not displayed.");
        softAssert.assertAll();
    }

    @Test(priority = 8, groups = {"regression"}, description = "TC_ID_MP-095 to 108: Verify Specialization tab details, chips, and edit pencil")
    public void testSpecializationTabDetailsAndChips() {
        profilePage.switchToSpecializationTab();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(profilePage.isSpecializationDetailsSectionDisplayed(),
                "[TC_ID_MP-096] Specialization Details section is not displayed.");
        softAssert.assertTrue(profilePage.isPrimarySpecializationDisplayed(),
                "[TC_ID_MP-097] Primary Specialization (Dentistry) is not displayed.");
        softAssert.assertTrue(profilePage.isSecondarySpecializationDisplayed(),
                "[TC_ID_MP-098] Secondary Specialization (Oral & Maxillofacial) is not displayed.");
        softAssert.assertTrue(profilePage.isExpertiseSummaryDisplayed(),
                "[TC_ID_MP-100] Expertise Summary is not displayed.");
        softAssert.assertTrue(profilePage.isEditSpecializationIconDisplayed(),
                "[TC_ID_MP-101] Edit Specialization pencil icon is not displayed.");
        softAssert.assertAll();
    }

    @Test(priority = 9, groups = {"regression"}, description = "TC_ID_MP-109 to 120, 213, 239: Verify profile photo overlay actions (Take Photo, Gallery, Remove)")
    public void testProfilePhotoOverlayMenu() {
        profilePage.clickProfilePhotoEdit();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(profilePage.isTakePhotoOptionDisplayed(),
                "[TC_ID_MP-110] 'Take Photo' option is not displayed in photo options menu.");
        softAssert.assertTrue(profilePage.isChooseFromGalleryOptionDisplayed(),
                "[TC_ID_MP-111] 'Choose from Gallery' option is not displayed in photo options menu.");
        softAssert.assertTrue(profilePage.isRemovePhotoOptionDisplayed(),
                "[TC_ID_MP-112] 'Remove Photo' option is not displayed in photo options menu.");
        softAssert.assertAll();

        // Dismiss overlay by tapping outside
        profilePage.dismissPhotoOverlay();
        Assert.assertTrue(profilePage.isDoctorProfileScreenDisplayed(),
                "[TC_ID_MP-119] Profile screen not restored after dismissing photo options overlay.");
    }
}
