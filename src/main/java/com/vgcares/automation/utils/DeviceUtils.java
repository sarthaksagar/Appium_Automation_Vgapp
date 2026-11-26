package com.vgcares.automation.utils;

import com.vgcares.automation.config.ConfigManager;
import com.vgcares.automation.driver.MobileInfrastructureException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

public final class DeviceUtils {
    private static final Pattern ADB_DEVICE = Pattern.compile("^(\\S+)\\s+(\\S+)(?:\\s.*)?$");
    private static final Pattern KEYGUARD = Pattern.compile("(?m)^\\s*(?:showing|mIsShowing)=true\\s*$");
    private static final Pattern SCREEN_OFF = Pattern.compile("(?m)^\\s*screenState=SCREEN_STATE_OFF\\s*$");

    private DeviceUtils() {}

    public static final class AndroidDevice {
        private final String udid;
        private final String release;
        private final String model;
        private final String launchActivity;

        public AndroidDevice(String udid, String release, String model, String launchActivity) {
            this.udid = udid;
            this.release = release;
            this.model = model;
            this.launchActivity = launchActivity;
        }

        public String udid() { return udid; }
        public String release() { return release; }
        public String model() { return model; }
        public String launchActivity() { return launchActivity; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AndroidDevice that = (AndroidDevice) o;
            return java.util.Objects.equals(udid, that.udid) &&
                   java.util.Objects.equals(release, that.release) &&
                   java.util.Objects.equals(model, that.model) &&
                   java.util.Objects.equals(launchActivity, that.launchActivity);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(udid, release, model, launchActivity);
        }

        @Override
        public String toString() {
            return "AndroidDevice{" +
                    "udid='" + udid + '\'' +
                    ", release='" + release + '\'' +
                    ", model='" + model + '\'' +
                    ", launchActivity='" + launchActivity + '\'' +
                    '}';
        }
    }

    public static AndroidDevice requirePhysicalDevice(String configuredUdid, String appPackage, String appActivity) {
        String udid = selectPhysicalDevice(adb("devices", "-l"), configuredUdid);
        if ("1".equals(shell(udid, "getprop", "ro.kernel.qemu"))) {
            throw new MobileInfrastructureException("Physical device", "The selected device reports emulator hardware.");
        }
        String release = shell(udid, "getprop", "ro.build.version.release");
        String model = shell(udid, "getprop", "ro.product.model");
        if (release.isBlank() || model.isBlank()) {
            throw new MobileInfrastructureException("Physical device", "ADB did not report an Android release and model.");
        }
        if (shell(udid, "pm", "list", "packages", appPackage).lines().noneMatch(("package:" + appPackage)::equals)) {
            throw new MobileInfrastructureException("Driver configuration", "VG Cares is missing on the selected device: " + appPackage);
        }
        String resolution = shell(udid, "cmd", "package", "resolve-activity", "--brief",
                "-a", "android.intent.action.MAIN", "-c", "android.intent.category.LAUNCHER", appPackage);
        String launcher = resolution.lines().map(String::trim).filter(line -> line.startsWith(appPackage + "/"))
                .findFirst().orElseThrow(() -> new MobileInfrastructureException("Driver configuration",
                        "ADB could not resolve the launcher activity for " + appPackage));
        String resolvedActivity = qualifiedActivity(appPackage, launcher.substring(appPackage.length() + 1));
        if (!appActivity.isBlank() && !qualifiedActivity(appPackage, appActivity).equals(resolvedActivity)) {
            throw new MobileInfrastructureException("Driver configuration",
                    "Configured appActivity differs from the installed launcher " + launcher);
        }
        String windowPolicy = shell(udid, "dumpsys", "window", "policy");
        if (KEYGUARD.matcher(windowPolicy).find() || SCREEN_OFF.matcher(windowPolicy).find()) {
            try {
                shell(udid, "input", "keyevent", "KEYCODE_WAKEUP");
                shell(udid, "wm", "dismiss-keyguard");
                shell(udid, "input", "keyevent", "82");
                Thread.sleep(800);
            } catch (Exception ignored) {}
            windowPolicy = shell(udid, "dumpsys", "window", "policy");
            if (KEYGUARD.matcher(windowPolicy).find() || SCREEN_OFF.matcher(windowPolicy).find()) {
                throw new MobileInfrastructureException("Physical device", "Wake and unlock the USB Android device before running automation.");
            }
        }
        return new AndroidDevice(udid, release, model, resolvedActivity);
    }

