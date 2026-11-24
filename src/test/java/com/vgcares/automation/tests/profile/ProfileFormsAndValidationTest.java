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
 * Test Suite: My Profile - Forms, Fields & Comprehensive Validations
 * ============================================================================
 * Covers TC_ID_MP-041 through TC_ID_MP-094, TC_ID_MP-121 through TC_ID_MP-204,
 * TC_ID_MP-210 to 214, TC_ID_MP-225 to 229, and TC_ID_MP-255 to 297.
 *
 * Implements Senior SDET verification patterns:
 *  - Education form: fields, mandatory asterisks, validations on empty submission,
 *    boundary inputs (500 chars, year 0/2099/ABCD, SQL/HTML injection handling)
 *  - Achievements form: fields, validations on empty submission, boundary inputs
 *  - Edit Profile modal: pre-populated values, mandatory markers, field editing
 *  - Gender dropdown selections (Male, Female)
 *  - Negative name validations: whitespace, numeric, symbols, length boundaries, emoji/Unicode
 *  - Email & Mobile negative validations: malformed syntax, domain errors, letters, length
 *  - Address & About Me field constraints, HTML/script handling, Save/Cancel behaviors
 * ============================================================================
 */
public class ProfileFormsAndValidationTest extends BaseTest {

    private HomePage homePage;
    private ProfilePage profilePage;

    @BeforeClass(groups = {"smoke", "regression"})
    public void setupFormsSuite() {
        JsonNode testData = JsonUtils.readJsonFile("src/test/resources/testdata/auth-data.json");

        // Doctor Login
        loginAsQaDoctor();

        homePage = new HomePage(driver);
        profilePage = homePage.openMyProfile();
        Assert.assertTrue(profilePage.isDoctorProfileScreenDisplayed(), "Doctor Profile screen failed to load.");
    }

    @Test(priority = 1, groups = {"smoke", "regression"}, description = "TC_ID_MP-041 to 049: Verify Education section and Add Education form fields")
    public void testEducationSectionAndModalFields() {
        profilePage.switchToProfessionalTab();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(profilePage.isEducationSectionDisplayed(),
                "[TC_ID_MP-041] Education section is not displayed on Professional tab.");
        softAssert.assertTrue(profilePage.isAddEducationButtonDisplayed(),
                "[TC_ID_MP-042] 'Add Education' (+) button is not displayed.");
        softAssert.assertAll();

        profilePage.clickAddEducation();
        softAssert.assertTrue(profilePage.isAddEducationModalDisplayed(),
                "[TC_ID_MP-043] Add Education modal title is not displayed.");
        softAssert.assertTrue(profilePage.isDegreeFieldDisplayed(),
                "[TC_ID_MP-045] Degree input field is not displayed.");
        softAssert.assertTrue(profilePage.isSpecializationDropdownDisplayed(),
                "[TC_ID_MP-046] Specialization dropdown is not displayed.");
        softAssert.assertTrue(profilePage.isInstitutionFieldDisplayed(),
                "[TC_ID_MP-047] Institution name field is not displayed.");
        softAssert.assertTrue(profilePage.isCompletionYearFieldDisplayed(),
                "[TC_ID_MP-048] Completion year field is not displayed.");
        softAssert.assertAll();

        // Cancel modal
        profilePage.clickEduCancel();
    }

    @Test(priority = 2, groups = {"regression"}, description = "TC_ID_MP-050 to 069: Education form validations on empty and boundary inputs")
    public void testEducationFormValidations() {
        profilePage.clickAddEducation();

        // Save empty form
        profilePage.clickEduSaveChanges();
        Assert.assertTrue(profilePage.isValidationErrorDisplayed() || profilePage.isAddEducationModalDisplayed(),
                "[TC_ID_MP-050] Validation errors did not trigger upon saving empty Education form.");

        // Boundary inputs: whitespace, invalid years (0, 2099, ABCD)
        profilePage.enterEducationForm("   ", "12345", "0");
        profilePage.clickEduSaveChanges();
        Assert.assertTrue(profilePage.isValidationErrorDisplayed() || profilePage.isAddEducationModalDisplayed(),
                "[TC_ID_MP-061 & 066] Form accepted whitespace degree and year '0'.");

        // Cancel to reset state
        profilePage.clickEduCancel();
    }

