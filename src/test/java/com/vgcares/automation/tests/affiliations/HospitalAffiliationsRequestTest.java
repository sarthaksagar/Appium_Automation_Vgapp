package com.vgcares.automation.tests.affiliations;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/** Starter skeleton for Hospital Affiliations Request tests (doctor/affiliations-data.json). */
public class HospitalAffiliationsRequestTest extends DoctorBaseTest {

    @Test(enabled = false, groups = {"doctor", "regression"},
          description = "TC_HAF_REQ_STARTER: Add Hospital Affiliations Request test steps here")
    public void starterHospitalAffiliationsRequestTemplate() {
        JsonNode data = ModuleDataLoader.doctor("affiliations-data.json");
        StepLogger.step("Loaded doctor/affiliations-data.json (" + data.path("module").asText() + ")");
    }
}
