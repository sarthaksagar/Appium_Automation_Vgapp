package com.vgcares.automation.pages;

import com.vgcares.automation.utils.GestureUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * ============================================================================
 * Profile Page Object Model (Doctor Profile Module)
 * ============================================================================
 * Senior SDET robust page object encapsulating all UI elements, interactions,
 * and state verifications for:
 *  - Profile Header, Doctor Identity, Summary Cards (Experience, Rating, Gender)
 *  - Primary Tabs (Personal, Professional, Specialization)
 *  - Personal Information & Address Display
 *  - Professional Information, Education & Achievements (View, Add Modal, Validations)
 *  - Specialization Details, Primary/Secondary Chips & Edit Flows
 *  - Profile Photo Bottom Sheet Overlay (Take Photo, Gallery, Remove Photo)
 *  - Edit Profile Form (Mandatory fields, Validation states, Save & Cancel)
 *  - Date of Birth Date Picker Modal (Navigation, Boundaries, Leap Year, OK/Cancel)
 * ============================================================================
 */
public class ProfilePage extends BasePage {

    // ── Header & Navigation ──────────────────────────────────────────────────
    private final By backButton = By.xpath("//*[contains(@content-desc, 'Back') or @content-desc='←' or contains(@class, 'BackButton')]");
    private final By doctorProfileTitle = By.xpath("//*[contains(@text, 'Doctor Profile') or contains(@content-desc, 'Doctor Profile') or contains(@text, 'Profile') or contains(@content-desc, 'Profile')]");
    private final By doctorProfileImage = By.xpath("//*[contains(@resource-id, 'profile_image') or contains(@content-desc, 'Profile image') or contains(@class, 'ImageView')]");
    private final By doctorName = By.xpath("//*[contains(@text, 'Prateek Aatmaram Shinde') or contains(@content-desc, 'Prateek Aatmaram Shinde') or contains(@text, 'Prateek') or contains(@content-desc, 'Prateek')]");
    private final By doctorSpecialization = By.xpath("//*[contains(@text, 'Dentistry') or contains(@content-desc, 'Dentistry')]");
    private final By doctorId = By.xpath("//*[contains(@text, 'ID') or contains(@content-desc, 'ID')]");
    private final By profileImageEditIcon = By.xpath("//*[contains(@content-desc, 'edit') or contains(@resource-id, 'edit_profile_pic') or contains(@content-desc, 'camera')]");

    // ── Summary Cards ────────────────────────────────────────────────────────
    private final By experienceCard = By.xpath("//*[contains(@text, '4.0 yrs') or contains(@content-desc, '4.0 yrs') or contains(@text, 'Experience') or contains(@content-desc, 'Experience')]");
    private final By experienceValue = By.xpath("//*[contains(@text, '4.0') or contains(@content-desc, '4.0') or contains(@text, '4') or contains(@content-desc, '4')]");
    private final By ratingCard = By.xpath("//*[contains(@text, 'Rating') or contains(@content-desc, 'Rating') or contains(@text, '5') or contains(@content-desc, '5')]");
    private final By genderSummaryCard = By.xpath("//*[contains(@text, 'Gender') or contains(@content-desc, 'Gender') or contains(@text, 'Male') or contains(@content-desc, 'Male')]");

    // ── Tabs ─────────────────────────────────────────────────────────────────
    private final By tabPersonal = By.xpath("//*[contains(@text, 'Personal') or contains(@content-desc, 'Personal')]");
    private final By tabProfessional = By.xpath("//*[contains(@text, 'Professional') or contains(@content-desc, 'Professional')]");
    private final By tabSpecialization = By.xpath("//*[contains(@text, 'Specialization') or contains(@content-desc, 'Specialization')]");

