package com.vgcares.automation.tests.slots;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/** Starter skeleton for Slot Empty State tests (doctor/slots-data.json). */
public class SlotEmptyStateTest extends DoctorBaseTest {

    @Test(enabled = false, groups = {"doctor", "regression"},
          description = "TC_SLOT_EMPTY_STARTER: Add Slot Empty State test steps here")
    public void starterSlotEmptyStateTemplate() {
        JsonNode data = ModuleDataLoader.doctor("slots-data.json");
        StepLogger.step("Loaded doctor/slots-data.json (" + data.path("module").asText() + ")");
    }
}
