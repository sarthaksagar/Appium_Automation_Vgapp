package com.vgcares.automation.tests.slots;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/** Starter skeleton for Slot Advanced Stress & UI tests (doctor/slots-data.json). */
public class SlotAdvancedStressAndUITest extends DoctorBaseTest {

    @Test(enabled = false, groups = {"doctor", "regression"},
          description = "TC_SLOT_UI_STARTER: Add Slot UI/Stress test steps here")
    public void starterSlotAdvancedUITemplate() {
        JsonNode data = ModuleDataLoader.doctor("slots-data.json");
        StepLogger.step("Loaded doctor/slots-data.json (" + data.path("module").asText() + ")");
    }
}
