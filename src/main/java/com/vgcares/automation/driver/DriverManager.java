package com.vgcares.automation.driver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;

import java.util.function.Supplier;

public final class DriverManager {
    private static AndroidDriver activeDriver;
    private static AppiumDriverLocalService localServer;
    private static Thread owner;

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
    }

    public static synchronized boolean hasActiveSession() {
        return owner == Thread.currentThread() && activeDriver.getSessionId() != null;
    }

    public static synchronized AndroidDriver getDriver() {
        if (!hasActiveSession()) {
            throw new MobileInfrastructureException("Driver session",
                    "This thread has no active Android session. Check the suite setup result.");
        }
        return activeDriver;
    }

    public static synchronized void quitDriver() {
        if (owner != Thread.currentThread()) {
            return;
        }
        MobileInfrastructureException failure = null;
        try {
            activeDriver.quit();
        } catch (RuntimeException e) {
            failure = new MobileInfrastructureException("Driver session", "Could not close the Android session.", e);
            throw failure;
        } finally {
            activeDriver = null;
            owner = null;
            stopLocalServer(failure);
        }
    }

    private static void stopLocalServer(RuntimeException failure) {
        AppiumDriverLocalService service = localServer;
        localServer = null;
        LocalAppiumServer.stop(service, failure);
    }
}
