package com.vgcares.automation.tests.consultation;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/** Starter skeleton for Consultation Fees tests (doctor/consultation-fees-data.json). */
public class ConsultationFeesTest extends DoctorBaseTest {

    @Test(enabled = false, groups = {"doctor", "regression"},
          description = "TC_CF_STARTER: Add Consultation Fees test steps here")
    public void starterConsultationFeesTemplate() {
        JsonNode data = ModuleDataLoader.doctor("consultation-fees-data.json");
        StepLogger.step("Loaded doctor/consultation-fees-data.json (" + data.path("module").asText() + ")");
        // TODO: Use ensureDoctorSession().openConsultationFees() and ConsultationFeesPage
    }
}
