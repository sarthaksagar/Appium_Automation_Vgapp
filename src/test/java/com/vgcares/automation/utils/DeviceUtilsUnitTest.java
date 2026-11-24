package com.vgcares.automation.utils;

import com.vgcares.automation.driver.MobileInfrastructureException;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class DeviceUtilsUnitTest {
    private static final String HEADER = "List of devices attached\n";

    @Test
    public void selectsOneUsbDeviceAndNormalizesLauncherActivity() {
        assertEquals(DeviceUtils.selectPhysicalDevice(HEADER + "USB-ONE\tdevice model:Fixture", "auto"), "USB-ONE");
        assertEquals(DeviceUtils.qualifiedActivity("example.app", ".Main"), "example.app.Main");
        assertEquals(DeviceUtils.qualifiedActivity("example.app", "other.app.Main"), "other.app.Main");
    }

    @Test
    public void requiresSelectionWithMultipleDevices() {
        String listing = HEADER + "USB-ONE\tdevice\nemulator-5554\tdevice\n";
        expectThrows(MobileInfrastructureException.class, () -> DeviceUtils.selectPhysicalDevice(listing, "auto"));
        assertEquals(DeviceUtils.selectPhysicalDevice(listing, "USB-ONE"), "USB-ONE");
        expectThrows(MobileInfrastructureException.class, () -> DeviceUtils.selectPhysicalDevice(listing, "USB-TWO"));
        expectThrows(MobileInfrastructureException.class, () -> DeviceUtils.selectPhysicalDevice(listing, "emulator-5554"));
    }

    @Test
    public void rejectsMissingUnauthorizedOfflineWifiAndEmulatorDevices() {
        for (String row : new String[] {"", "USB-ONE unauthorized", "USB-ONE offline",
                "192.0.2.5:5555 device", "adb-123._adb-tls-connect._tcp device", "emulator-5554 device"}) {
            expectThrows(MobileInfrastructureException.class,
                    () -> DeviceUtils.selectPhysicalDevice(HEADER + row, "auto"));
        }
    }
}
