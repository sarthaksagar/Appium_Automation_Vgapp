package com.vgcares.automation.driver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.vgcares.automation.pages.LaunchPage;
import com.vgcares.automation.utils.DeviceUtils.AndroidDevice;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.*;

public class DriverInfrastructureUnitTest {
    @Test
    public void validatesServerUrlAndRetainsBasePath() {
        assertEquals(AppiumStatus.statusUri(AppiumStatus.serverUri("http://127.0.0.1:4723/wd/hub/")).toString(),
                "http://127.0.0.1:4723/wd/hub/status");
        for (String url : new String[] {"localhost:4723", "ftp://host", "http://user:pass@host", "http://host?bad=1",
                "http://host:99999", "http://host/#anchor"}) {
            expectThrows(MobileInfrastructureException.class, () -> AppiumStatus.serverUri(url));
        }
    }

    @Test
    public void requiresAppiumReadinessAndPreservesConnectionCause() throws IOException {
        HttpServer stub = newServer();
        stub.createContext("/wd/hub/status", exchange -> respond(exchange, 200, "{\"value\":{\"ready\":true}}"));
        stub.createContext("/busy/status", exchange -> respond(exchange, 200, "{\"value\":{\"ready\":false}}"));
        stub.createContext("/other/status", exchange -> respond(exchange, 200, "{\"ready\":true}"));
        stub.start();
        URI server = address(stub);
        try {
            AppiumStatus.requireReady(server.resolve("/wd/hub"), Duration.ofSeconds(2));
            assertNull(LocalAppiumServer.ensureReady(server.resolve("/wd/hub"), Duration.ofSeconds(2)),
                    "A ready Appium endpoint should be reused without owning a server process.");
            expectThrows(MobileInfrastructureException.class,
                    () -> AppiumStatus.requireReady(server.resolve("/busy"), Duration.ofSeconds(2)));
            expectThrows(MobileInfrastructureException.class,
                    () -> AppiumStatus.requireReady(server.resolve("/other"), Duration.ofSeconds(2)));
            expectThrows(MobileInfrastructureException.class,
                    () -> AppiumStatus.requireReady(server, Duration.ofSeconds(2)));
        } finally {
            stub.stop(0);
        }
        assertNotNull(expectThrows(MobileInfrastructureException.class,
                () -> LocalAppiumServer.ensureReady(server, Duration.ofSeconds(2))).getCause());
    }

    @Test
    public void buildsCapabilitiesFromTheSelectedDeviceAndPreservesAppData() {
        var caps = DriverFactory.capabilities(new AndroidDevice("USB-TEST", "14", "QA phone", "example.Main"), "example.app");
        assertEquals(caps.getCapability("platformName").toString(), "ANDROID");
        assertEquals(caps.getCapability("appium:automationName"), "UIAutomator2");
        assertEquals(caps.getCapability("appium:udid"), "USB-TEST");
        assertEquals(caps.getCapability("appium:platformVersion"), "14");
        assertEquals(caps.getCapability("appium:appPackage"), "example.app");
        assertEquals(caps.getCapability("appium:appActivity"), "example.Main");
        assertEquals(caps.getCapability("appium:noReset"), true);
        assertEquals(caps.getCapability("appium:fullReset"), false);
        assertEquals(caps.getCapability("appium:skipUnlock"), true);
    }

    @Test
    public void checksTheLaunchScreenAgainAfterAllowingAndroidPermission() throws Exception {
        HttpServer stub = newServer();
        var json = new ObjectMapper();
        AtomicBoolean permissionAccepted = new AtomicBoolean();
        AtomicInteger permissionChecks = new AtomicInteger();
        stub.createContext("/session", exchange -> {
            String path = exchange.getRequestURI().getPath();
            String payload;
            if ("/session".equals(path)) {
                payload = "{\"sessionId\":\"test-session\",\"capabilities\":{\"platformName\":\"Android\"}}";
            } else if (path.endsWith("/elements")) {
                String locator = json.readTree(exchange.getRequestBody()).path("value").asText();
                if (locator.contains("permissioncontroller:id/permission_allow_button")) {
                    permissionChecks.incrementAndGet();
                    payload = permissionAccepted.get() ? "[]" : elements("permission");
                } else {
                    payload = locator.contains("Choose Your Language") && permissionChecks.get() >= 2
                            ? elements("language") : "[]";
                }
            } else if (path.endsWith("/displayed")) {
                payload = "true";
            } else {
                if (path.endsWith("/permission/click")) {
                    permissionAccepted.set(true);
                }
                payload = "null";
            }
            respond(exchange, 200, "{\"value\":" + payload + "}");
        });
        stub.start();
        try {
            AndroidDriver driver = new AndroidDriver(address(stub).toURL(), new UiAutomator2Options());
            try {
                assertEquals(new LaunchPage(driver).waitForAuthEntryScreen(), LaunchPage.Screen.LANGUAGE);
                assertTrue(permissionAccepted.get());
            } finally {
                driver.quit();
            }
        } finally {
            stub.stop(0);
        }
    }

    @Test
    public void reservesOneSessionAndCleansUpAfterSuccessOrFailure() throws Exception {
        RuntimeException original = new IllegalStateException("fixture initialization error");
        assertSame(expectThrows(IllegalStateException.class, () -> DriverManager.startSession(() -> { throw original; })), original);
        assertFalse(DriverManager.hasActiveSession());
        assertThrows(MobileInfrastructureException.class, DriverManager::getDriver);
        DriverManager.quitDriver();

        HttpServer stub = newServer();
        AtomicInteger starts = new AtomicInteger();
        AtomicInteger ends = new AtomicInteger();
        stub.createContext("/session", exchange -> {
            if ("POST".equals(exchange.getRequestMethod())) {
                exchange.getRequestBody().readAllBytes();
                starts.incrementAndGet();
                respond(exchange, 200, "{\"value\":{\"sessionId\":\"test-session\",\"capabilities\":{\"platformName\":\"Android\"}}}");
            } else {
                ends.incrementAndGet();
                respond(exchange, 200, "{\"value\":null}");
            }
        });
        stub.start();
        try {
            var endpoint = address(stub).toURL();
            DriverManager.startSession(() -> new AndroidDriver(endpoint, new UiAutomator2Options()));
            assertTrue(DriverManager.hasActiveSession());
            assertEquals(DriverManager.getDriver().getSessionId().toString(), "test-session");
            expectThrows(MobileInfrastructureException.class, () -> DriverManager.startSession(() -> null));
        } finally {
            try {
                DriverManager.quitDriver();
            } finally {
                stub.stop(0);
            }
        }
        assertFalse(DriverManager.hasActiveSession());
        assertEquals(starts.get(), 1);
        assertEquals(ends.get(), 1);
    }

    private static HttpServer newServer() throws IOException {
        return HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
    }

    private static String elements(String id) {
        return "[{\"element-6066-11e4-a52e-4f735466cecf\":\"" + id + "\"}]";
    }

    private static URI address(HttpServer server) {
        return URI.create("http://127.0.0.1:" + server.getAddress().getPort());
    }

    private static void respond(HttpExchange exchange, int status, String payload) throws IOException {
        byte[] body = payload.getBytes(StandardCharsets.UTF_8);
        try (exchange) {
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=utf-8");
            exchange.sendResponseHeaders(status, body.length);
            exchange.getResponseBody().write(body);
        }
    }
}
