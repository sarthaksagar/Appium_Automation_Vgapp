# VGCares Execution Guide (Physical Android Device)

## 1. Prerequisites

1. **Physical Android Phone**: Connected via USB with **USB Debugging** enabled, screen unlocked, and authorized (`adb devices -l` lists your device as `device`, not `unauthorized` or `emulator-*`).
2. **Appium 2.x Server**: UiAutomator2 installed (`appium driver list --installed`).
3. **Role Credentials (Never Commit Real Secrets)**:
   Copy `.env.example` to `.env` (gitignored) or set OS environment variables:
   - `QA_PATIENT_EMAIL` & `QA_PATIENT_PASSWORD`
   - `QA_DOCTOR_EMAIL` & `QA_DOCTOR_PASSWORD`

## 2. Interactive Windows Menu (`scripts/run-tests.ps1`)

Run from PowerShell in `D:\My_own_project\Appium\`:

```powershell
.\scripts\run-tests.ps1
```

Select:
1. **User Role**: `1. Patient`, `2. Doctor`, or `3. Patient and Doctor`
2. **Test Suite**: `1. Smoke Testing` or `2. Regression Testing`
3. **Execution Mode**: `1. Learning Mode` or `2. Standard Mode`

The script automatically checks credentials, verifies the physical Android device via ADB, starts Appium if needed (`scripts/start-appium.ps1`), executes the matching Maven command, preserves Maven's exit code, and prints the report path under `target/reports/`.

## 3. Direct Maven Execution Commands

### Framework Unit Tests (Offline — No Device Needed)
```powershell
mvn clean test "-DsuiteXmlFile=src/test/resources/suites/unit/unit-tests.xml"
```

### Role-Specific & Combined Suites
```powershell
# Common Launch & Onboarding Smoke
mvn clean test "-DsuiteXmlFile=src/test/resources/suites/common/common-smoke.xml"

# Patient Smoke & Patient Regression
mvn clean test "-DsuiteXmlFile=src/test/resources/suites/patient/patient-smoke.xml"
mvn clean test "-DsuiteXmlFile=src/test/resources/suites/patient/patient-regression.xml"

# Doctor Smoke & Doctor Regression
mvn clean test "-DsuiteXmlFile=src/test/resources/suites/doctor/doctor-smoke.xml"
mvn clean test "-DsuiteXmlFile=src/test/resources/suites/doctor/doctor-regression.xml"

# Combined Sequential Smoke & Combined Regression
mvn clean test "-DsuiteXmlFile=src/test/resources/suites/combined/combined-smoke.xml"
mvn clean test "-DsuiteXmlFile=src/test/resources/suites/combined/combined-regression.xml"

# Cross-Role E2E Session Transition
mvn clean test "-DsuiteXmlFile=src/test/resources/suites/e2e/cross-role-e2e.xml"
```

### Override Execution Mode from CLI
Add `"-Dexecution.mode=standard"` (fast CI/CD) or `"-Dexecution.mode=learning"` (1000ms visible delay).

### Run Individual Class or Method
```powershell
mvn test -Dtest=LoginTest
mvn test -Dtest=PatientAuthTest
mvn test -Dtest=DynamicLiveLoginTest
mvn test -Dtest=LoginTest#testDynamicLiveLoginToDashboard
```

## 4. Adding Your Own Module-Wise Data & Tests

1. Open the empty JSON template for your module, e.g. `src/test/resources/testdata/doctor/slots-data.json` or `src/test/resources/testdata/patient/signup-data.json`, and add your test data under `"testData"`.
2. Open the matching skeleton test class (e.g. `SlotNavigationTest.java`), change `enabled = false` to `enabled = true`, read fields from `data.path("testData")`, and call the existing Page Object methods.
