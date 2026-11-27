package com.vgcares.automation.tests.notifications;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/**
 * Starter skeleton for Notifications tests.
 * Populate module test data in src/test/resources/testdata/doctor/notifications-data.json.
 */
public class NotificationBellTest extends DoctorBaseTest {

    @Test(enabled = false, groups = {"doctor", "regression"},
          description = "TC_NB_STARTER: Add Notifications test steps here")
    public void starterNotificationBellTemplate() {
        JsonNode data = ModuleDataLoader.doctor("notifications-data.json");
        StepLogger.step("Loaded doctor/notifications-data.json (" + data.path("module").asText() + ")");
        // TODO: Use ensureDoctorSession().openNotifications() to add Notifications tests
    }
}
