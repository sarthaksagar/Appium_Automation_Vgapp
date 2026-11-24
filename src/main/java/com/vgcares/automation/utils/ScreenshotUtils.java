package com.vgcares.automation.utils;

import com.vgcares.automation.constants.FrameworkConstants;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ScreenshotUtils {

    private static final Logger log = LogManager.getLogger(ScreenshotUtils.class);

    private ScreenshotUtils() {}

    /**
     * Captures screenshot and saves to disk, returning absolute path.
     */
    public static String captureScreenshot(AndroidDriver driver, String testName) {
        if (driver == null) {
            log.warn("Cannot capture screenshot because driver is null.");
            return "";
        }
        try {
            File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            String sanitizedTestName = testName.replaceAll("[^a-zA-Z0-9_-]", "_");
            String fileName = sanitizedTestName + "_" + timestamp + ".png";
            File destFile = new File(FrameworkConstants.SCREENSHOTS_DIR + File.separator + fileName);

            FileUtils.copyFile(srcFile, destFile);
            log.info("Saved failure screenshot to: {}", destFile.getAbsolutePath());
            return destFile.getAbsolutePath();
        } catch (IOException e) {
            log.error("Failed to save screenshot to disk: {}", e.getMessage(), e);
            return "";
        }
    }

    /**
     * Captures screenshot as Base64 string for direct embedding into ExtentReports.
     */
    public static String captureBase64(AndroidDriver driver) {
        if (driver == null) {
            return "";
        }
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
        } catch (Exception e) {
            log.warn("Failed to capture Base64 screenshot: {}", e.getMessage());
            return "";
        }
    }
}
