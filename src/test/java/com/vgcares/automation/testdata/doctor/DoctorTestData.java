package com.vgcares.automation.testdata.doctor;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.testdata.ModuleDataLoader;

/**
 * Clean Doctor test data accessor delegating to module-wise JSON templates
 * under src/test/resources/testdata/doctor/.
 */
public final class DoctorTestData {

    private DoctorTestData() {}

    public static JsonNode loadModule(String fileName) {
        return ModuleDataLoader.doctor(fileName);
    }
}
