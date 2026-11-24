package com.vgcares.automation.pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class NotificationsPage extends BasePage {

    // Locators
    private final By notificationsTitle = By.xpath("//*[contains(@text, 'Notifications') or contains(@content-desc, 'Notifications')]");
    private final By filterAll = By.xpath("//*[contains(@text, 'All') or contains(@content-desc, 'All')]");
    private final By filterUnread = By.xpath("//*[contains(@text, 'Unread') or contains(@content-desc, 'Unread')]");
    private final By markAllAsReadButton = By.xpath("//*[contains(@text, 'Mark all as read') or contains(@content-desc, 'Mark all as read')]");
    private final By notificationCard = By.xpath("//*[contains(@content-desc, 'Appointment') or contains(@text, 'Appointment')]");
    private final By backButton = By.xpath("//*[contains(@content-desc, 'Back') or @content-desc='←' or contains(@class, 'BackButton')]");

    public NotificationsPage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isNotificationsScreenDisplayed() {
        return isElementDisplayed(notificationsTitle, 10);
    }

    public boolean isAllTabDisplayed() {
        return isElementDisplayed(filterAll, 5);
    }

    public boolean isUnreadTabDisplayed() {
        return isElementDisplayed(filterUnread, 5);
    }

    public boolean isMarkAllReadDisplayed() {
        return isElementDisplayed(markAllAsReadButton, 5);
    }

    public void switchToAllTab() {
        log.info("Selecting 'All' notifications filter");
        if (isAllTabDisplayed()) click(filterAll);
    }

    public void switchToUnreadTab() {
        log.info("Selecting 'Unread' notifications filter");
        if (isUnreadTabDisplayed()) click(filterUnread);
    }

    /** @deprecated use switchToAllTab() */
    @Deprecated
    public void filterByAll() { switchToAllTab(); }

    /** @deprecated use switchToUnreadTab() */
    @Deprecated
    public void filterByUnread() { switchToUnreadTab(); }

    public void clickMarkAllAsRead() {
        log.info("Clicking 'Mark all as read'");
        if (isMarkAllReadDisplayed()) click(markAllAsReadButton);
    }

    public boolean hasNotifications() {
        return isElementDisplayed(notificationCard, 5);
    }

    public void navigateBackToHome() {
        log.info("Navigating back from Notifications to Home");
        if (isElementDisplayed(backButton, 4)) {
            click(backButton);
        } else {
            driver.navigate().back();
        }
    }
}
