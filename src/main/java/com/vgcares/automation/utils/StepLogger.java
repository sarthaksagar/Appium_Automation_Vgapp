package com.vgcares.automation.utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class StepLogger {

    private static final Logger log = LogManager.getLogger("STEP");
    private static final ThreadLocal<Integer> stepCounter = ThreadLocal.withInitial(() -> 1);
    private static final ThreadLocal<ExtentTest> extentTestThreadLocal = new ThreadLocal<>();

    private StepLogger() {}

    public static void setExtentTest(ExtentTest test) {
        extentTestThreadLocal.set(test);
    }

    public static void clearExtentTest() {
        extentTestThreadLocal.remove();
        stepCounter.set(1);
    }

    public static void reset() {
        stepCounter.set(1);
    }

    public static void step(String stepDescription) {
        int currentStep = stepCounter.get();
        String formatted = String.format("[STEP %02d] %s", currentStep, stepDescription);
        System.out.println("\n" + formatted);
        log.info(formatted);

        ExtentTest test = extentTestThreadLocal.get();
        if (test != null) {
            test.info("<b>" + formatted + "</b>");
        }
        stepCounter.set(currentStep + 1);
    }

    public static void pass(String message) {
        String formatted = "[PASS] " + message;
        System.out.println(formatted);
        log.info(formatted);

        ExtentTest test = extentTestThreadLocal.get();
        if (test != null) {
            test.log(Status.PASS, message);
        }
    }

    public static void fail(String message) {
        String formatted = "[FAIL] " + message;
        System.out.println(formatted);
        log.error(formatted);

        ExtentTest test = extentTestThreadLocal.get();
        if (test != null) {
            test.log(Status.FAIL, message);
        }
    }

    public static void info(String message) {
        String formatted = "[INFO] " + message;
        System.out.println(formatted);
        log.info(formatted);

        ExtentTest test = extentTestThreadLocal.get();
        if (test != null) {
            test.info(message);
        }
    }
}
