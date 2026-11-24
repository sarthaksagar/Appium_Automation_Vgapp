package com.vgcares.automation.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * ============================================================================
 * Messages Page Object Model (Encapsulating Messages Module)
 * ============================================================================
 * Implements senior SDET robust page patterns for:
 *  - Conversation List & Hospital Filter Tabs
 *  - Conversation Search with character manipulation & debouncing
 *  - Active Chat Conversation & Message Composer (Draft, Masking, Sanitization)
 *  - Attachment Menu interaction
 *  - Chat Details / Participant Bottom Sheet
 * ============================================================================
 */
public class MessagesPage extends BasePage {

    // ── Header & Navigation Locators ─────────────────────────────────────────
    private final By messagesHeaderTitle = By.xpath("//*[contains(@text, 'Messages') or contains(@content-desc, 'Messages')]");
    private final By searchField = By.xpath("//android.widget.EditText[contains(@hint, 'Search') or contains(@text, 'Search') or contains(@content-desc, 'Search')]");
    private final By clearSearchButton = By.xpath("//*[contains(@content-desc, 'Clear') or contains(@content-desc, 'clear') or contains(@resource-id, 'clear')]");
    private final By loadingProgressBar = By.xpath("//android.widget.ProgressBar | //*[contains(@content-desc, 'Loading') or contains(@content-desc, 'loading')]");

    // ── Conversation List Locators ───────────────────────────────────────────
    private final By conversationCardItem = By.xpath("//*[contains(@resource-id, 'conversation_item') or contains(@resource-id, 'chat_item') or contains(@content-desc, 'Hospital') or contains(@content-desc, 'Team') or contains(@content-desc, 'Dr.')]");
    private final By emptyStateContainer = By.xpath("//*[contains(@text, 'No messages') or contains(@text, 'No conversations') or contains(@content-desc, 'No messages') or contains(@content-desc, 'No conversations')]");
    private final By retryButton = By.xpath("//*[contains(@text, 'Retry') or contains(@content-desc, 'Retry') or contains(@text, 'Try Again')]");

    // ── Active Chat Conversation Locators ────────────────────────────────────
    private final By chatTitleHeading = By.xpath("//*[contains(@resource-id, 'chat_title') or contains(@resource-id, 'title') or (contains(@class, 'TextView') and contains(@content-desc, 'Hospital'))]");
    private final By chatBackButton = By.xpath("//*[contains(@content-desc, 'Navigate up') or contains(@content-desc, 'Back') or contains(@content-desc, 'back') or contains(@resource-id, 'back')]");
    private final By chatInfoButton = By.xpath("//*[contains(@content-desc, 'Info') or contains(@content-desc, 'info') or contains(@content-desc, 'Details') or contains(@resource-id, 'info') or contains(@content-desc, 'more')]");

    // ── Message Composer Locators ────────────────────────────────────────────
    private final By composerInputField = By.xpath("//android.widget.EditText[contains(@hint, 'Type a message') or contains(@hint, 'message') or contains(@content-desc, 'message_input') or (contains(@class, 'EditText') and not(contains(@hint, 'Search')))]");
    private final By sendButton = By.xpath("//*[contains(@content-desc, 'Send') or contains(@content-desc, 'send') or contains(@resource-id, 'send')]");
    private final By attachmentPickerButton = By.xpath("//*[contains(@content-desc, 'Attach') or contains(@content-desc, 'attach') or contains(@content-desc, 'file') or contains(@resource-id, 'attachment')]");
    private final By attachmentOptionsPopup = By.xpath("//*[contains(@text, 'Document') or contains(@text, 'Gallery') or contains(@text, 'Camera') or contains(@content-desc, 'Document')]");

    // ── Chat Details Bottom Sheet Locators ───────────────────────────────────
    private final By chatDetailsSheetTitle = By.xpath("//*[contains(@text, 'Chat Details') or contains(@content-desc, 'Chat Details') or contains(@text, 'Group Info') or contains(@content-desc, 'Group Info')]");
    private final By participantCountText = By.xpath("//*[contains(@text, 'Participant') or contains(@content-desc, 'Participant')]");
    private final By participantListItems = By.xpath("//*[contains(@resource-id, 'participant_item') or contains(@content-desc, 'Doctor') or contains(@content-desc, 'Dr.') or contains(@content-desc, 'Staff')]");
    private final By closeDetailsButton = By.xpath("//*[contains(@content-desc, 'Close') or contains(@content-desc, 'close') or contains(@content-desc, 'Cancel') or contains(@resource-id, 'close')]");

    public MessagesPage(AndroidDriver driver) {
        super(driver);
    }

    // ── Screen Presence & Verification ───────────────────────────────────────

    public boolean isMessagesTabLoaded() {
        return isElementDisplayed(messagesHeaderTitle, 10) || isElementDisplayed(searchField, 8);
    }

    public boolean isSearchFieldDisplayed() {
        return isElementDisplayed(searchField, 8);
    }

    public boolean isProgressBarVisible() {
        return isElementDisplayed(loadingProgressBar, 2);
    }

    // ── Search & Filter Interactions ─────────────────────────────────────────