    // ── Personal Tab Elements ────────────────────────────────────────────────
    private final By personalInfoSection = By.xpath("//*[contains(@text, 'Personal Information') or contains(@content-desc, 'Personal Information') or contains(@text, 'Personal')]");
    private final By personalFullName = By.xpath("//*[contains(@text, 'Prateek Aatmaram Shinde') or contains(@content-desc, 'Prateek Aatmaram Shinde')]");
    private final By personalDob = By.xpath("//*[contains(@text, '3/7/1990') or contains(@content-desc, '3/7/1990') or contains(@text, '1990') or contains(@content-desc, '1990')]");
    private final By personalNationality = By.xpath("//*[contains(@text, 'Not Provided') or contains(@content-desc, 'Not Provided') or contains(@text, 'India') or contains(@content-desc, 'India')]");
    private final By personalEmail = By.xpath("//*[contains(@text, '@gmail.com') or contains(@content-desc, '@gmail.com') or contains(@text, 'Gmail') or contains(@content-desc, 'Gmail')]");
    private final By personalMobile = By.xpath("//*[contains(@text, '+91') or contains(@content-desc, '+91') or contains(@text, 'Mobile') or contains(@content-desc, 'Mobile')]");
    private final By personalAddress = By.xpath("//*[contains(@text, 'Hinjewadi') or contains(@content-desc, 'Hinjewadi') or contains(@text, 'Pune') or contains(@content-desc, 'Pune')]");
    private final By personalCountry = By.xpath("//*[contains(@text, 'India') or contains(@content-desc, 'India')]");
    private final By personalAboutMe = By.xpath("//*[contains(@text, 'About Me') or contains(@content-desc, 'About Me') or contains(@text, 'Doctor description') or contains(@content-desc, 'Doctor description')]");
    private final By editProfileButton = By.xpath("//*[contains(@content-desc, 'Edit Profile') or contains(@text, 'Edit Profile') or contains(@content-desc, 'Edit') or contains(@text, 'Edit')]");

    // ── Professional Tab Elements ────────────────────────────────────────────
    private final By professionalInfoSection = By.xpath("//*[contains(@text, 'Professional Information') or contains(@content-desc, 'Professional Information')]");
    private final By professionalExperience = By.xpath("//*[contains(@text, 'Years of Experience') or contains(@content-desc, 'Years of Experience')]");
    private final By professionalRegNumber = By.xpath("//*[contains(@text, 'Registration Number') or contains(@content-desc, 'Registration Number')]");
    private final By professionalLicenseNumber = By.xpath("//*[contains(@text, '123456') or contains(@content-desc, '123456') or contains(@text, 'License Number') or contains(@content-desc, 'License Number')]");
    private final By professionalDesignation = By.xpath("//*[contains(@text, 'Designation') or contains(@content-desc, 'Designation')]");

    // ── Education Section ────────────────────────────────────────────────────
    private final By educationSection = By.xpath("//*[contains(@text, 'Education') or contains(@content-desc, 'Education')]");
    private final By addEducationButton = By.xpath("//*[contains(@text, 'Education') or contains(@content-desc, 'Education')]/following::*[contains(@text, '+') or contains(@content-desc, '+') or contains(@content-desc, 'Add')][1]");
    private final By emptyEducationMessage = By.xpath("//*[contains(@text, 'No education') or contains(@content-desc, 'No education')]");

    // Add Education Modal
    private final By addEducationModalTitle = By.xpath("//*[contains(@text, 'Add Education') or contains(@content-desc, 'Add Education')]");
    private final By eduDegreeField = By.xpath("//android.widget.EditText[contains(@hint, 'Degree') or contains(@text, 'Degree') or @resource-id='degree']");
    private final By eduSpecializationDropdown = By.xpath("//*[contains(@hint, 'Specialization') or contains(@text, 'Specialization') or contains(@content-desc, 'Specialization')]");
    private final By eduInstitutionField = By.xpath("//android.widget.EditText[contains(@hint, 'Institution') or contains(@text, 'Institution') or contains(@hint, 'College')]");
    private final By eduCompletionYearField = By.xpath("//android.widget.EditText[contains(@hint, 'Year') or contains(@text, 'Year') or contains(@hint, 'Completion')]");
    private final By eduSaveChangesButton = By.xpath("//*[contains(@text, 'Save Changes') or contains(@content-desc, 'Save Changes') or contains(@text, 'Save')]");
    private final By eduCancelButton = By.xpath("//*[contains(@text, 'Cancel') or contains(@content-desc, 'Cancel')]");

    // ── Achievements Section ─────────────────────────────────────────────────
    private final By achievementsSection = By.xpath("//*[contains(@text, 'Achievements') or contains(@content-desc, 'Achievements')]");
    private final By addAchievementButton = By.xpath("//*[contains(@text, 'Achievements') or contains(@content-desc, 'Achievements')]/following::*[contains(@text, '+') or contains(@content-desc, '+') or contains(@content-desc, 'Add')][1]");
    private final By emptyAchievementsMessage = By.xpath("//*[contains(@text, 'No achievements') or contains(@content-desc, 'No achievements')]");

