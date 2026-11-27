package com.vgcares.automation.tests.documents;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/** Starter skeleton for Documents Cards & Lifecycle tests (doctor/documents-data.json). */
public class DocumentsCardsAndLifecycleTest extends DoctorBaseTest {

    @Test(enabled = false, groups = {"doctor", "regression"},
          description = "TC_DOC_LIFECYCLE_STARTER: Add Documents Cards & Lifecycle test steps here")
    public void starterDocumentsLifecycleTemplate() {
        JsonNode data = ModuleDataLoader.doctor("documents-data.json");
        StepLogger.step("Loaded doctor/documents-data.json (" + data.path("module").asText() + ")");
    }
}
