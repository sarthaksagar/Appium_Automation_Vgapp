package com.vgcares.automation.driver;

import com.vgcares.automation.config.ConfigManager;
import com.vgcares.automation.utils.DeviceUtils;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public final class DriverFactory {

    private static final Logger log = LogManager.getLogger(DriverFactory.class);

    private DriverFactory() {}

    public static AndroidDriver createDriver() {
        UiAutomator2Options options = new UiAutomator2Options();

        // 1. Resolve Physical Device UDID
        String configuredUdid = ConfigManager.get("udid", "auto");
        String finalUdid;
        if ("auto".equalsIgnoreCase(configuredUdid)) {
            finalUdid = DeviceUtils.getFirstConnectedDeviceUdid();
            if (finalUdid == null) {
                // Fallback to known physical device if auto-detection query had latency
                finalUdid = "GIPV9HBMLBHITO5H";
                log.info("Using fallback physical device UDID: {}", finalUdid);
            }
        } else {
            finalUdid = configuredUdid;
        }

        options.setPlatformName(ConfigManager.get("platformName", "Android"));
        options.setAutomationName(ConfigManager.get("automationName", "UiAutomator2"));
        options.setUdid(finalUdid);
        options.setDeviceName(ConfigManager.get("deviceName", "Real_Android_Device"));

        // Platform version
        String platformVersion = ConfigManager.get("platformVersion", "");
        if (platformVersion.isEmpty()) {
            platformVersion = DeviceUtils.getDeviceReleaseVersion(finalUdid);
        }
        options.setPlatformVersion(platformVersion);

        // Application Under Test
        String appPkg = ConfigManager.get("appPackage", "com.vg.vgcaresglobal.qa");
        String appAct = ConfigManager.get("appActivity", "com.vg.vgcaresglobal.MainActivity");
        options.setAppPackage(appPkg);
        options.setAppActivity(appAct);
        options.setAppWaitActivity("com.vg.vgcaresglobal.*,*.permission.ui.GrantPermissionsActivity");
        options.setAppWaitPackage(appPkg + ",com.android.permissioncontroller,"
            + "com.google.android.permissioncontroller,com.android.packageinstaller");
        options.setAppWaitDuration(Duration.ofSeconds(20));

        // Real Physical Device Settings
        options.setNoReset(ConfigManager.getBoolean("noReset", true));
        options.setFullReset(ConfigManager.getBoolean("fullReset", false));
        options.setAutoGrantPermissions(true);

        // Xiaomi / Redmi MIUI Stability Settings
        options.setCapability("waitForQuiescence", false);
        options.setCapability("ignoreHiddenApiPolicyError", true);

        int installTimeout    = ConfigManager.getInt("driverInstallTimeout", 60);
        int launchTimeout     = ConfigManager.getInt("driverLaunchTimeout",  60);
        int newCommandTimeout = ConfigManager.getInt("newCommandTimeout",     60);

        options.setUiautomator2ServerInstallTimeout(Duration.ofSeconds(installTimeout));
        options.setUiautomator2ServerLaunchTimeout(Duration.ofSeconds(launchTimeout));
        options.setNewCommandTimeout(Duration.ofSeconds(newCommandTimeout));

        String serverUrlStr = ConfigManager.get("appiumServerUrl", "http://127.0.0.1:4723/");
        log.info("Initializing AndroidDriver session for physical device [{}] on Appium server [{}]", finalUdid, serverUrlStr);

        try {
            URL serverUrl = new URL(serverUrlStr);
            AndroidDriver driver = new AndroidDriver(serverUrl, options);
            log.info("AndroidDriver initialized successfully. Session ID: {}", driver.getSessionId());
            return driver;
        } catch (MalformedURLException e) {
            log.error("Invalid Appium server URL: {}", serverUrlStr, e);
            throw new RuntimeException("Malformed Appium Server URL: " + serverUrlStr, e);
        } catch (Exception e) {
            log.error("Failed to initialize AndroidDriver on real device [{}]: {}", finalUdid, e.getMessage(), e);
            throw new RuntimeException("Could not create AndroidDriver session on device: " + finalUdid, e);
        }
    }
}