    // Add Achievement Modal
    private final By addAchievementModalTitle = By.xpath("//*[contains(@text, 'Add Achievement') or contains(@content-desc, 'Add Achievement')]");
    private final By achNameField = By.xpath("//android.widget.EditText[contains(@hint, 'Achievement Name') or contains(@text, 'Achievement Name') or contains(@hint, 'Name')]");
    private final By achDescriptionField = By.xpath("//android.widget.EditText[contains(@hint, 'Description') or contains(@text, 'Description')]");
    private final By achYearField = By.xpath("//android.widget.EditText[contains(@hint, 'Year') or contains(@text, 'Year')]");
    private final By achModalCloseX = By.xpath("//*[contains(@content-desc, 'Close') or contains(@text, '✕') or contains(@content-desc, '✕') or @content-desc='x']");
    private final By achSaveChangesButton = By.xpath("//*[contains(@text, 'Save Changes') or contains(@content-desc, 'Save Changes') or contains(@text, 'Save')]");
    private final By achCancelButton = By.xpath("//*[contains(@text, 'Cancel') or contains(@content-desc, 'Cancel')]");

    // ── Specialization Tab Elements ──────────────────────────────────────────
    private final By specializationDetailsSection = By.xpath("//*[contains(@text, 'Specialization Details') or contains(@content-desc, 'Specialization Details') or contains(@text, 'Specialization')]");
    private final By primarySpecialization = By.xpath("//*[contains(@text, 'Dentistry') or contains(@content-desc, 'Dentistry')]");
    private final By secondarySpecialization = By.xpath("//*[contains(@text, 'Oral & Maxillofacial') or contains(@content-desc, 'Oral & Maxillofacial')]");
    private final By specializationChips = By.xpath("//*[contains(@class, 'Chip') or contains(@content-desc, 'Dentistry') or contains(@content-desc, 'Surgery')]");
    private final By expertiseSummary = By.xpath("//*[contains(@text, 'Expertise') or contains(@content-desc, 'Expertise') or contains(@text, 'Not Provided') or contains(@content-desc, 'Not Provided')]");
    private final By editSpecializationPencil = By.xpath("//*[contains(@content-desc, 'Edit Specialization') or contains(@resource-id, 'edit_specialization') or (contains(@content-desc, 'Edit') and contains(@class, 'Button'))]");

    // ── Profile Photo Options Overlay ────────────────────────────────────────
    private final By takePhotoOption = By.xpath("//*[contains(@text, 'Take Photo') or contains(@content-desc, 'Take Photo')]");
    private final By chooseFromGalleryOption = By.xpath("//*[contains(@text, 'Choose from Gallery') or contains(@content-desc, 'Choose from Gallery') or contains(@text, 'Gallery') or contains(@content-desc, 'Gallery')]");
    private final By removePhotoOption = By.xpath("//*[contains(@text, 'Remove Photo') or contains(@content-desc, 'Remove Photo') or contains(@text, 'Remove') or contains(@content-desc, 'Remove')]");

    // ── Edit Profile Form Elements ───────────────────────────────────────────
    private final By editProfileModalTitle = By.xpath("//*[contains(@text, 'Edit Profile') or contains(@content-desc, 'Edit Profile')]");
    private final By editFirstNameField = By.xpath("(//android.widget.EditText)[1]");
    private final By editMiddleNameField = By.xpath("(//android.widget.EditText)[2]");
    private final By editLastNameField = By.xpath("(//android.widget.EditText)[3]");
    private final By editDobField = By.xpath("//*[contains(@hint, 'DOB') or contains(@hint, 'Date of Birth') or contains(@text, '3/7/1990') or contains(@content-desc, '3/7/1990')]");
    private final By editGenderDropdown = By.xpath("//*[contains(@text, 'Male') or contains(@content-desc, 'Male') or contains(@text, 'Gender') or contains(@content-desc, 'Gender')]");
    private final By editNationalityField = By.xpath("//android.widget.EditText[contains(@hint, 'Nationality') or contains(@text, 'Nationality')]");
    private final By editEmailField = By.xpath("//android.widget.EditText[contains(@hint, 'Email') or contains(@text, '@') or contains(@hint, 'Gmail')]");
    private final By editMobileField = By.xpath("//android.widget.EditText[contains(@hint, 'Mobile') or contains(@hint, 'Phone')]");
    private final By editAddressField = By.xpath("//android.widget.EditText[contains(@hint, 'Address') or contains(@text, 'Hinjewadi') or contains(@content-desc, 'Hinjewadi')]");
    private final By editCountryField = By.xpath("//android.widget.EditText[contains(@hint, 'Country') or contains(@text, 'India')]");
    private final By editAboutMeField = By.xpath("//android.widget.EditText[contains(@hint, 'About Me') or contains(@text, 'About Me')]");
    private final By editSaveChangesButton = By.xpath("//*[contains(@text, 'Save Changes') or contains(@content-desc, 'Save Changes') or contains(@text, 'Save')]");
    private final By editCancelButton = By.xpath("//*[contains(@text, 'Cancel') or contains(@content-desc, 'Cancel')]");
    private final By editModalCloseX = By.xpath("//*[contains(@content-desc, 'Close') or contains(@text, '✕') or contains(@content-desc, '✕') or @content-desc='x']");

