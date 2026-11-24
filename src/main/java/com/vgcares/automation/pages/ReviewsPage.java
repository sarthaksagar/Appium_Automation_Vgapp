package com.vgcares.automation.pages;

import com.vgcares.automation.utils.GestureUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * ============================================================================
 * Reviews Page Object Model (My Reviews Module)
 * ============================================================================
 * Senior SDET enterprise Page Object encapsulating all UI elements,
 * interactions, and state verifications for:
 *  - Header & Navigation (Title "My Reviews", Back navigation button)
 *  - Overall Rating Summary (Overall score, Star icons, Review count, Recommendation %)
 *  - Rating Distribution (5-star down to 1-star distribution rows & progress indicators)
 *  - Category Ratings (Communication, Empathy & Care, Medical Expertise, Punctuality)
 *  - Patient Reviews Section:
 *      * Patient identity (Name, Avatar initial)
 *      * Review rating badge (e.g. 5.0)
 *      * Written feedback comment / "No written feedback" placeholder
 *      * Review date (e.g. 3-Aug-26)
 *      * Consultation mode badge (Teleconsultation / In-Person)
 *  - Vertical Scrolling and Gesture Navigation
 * ============================================================================
 */
public class ReviewsPage extends BasePage {

    // ── Header & Navigation ──────────────────────────────────────────────────
    private final By screenTitle = By.xpath("//*[contains(@text, 'My Reviews') or contains(@content-desc, 'My Reviews') or contains(@text, 'Reviews') or contains(@content-desc, 'Reviews')]");
    private final By backButton = By.xpath("//*[contains(@content-desc, 'Back') or contains(@content-desc, '<') or contains(@class, 'BackButton')]");

    // ── Review Summary Section ───────────────────────────────────────────────
    private final By overallRatingValue = By.xpath("//*[contains(@text, '5.0') or contains(@content-desc, '5.0') or contains(@text, '5') or contains(@content-desc, '5')][1]");
    private final By reviewCountText = By.xpath("//*[contains(@text, 'Based on') or contains(@content-desc, 'Based on') or contains(@text, 'reviews') or contains(@content-desc, 'reviews')]");
    private final By recommendationPercentage = By.xpath("//*[contains(@text, 'Recommended') or contains(@content-desc, 'Recommended') or contains(@text, '100%') or contains(@content-desc, '100%')]");
    private final By summaryStars = By.xpath("//*[contains(@content-desc, 'star') or contains(@resource-id, 'star') or contains(@class, 'RatingBar')]");

    // ── Rating Distribution (5★ to 1★) ────────────────────────────────────────
    private final By row5Star = By.xpath("//*[contains(@text, '5') or contains(@content-desc, '5')]/following::*[contains(@class, 'ProgressBar') or contains(@class, 'LinearProgress') or contains(@resource-id, 'bar')][1]");
    private final By row4Star = By.xpath("//*[contains(@text, '4') or contains(@content-desc, '4')]");
    private final By row3Star = By.xpath("//*[contains(@text, '3') or contains(@content-desc, '3')]");
    private final By row2Star = By.xpath("//*[contains(@text, '2') or contains(@content-desc, '2')]");
    private final By row1Star = By.xpath("//*[contains(@text, '1') or contains(@content-desc, '1')]");

    // ── Category Ratings ─────────────────────────────────────────────────────
    private final By catCommunication = By.xpath("//*[contains(@text, 'Communication') or contains(@content-desc, 'Communication')]");
    private final By catEmpathyAndCare = By.xpath("//*[contains(@text, 'Empathy & Care') or contains(@content-desc, 'Empathy & Care') or contains(@text, 'Empathy') or contains(@content-desc, 'Empathy')]");
    private final By catMedicalExpertise = By.xpath("//*[contains(@text, 'Medical Expertise') or contains(@content-desc, 'Medical Expertise') or contains(@text, 'Expertise') or contains(@content-desc, 'Expertise')]");
    private final By catPunctuality = By.xpath("//*[contains(@text, 'Punctuality') or contains(@content-desc, 'Punctuality')]");

    // ── Patient Reviews List Section ─────────────────────────────────────────
    private final By patientReviewsHeading = By.xpath("//*[contains(@text, 'Patient Reviews') or contains(@content-desc, 'Patient Reviews')]");
    private final By reviewCards = By.xpath("//*[contains(@resource-id, 'card') or contains(@content-desc, 'Aug') or contains(@content-desc, 'Jul') or contains(@text, 'Aug') or contains(@text, 'Jul')]");
    private final By patientAvatar = By.xpath("//*[contains(@resource-id, 'avatar') or contains(@content-desc, 'avatar') or contains(@class, 'CircleAvatar')]");
    private final By noWrittenFeedbackPlaceholder = By.xpath("//*[contains(@text, 'No written feedback') or contains(@content-desc, 'No written feedback') or contains(@text, 'No comment') or contains(@content-desc, 'No comment')]");
    private final By modeTeleconsultation = By.xpath("//*[contains(@text, 'Teleconsultation') or contains(@content-desc, 'Teleconsultation')]");
    private final By modeInPerson = By.xpath("//*[contains(@text, 'In-Person') or contains(@content-desc, 'In-Person')]");

