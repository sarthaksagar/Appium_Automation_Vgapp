package com.vgcares.automation.pages.patient;

import com.vgcares.automation.pages.BasePage;
import com.vgcares.automation.pages.LoginPage;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

/**
 * Page Object for verified post-login Patient Dashboard interactions
 * (header presence, navigation stability, and session logout).
 * Does not fabricate unverified Patient feature screens.
 */
public class PatientHomePage extends BasePage {

    private final By loginButton = By.xpath(
            "//*[@clickable='true' and (@content-desc='Login' or @text='Login' or @content-desc='Sign In' or @text='Sign In')]");

    private final By patientGreetingOrHome = By.xpath(
            "//*[contains(@content-desc,'Hello') or contains(@text,'Hello')"
                    + " or contains(@content-desc,'Hi') or contains(@text,'Hi')"
                    + " or contains(@content-desc,'Home') or contains(@text,'Home')"
                    + " or contains(@content-desc,'Welcome') or contains(@text,'Welcome')]");

    private final By sideMenuButton = By.xpath(
            "//*[contains(@content-desc, 'Open navigation drawer') or contains(@content-desc, 'drawer')"
                    + " or contains(@content-desc, 'Menu') or contains(@resource-id, 'menu')]");

    private final By logoutButton = By.xpath(
            "//*[contains(@text, 'Logout') or contains(@content-desc, 'Logout')"
                    + " or contains(@text, 'Sign Out') or contains(@content-desc, 'Sign Out')]");

    private final By logoutConfirmButton = By.xpath(
            "//android.widget.Button[contains(@text, 'Yes') or contains(@content-desc, 'Yes')"
                    + " or contains(@text, 'Logout') or contains(@content-desc, 'Logout')]");

    private final By doctorOnlySnapshot = By.xpath(
            "//*[contains(@text, \"Today's Snapshot\") or contains(@content-desc, \"Today's Snapshot\")]");

    public PatientHomePage(AndroidDriver driver) {
        super(driver);
    }

    public boolean isPatientDashboardLoaded() {
        boolean notOnLogin = !isElementDisplayed(loginButton, 3);
        return notOnLogin && isElementDisplayed(patientGreetingOrHome, 12);
    }

    public boolean isDoctorOnlyClinicalSnapshotVisible() {
        return isElementDisplayed(doctorOnlySnapshot, 3);
    }

    public boolean isGreetingDisplayed() {
        return isElementDisplayed(patientGreetingOrHome, 8);
    }

    public boolean openSideDrawerIfPresent() {
        if (isElementDisplayed(sideMenuButton, 4)) {
            click(sideMenuButton);
            return true;
        }
        return false;
    }

    public LoginPage logout() {
        log.info("Performing Patient Logout");
        if (!isElementDisplayed(logoutButton, 3)) {
            openSideDrawerIfPresent();
        }
        if (isElementDisplayed(logoutButton, 5)) {
            click(logoutButton);
            if (isElementDisplayed(logoutConfirmButton, 4)) {
                click(logoutConfirmButton);
            }
        }
        return new LoginPage(driver);
    }
}
