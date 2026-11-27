package com.vgcares.automation.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.vgcares.automation.base.BaseTest;
import com.vgcares.automation.config.ConfigManager;
import com.vgcares.automation.driver.DriverManager;
import com.vgcares.automation.driver.MobileInfrastructureException;
import com.vgcares.automation.enums.UserRole;
import com.vgcares.automation.reports.ExtentReportManager;
import com.vgcares.automation.utils.ExecutionUtils;
import com.vgcares.automation.utils.ScreenshotUtils;
import com.vgcares.automation.utils.StepLogger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriverException;
import org.testng.IConfigurationListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestListener implements ITestListener, IConfigurationListener {
    private static final Logger log = LogManager.getLogger(TestListener.class);
    private static final String REPORT_TEST = TestListener.class.getName() + ".reportTest";
    private static final Pattern TC_PATTERN = Pattern.compile("TC_[A-Za-z0-9_\\-]+");

    @Override
    public void onStart(ITestContext context) {
        String suiteName = context.getSuite() != null ? context.getSuite().getName() : context.getName();
        ExecutionUtils.setActiveSuiteName(suiteName);
        ExtentReportManager.initReports(suiteName);
        System.out.println("\n##################################################");
        System.out.println("VG CARES AUTOMATION SUITE STARTED");
        System.out.println("EXECUTION ID : " + ExecutionUtils.getExecutionId());
        System.out.println("SUITE        : " + suiteName);
        System.out.println("MODE         : " + ExecutionUtils.getExecutionMode().name());
        System.out.println("DEVICE       : Physical Android");
        System.out.println("##################################################\n");
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flushReports();
        System.out.println("\n##################################################");
        System.out.println("SUITE FINISHED : " + context.getName());
        System.out.println("Passed: " + context.getPassedTests().size()
                + " | Failed: " + context.getFailedTests().size()
                + " | Skipped: " + context.getSkippedTests().size());
        System.out.println("Report: " + ExtentReportManager.getCurrentReportPath());
        System.out.println("##################################################\n");
    }

    @Override
    public void onTestStart(ITestResult result) {
        StepLogger.reset();
        String role = resolveRole(result).displayName();
        String suite = resolveSuiteLabel(result);
        String moduleName = resolveModule(result);
        String testCaseId = resolveTestCaseId(result);
        String scenario = resolveScenario(result);
        String priority = resolvePriority(result);

        ExtentTest test = report(result);
        test.assignCategory("Role:" + role);
        test.assignCategory("Module:" + moduleName);
        test.info("<b>Execution ID:</b> " + ExecutionUtils.getExecutionId()
                + " | <b>Role:</b> " + role
                + " | <b>Suite:</b> " + suite
                + " | <b>Priority:</b> " + priority
                + " | <b>Device:</b> Physical Android");
        StepLogger.setExtentTest(test);

        System.out.println("\n========================================");
        System.out.println(" VG CARES AUTOMATION EXECUTION");
        System.out.println("========================================");
        System.out.printf("ROLE         : %s%n", role);
        System.out.printf("SUITE        : %s%n", suite);
        System.out.printf("MODULE       : %s%n", moduleName);
        System.out.printf("TEST CASE ID : %s%n", testCaseId);
        System.out.printf("SCENARIO     : %s%n", scenario);
        System.out.printf("PRIORITY     : %s%n", priority);
        System.out.printf("DEVICE       : Physical Android%n");
        System.out.printf("STATUS       : RUNNING%n");

        log.info("[START] Role: {} | Test: {} | ID: {} | Scenario: {}", role, result.getName(), testCaseId, scenario);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        double durationSeconds = (result.getEndMillis() - result.getStartMillis()) / 1000.0;
        String testCaseId = resolveTestCaseId(result);
        String scenario = resolveScenario(result);

        System.out.println("\n----------------------------------------");
        System.out.printf("RESULT       : PASS%n");
        System.out.printf("ACTUAL       : %s — verified successfully%n", scenario);
        System.out.printf("DURATION     : %.2f seconds%n", durationSeconds);
        System.out.println("========================================\n");

        log.info("[PASS] {} (ID: {}) in {}s", result.getName(), testCaseId, String.format("%.2f", durationSeconds));
        report(result).pass("Execution passed in " + String.format("%.2f", durationSeconds) + "s");
        StepLogger.clearExtentTest();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        double durationSeconds = (result.getEndMillis() - result.getStartMillis()) / 1000.0;
        String testCaseId = resolveTestCaseId(result);
        Throwable throwable = result.getThrowable();
        String reason = safeFailure(throwable);
        String exceptionType = throwable != null ? throwable.getClass().getSimpleName() : "UnknownError";
        String defectCategory = classifyFailure(throwable);

        String screenshotPath = "N/A";
        if (DriverManager.hasActiveSession() && ConfigManager.getBoolean("captureScreenshots", true)) {
            try {
                screenshotPath = ScreenshotUtils.captureScreenshot(DriverManager.getDriver(), result.getName());
            } catch (WebDriverException e) {
                screenshotPath = "Capture Failed: " + e.getMessage();
            }
        }

        System.out.println("\n----------------------------------------");
        System.out.printf("RESULT       : FAIL (%s)%n", defectCategory);
        System.out.printf("ERROR        : %s%n", exceptionType);
        System.out.printf("REASON       : %s%n", reason);
        System.out.printf("EVIDENCE     : %s%n", screenshotPath);
        System.out.printf("DURATION     : %.2f seconds%n", durationSeconds);
        System.out.println("========================================\n");

        log.error("[FAIL] {} (ID: {}) | Category: {} | Error: {} | Reason: {}",
                result.getName(), testCaseId, defectCategory, exceptionType, reason);
        ExtentTest test = report(result);
        test.fail("<b>CATEGORY:</b> " + defectCategory
                + "<br><b>ERROR:</b> " + exceptionType
                + "<br><b>REASON:</b> " + reason);
        if (!screenshotPath.startsWith("N/A") && !screenshotPath.startsWith("Capture Failed")) {
            test.addScreenCaptureFromPath(screenshotPath);
        }
        StepLogger.clearExtentTest();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Object suiteFailure = result.getTestContext().getSuite().getAttribute(BaseTest.SUITE_SETUP_FAILURE);
        String statusLabel = suiteFailure != null ? "BLOCKED" : "SKIPPED";
        String reason;
        if (suiteFailure instanceof Throwable) {
            Throwable failure = (Throwable) suiteFailure;
            reason = "Blocked by suite setup: " + safeFailure(failure);
        } else {
            reason = "Skipped: " + safeFailure(result.getThrowable());
        }

        String testCaseId = resolveTestCaseId(result);
        System.out.println("\n----------------------------------------");
        System.out.printf("TEST CASE ID : %s%n", testCaseId);
        System.out.printf("RESULT       : %s%n", statusLabel);
        System.out.printf("REASON       : %s%n", reason);
        System.out.println("========================================\n");

        log.warn("[{}] {} (ID: {}) | Reason: {}", statusLabel, result.getName(), testCaseId, reason);
        report(result).skip("<b>" + statusLabel + ":</b> " + reason);
        StepLogger.clearExtentTest();
    }

    @Override
    public void onConfigurationFailure(ITestResult result) {
        String reason = safeFailure(result.getThrowable());
        log.error("TestNG configuration {} failed. {}", result.getName(), reason);
        report(result).assignCategory("Environment / Configuration").log(Status.FAIL, reason);
        ExtentReportManager.flushReports();
    }

    private ExtentTest report(ITestResult result) {
        Object existingObj = result.getAttribute(REPORT_TEST);
        if (existingObj instanceof ExtentTest) {
            return (ExtentTest) existingObj;
        }
        ExtentTest test = ExtentReportManager.createTest(result.getName(), result.getMethod().getDescription());
        test.assignCategory(resolveModule(result));
        result.setAttribute(REPORT_TEST, test);
        return test;
    }

    private UserRole resolveRole(ITestResult result) {
        String packageName = result.getMethod().getRealClass().getPackageName().toLowerCase();
        String[] groups = result.getMethod().getGroups();
        for (String group : groups) {
            if ("common".equalsIgnoreCase(group)) {
                return UserRole.COMMON;
            }
        }
        for (String group : groups) {
            if ("patient".equalsIgnoreCase(group)) {
                return UserRole.PATIENT;
            }
            if ("doctor".equalsIgnoreCase(group)) {
                return UserRole.DOCTOR;
            }
        }
        if (packageName.contains(".patient.")) {
            return UserRole.PATIENT;
        }
        if (packageName.contains(".doctor.") || packageName.contains(".slots")
                || packageName.contains(".affiliations") || packageName.contains(".surgery")
                || packageName.contains(".consultation") || packageName.contains(".reviews")
                || packageName.contains(".documents")) {
            return UserRole.DOCTOR;
        }
        return ExecutionUtils.getActiveRole();
    }

    private String resolveSuiteLabel(ITestResult result) {
        String suiteProp = ExecutionUtils.getActiveSuiteName();
        if (suiteProp != null && !suiteProp.isBlank() && !"Surefire test".equalsIgnoreCase(suiteProp)) {
            return suiteProp;
        }
        for (String group : result.getMethod().getGroups()) {
            if ("smoke".equalsIgnoreCase(group)) {
                return "SMOKE";
            }
        }
        return "REGRESSION";
    }

    private String resolveModule(ITestResult result) {
        String className = result.getMethod().getRealClass().getSimpleName();
        String packageName = result.getMethod().getRealClass().getPackageName();
        if (packageName.contains(".tests.")) {
            String sub = packageName.substring(packageName.indexOf(".tests.") + 7);
            if (!sub.isBlank()) {
                return sub.toUpperCase().replace('.', '/');
            }
        }
        return className.replace("Test", "").toUpperCase();
    }

    private String resolveTestCaseId(ITestResult result) {
        String desc = result.getMethod().getDescription();
        if (desc != null) {
            Matcher m = TC_PATTERN.matcher(desc);
            if (m.find()) {
                return m.group();
            }
        }
        return result.getMethod().getMethodName();
    }

    private String resolveScenario(ITestResult result) {
        String desc = result.getMethod().getDescription();
        if (desc != null && !desc.isBlank()) {
            int colonIndex = desc.indexOf(':');
            if (colonIndex > 0 && colonIndex < desc.length() - 1) {
                return desc.substring(colonIndex + 1).trim();
            }
            return desc.trim();
        }
        return result.getMethod().getMethodName();
    }

    private String resolvePriority(ITestResult result) {
        int priority = result.getMethod().getPriority();
        if (priority <= 1) return "P1 (Critical)";
        if (priority <= 3) return "P2 (High)";
        return "P3 (Medium)";
    }

    private String classifyFailure(Throwable failure) {
        if (failure instanceof MobileInfrastructureException) {
            return "Environment / Infrastructure Defect";
        }
        if (failure instanceof AssertionError) {
            return "Application / Functional Defect";
        }
        return "Framework / Automation Defect";
    }

    private String safeFailure(Throwable failure) {
        if (failure instanceof MobileInfrastructureException || failure instanceof org.testng.SkipException) {
            return failure.getMessage();
        }
        if (failure == null) {
            return "See TestNG results for the prerequisite.";
        }
        return failure.getMessage() != null ? failure.getMessage() : failure.toString();
    }
}
