package com.vgcares.automation.listeners;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.vgcares.automation.driver.DriverManager;
import com.vgcares.automation.reports.ExtentReportManager;
import com.vgcares.automation.utils.ScreenshotUtils;
import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    private static final Logger log = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        log.info("=== SUITE EXECUTION STARTED: {} ===", context.getName());
        ExtentReportManager.initReports();
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("=== SUITE EXECUTION COMPLETED: {} ===", context.getName());
        ExtentReportManager.flushReports();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        log.info("--- Starting Test: {} [{}] ---", testName, description);

        ExtentReportManager.createTest(testName, description != null ? description : testName);
        ExtentReportManager.getTest().assignCategory(result.getTestClass().getRealClass().getSimpleName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("+++ Test Passed: {} +++", result.getMethod().getMethodName());
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().log(Status.PASS, "Test passed successfully.");
            ExtentReportManager.unloadTest();
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        log.error("--- Test Failed: {} | Reason: {} ---", testName, result.getThrowable().getMessage());

        try {
            AndroidDriver driver = DriverManager.getDriver();
            String base64Screenshot = ScreenshotUtils.captureBase64(driver);
            String savedFilePath = ScreenshotUtils.captureScreenshot(driver, testName);

            if (ExtentReportManager.getTest() != null) {
                ExtentReportManager.getTest().log(Status.FAIL, "Test failed: " + result.getThrowable().getMessage());
                if (!base64Screenshot.isEmpty()) {
                    ExtentReportManager.getTest().fail(result.getThrowable(),
                            MediaEntityBuilder.createScreenCaptureFromBase64String(base64Screenshot, "Failure Screenshot").build());
                } else if (!savedFilePath.isEmpty()) {
                    ExtentReportManager.getTest().fail(result.getThrowable(),
                            MediaEntityBuilder.createScreenCaptureFromPath(savedFilePath).build());
                } else {
                    ExtentReportManager.getTest().fail(result.getThrowable());
                }
            }
        } catch (Exception e) {
            log.warn("Failed to capture screenshot on test failure: {}", e.getMessage());
            if (ExtentReportManager.getTest() != null) {
                ExtentReportManager.getTest().fail(result.getThrowable());
            }
        } finally {
            ExtentReportManager.unloadTest();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("~~~ Test Skipped: {} ~~~", result.getMethod().getMethodName());
        if (ExtentReportManager.getTest() != null) {
            ExtentReportManager.getTest().log(Status.SKIP, "Test skipped: " + 
                    (result.getThrowable() != null ? result.getThrowable().getMessage() : "Unknown reason"));
            ExtentReportManager.unloadTest();
        }
    }
}