    public MessagesPage enterSearchQuery(String query) {
        log.info("Entering search query in Messages: '{}'", query);
        clearAndSendKeys(searchField, query);
        return this;
    }

    public MessagesPage clearSearchField() {
        log.info("Clearing Messages search field");
        if (isElementDisplayed(clearSearchButton, 3)) {
            click(clearSearchButton);
        } else {
            clearAndSendKeys(searchField, "");
        }
        return this;
    }

    public String getEnteredSearchText() {
        return getText(searchField);
    }

    public MessagesPage selectHospitalFilter(String hospitalName) {
        log.info("Selecting Hospital filter in Messages: {}", hospitalName);
        By hospitalFilterChip = By.xpath("//*[contains(@text, '" + hospitalName + "') or contains(@content-desc, '" + hospitalName + "')]");
        if (isElementDisplayed(hospitalFilterChip, 5)) {
            click(hospitalFilterChip);
        }
        return this;
    }

    // ── Conversation Card Navigation ─────────────────────────────────────────

    public boolean hasConversations() {
        return isElementDisplayed(conversationCardItem, 8);
    }

    public boolean openFirstConversation() {
        log.info("Attempting to open the first available conversation");
        if (isElementDisplayed(conversationCardItem, 8)) {
            click(conversationCardItem);
            return isElementDisplayed(composerInputField, 10);
        }
        log.warn("No conversation card displayed on Messages screen.");
        return false;
    }

    public void tapConversationRepeatedly(int taps) {
        log.info("Stress Testing: Rapidly tapping conversation card {} times", taps);
        if (isElementDisplayed(conversationCardItem, 5)) {
            WebElement card = waitForVisibility(conversationCardItem);
            for (int i = 0; i < taps; i++) {
                try {
                    card.click();
                } catch (Exception ignored) {
                    break;
                }
            }
        }
    }

    // ── Active Chat Conversation & Composer ──────────────────────────────────

    public boolean isChatConversationOpen() {
        return isElementDisplayed(composerInputField, 10) || isElementDisplayed(chatBackButton, 8);
    }

    public boolean isComposerFieldDisplayed() {
        return isElementDisplayed(composerInputField, 8);
    }

    public MessagesPage typeDraftMessage(String draftText) {
        log.info("Typing message draft into composer: '{}'", draftText);
        clearAndSendKeys(composerInputField, draftText);
        return this;
    }

    public String getComposerText() {
        return getText(composerInputField);
    }

    public MessagesPage clearComposer() {
        log.info("Clearing composer input field");
        clearAndSendKeys(composerInputField, "");
        return this;
    }

    public boolean isSendButtonDisplayed() {
        return isElementDisplayed(sendButton, 5);
    }

    public void clickSendButton() {
        log.info("Tapping Send button in message composer");
        if (isElementDisplayed(sendButton, 5)) {
            click(sendButton);
        }
    }

    public boolean clickAttachmentButton() {
        log.info("Tapping Attachment button in chat");
        if (isElementDisplayed(attachmentPickerButton, 5)) {
            click(attachmentPickerButton);
            return isElementDisplayed(attachmentOptionsPopup, 5);
        }
        return false;
    }

    public void dismissAttachmentOptionsIfPresent() {
        log.info("Dismissing attachment picker/dialog if visible");
        if (isElementDisplayed(attachmentOptionsPopup, 3)) {
            try {
                driver.navigate().back();
            } catch (Exception e) {
                log.debug("Back action on attachment picker ignored: {}", e.getMessage());
            }
        }
    }

    public void pressChatBackButton() {
        log.info("Tapping Chat back button to return to conversation list");
        if (isElementDisplayed(chatBackButton, 6)) {
            click(chatBackButton);
        } else {
            driver.navigate().back();
        }
    }

    // ── Chat Details / Participant Sheet ─────────────────────────────────────

    public boolean openChatDetails() {
        log.info("Opening Chat Details / Info overlay");
        if (isElementDisplayed(chatInfoButton, 6)) {
            click(chatInfoButton);
            return isElementDisplayed(chatDetailsSheetTitle, 8) || isElementDisplayed(participantCountText, 8);
        }
        return false;
    }

    public boolean isChatDetailsSheetOpen() {
        return isElementDisplayed(chatDetailsSheetTitle, 6) || isElementDisplayed(participantCountText, 6);
    }

    public void closeChatDetails() {
        log.info("Closing Chat Details sheet");
        if (isElementDisplayed(closeDetailsButton, 4)) {
            click(closeDetailsButton);
        } else {
            driver.navigate().back();
        }
    }

    public boolean isRetryButtonDisplayed() {
        return isElementDisplayed(retryButton, 4);
    }

    public void clickRetryRepeatedly(int taps) {
        log.info("Stress Testing: Rapidly tapping Retry button {} times", taps);
        if (isElementDisplayed(retryButton, 4)) {
            WebElement btn = waitForVisibility(retryButton);
            for (int i = 0; i < taps; i++) {
                try {
                    btn.click();
                } catch (Exception ignored) {
                    break;
                }
            }
        }
    }
}
