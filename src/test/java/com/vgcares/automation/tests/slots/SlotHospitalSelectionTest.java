package com.vgcares.automation.tests.slots;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/** Starter skeleton for Slot Hospital Selection tests (doctor/slots-data.json). */
public class SlotHospitalSelectionTest extends DoctorBaseTest {

    @Test(enabled = false, groups = {"doctor", "regression"},
          description = "TC_SLOT_HOSP_STARTER: Add Slot Hospital Selection test steps here")
    public void starterSlotHospitalSelectionTemplate() {
        JsonNode data = ModuleDataLoader.doctor("slots-data.json");
        StepLogger.step("Loaded doctor/slots-data.json (" + data.path("module").asText() + ")");
    }
}
