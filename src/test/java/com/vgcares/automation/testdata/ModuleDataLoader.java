package com.vgcares.automation.testdata;

import com.fasterxml.jackson.databind.JsonNode;
import com.vgcares.automation.constants.FrameworkConstants;
import com.vgcares.automation.utils.JsonUtils;

import java.io.File;

/**
 * Loads module-wise JSON test data templates for Common, Patient, and Doctor modules.
 * Dynamic login credentials are always loaded separately from .env / environment via QaCredentials.
 */
public final class ModuleDataLoader {

    private ModuleDataLoader() {}

    public static JsonNode common(String fileName) {
        return JsonUtils.readJsonFile(FrameworkConstants.COMMON_TEST_DATA_DIR + File.separator + fileName);
    }

    public static JsonNode patient(String fileName) {
        return JsonUtils.readJsonFile(FrameworkConstants.PATIENT_TEST_DATA_DIR + File.separator + fileName);
    }

    public static JsonNode doctor(String fileName) {
        return JsonUtils.readJsonFile(FrameworkConstants.DOCTOR_TEST_DATA_DIR + File.separator + fileName);
    }
}