    // Gender Dropdown Options
    private final By genderOptionMale = By.xpath("//*[contains(@text, 'Male') or contains(@content-desc, 'Male')]");
    private final By genderOptionFemale = By.xpath("//*[contains(@text, 'Female') or contains(@content-desc, 'Female')]");

    // ── Date Picker Elements ─────────────────────────────────────────────────
    private final By datePickerModal = By.xpath("//*[contains(@class, 'DatePicker') or contains(@resource-id, 'date_picker_header') or contains(@text, 'Select date')]");
    private final By datePickerPrevMonth = By.xpath("//*[contains(@content-desc, 'Previous month') or contains(@content-desc, 'prev') or contains(@resource-id, 'prev')]");
    private final By datePickerNextMonth = By.xpath("//*[contains(@content-desc, 'Next month') or contains(@content-desc, 'next') or contains(@resource-id, 'next')]");
    private final By datePickerOkButton = By.xpath("//*[contains(@text, 'OK') or contains(@content-desc, 'OK') or contains(@text, 'Select')]");
    private final By datePickerCancelButton = By.xpath("//*[contains(@text, 'Cancel') or contains(@content-desc, 'Cancel')]");

    // ── Validation Errors & Generic Badges ───────────────────────────────────
    private final By validationError = By.xpath("//*[contains(@text, 'required') or contains(@content-desc, 'required') or contains(@text, 'Invalid') or contains(@content-desc, 'Invalid')]");

    public ProfilePage(AndroidDriver driver) {
        super(driver);
    }

    // ── Navigation & Header Verification ─────────────────────────────────────
    public boolean isDoctorProfileScreenDisplayed() {
        return isElementDisplayed(doctorProfileTitle, 10) || isElementDisplayed(doctorName, 8);
    }

    public boolean isBackButtonDisplayed() {
        return isElementDisplayed(backButton, 6);
    }

    public void clickBack() {
        log.info("Clicking Profile Back Button");
        click(backButton);
    }

    public boolean isDoctorProfileImageDisplayed() {
        return isElementDisplayed(doctorProfileImage, 6);
    }

    public boolean isDoctorNameDisplayed() {
        return isElementDisplayed(doctorName, 8);
    }

    public String getDoctorNameText() {
        return getText(doctorName);
    }

    public boolean isDoctorSpecializationDisplayed() {
        return isElementDisplayed(doctorSpecialization, 6);
    }

    public String getDoctorSpecializationText() {
        return getText(doctorSpecialization);
    }

    public boolean isDoctorIdDisplayed() {
        return isElementDisplayed(doctorId, 6);
    }

    public boolean isProfileImageEditIconDisplayed() {
        return isElementDisplayed(profileImageEditIcon, 6);
    }

    // ── Summary Cards ────────────────────────────────────────────────────────
    public boolean isExperienceCardDisplayed() {
        return isElementDisplayed(experienceCard, 6) || isElementDisplayed(experienceValue, 6);
    }

    public boolean isRatingCardDisplayed() {
        return isElementDisplayed(ratingCard, 6);
    }

    public boolean isGenderSummaryCardDisplayed() {
        return isElementDisplayed(genderSummaryCard, 6);
    }

