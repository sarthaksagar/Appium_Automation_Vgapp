package com.vgcares.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class DeviceUtils {

    private static final Logger log = LogManager.getLogger(DeviceUtils.class);

    private DeviceUtils() {}

    /**
     * Resolves the path to the adb executable on Windows or unix.
     */
    public static String getAdbExecutable() {
        String androidHome = System.getenv("ANDROID_HOME");
        if (androidHome == null || androidHome.trim().isEmpty()) {
            androidHome = System.getenv("ANDROID_SDK_ROOT");
        }
        if (androidHome == null || androidHome.trim().isEmpty()) {
            // Default Windows fallback location
            String userHome = System.getProperty("user.home");
            String defaultSdk = userHome + File.separator + "AppData" + File.separator 
                    + "Local" + File.separator + "Android" + File.separator + "Sdk";
            if (new File(defaultSdk).exists()) {
                androidHome = defaultSdk;
            }
        }

        if (androidHome != null) {
            File adbFile = new File(androidHome + File.separator + "platform-tools" + File.separator + "adb.exe");
            if (adbFile.exists()) {
                return adbFile.getAbsolutePath();
            }
            File adbUnix = new File(androidHome + File.separator + "platform-tools" + File.separator + "adb");
            if (adbUnix.exists()) {
                return adbUnix.getAbsolutePath();
            }
        }
        // Fallback to adb in system PATH
        return "adb";
    }

    /**
     * Programmatically detects connected authorized physical Android devices via ADB.
     */
    public static List<String> getConnectedDeviceUdids() {
        List<String> devices = new ArrayList<>();
        String adb = getAdbExecutable();
        try {
            Process process = new ProcessBuilder(adb, "devices").start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.endsWith("device") && !line.startsWith("List of devices")) {
                        String udid = line.split("\\s+")[0];
                        devices.add(udid);
                    }
                }
            }
            process.waitFor();
        } catch (Exception e) {
            log.warn("Failed to query connected ADB devices: {}", e.getMessage());
        }
        return devices;
    }

    /**
     * Returns the first connected authorized physical device UDID, or null if none found.
     */
    public static String getFirstConnectedDeviceUdid() {
        List<String> devices = getConnectedDeviceUdids();
        if (!devices.isEmpty()) {
            String udid = devices.get(0);
            log.info("Detected connected Android physical device: {}", udid);
            return udid;
        }
        log.warn("No authorized physical Android device detected via adb devices.");
        return null;
    }

    /**
     * Reads the Android OS release version (e.g. "13") from the connected device.
     */
    public static String getDeviceReleaseVersion(String udid) {
        String adb = getAdbExecutable();
        try {
            ProcessBuilder pb = (udid != null && !udid.isEmpty())
                    ? new ProcessBuilder(adb, "-s", udid, "shell", "getprop", "ro.build.version.release")
                    : new ProcessBuilder(adb, "shell", "getprop", "ro.build.version.release");
            Process process = pb.start();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String version = reader.readLine();
                if (version != null && !version.trim().isEmpty()) {
                    return version.trim();
                }
            }
            process.waitFor();
        } catch (Exception e) {
            log.warn("Could not read Android release version for device {}: {}", udid, e.getMessage());
        }
        return "13";
    }
}
