package com.vgcares.automation.tests.affiliations;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/** Starter skeleton for Hospital Affiliations Extended Flow tests (doctor/affiliations-data.json). */
public class HospitalAffiliationsExtendedFlowTest extends DoctorBaseTest {

    @Test(enabled = false, groups = {"doctor", "regression"},
          description = "TC_HAF_EXT_STARTER: Add Hospital Affiliations Extended Flow test steps here")
    public void starterHospitalAffiliationsExtendedTemplate() {
        JsonNode data = ModuleDataLoader.doctor("affiliations-data.json");
        StepLogger.step("Loaded doctor/affiliations-data.json (" + data.path("module").asText() + ")");
    }
}