    // ── Tabs Switching ───────────────────────────────────────────────────────
    public boolean isPersonalTabDisplayed() {
        return isElementDisplayed(tabPersonal, 6);
    }

    public boolean isProfessionalTabDisplayed() {
        return isElementDisplayed(tabProfessional, 6);
    }

    public boolean isSpecializationTabDisplayed() {
        return isElementDisplayed(tabSpecialization, 6);
    }

    public void switchToPersonalTab() {
        log.info("Switching to 'Personal' tab");
        click(tabPersonal);
    }

    public void switchToProfessionalTab() {
        log.info("Switching to 'Professional' tab");
        click(tabProfessional);
    }

    public void switchToSpecializationTab() {
        log.info("Switching to 'Specialization' tab");
        click(tabSpecialization);
    }

    // ── Personal Info Verifications ──────────────────────────────────────────
    public boolean isPersonalInfoSectionDisplayed() {
        return isElementDisplayed(personalInfoSection, 6);
    }

    public boolean isPersonalDobDisplayed() {
        return isElementDisplayed(personalDob, 6);
    }

    public boolean isPersonalNationalityDisplayed() {
        return isElementDisplayed(personalNationality, 6);
    }

    public boolean isPersonalEmailDisplayed() {
        return isElementDisplayed(personalEmail, 6);
    }

    public boolean isPersonalMobileDisplayed() {
        return isElementDisplayed(personalMobile, 6);
    }

    public boolean isPersonalAddressDisplayed() {
        return isElementDisplayed(personalAddress, 6);
    }

    public boolean isPersonalCountryDisplayed() {
        return isElementDisplayed(personalCountry, 6);
    }

    public boolean isPersonalAboutMeDisplayed() {
        return isElementDisplayed(personalAboutMe, 6);
    }

    public boolean isEditProfileButtonDisplayed() {
        return isElementDisplayed(editProfileButton, 6);
    }

    public void clickEditProfile() {
        log.info("Clicking Edit Profile button");
        click(editProfileButton);
    }

    // ── Professional Info Verifications ──────────────────────────────────────
    public boolean isProfessionalInfoSectionDisplayed() {
        return isElementDisplayed(professionalInfoSection, 6);
    }

    public boolean isYearsOfExperienceDisplayed() {
        return isElementDisplayed(professionalExperience, 6);
    }

    public boolean isRegistrationNumberDisplayed() {
        return isElementDisplayed(professionalRegNumber, 6);
    }

    public boolean isLicenseNumberDisplayed() {
        return isElementDisplayed(professionalLicenseNumber, 6);
    }

    public boolean isDesignationDisplayed() {
        return isElementDisplayed(professionalDesignation, 6);
    }

    // ── Education Section ────────────────────────────────────────────────────
    public boolean isEducationSectionDisplayed() {
        return isElementDisplayed(educationSection, 6);
    }

    public boolean isAddEducationButtonDisplayed() {
        return isElementDisplayed(addEducationButton, 6);
    }

    public void clickAddEducation() {
        log.info("Clicking Add Education (+) button");
        click(addEducationButton);
    }

    public boolean isAddEducationModalDisplayed() {
        return isElementDisplayed(addEducationModalTitle, 6);
    }

    public boolean isDegreeFieldDisplayed() {
        return isElementDisplayed(eduDegreeField, 6);
    }

    public boolean isSpecializationDropdownDisplayed() {
        return isElementDisplayed(eduSpecializationDropdown, 6);
    }

    public boolean isInstitutionFieldDisplayed() {
        return isElementDisplayed(eduInstitutionField, 6);
    }

    public boolean isCompletionYearFieldDisplayed() {
        return isElementDisplayed(eduCompletionYearField, 6);
    }

    public void enterEducationForm(String degree, String institution, String year) {
        log.info("Filling Education Form: Degree={}, Inst={}, Year={}", degree, institution, year);
        if (degree != null) clearAndSendKeys(eduDegreeField, degree);
        if (institution != null) clearAndSendKeys(eduInstitutionField, institution);
        if (year != null) clearAndSendKeys(eduCompletionYearField, year);
    }

    public void clickEduSaveChanges() {
        log.info("Saving Education Form");
        click(eduSaveChangesButton);
    }

    public void clickEduCancel() {
        log.info("Canceling Education Form");
        click(eduCancelButton);
    }

