package com.vgcares.automation.driver;

import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class DriverManager {

    private static final Logger log = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<AndroidDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {}

    public static boolean hasActiveSession() {
        AndroidDriver driver = driverThreadLocal.get();
        if (driver == null) return false;
        try {
            return driver.getSessionId() != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static AndroidDriver getDriver() {
        AndroidDriver driver = driverThreadLocal.get();
        if (driver == null) {
            log.error("Driver instance is null for the current thread: {}", Thread.currentThread().getId());
            throw new IllegalStateException("AndroidDriver has not been initialized. Call DriverFactory.createDriver() first.");
        }
        return driver;
    }

    public static void setDriver(AndroidDriver driver) {
        if (driver != null) {
            driverThreadLocal.set(driver);
            log.debug("AndroidDriver set for thread: {}", Thread.currentThread().getId());
        }
    }

    public static void quitDriver() {
        AndroidDriver driver = driverThreadLocal.get();
        if (driver != null) {
            try {
                driver.quit();
                log.info("AndroidDriver session closed successfully for thread: {}", Thread.currentThread().getId());
            } catch (Exception e) {
                log.warn("Error while quitting AndroidDriver session: {}", e.getMessage());
            } finally {
                driverThreadLocal.remove();
            }
        }
    }
}
