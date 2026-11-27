package com.vgcares.automation.tests.consultation;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/** Starter skeleton for Consultation Fees History & E2E tests (doctor/consultation-fees-data.json). */
public class ConsultationFeesHistoryAndE2ETest extends DoctorBaseTest {

    @Test(enabled = false, groups = {"doctor", "regression"},
          description = "TC_CF_HIST_STARTER: Add Consultation Fees History test steps here")
    public void starterConsultationFeesHistoryTemplate() {
        JsonNode data = ModuleDataLoader.doctor("consultation-fees-data.json");
        StepLogger.step("Loaded doctor/consultation-fees-data.json (" + data.path("module").asText() + ")");
    }
}
