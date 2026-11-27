package com.vgcares.automation.unit;

import com.vgcares.automation.config.ConfigManager;
import com.vgcares.automation.driver.DriverManager;
import com.vgcares.automation.enums.ExecutionMode;
import com.vgcares.automation.enums.UserRole;
import com.vgcares.automation.testdata.ModuleDataLoader;
import com.vgcares.automation.testdata.doctor.DoctorTestData;
import com.vgcares.automation.testdata.patient.PatientTestData;
import com.vgcares.automation.utils.ExecutionUtils;
import org.testng.annotations.Test;

import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class FrameworkArchitectureUnitTest {

    @Test
    public void selectsExecutionModeAndCentralizesDemonstrationDelay() {
        assertEquals(ExecutionMode.fromString("learning"), ExecutionMode.LEARNING);
        assertEquals(ExecutionMode.fromString("STANDARD"), ExecutionMode.STANDARD);
        assertEquals(ExecutionMode.fromString(null), ExecutionMode.LEARNING);
        expectThrows(IllegalArgumentException.class, () -> ExecutionMode.fromString("sprint"));

        String prev = System.getProperty("execution.mode");
        try {
            System.setProperty("execution.mode", "standard");
            assertFalse(ExecutionUtils.isLearningMode());
            assertEquals(ExecutionUtils.getActionDelayMillis(), 0);

            System.setProperty("execution.mode", "learning");
            assertTrue(ExecutionUtils.isLearningMode());
            assertTrue(ExecutionUtils.getActionDelayMillis() > 0);
        } finally {
            if (prev == null) {
                System.clearProperty("execution.mode");
            } else {
                System.setProperty("execution.mode", prev);
            }
        }
    }

    @Test
    public void tracksUserRoleAndRedactsSecrets() {
        UserRole original = ExecutionUtils.getActiveRole();
        try {
            ExecutionUtils.setActiveRole(UserRole.PATIENT);
            assertEquals(ExecutionUtils.getActiveRole(), UserRole.PATIENT);
            assertEquals(ExecutionUtils.getActiveRole().displayName(), "PATIENT");

            ExecutionUtils.setActiveRole(UserRole.DOCTOR);
            assertEquals(ExecutionUtils.getActiveRole(), UserRole.DOCTOR);
            assertEquals(DriverManager.getAuthenticatedRole(), UserRole.COMMON);
        } finally {
            ExecutionUtils.setActiveRole(original);
        }

        assertEquals(ExecutionUtils.maskSecret("patient.qa@example.com"), "pa***@example.com");
        assertEquals(ExecutionUtils.maskSecret("MySecretPass!9"), "********");
        assertEquals(ExecutionUtils.maskSecret(" "), "[unset]");
    }

    @Test
    public void loadsRoleSpecificTestDataAndDotEnvFixtures() throws Exception {
        assertEquals(ModuleDataLoader.common("launch-data.json").path("module").asText(), "common/launch");
        assertEquals(PatientTestData.loadModule("signup-data.json").path("role").asText(), "PATIENT");
        assertEquals(DoctorTestData.loadModule("slots-data.json").path("role").asText(), "DOCTOR");
        assertTrue(DoctorTestData.loadModule("profile-data.json").path("testData").isObject());

        Path tempEnv = Files.createTempFile("vgcares-unit", ".env");
        try {
            Files.writeString(tempEnv, "# comment\nQA_PATIENT_EMAIL='pat@example.invalid'\nQA_DOCTOR_EMAIL=\"doc@example.invalid\"\n",
                    StandardCharsets.UTF_8);
            Map<String, String> parsed = ConfigManager.loadDotEnv(tempEnv);
            assertEquals(parsed.get("QA_PATIENT_EMAIL"), "pat@example.invalid");
            assertEquals(parsed.get("QA_DOCTOR_EMAIL"), "doc@example.invalid");
        } finally {
            Files.deleteIfExists(tempEnv);
        }
    }

    @Test
    public void validatesAllRoleCombinedE2eAndUnitSuitesExistWithoutSprintReferences() throws Exception {
        List<String> expectedSuites = List.of(
                "src/test/resources/suites/common/common-smoke.xml",
                "src/test/resources/suites/patient/patient-smoke.xml",
                "src/test/resources/suites/patient/patient-regression.xml",
                "src/test/resources/suites/doctor/doctor-smoke.xml",
                "src/test/resources/suites/doctor/doctor-regression.xml",
                "src/test/resources/suites/doctor/doctor-auth-complete.xml",
                "src/test/resources/suites/combined/combined-smoke.xml",
                "src/test/resources/suites/combined/combined-regression.xml",
                "src/test/resources/suites/e2e/cross-role-e2e.xml",
                "src/test/resources/suites/unit/unit-tests.xml"
        );
        var builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        builder.setEntityResolver((publicId, systemId) -> new org.xml.sax.InputSource(new java.io.StringReader("")));
        for (String suitePath : expectedSuites) {
            Path path = Path.of(suitePath);
            assertTrue(Files.isRegularFile(path), "Missing required TestNG suite: " + suitePath);
            String xml = Files.readString(path, StandardCharsets.UTF_8);
            assertFalse(xml.toLowerCase().contains("sprint"), "Sprint Testing must not appear in " + suitePath);
            builder.parse(path.toFile());
        }
    }
}