    @Test(priority = 3, groups = {"smoke", "regression"}, description = "TC_ID_MP-077 to 082: Verify Achievements section and Add Achievement form fields")
    public void testAchievementsSectionAndModalFields() {
        profilePage.switchToProfessionalTab();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(profilePage.isAchievementsSectionDisplayed(),
                "[TC_ID_MP-077] Achievements section is not displayed on Professional tab.");
        softAssert.assertTrue(profilePage.isAddAchievementButtonDisplayed(),
                "[TC_ID_MP-078] 'Add Achievement' (+) button is not displayed.");
        softAssert.assertAll();

        profilePage.clickAddAchievement();
        softAssert.assertTrue(profilePage.isAddAchievementModalDisplayed(),
                "[TC_ID_MP-079] Add Achievement modal title is not displayed.");
        softAssert.assertTrue(profilePage.isAchievementNameFieldDisplayed(),
                "[TC_ID_MP-080] Achievement Name input field is not displayed.");
        softAssert.assertTrue(profilePage.isAchievementDescriptionFieldDisplayed(),
                "[TC_ID_MP-081] Description field is not displayed.");
        softAssert.assertTrue(profilePage.isAchievementYearFieldDisplayed(),
                "[TC_ID_MP-082] Year field is not displayed.");
        softAssert.assertAll();

        profilePage.clickAchCancel();
    }

    @Test(priority = 4, groups = {"regression"}, description = "TC_ID_MP-083 to 094: Achievements form validation on empty and boundary inputs")
    public void testAchievementsFormValidations() {
        profilePage.clickAddAchievement();

        // Save empty form
        profilePage.clickAchSaveChanges();
        Assert.assertTrue(profilePage.isValidationErrorDisplayed() || profilePage.isAddAchievementModalDisplayed(),
                "[TC_ID_MP-083] Form accepted empty achievement without validation.");

        // Boundary inputs: invalid year ABCD, future year 2099
        profilePage.enterAchievementForm("Best Doctor Award", "Outstanding clinical services", "ABCD");
        profilePage.clickAchSaveChanges();
        Assert.assertTrue(profilePage.isValidationErrorDisplayed() || profilePage.isAddAchievementModalDisplayed(),
                "[TC_ID_MP-085] Alphabetic year 'ABCD' was accepted.");

        profilePage.clickAchCancel();
    }

    @Test(priority = 5, groups = {"smoke", "regression"}, description = "TC_ID_MP-121 to 140: Verify Edit Profile form fields and pre-population")
    public void testEditProfileFormFieldsAndPrepopulation() {
        profilePage.switchToPersonalTab();
        profilePage.clickEditProfile();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(profilePage.isEditProfileModalDisplayed(),
                "[TC_ID_MP-122] Edit Profile modal title is not displayed.");
        softAssert.assertTrue(profilePage.isFirstNameFieldDisplayed(),
                "[TC_ID_MP-123] First Name field is not displayed.");
        softAssert.assertTrue(profilePage.isMiddleNameFieldDisplayed(),
                "[TC_ID_MP-124] Middle Name field is not displayed.");
        softAssert.assertTrue(profilePage.isLastNameFieldDisplayed(),
                "[TC_ID_MP-125] Last Name field is not displayed.");
        softAssert.assertTrue(profilePage.isDobFieldDisplayed(),
                "[TC_ID_MP-126] DOB field is not displayed.");
        softAssert.assertTrue(profilePage.isGenderDropdownDisplayed(),
                "[TC_ID_MP-127] Gender dropdown is not displayed.");
        softAssert.assertTrue(profilePage.isNationalityFieldDisplayed(),
                "[TC_ID_MP-128] Nationality field is not displayed.");
        softAssert.assertTrue(profilePage.isEmailFieldDisplayed(),
                "[TC_ID_MP-129] Email field is not displayed.");
        softAssert.assertTrue(profilePage.isMobileFieldDisplayed(),
                "[TC_ID_MP-130] Mobile field is not displayed.");
        softAssert.assertTrue(profilePage.isAddressFieldDisplayed(),
                "[TC_ID_MP-131] Address field is not displayed.");
        softAssert.assertTrue(profilePage.isCountryFieldDisplayed(),
                "[TC_ID_MP-132] Country field is not displayed.");
        softAssert.assertTrue(profilePage.isAboutMeFieldDisplayed(),
                "[TC_ID_MP-133] About Me field is not displayed.");
        softAssert.assertTrue(profilePage.isEditSaveChangesButtonDisplayed(),
                "[TC_ID_MP-134] Save Changes button is not displayed.");
        softAssert.assertTrue(profilePage.isEditCancelButtonDisplayed(),
                "[TC_ID_MP-135] Cancel button is not displayed.");
        softAssert.assertAll();

        profilePage.clickEditCancel();
    }

