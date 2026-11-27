package com.vgcares.automation.tests.messages;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/**
 * Starter skeleton for Messages tests.
 * Populate module test data in src/test/resources/testdata/doctor/messages-data.json.
 */
public class MessagesNegativeTest extends DoctorBaseTest {

    @Test(enabled = false, groups = {"doctor", "regression"},
          description = "TC_MSG_STARTER: Add Messages test steps here")
    public void starterMessagesTemplate() {
        JsonNode data = ModuleDataLoader.doctor("messages-data.json");
        StepLogger.step("Loaded doctor/messages-data.json (" + data.path("module").asText() + ")");
        // TODO: Use ensureDoctorSession().tapNavigationTab("messages") and new MessagesPage(getDriver())
    }
}
