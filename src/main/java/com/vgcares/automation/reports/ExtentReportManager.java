package com.vgcares.automation.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.vgcares.automation.config.ConfigManager;
import com.vgcares.automation.constants.FrameworkConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ExtentReportManager {

    private static final Logger log = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>();

    private ExtentReportManager() {}

    public static synchronized void initReports() {
        if (extent == null) {
            File reportDir = new File(FrameworkConstants.REPORTS_DIR);
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            String reportPath = FrameworkConstants.REPORTS_DIR + File.separator + "VGCares_Execution_Report_" + timestamp + ".html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("VGCares Automation Execution Report");
            spark.config().setReportName("Mobile Appium Automation Results");
            spark.config().setTimeStampFormat("MMM dd, yyyy HH:mm:ss");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Platform", ConfigManager.get("platformName", "Android"));
            extent.setSystemInfo("Target App", ConfigManager.get("appPackage", "com.vg.vgcaresglobal.qa"));
            extent.setSystemInfo("Device Type", "Real Physical Android Phone (USB)");

            log.info("Initialized ExtentReports at: {}", reportPath);
        }
    }

    public static synchronized void flushReports() {
        if (extent != null) {
            extent.flush();
            log.info("Flushed ExtentReports.");
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