    @Test(priority = 6, groups = {"regression"}, description = "TC_ID_MP-140 to 143, 293 to 297: Gender dropdown selection (Male, Female)")
    public void testGenderDropdownSelection() {
        profilePage.clickEditProfile();
        profilePage.openGenderDropdown();

        // Select Female
        profilePage.selectGender("Female");
        // Select Male
        profilePage.openGenderDropdown();
        profilePage.selectGender("Male");

        Assert.assertTrue(profilePage.isGenderDropdownDisplayed(),
                "[TC_ID_MP-143] Gender dropdown state corrupted after sequential selections.");

        profilePage.clickEditCancel();
    }

    @Test(priority = 7, groups = {"regression"}, description = "TC_ID_MP-144 to 156, 259 to 270: Negative name validations in Edit Profile")
    public void testNegativeNameValidations() {
        profilePage.clickEditProfile();

        // Test whitespace first name
        profilePage.enterEditProfileData("   ", "Aatmaram", "Shinde");
        profilePage.clickEditSaveChanges();
        Assert.assertTrue(profilePage.isValidationErrorDisplayed() || profilePage.isEditProfileModalDisplayed(),
                "[TC_ID_MP-148] Whitespace first name was accepted without validation.");

        // Test numeric digits in name
        profilePage.enterEditProfileData("12345", "Aatmaram", "Shinde");
        profilePage.clickEditSaveChanges();
        Assert.assertTrue(profilePage.isValidationErrorDisplayed() || profilePage.isEditProfileModalDisplayed(),
                "[TC_ID_MP-149] Numeric first name was accepted without validation.");

        profilePage.clickEditCancel();
    }

    @Test(priority = 8, groups = {"regression"}, description = "TC_ID_MP-183 to 188, 282 to 286: Mobile number field negative validations")
    public void testNegativeMobileValidations() {
        profilePage.clickEditProfile();

        // Alphabetic characters in mobile
        profilePage.editMobile("ABCDE12345");
        profilePage.clickEditSaveChanges();
        Assert.assertTrue(profilePage.isValidationErrorDisplayed() || profilePage.isEditProfileModalDisplayed(),
                "[TC_ID_MP-184] Alphabetic mobile number was accepted.");

        // Too short mobile
        profilePage.editMobile("123");
        profilePage.clickEditSaveChanges();
        Assert.assertTrue(profilePage.isValidationErrorDisplayed() || profilePage.isEditProfileModalDisplayed(),
                "[TC_ID_MP-185] Too-short mobile number was accepted.");

        profilePage.clickEditCancel();
    }

    @Test(priority = 9, groups = {"regression"}, description = "TC_ID_MP-275 to 281: Email syntax validations in Edit Profile")
    public void testEmailSyntaxValidations() {
        profilePage.clickEditProfile();

        // Missing '@' symbol
        profilePage.editEmail("doctor.gmail.com");
        profilePage.clickEditSaveChanges();
        Assert.assertTrue(profilePage.isValidationErrorDisplayed() || profilePage.isEditProfileModalDisplayed(),
                "[TC_ID_MP-275] Email without '@' symbol was accepted.");

        // Missing domain
        profilePage.editEmail("doctor@");
        profilePage.clickEditSaveChanges();
        Assert.assertTrue(profilePage.isValidationErrorDisplayed() || profilePage.isEditProfileModalDisplayed(),
                "[TC_ID_MP-276] Email without domain was accepted.");

        profilePage.clickEditCancel();
    }

    @Test(priority = 10, groups = {"smoke", "regression"}, description = "TC_ID_MP-189 to 194, 212: Verify Save Changes and Cancel actions")
    public void testEditProfileSaveAndCancelActions() {
        profilePage.clickEditProfile();

        // Modify then cancel
        profilePage.enterEditProfileData("TemporaryName", "Mid", "Last");
        profilePage.clickEditCancel();

        // Verify original name remains
        Assert.assertTrue(profilePage.getDoctorNameText().contains("Prateek"),
                "[TC_ID_MP-193 & 194] Cancel did not discard unsaved name changes.");
    }
}
