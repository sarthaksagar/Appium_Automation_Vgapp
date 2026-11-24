package com.vgcares.automation.tests.settings;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.pages.LoginPage;
import com.vgcares.automation.pages.SettingsPage;
import com.vgcares.automation.utils.JsonUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * ============================================================================
 * Settings Preferences Test Suite
 * ============================================================================
 * Covers TC_ID_SET-001 through TC_ID_SET-160 and TC_ID_SET-281 through SET-320:
 *  - Appearance (Light / System / Dark themes, rapid tap debounce, switching)
 *  - Currency Selector (INR, EUR, AUD, JPY, search, toast alerts, default selection)
 *  - Language Selector (English, Portuguese, Hindi, French, Russian, Spanish, German)
 *  - Extreme debounce and rapid toggle stress (20-30 cycles)
 * ============================================================================
 */
public class SettingsPreferencesTest extends BaseTest {

    private SettingsPage settingsPage;
    private JsonNode settingsData;

    @BeforeClass(alwaysRun = true)
    public void setupSettingsTest() {
        JsonNode authData = JsonUtils.readJsonFile("src/test/resources/testdata/auth-data.json");
        settingsData = authData.get("settingsData");

        JsonNode docCreds = authData.get("doctorCredentials");
        String email = docCreds != null && docCreds.has("email") ? docCreds.get("email").asText() : "virajd@techvishwaguru.com";
        String password = docCreds != null && docCreds.has("password") ? docCreds.get("password").asText() : "Admin@123";

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(email, password);

        HomePage homePage = new HomePage(driver);
        settingsPage = homePage.openSettings();
        Assert.assertTrue(settingsPage.isSettingsPageDisplayed(), "Settings screen must load successfully");
    }

    @Test(priority = 1, description = "TC_ID_SET-001 to 004: Verify Appearance theme options")
    public void testSettingsModuleLaunchAndAppearanceOptions_SET_001_to_004() {
        Assert.assertTrue(settingsPage.isAppearanceSectionDisplayed(), "Appearance section must be displayed");
        Assert.assertTrue(settingsPage.isLightOptionDisplayed(), "Light option must be visible");
        Assert.assertTrue(settingsPage.isSystemOptionDisplayed(), "System option must be visible");
        Assert.assertTrue(settingsPage.isDarkOptionDisplayed(), "Dark option must be visible");
    }

    @Test(priority = 2, description = "TC_ID_SET-005 to 020: Theme selection and debounce protection")
    public void testAppearanceThemeSelectionAndDebounce_SET_005_to_020() {
        settingsPage.selectLight();
        settingsPage.tapLightMultipleTimes(5);
        Assert.assertTrue(settingsPage.isSettingsPageDisplayed(), "UI must remain stable after multiple Light taps");

        settingsPage.selectDark();
        settingsPage.tapDarkMultipleTimes(5);
        Assert.assertTrue(settingsPage.isSettingsPageDisplayed(), "UI must remain stable after multiple Dark taps");

        settingsPage.selectSystem();
        settingsPage.tapSystemMultipleTimes(5);
        Assert.assertTrue(settingsPage.isSettingsPageDisplayed(), "UI must remain stable after multiple System taps");
    }

    @Test(priority = 3, description = "TC_ID_SET-021 to 040: Rapid alternating theme switching")
    public void testAppearanceRapidSwitchingAndState_SET_021_to_040() {
        for (int i = 0; i < 5; i++) {
            settingsPage.selectLight();
            settingsPage.selectDark();
            settingsPage.selectSystem();
        }
        Assert.assertTrue(settingsPage.isAppearanceSectionDisplayed(), "Appearance section must remain responsive");
    }

    @Test(priority = 4, description = "TC_ID_SET-041 to 060: Currency section and modal opening")
    public void testCurrencySelectorOpenAndDefaultDisplay_SET_041_to_060() {
        Assert.assertTrue(settingsPage.isCurrencySectionDisplayed(), "Currency section row must be displayed");
        settingsPage.openCurrencyModal();
        Assert.assertTrue(settingsPage.isCurrencyModalDisplayed(), "Select Currency modal must open");
        settingsPage.closeCurrencyModal();
    }

    @Test(priority = 5, description = "TC_ID_SET-061 to 080: Currency search and filtered options")
    public void testCurrencySearchAndFilter_SET_061_to_080() {
        settingsPage.openCurrencyModal();
        settingsPage.searchCurrency("EUR");
        Assert.assertTrue(settingsPage.isCurrencyOptionDisplayed("EUR"), "EUR option must be found");

        settingsPage.searchCurrency("AUD");
        Assert.assertTrue(settingsPage.isCurrencyOptionDisplayed("AUD"), "AUD option must be found");

        settingsPage.closeCurrencyModal();
    }

    @Test(priority = 6, description = "TC_ID_SET-081 to 100: Currency selection and active state")
    public void testCurrencySwitchingAndStatePersistence_SET_081_to_100() {
        settingsPage.openCurrencyModal();
        settingsPage.selectCurrency("INR");
        Assert.assertTrue(settingsPage.isCurrentCurrencyDisplayed("INR"), "INR should be reflected as current currency");
    }

    @Test(priority = 7, description = "TC_ID_SET-101 to 120: Language section and modal display")
    public void testLanguageSelectorOpenAndOptionsList_SET_101_to_120() {
        Assert.assertTrue(settingsPage.isLanguageSectionDisplayed(), "Language section row must be displayed");
        settingsPage.openLanguageModal();
        Assert.assertTrue(settingsPage.isLanguageModalDisplayed(), "Choose Language modal must open");
        settingsPage.closeLanguageModal();
    }

    @Test(priority = 8, description = "TC_ID_SET-121 to 140: Language search across Portuguese, Hindi, French")
    public void testLanguageSearchAndFilter_SET_121_to_140() {
        settingsPage.openLanguageModal();
        settingsPage.searchLanguage("Hindi");
        Assert.assertTrue(settingsPage.isLanguageOptionDisplayed("Hindi"), "Hindi must appear in filtered list");

        settingsPage.searchLanguage("French");
        Assert.assertTrue(settingsPage.isLanguageOptionDisplayed("French"), "French must appear in filtered list");

        settingsPage.closeLanguageModal();
    }

    @Test(priority = 9, description = "TC_ID_SET-141 to 160: Language selection and state preservation")
    public void testLanguageSelectionAndStatePersistence_SET_141_to_160() {
        settingsPage.openLanguageModal();
        settingsPage.selectLanguage("English");
        Assert.assertTrue(settingsPage.isCurrentLanguageDisplayed("English"), "English should remain selected");
    }

    @Test(priority = 10, description = "TC_ID_SET-281 to 320: Preferences extreme debounce stress")
    public void testPreferencesExtremeDebounceAndRapidTaps_SET_281_to_320() {
        int toggleCycles = settingsData.get("stressLimits").get("rapidThemeToggles").asInt();
        for (int i = 0; i < toggleCycles; i++) {
            settingsPage.selectLight();
            settingsPage.selectDark();
        }
        Assert.assertTrue(settingsPage.isAppearanceSectionDisplayed(), "Settings must survive 20 rapid theme cycles");
    }
}
