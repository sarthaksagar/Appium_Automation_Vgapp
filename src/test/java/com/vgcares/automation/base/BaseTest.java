package com.vgcares.automation.base;

import com.vgcares.automation.driver.DriverFactory;
import com.vgcares.automation.driver.DriverManager;
import com.vgcares.automation.listeners.TestListener;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

/**
 * BaseTest — class-level driver lifecycle.
 *
 * <p><b>Why @BeforeClass / @AfterClass?</b>
 * Using {@code @BeforeMethod} / {@code @AfterMethod} restarts the Appium session
 * (and re-launches the app) for EVERY single test method — adding 15–25 seconds
 * overhead per test.  With class-level lifecycle one session serves all test
 * methods inside a class, reducing total run time by 70-80 %.
 *
 * <p>Test classes that need a fresh session for a specific method can call
 * {@code DriverManager.quitDriver()} + {@code driver = DriverFactory.createDriver()}
 * inside that method directly.
 */
@Listeners(TestListener.class)
public abstract class BaseTest {

    protected final Logger log = LogManager.getLogger(getClass());
    protected AndroidDriver driver;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        log.info(">>> [{}] Initializing Driver Session <<<", getClass().getSimpleName());
        try {
            if (DriverManager.hasActiveSession()) {
                driver = DriverManager.getDriver();
                log.info("Reusing active AndroidDriver session [{}] for {}.", driver.getSessionId(), getClass().getSimpleName());
            } else {
                driver = DriverFactory.createDriver();
                DriverManager.setDriver(driver);
                log.info("Driver session created. Session ID: {}", driver.getSessionId());
            }
        } catch (Exception e) {
            log.error("FATAL — could not create driver session: {}", e.getMessage(), e);
            throw e;
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        log.info(">>> [{}] Finished Execution <<<", getClass().getSimpleName());
    }

    @org.testng.annotations.AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        log.info("=== SUITE COMPLETED: Closing AndroidDriver Session ===");
        try {
            DriverManager.quitDriver();
        } catch (Exception e) {
            log.warn("Error during driver suite teardown: {}", e.getMessage());
        } finally {
            driver = null;
        }
    }

    /** Always returns the current live driver from the thread-local store. */
    protected AndroidDriver getDriver() {
        return DriverManager.getDriver();
    }
}