    public boolean isEducationEmptyStateDisplayed() {
        return isElementDisplayed(emptyEducationMessage, 6);
    }

    // ── Achievements Section ─────────────────────────────────────────────────
    public boolean isAchievementsSectionDisplayed() {
        return isElementDisplayed(achievementsSection, 6);
    }

    public boolean isAddAchievementButtonDisplayed() {
        return isElementDisplayed(addAchievementButton, 6);
    }

    public void clickAddAchievement() {
        log.info("Clicking Add Achievement (+) button");
        click(addAchievementButton);
    }

    public boolean isAddAchievementModalDisplayed() {
        return isElementDisplayed(addAchievementModalTitle, 6);
    }

    public boolean isAchievementNameFieldDisplayed() {
        return isElementDisplayed(achNameField, 6);
    }

    public boolean isAchievementDescriptionFieldDisplayed() {
        return isElementDisplayed(achDescriptionField, 6);
    }

    public boolean isAchievementYearFieldDisplayed() {
        return isElementDisplayed(achYearField, 6);
    }

    public void enterAchievementForm(String name, String description, String year) {
        log.info("Filling Achievement Form: Name={}, Desc={}, Year={}", name, description, year);
        if (name != null) clearAndSendKeys(achNameField, name);
        if (description != null) clearAndSendKeys(achDescriptionField, description);
        if (year != null) clearAndSendKeys(achYearField, year);
    }

    public void clickAchSaveChanges() {
        log.info("Saving Achievement Form");
        click(achSaveChangesButton);
    }

    public void clickAchCancel() {
        log.info("Canceling Achievement Form");
        if (isElementDisplayed(achCancelButton, 4)) {
            click(achCancelButton);
        } else if (isElementDisplayed(achModalCloseX, 4)) {
            click(achModalCloseX);
        }
    }

    public boolean isAchievementsEmptyStateDisplayed() {
        return isElementDisplayed(emptyAchievementsMessage, 6);
    }

    // ── Specialization Tab ───────────────────────────────────────────────────
    public boolean isSpecializationDetailsSectionDisplayed() {
        return isElementDisplayed(specializationDetailsSection, 6);
    }

    public boolean isPrimarySpecializationDisplayed() {
        return isElementDisplayed(primarySpecialization, 6);
    }

    public boolean isSecondarySpecializationDisplayed() {
        return isElementDisplayed(secondarySpecialization, 6);
    }

    public boolean isExpertiseSummaryDisplayed() {
        return isElementDisplayed(expertiseSummary, 6);
    }

    public boolean isEditSpecializationIconDisplayed() {
        return isElementDisplayed(editSpecializationPencil, 6);
    }

    public void clickEditSpecialization() {
        log.info("Clicking Edit Specialization pencil icon");
        click(editSpecializationPencil);
    }

    // ── Profile Photo Options ────────────────────────────────────────────────
    public void clickProfilePhotoEdit() {
        log.info("Tapping Profile Photo Edit Icon");
        click(profileImageEditIcon);
    }

    public boolean isTakePhotoOptionDisplayed() {
        return isElementDisplayed(takePhotoOption, 6);
    }

    public boolean isChooseFromGalleryOptionDisplayed() {
        return isElementDisplayed(chooseFromGalleryOption, 6);
    }

    public boolean isRemovePhotoOptionDisplayed() {
        return isElementDisplayed(removePhotoOption, 6);
    }

    public void dismissPhotoOverlay() {
        log.info("Dismissing photo options overlay by tapping backdrop");
        GestureUtils.tapByCoordinates(driver, 100, 200);
    }

    // ── Edit Profile Form ────────────────────────────────────────────────────
    public boolean isEditProfileModalDisplayed() {
        return isElementDisplayed(editProfileModalTitle, 6);
    }

    public boolean isFirstNameFieldDisplayed() {
        return isElementDisplayed(editFirstNameField, 6);
    }

    public boolean isMiddleNameFieldDisplayed() {
        return isElementDisplayed(editMiddleNameField, 6);
    }

    public boolean isLastNameFieldDisplayed() {
        return isElementDisplayed(editLastNameField, 6);
    }

    public boolean isDobFieldDisplayed() {
        return isElementDisplayed(editDobField, 6);
    }

    public boolean isGenderDropdownDisplayed() {
        return isElementDisplayed(editGenderDropdown, 6);
    }

