package com.vgcares.automation.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.vgcares.automation.config.ConfigManager;
import com.vgcares.automation.constants.FrameworkConstants;
import com.vgcares.automation.utils.ExecutionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ExtentReportManager {

    private static final Logger log = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extent;
    private static String currentReportPath = "";
    private static final ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>();

    private ExtentReportManager() {}

    public static synchronized void initReports() {
        initReports(ExecutionUtils.getActiveSuiteName());
    }

    public static synchronized void initReports(String suiteLabel) {
        if (extent == null) {
            String safeSuite = (suiteLabel == null || suiteLabel.isBlank() ? "execution" : suiteLabel)
                    .replaceAll("[^a-zA-Z0-9_-]", "_").toLowerCase();
            File reportDir = new File(FrameworkConstants.REPORTS_DIR + File.separator + safeSuite);
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            currentReportPath = reportDir.getAbsolutePath() + File.separator
                    + "VGCares_" + safeSuite + "_Report_" + timestamp + ".html";

            ExtentSparkReporter spark = new ExtentSparkReporter(currentReportPath);
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("VGCares Automation Report - " + suiteLabel);
            spark.config().setReportName("VG Cares Mobile Automation (" + suiteLabel + ")");
            spark.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Execution ID", ExecutionUtils.getExecutionId());
            extent.setSystemInfo("Suite", suiteLabel);
            extent.setSystemInfo("Execution Mode", ExecutionUtils.getExecutionMode().name());
            extent.setSystemInfo("Platform", ConfigManager.get("platformName", "Android"));
            extent.setSystemInfo("Target App", ConfigManager.get("appPackage", "com.vg.vgcaresglobal.qa"));
            extent.setSystemInfo("Device Type", "Physical Android Device (USB)");
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            extent.setSystemInfo("Java Version", System.getProperty("java.version"));

            log.info("Initialized ExtentReports at: {}", currentReportPath);
        }
    }

    public static String getCurrentReportPath() {
        return currentReportPath;
    }

    public static synchronized void flushReports() {
        if (extent != null) {
            extent.flush();
            log.info("Flushed ExtentReports at: {}", currentReportPath);
        }
    }

    public static ExtentTest createTest(String testName, String description) {
        initReports();
        ExtentTest test = extent.createTest(testName, description == null ? testName : description);
        testThreadLocal.set(test);
        return test;
    }

    public static ExtentTest getTest() {
        return testThreadLocal.get();
    }

    public static void unloadTest() {
        testThreadLocal.remove();
    }

    public static void logInfo(String message) {
        if (getTest() != null) {
            getTest().log(Status.INFO, message);
        }
    }

    public static void logPass(String message) {
        if (getTest() != null) {
            getTest().log(Status.PASS, message);
        }
    }

    public static void logFail(String message) {
        if (getTest() != null) {
            getTest().log(Status.FAIL, message);
        }
    }
}
