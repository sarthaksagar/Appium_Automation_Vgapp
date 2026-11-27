package com.vgcares.automation.tests.surgery;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/** Starter skeleton for Surgery Requests List tests (doctor/surgery-requests-data.json). */
public class SurgeryRequestsListTest extends DoctorBaseTest {

    @Test(enabled = false, groups = {"doctor", "regression"},
          description = "TC_SR_LIST_STARTER: Add Surgery Requests List test steps here")
    public void starterSurgeryRequestsListTemplate() {
        JsonNode data = ModuleDataLoader.doctor("surgery-requests-data.json");
        StepLogger.step("Loaded doctor/surgery-requests-data.json (" + data.path("module").asText() + ")");
        // TODO: Use ensureDoctorSession().openSurgeryRequests() and SurgeryRequestsPage
    }
}