    public boolean isNationalityFieldDisplayed() {
        return isElementDisplayed(editNationalityField, 6);
    }

    public boolean isEmailFieldDisplayed() {
        return isElementDisplayed(editEmailField, 6);
    }

    public boolean isMobileFieldDisplayed() {
        return isElementDisplayed(editMobileField, 6);
    }

    public boolean isAddressFieldDisplayed() {
        return isElementDisplayed(editAddressField, 6);
    }

    public boolean isCountryFieldDisplayed() {
        return isElementDisplayed(editCountryField, 6);
    }

    public boolean isAboutMeFieldDisplayed() {
        return isElementDisplayed(editAboutMeField, 6);
    }

    public boolean isEditSaveChangesButtonDisplayed() {
        return isElementDisplayed(editSaveChangesButton, 6);
    }

    public boolean isEditCancelButtonDisplayed() {
        return isElementDisplayed(editCancelButton, 6);
    }

    public void enterEditProfileData(String firstName, String middleName, String lastName) {
        log.info("Editing Profile Names: {} {} {}", firstName, middleName, lastName);
        if (firstName != null) clearAndSendKeys(editFirstNameField, firstName);
        if (middleName != null) clearAndSendKeys(editMiddleNameField, middleName);
        if (lastName != null) clearAndSendKeys(editLastNameField, lastName);
    }

    public void editEmail(String email) {
        log.info("Editing email to: {}", email);
        clearAndSendKeys(editEmailField, email);
    }

    public void editMobile(String mobile) {
        log.info("Editing mobile to: {}", mobile);
        clearAndSendKeys(editMobileField, mobile);
    }

    public void editAddress(String address) {
        log.info("Editing address to: {}", address);
        clearAndSendKeys(editAddressField, address);
    }

    public void editAboutMe(String aboutMe) {
        log.info("Editing About Me to: {}", aboutMe);
        clearAndSendKeys(editAboutMeField, aboutMe);
    }

    public void clickEditSaveChanges() {
        log.info("Clicking Save Changes in Edit Profile");
        click(editSaveChangesButton);
    }

    public void clickEditCancel() {
        log.info("Clicking Cancel in Edit Profile");
        if (isElementDisplayed(editCancelButton, 4)) {
            click(editCancelButton);
        } else if (isElementDisplayed(editModalCloseX, 4)) {
            click(editModalCloseX);
        }
    }

    // ── Gender Dropdown ──────────────────────────────────────────────────────
    public void openGenderDropdown() {
        log.info("Opening Gender Dropdown");
        click(editGenderDropdown);
    }

    public void selectGender(String gender) {
        log.info("Selecting gender: {}", gender);
        if (gender.equalsIgnoreCase("male")) {
            click(genderOptionMale);
        } else {
            click(genderOptionFemale);
        }
    }

    // ── Date of Birth Date Picker ────────────────────────────────────────────
    public void openDobDatePicker() {
        log.info("Opening DOB Date Picker");
        click(editDobField);
    }

    public boolean isDatePickerModalDisplayed() {
        return isElementDisplayed(datePickerModal, 6) || isElementDisplayed(datePickerOkButton, 6);
    }

    public void clickDatePickerPrevMonth() {
        log.info("Navigating to previous month in date picker");
        if (isElementDisplayed(datePickerPrevMonth, 4)) click(datePickerPrevMonth);
    }

    public void clickDatePickerNextMonth() {
        log.info("Navigating to next month in date picker");
        if (isElementDisplayed(datePickerNextMonth, 4)) click(datePickerNextMonth);
    }

    public void confirmDateSelection() {
        log.info("Confirming date picker selection");
        if (isElementDisplayed(datePickerOkButton, 4)) click(datePickerOkButton);
    }

    public void cancelDateSelection() {
        log.info("Canceling date picker selection");
        if (isElementDisplayed(datePickerCancelButton, 4)) click(datePickerCancelButton);
    }

    // ── Validation Errors ────────────────────────────────────────────────────
    public boolean isValidationErrorDisplayed() {
        return isElementDisplayed(validationError, 4);
    }

    // ── Gestures ─────────────────────────────────────────────────────────────
    public void scrollDown() {
        GestureUtils.scrollDown(driver);
    }

    public void scrollUp() {
        GestureUtils.scrollUp(driver);
    }
}
