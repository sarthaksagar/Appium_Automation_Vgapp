package com.vgcares.automation.testdata.patient;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.testdata.ModuleDataLoader;

/**
 * Clean Patient test data accessor delegating to module-wise JSON templates
 * under src/test/resources/testdata/patient/.
 */
public final class PatientTestData {

    private PatientTestData() {}

    public static JsonNode loadModule(String fileName) {
        return ModuleDataLoader.patient(fileName);
    }
}
