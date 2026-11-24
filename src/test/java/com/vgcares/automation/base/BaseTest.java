package com.vgcares.automation.base;

import com.vgcares.automation.driver.DriverManager;
import com.vgcares.automation.driver.MobileInfrastructureException;
import com.vgcares.automation.listeners.TestListener;
import com.vgcares.automation.config.ConfigManager;
import com.vgcares.automation.pages.HomePage;
import com.vgcares.automation.pages.LaunchPage;
import com.vgcares.automation.pages.LoginPage;
import com.vgcares.automation.testdata.QaCredentials;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

@Listeners(TestListener.class)
public abstract class BaseTest {
    public static final String SUITE_SETUP_FAILURE = BaseTest.class.getName() + ".suiteSetupFailure";

    protected final Logger log = LogManager.getLogger(getClass());
    protected AndroidDriver driver;

    @BeforeSuite(alwaysRun = true)
    public final void startMobileSuite(ITestContext context) {
        try {
            var suite = context.getCurrentXmlTest().getSuite();
            if (suite.getParallel().isParallel() || suite.getTests().stream().anyMatch(test -> test.getParallel().isParallel())) {
                throw new MobileInfrastructureException("Driver configuration",
                        "Parallel TestNG execution is incompatible with the shared physical Android session.");
            }
            DriverManager.startSession();
        } catch (RuntimeException e) {
            context.getSuite().setAttribute(SUITE_SETUP_FAILURE, e);
            throw e;
        }
    }

    @BeforeClass(alwaysRun = true)
    public final void setUp() {
        if (!DriverManager.hasActiveSession()) {
            DriverManager.startSession();
        }
        driver = getDriver();
    }

    @AfterSuite(alwaysRun = true)
    public final void finishMobileSuite() {
        DriverManager.quitDriver();
    }

    protected final LoginPage openFreshLogin() {
        prepareAuthScreen();
        return new LoginPage(getDriver()).openLoginTab();
    }

    protected final void prepareAuthScreen() {
        AndroidDriver current = getDriver();
        LaunchPage launch = new LaunchPage(current);
        LaunchPage.Screen screen;
        try {
            screen = launch.waitForAuthEntryScreen();
        } catch (TimeoutException initialTimeout) {
            log.warn("VG Cares did not reach a recognized launch screen. Reopening the app once.");
            try {
                String appPackage = ConfigManager.get("appPackage", "");
                current.terminateApp(appPackage);
                current.activateApp(appPackage);
                screen = launch.waitForAuthEntryScreen();
            } catch (RuntimeException recoveryFailure) {
                recoveryFailure.addSuppressed(initialTimeout);
                throw recoveryFailure;
            }
        }
        log.info("Preparing authentication from the {} screen", screen);
        if (screen == LaunchPage.Screen.HOME) {
            new HomePage(current).logout();
            launch.waitForAuthTabs();
        } else {
            launch.passOnboardingIfPresent(screen);
        }
    }

    protected final HomePage loginAsQaDoctor(LoginPage loginPage) {
        QaCredentials account = QaCredentials.doctor();
        return loginPage.loginWithDynamicAccount(account.email(), account.password());
    }

    protected final HomePage loginAsQaDoctor() {
        QaCredentials account = QaCredentials.doctor();
        HomePage homePage = openFreshLogin().loginWithDynamicAccount(account.email(), account.password());
        Assert.assertTrue(homePage.isHomeScreenLoaded(), "The authorized QA account must reach the doctor dashboard.");
        return homePage;
    }

    protected final AndroidDriver getDriver() {
        return DriverManager.getDriver();
    }
}
