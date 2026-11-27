# VGCares Test Traceability & Clean Starter Template Status

All static/pre-populated module test cases and hardcoded data have been cleared into clean module-wise JSON templates (`src/test/resources/testdata/{common,patient,doctor}/*.json`) and starter skeleton test classes (`enabled = false` placeholder per module), while **Dynamic Login** (Doctor & Patient) and **Framework Unit Tests** remain active and executable.

## Active Executable Tests (Dynamic Login & Framework Unit)

| Test Case ID | Role | Module | Scenario | Java Class | Test Method | Status |
|---|---|---|---|---|---|---|
| `TC_ID_LOGIN-DYN-001` | Doctor | Auth | Dynamic Doctor login -> Doctor Dashboard | `LoginTest` | `testDynamicLiveLoginToDashboard` | Active (`smoke`, `regression`) |
| `TC_ID_LOGIN-DYN-002` | Patient | Auth | Dynamic Patient login -> Patient Dashboard | `LoginTest` | `testPatientRoleLoginToDashboard` | Active (`smoke`, `regression`) |
| `TC_ID_LAUNCH-001..007, 009..025` | Patient & Doctor | Launch | Language selection (10 languages, native scripts), Welcome/Connect/Hospital Network/Global Healthcare onboarding, Skip, Back, alignment, rapid Next debounce, Get Started | `LaunchTest` | `testSelectEnglishLanguageAndProceed`..`testContinueActionAcrossSelectedLanguages` | Active (`common`, `patient`, `doctor`, `smoke`, `regression`) |
| `TC_ID_SIGNUP-001..008` | Doctor | Sign Up | Account creation validation (valid details, empty Full Name/Email/Password/Confirm Password, invalid email, password mismatch, already-registered email / toaster messages; `doctor/signup-data.json`) | `SignUpTest` | `testValidSignUpCreatesAccount`..`testExistingEmailRejected` | Active (`doctor`, `smoke`, `regression`) |
| `TC_ID_LOGIN-048..079` | Doctor | Login | Responsiveness, device back, placeholder focus, email/password negative validations, toast UX, rapid tapping, case-sensitivity, whitespace, role & biometric checks (`doctor/doctor-login-data.json`) | `DoctorLoginTest` | `testLoginScreenResponsivenessAndScaling`..`testBiometricLoginOptionAvailability` | Active (`doctor`, `smoke`, `regression`) |
| `TC_ID_DYN_LOG-001..002` | Doctor | Doctor Auth | Dynamic Doctor credential config & login (also runs at end of `doctor-auth-complete.xml` after Sign Up & Login) | `DynamicLiveLoginTest` | `testDynamicLiveUserConfigurationCheck`, `testDynamicLiveAuthenticationAndDashboardRedirection` | Active (`doctor`, `smoke`, `regression`) |
| `TC_ID_BIO-001..035` | Common / Doctor | Biometric Setup | Quick Access display/branding/tagline/buttons, Enable/Skip/Cancel/Back, Settings toggle & physical-sensor preconditions (`common/biometric-data.json`) | `BiometricTest` | `testBiometricScreenLoadsAfterLogin`..`testBiometricMaintainsValidSession` | Active (`common`, `doctor`, `regression`) |
| `TC_ID_PAT_AUTH-001..002` | Patient | Patient Auth | Dynamic Patient login & clean logout | `PatientAuthTest` | `testValidPatientLoginReachesDashboard`, `testPatientLogoutReturnsToLogin` | Active (`patient`, `smoke`, `regression`) |
| `TC_ID_E2E_ROLE-001` | Cross-Role | E2E | Sequential Patient -> Logout -> Doctor -> Logout | `CrossRoleSessionTransitionTest` | `testSequentialPatientToDoctorSessionHandoff` | Active (`e2e`, `regression`) |
| `UNIT-001..018` | Framework | Unit | Config, `.env`, ExecutionMode, UserRole, ModuleDataLoader, Suites, Capabilities | `*UnitTest` | All 6 unit test classes | Active (`unit-tests.xml`) |

## Clean Module-Wise Skeletons & Data Templates (Ready for Your Cases)

| Role | Module | Skeleton Test Class(es) | Module JSON Template File |
|---|---|---|---|
| Common | App | `AppLaunchTest` | `src/test/resources/testdata/common/launch-data.json` |
| Common | Splash | `SplashScreenComprehensiveTest` | `src/test/resources/testdata/common/splash-data.json` |
| Common | Forgot Password | `ForgotPasswordTest` | `src/test/resources/testdata/common/forgot-password-data.json` |
| Patient | Dashboard | `PatientDashboardTest` | `src/test/resources/testdata/patient/patient-home-data.json` |
| Doctor | Home / Dashboard | `HomeTest` | `src/test/resources/testdata/doctor/home-data.json` |
| Doctor | Notifications | `NotificationBellTest` | `src/test/resources/testdata/doctor/notifications-data.json` |
| Doctor | Slots / Availability | `Slot*Test` (7 classes) | `src/test/resources/testdata/doctor/slots-data.json` |
| Doctor | Messages | `MessagesNegativeTest` | `src/test/resources/testdata/doctor/messages-data.json` |
| Doctor | Profile | `Profile*Test` (3 classes) | `src/test/resources/testdata/doctor/profile-data.json` |
| Doctor | Hospital Affiliations | `HospitalAffiliations*Test` (3 classes) | `src/test/resources/testdata/doctor/affiliations-data.json` |
| Doctor | Surgery Requests | `SurgeryRequests*Test` (2 classes) | `src/test/resources/testdata/doctor/surgery-requests-data.json` |
| Doctor | Consultation Fees | `ConsultationFees*Test` (2 classes) | `src/test/resources/testdata/doctor/consultation-fees-data.json` |
| Doctor | Reviews | `Reviews*Test` (2 classes) | `src/test/resources/testdata/doctor/reviews-data.json` |
| Doctor | Documents | `Documents*Test` (2 classes) | `src/test/resources/testdata/doctor/documents-data.json` |
| Doctor | Settings | `SettingsPreferencesTest`, `SettingsSecurityAndAccountTest` | `src/test/resources/testdata/doctor/settings-data.json` |
| Doctor | Navigation Drawer | `NavigationDrawerComprehensiveTest` | `src/test/resources/testdata/doctor/navigation-data.json` |
