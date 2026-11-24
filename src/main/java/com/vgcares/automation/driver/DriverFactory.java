package com.vgcares.automation.driver;

import com.vgcares.automation.config.ConfigManager;
import com.vgcares.automation.utils.DeviceUtils;
import com.vgcares.automation.utils.DeviceUtils.AndroidDevice;
import io.appium.java_client.AppiumClientConfig;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.time.Duration;
import java.util.function.BiConsumer;

public final class DriverFactory {
    private static final Logger log = LogManager.getLogger(DriverFactory.class);

    private DriverFactory() {}

    public static AndroidDriver createDriver() {
        return createDriver(AppiumStatus::requireReady);
    }

    static AndroidDriver createDriver(BiConsumer<URI, Duration> requireServer) {
        URI server = AppiumStatus.serverUri(ConfigManager.get("appiumServerUrl", LocalAppiumServer.DEFAULT_URL.toString()));
        String appPackage = ConfigManager.get("appPackage", "");
        String appActivity = ConfigManager.get("appActivity", "");
        if (!appPackage.matches("[A-Za-z][\\w]*(?:\\.[A-Za-z][\\w]*)+")
                || (!appActivity.isEmpty() && !appActivity.matches("\\.?[A-Za-z_$][\\w.$]*"))) {
            throw new MobileInfrastructureException("Driver configuration", "Invalid appPackage or appActivity.");
        }
        if (!"Android".equalsIgnoreCase(ConfigManager.get("platformName", "Android"))
                || !"UiAutomator2".equalsIgnoreCase(ConfigManager.get("automationName", "UiAutomator2"))) {
            throw new MobileInfrastructureException("Driver configuration", "This suite requires Android and UiAutomator2.");
        }
        if (!ConfigManager.getBoolean("noReset", true) || ConfigManager.getBoolean("fullReset", false)) {
            throw new MobileInfrastructureException("Driver configuration", "Physical device runs require noReset=true and fullReset=false.");
        }

        requireServer.accept(server, timeout("appiumHealthTimeout", 5));
        AndroidDevice device = DeviceUtils.requirePhysicalDevice(ConfigManager.get("udid", "auto"), appPackage, appActivity);
        UiAutomator2Options options = capabilities(device, appPackage);
        var client = AppiumClientConfig.defaultConfig().baseUri(server)
                .connectionTimeout(timeout("driverConnectTimeout", 10))
                .readTimeout(timeout("driverReadTimeout", 180));

        log.info("Starting UiAutomator2 on {} (Android {}) at {}", device.model(), device.release(), server);
        try {
            return new AndroidDriver(client, options);
        } catch (RuntimeException e) {
            throw new MobileInfrastructureException("Driver session",
                    "Could not create a UiAutomator2 session at " + server
                            + ". Check the Appium server log for the session startup error.", e);
        }
    }

    static UiAutomator2Options capabilities(AndroidDevice device, String appPackage) {
        UiAutomator2Options options = new UiAutomator2Options()
                .setUdid(device.udid())
                .setDeviceName(device.model())
                .setPlatformVersion(device.release())
                .setAppPackage(appPackage)
                .setAppActivity(device.launchActivity())
                .setAppWaitDuration(timeout("appWaitTimeout", 20))
                .setNoReset(true)
                .setFullReset(false)
                .setUiautomator2ServerInstallTimeout(timeout("driverInstallTimeout", 60))
                .setUiautomator2ServerLaunchTimeout(timeout("driverLaunchTimeout", 60))
                .setNewCommandTimeout(timeout("newCommandTimeout", 300));
        options.setAppWaitPackage(appPackage + ",com.android.permissioncontroller,"
                + "com.google.android.permissioncontroller,com.android.packageinstaller");
        options.setAppWaitActivity(device.launchActivity() + ",*.permission.ui.GrantPermissionsActivity");
        options.setCapability("appium:skipUnlock", true);
        options.setCapability("appium:forceAppLaunch", true);
        options.setCapability("appium:ignoreHiddenApiPolicyError", true);
        options.setCapability("appium:settings[waitForIdleTimeout]",
                ConfigManager.getInt("uiautomator2IdleTimeoutMillis", 1000));
        return options;
    }

    private static Duration timeout(String key, int defaultSeconds) {
        return Duration.ofSeconds(ConfigManager.getInt(key, defaultSeconds));
    }
}
