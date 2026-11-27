package com.vgcares.automation.tests.documents;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.base.DoctorBaseTest;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.utils.StepLogger;
import org.testng.annotations.Test;

/** Starter skeleton for Documents Upload & Validation tests (doctor/documents-data.json). */
public class DocumentsUploadAndValidationTest extends DoctorBaseTest {

    @Test(enabled = false, groups = {"doctor", "regression"},
          description = "TC_DOC_UP_STARTER: Add Documents Upload test steps here")
    public void starterDocumentsUploadTemplate() {
        JsonNode data = ModuleDataLoader.doctor("documents-data.json");
        StepLogger.step("Loaded doctor/documents-data.json (" + data.path("module").asText() + ")");
        // TODO: Use ensureDoctorSession().openDocuments() and DocumentsPage
    }
}
