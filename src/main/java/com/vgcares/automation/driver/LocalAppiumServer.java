package com.vgcares.automation.driver;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.ConnectException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.HashMap;

final class LocalAppiumServer {
    static final URI DEFAULT_URL = URI.create("http://127.0.0.1:4723");
    private static final Logger log = LogManager.getLogger(LocalAppiumServer.class);

    private LocalAppiumServer() {}

    static AppiumDriverLocalService ensureReady(URI server, Duration healthTimeout) {
        try {
            AppiumStatus.requireReady(server, healthTimeout);
            return null;
        } catch (MobileInfrastructureException e) {
            if (!DEFAULT_URL.equals(server) || !System.getProperty("os.name").startsWith("Windows")
                    || !(e.getCause() instanceof ConnectException)) {
                throw e;
            }
        }

        AppiumDriverLocalService service = null;
        try {
            var environment = new HashMap<>(System.getenv());
            Path userAppiumHome = Path.of(System.getProperty("user.home", ""), ".appium");
            Path projectAppiumHome = Path.of("temp", "appium-2-home").toAbsolutePath();
            if (environment.getOrDefault("APPIUM_HOME", "").isBlank()) {
                if (Files.isDirectory(userAppiumHome)) {
                    environment.put("APPIUM_HOME", userAppiumHome.toString());
                } else if (Files.isDirectory(projectAppiumHome)) {
                    environment.put("APPIUM_HOME", projectAppiumHome.toString());
                }
            }

            var builder = new AppiumServiceBuilder()
                    .withIPAddress(server.getHost())
                    .usingPort(server.getPort())
                    .withArgument(GeneralServerFlag.BASEPATH, "/")
                    .withArgument(GeneralServerFlag.LOG_LEVEL, "warn")
                    .withArgument(GeneralServerFlag.LOG_NO_COLORS)
                    .withEnvironment(environment)
                    .withTimeout(Duration.ofSeconds(60));
            if (environment.getOrDefault("APPIUM_PATH", "").isBlank() && environment.containsKey("APPDATA")) {
                Path appiumScript = Path.of(environment.get("APPDATA"), "npm", "node_modules", "appium", "index.js");
                if (Files.isRegularFile(appiumScript)) {
                    builder.withAppiumJS(appiumScript.toFile());
                }
            }

            service = builder.build();
            service.clearOutPutStreams();
            service.addLogMessageConsumer(log::warn);
            log.info("Starting local Appium at {}", server);
            service.start();
            AppiumStatus.requireReady(server, healthTimeout);
            return service;
        } catch (RuntimeException e) {
            var failure = new MobileInfrastructureException("Appium server",
                    "Could not start local Appium at " + server
                            + ". Check the Appium installation and logs/automation.log.", e);
            stop(service, failure);
            throw failure;
        }
    }

    static void stop(AppiumDriverLocalService service, RuntimeException failure) {
        if (service != null) {
            try {
                service.stop();
            } catch (RuntimeException e) {
                if (failure == null) {
                    throw e;
                }
                failure.addSuppressed(e);
            }
        }
    }
}
