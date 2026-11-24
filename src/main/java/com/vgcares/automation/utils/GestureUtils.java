package com.vgcares.automation.utils;

import io.appium.java_client.android.AndroidDriver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import java.time.Duration;
import java.util.Collections;

public final class GestureUtils {

    private static final Logger log = LogManager.getLogger(GestureUtils.class);

    private GestureUtils() {}

    /**
     * Performs a vertical swipe using standard W3C Actions.
     */
    public static void swipe(AndroidDriver driver, double startYPercent, double endYPercent) {
        try {
            Dimension size = driver.manage().window().getSize();
            int x = size.width / 2;
            int startY = (int) (size.height * startYPercent);
            int endY = (int) (size.height * endYPercent);

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence swipe = new Sequence(finger, 1);

            swipe.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, startY));
            swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            swipe.addAction(finger.createPointerMove(Duration.ofMillis(600), PointerInput.Origin.viewport(), x, endY));
            swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Collections.singletonList(swipe));
        } catch (Exception e) {
            log.warn("Swipe gesture failed: {}", e.getMessage());
        }
    }

    public static void scrollDown(AndroidDriver driver) {
        swipe(driver, 0.75, 0.25);
    }

    public static void scrollUp(AndroidDriver driver) {
        swipe(driver, 0.25, 0.75);
    }

    public static void swipeUp(AndroidDriver driver) {
        scrollDown(driver);
    }

    public static void swipeDown(AndroidDriver driver) {
        scrollUp(driver);
    }

    /**
     * Taps on an exact (x, y) screen coordinate.
     */
    public static void tapByCoordinates(AndroidDriver driver, int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);

        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(new Pause(finger, Duration.ofMillis(100)));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        driver.perform(Collections.singletonList(tap));
    }

    public static void tap(AndroidDriver driver, int x, int y) {
        tapByCoordinates(driver, x, y);
    }
}
