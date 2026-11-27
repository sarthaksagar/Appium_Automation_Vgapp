package com.vgcares.automation.driver;

import com.vgcares.automation.enums.UserRole;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

public final class DriverManager {
    private static final Logger log = LogManager.getLogger(DriverManager.class);

    private static AndroidDriver activeDriver;
    private static AppiumDriverLocalService localServer;
    private static Thread owner;
    private static UserRole authenticatedRole = UserRole.COMMON;

    private DriverManager() {}

    public static synchronized void startSession() {
        requireAvailable();
        try {
            reserveSession(DriverFactory.createDriver(
                    (server, timeout) -> localServer = LocalAppiumServer.ensureReady(server, timeout)));
        } catch (RuntimeException e) {
            stopLocalServer(e);
            throw e;
        }
    }

    static synchronized void startSession(Supplier<AndroidDriver> factory) {
        requireAvailable();
        reserveSession(factory.get());
    }

    private static void requireAvailable() {
        if (owner != null) {
            throw new MobileInfrastructureException("Driver configuration",
                    "A physical-device session is already reserved. Run mobile suites sequentially.");
        }
    }

    private static void reserveSession(AndroidDriver created) {
        if (created == null || created.getSessionId() == null) {
            throw new MobileInfrastructureException("Driver session", "Appium did not return a valid Android session.");
        }
        activeDriver = created;
        owner = Thread.currentThread();
        authenticatedRole = UserRole.COMMON;
    }

    public static synchronized boolean hasActiveSession() {
        return owner == Thread.currentThread()
                && activeDriver != null
                && activeDriver.getSessionId() != null;
    }

    public static synchronized AndroidDriver getDriver() {
        if (!hasActiveSession()) {
            throw new MobileInfrastructureException("Driver session",
                    "This thread has no active Android session. Check the suite setup result.");
        }
        return activeDriver;
    }

    public static synchronized UserRole getAuthenticatedRole() {
        return authenticatedRole;
    }

    public static synchronized void setAuthenticatedRole(UserRole role) {
        authenticatedRole = role == null ? UserRole.COMMON : role;
    }

    public static synchronized void quitDriver() {
        if (owner != Thread.currentThread()) {
            return;
        }
        MobileInfrastructureException failure = null;
        try {
            if (activeDriver != null) {
                activeDriver.quit();
            }
        } catch (RuntimeException e) {
            log.warn("Non-fatal error while closing Android session (session may already be terminated): {}", e.getMessage());
            failure = new MobileInfrastructureException("Driver session", "Could not close the Android session.", e);
        } finally {
            activeDriver = null;
            owner = null;
            authenticatedRole = UserRole.COMMON;
            stopLocalServer(failure);
        }
    }

    private static void stopLocalServer(RuntimeException failure) {
        AppiumDriverLocalService service = localServer;
        localServer = null;
        LocalAppiumServer.stop(service, failure);
    }
}
