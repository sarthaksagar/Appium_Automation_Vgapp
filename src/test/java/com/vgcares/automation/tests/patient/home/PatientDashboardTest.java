package com.vgcares.automation.tests.patient.home;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.PatientBaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/**
 * Starter skeleton for Patient Dashboard tests.
 * Populate module test data in src/test/resources/testdata/patient/patient-home-data.json.
 */
public class PatientDashboardTest extends PatientBaseTest {

    @Test(enabled = false, groups = {"patient", "smoke", "regression"},
          description = "TC_PAT_HOME_STARTER: Add Patient Dashboard test steps here")
    public void starterPatientDashboardTemplate() {
        JsonNode data = ModuleDataLoader.patient("patient-home-data.json");
        StepLogger.step("Loaded patient/patient-home-data.json (" + data.path("module").asText() + ")");
        // TODO: Use loginAsQaPatient() and PatientHomePage to add Patient Dashboard assertions
    }
}