    static String qualifiedActivity(String appPackage, String activity) {
        if (activity.startsWith(".")) {
            return appPackage + activity;
        }
        return activity.contains(".") ? activity : appPackage + "." + activity;
    }

    static String selectPhysicalDevice(String output, String configuredUdid) {
        List<String[]> devices = new ArrayList<>();
        for (String line : output.lines().map(String::trim).toList()) {
            if (line.startsWith("List of devices") || line.startsWith("*")) {
                continue;
            }
            var match = ADB_DEVICE.matcher(line);
            if (match.matches()) {
                devices.add(new String[] {match.group(1), match.group(2)});
            }
        }
        if (devices.isEmpty()) {
            throw new MobileInfrastructureException("Physical device", "No ADB devices detected. Connect a USB Android device with debugging enabled.");
        }
        boolean auto = "auto".equalsIgnoreCase(configuredUdid);
        if (auto && devices.size() != 1) {
            throw new MobileInfrastructureException("Physical device",
                    "Multiple ADB devices detected. Select the USB phone with VGCARES_UDID or -Dudid.");
        }
        String[] selected = auto ? devices.get(0) : devices.stream().filter(device -> device[0].equals(configuredUdid))
                .findFirst().orElseThrow(() -> new MobileInfrastructureException("Physical device",
                        "The configured UDID is absent from adb devices -l."));
        String serial = selected[0].toLowerCase(Locale.ROOT);
        if (serial.startsWith("emulator-") || serial.contains(":") || serial.contains("_adb-tls")) {
            throw new MobileInfrastructureException("Physical device", "Select a physical USB device from adb devices -l.");
        }
        if (!"device".equals(selected[1])) {
            throw new MobileInfrastructureException("Physical device", "Selected USB device state is " + selected[1]
                    + ". Check the cable and approve the USB debugging prompt on the phone.");
        }
        return selected[0];
    }

    private static String shell(String udid, String... args) {
        List<String> command = new ArrayList<>(List.of("-s", udid, "shell"));
        command.addAll(Arrays.asList(args));
        return adb(command.toArray(String[]::new));
    }

    private static String adb(String... args) {
        List<String> command = new ArrayList<>(List.of(adbExecutable()));
        command.addAll(Arrays.asList(args));
        long timeout = ConfigManager.getInt("adbTimeout", 10);
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(timeout);
        Process process = null;
        try {
            Process started = new ProcessBuilder(command).redirectErrorStream(true).start();
            process = started;
            FutureTask<String> output = new FutureTask<>(() -> {
                try (var stream = started.getInputStream()) {
                    return new String(stream.readAllBytes(), StandardCharsets.UTF_8).strip();
                }
            });
            Thread reader = new Thread(output, "adb-output");
            reader.setDaemon(true);
            reader.start();
            if (!started.waitFor(timeout, TimeUnit.SECONDS)) {
                throw new TimeoutException("ADB process exceeded " + timeout + " seconds");
            }
            String result = output.get(Math.max(0, deadline - System.nanoTime()), TimeUnit.NANOSECONDS);
            if (started.exitValue() != 0) {
                throw new MobileInfrastructureException("Physical device",
                        "ADB command failed with exit code " + started.exitValue()
                                + ". Check the SDK, ADB permissions, and device state.");
            }
            return result;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new MobileInfrastructureException("Physical device", "ADB command interrupted.", e);
        } catch (IOException | ExecutionException | TimeoutException e) {
            throw new MobileInfrastructureException("Physical device", "ADB command could not complete.", e);
        } finally {
            if (process != null && process.isAlive()) {
                process.destroyForcibly();
            }
        }
    }

    private static String adbExecutable() {
        String executable = System.getProperty("os.name").startsWith("Windows") ? "adb.exe" : "adb";
        for (String sdk : new String[] {System.getenv("ANDROID_HOME"), System.getenv("ANDROID_SDK_ROOT")}) {
            if (sdk != null && !sdk.isBlank()) {
                Path candidate = Path.of(sdk, "platform-tools", executable);
                if (Files.isRegularFile(candidate)) {
                    return candidate.toString();
                }
            }
        }
        String localAppData = System.getenv("LOCALAPPDATA");
        if (localAppData != null) {
            Path candidate = Path.of(localAppData, "Android", "Sdk", "platform-tools", executable);
            if (Files.isRegularFile(candidate)) {
                return candidate.toString();
            }
        }
        return executable;
    }
}