    public ReviewsPage(AndroidDriver driver) {
        super(driver);
    }

    // ── Screen Load & Navigation ─────────────────────────────────────────────
    public boolean isReviewsScreenDisplayed() {
        return isElementDisplayed(screenTitle, 10);
    }

    public boolean isBackButtonDisplayed() {
        return isElementDisplayed(backButton, 6);
    }

    public void clickBackButton() {
        log.info("Clicking back navigation button on My Reviews screen");
        click(backButton);
    }

    // ── Review Summary Getters & Verifications ───────────────────────────────
    public boolean isOverallRatingDisplayed() {
        return isElementDisplayed(overallRatingValue, 6);
    }

    public String getOverallRatingText() {
        return getText(overallRatingValue);
    }

    public boolean isReviewCountDisplayed() {
        return isElementDisplayed(reviewCountText, 6);
    }

    public String getReviewCountText() {
        return getText(reviewCountText);
    }

    public boolean isRecommendationDisplayed() {
        return isElementDisplayed(recommendationPercentage, 6);
    }

    public String getRecommendationText() {
        return getText(recommendationPercentage);
    }

    // ── Rating Distribution ──────────────────────────────────────────────────
    public boolean isRatingDistributionSectionDisplayed() {
        return isElementDisplayed(row5Star, 5) || isElementDisplayed(row4Star, 5);
    }

    public boolean isStarRowDisplayed(int starNumber) {
        By rowBy = By.xpath("//*[contains(@text, '" + starNumber + "') or contains(@content-desc, '" + starNumber + "')]");
        return isElementDisplayed(rowBy, 5);
    }

    // ── Category Ratings ─────────────────────────────────────────────────────
    public boolean isCommunicationCategoryDisplayed() {
        return isElementDisplayed(catCommunication, 6);
    }

    public boolean isEmpathyAndCareCategoryDisplayed() {
        return isElementDisplayed(catEmpathyAndCare, 6);
    }

    public boolean isMedicalExpertiseCategoryDisplayed() {
        return isElementDisplayed(catMedicalExpertise, 6);
    }

    public boolean isPunctualityCategoryDisplayed() {
        return isElementDisplayed(catPunctuality, 6);
    }

    // ── Patient Reviews List ─────────────────────────────────────────────────
    public boolean isPatientReviewsHeadingDisplayed() {
        return isElementDisplayed(patientReviewsHeading, 8);
    }

    public List<WebElement> getReviewCardsList() {
        return findElements(reviewCards);
    }

    public boolean isPatientNameDisplayed(String patientName) {
        By nameBy = By.xpath("//*[contains(@text, '" + patientName + "') or contains(@content-desc, '" + patientName + "')]");
        return isElementDisplayed(nameBy, 6);
    }

    public boolean isReviewDateDisplayed(String dateText) {
        By dateBy = By.xpath("//*[contains(@text, '" + dateText + "') or contains(@content-desc, '" + dateText + "')]");
        return isElementDisplayed(dateBy, 6);
    }

    public boolean isReviewCommentDisplayed(String comment) {
        By commentBy = By.xpath("//*[contains(@text, '" + comment + "') or contains(@content-desc, '" + comment + "')]");
        return isElementDisplayed(commentBy, 6);
    }

    public boolean isNoWrittenFeedbackDisplayed() {
        return isElementDisplayed(noWrittenFeedbackPlaceholder, 6);
    }

    public boolean isConsultationModeDisplayed(String mode) {
        By modeBy = By.xpath("//*[contains(@text, '" + mode + "') or contains(@content-desc, '" + mode + "')]");
        return isElementDisplayed(modeBy, 6);
    }

    public boolean isPatientAvatarDisplayed() {
        return isElementDisplayed(patientAvatar, 6);
    }

    // ── Scrolling & Gestures ─────────────────────────────────────────────────
    public void scrollToPatientReviews() {
        log.info("Scrolling down to Patient Reviews section");
        GestureUtils.scrollDown(driver);
    }

    public void scrollToSummaryTop() {
        log.info("Scrolling up to review summary section");
        GestureUtils.scrollUp(driver);
    }
}
