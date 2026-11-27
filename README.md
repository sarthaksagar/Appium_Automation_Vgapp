# VGCares Mobile Automation Framework (Patient & Doctor)

Enterprise Appium mobile automation framework built with **Java 17, Appium 2.x, UiAutomator2, TestNG, Maven, ExtentReports 5, and Log4j2** for testing the VG Cares Medical Tourism Flutter Android application on a **physical Android device** over USB.

---

## Key Capabilities
- **Role-Based Architecture**: Supports **Common**, **Patient**, and **Doctor** workflows with clean role-boundary enforcement (`PatientBaseTest`, `DoctorBaseTest`, `PatientAuthWorkflow`, `DoctorAuthWorkflow`) so Patient and Doctor sessions never leak into one another on a single physical device.
- **Independent & Combined Suites**: Run Patient Smoke/Regression, Doctor Smoke/Regression, Combined Sequential Smoke/Regression, Common Smoke, Cross-Role E2E, or offline Framework Unit Tests.
- **Dual Execution Modes**:
  - **Learning Mode** (`execution.mode=learning`, `1000ms` visible action delay + step-by-step terminal banners).
  - **Standard Mode** (`execution.mode=standard`, condition-based explicit waits for fast CI/CD runs).
- **Zero Committed Secrets**: Credentials load securely from OS environment variables or an untracked local `.env` file (see [.env.example](.env.example)).
- **Interactive Windows Launcher**: Beginner-friendly menu at [scripts/run-tests.ps1](scripts/run-tests.ps1).

---

## Quick Start

1. Copy `.env.example` to `.env` and set your authorized QA accounts:
   - `QA_PATIENT_EMAIL`, `QA_PATIENT_PASSWORD`
   - `QA_DOCTOR_EMAIL`, `QA_DOCTOR_PASSWORD`
2. Connect your physical Android phone via USB with USB Debugging enabled (`adb devices -l`).
3. Run the interactive Windows launcher:
   ```powershell
   .\scripts\run-tests.ps1
   ```
   Or execute Maven directly:
   ```powershell
   # Offline unit tests (no device required)
   mvn clean test "-DsuiteXmlFile=src/test/resources/suites/unit/unit-tests.xml"

   # Role-specific & combined suites
   mvn clean test "-DsuiteXmlFile=src/test/resources/suites/patient/patient-smoke.xml"
   mvn clean test "-DsuiteXmlFile=src/test/resources/suites/doctor/doctor-smoke.xml"
   mvn clean test "-DsuiteXmlFile=src/test/resources/suites/combined/combined-smoke.xml"
   ```

---

## Documentation
- [Architecture Overview](docs/architecture.md)
- [Execution Guide](docs/execution-guide.md)
- [Test Traceability Matrix](docs/test-traceability.md)
