package com.vgcares.automation.driver;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

final class AppiumStatus {
    private static final ObjectMapper JSON = new ObjectMapper();

    private AppiumStatus() {}

    static URI serverUri(String configuredUrl) {
        URI uri;
        try {
            uri = URI.create(configuredUrl);
        } catch (IllegalArgumentException e) {
            throw new MobileInfrastructureException("Driver configuration", "Invalid appiumServerUrl syntax");
        }
        if ((!"http".equalsIgnoreCase(uri.getScheme()) && !"https".equalsIgnoreCase(uri.getScheme()))
                || uri.getHost() == null || uri.getUserInfo() != null || uri.getQuery() != null
                || uri.getFragment() != null || uri.getPort() == 0 || uri.getPort() > 65535) {
            throw new MobileInfrastructureException("Driver configuration",
                    "appiumServerUrl must be an HTTP(S) server URL with an optional base path");
        }
        return URI.create(uri.toString().replaceFirst("/+$", ""));
    }

    static URI statusUri(URI server) {
        return URI.create(server + "/status");
    }

    static void requireReady(URI server, Duration timeout) {
        URI status = statusUri(server);
        HttpClient client = HttpClient.newBuilder().connectTimeout(timeout).build();
        HttpRequest request = HttpRequest.newBuilder(status).timeout(timeout).GET().build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new MobileInfrastructureException("Appium server", "GET " + status + " returned HTTP "
                        + response.statusCode() + ". Verify the Appium port and base path.");
            }
            JsonNode payload = JSON.readTree(response.body());
            if (payload == null || !payload.path("value").path("ready").isBoolean()
                    || !payload.path("value").path("ready").booleanValue()) {
                throw new MobileInfrastructureException("Appium server",
                        "GET " + status + " did not report value.ready=true.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new MobileInfrastructureException("Appium server", "Appium status check was interrupted.", e);
        } catch (IOException e) {
            throw new MobileInfrastructureException("Appium server",
                    "Could not verify readiness at " + status + ". Check that Appium is running at this URL.", e);
        }
    }
}
