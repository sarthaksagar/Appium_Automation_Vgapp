# VGCares Enterprise Automation Architecture

## 1. Design Evaluation of the Role-Based Architecture

- **Single Maven Project & Reusable Core**: Keeps `DriverFactory`, `DriverManager`, `ConfigManager`, `WaitUtils`, `GestureUtils`, `ScreenshotUtils`, `ExecutionUtils`, `StepLogger`, and `ExtentReportManager` shared across all roles without code duplication.
- **Role Separation (`COMMON`, `PATIENT`, `DOCTOR`)**:
  - **Common / Shared UI** (`LaunchPage`, `LoginPage`, `SignUpPage`, `ForgotPasswordPage`, `BiometricPage`): Observed from the app hierarchy (`ui.xml`), both Patient and Doctor authenticate via the same Login screen (role routing happens server-side by account), while mobile Sign Up explicitly states it is available for **patients only**.
  - **Patient Role** (`PatientHomePage`, `PatientAuthWorkflow`, `PatientBaseTest`, `PatientAuthTest`, `PatientDashboardTest`): Enforces Patient authentication state and verifies Patient-specific dashboard behavior and role isolation (Doctor-only Clinical Snapshot is not exposed in a Patient session).
  - **Doctor Role** (`HomePage` + Doctor feature Page Objects, `DoctorAuthWorkflow`, `DoctorBaseTest`, Doctor module tests): Enforces Doctor authentication state across all Doctor modules (Slots, Affiliations, Surgery Requests, Consultation Fees, Reviews, Documents, Messages, Notifications, Profile, Settings, Navigation Drawer).
- **No Fabricated Patient Screens**: Additional Patient screens (e.g., Doctor Search, Hospital Search, Appointment Booking, Medical Records) were intentionally not invented until those live screens are inspected via Appium / UI dump on a connected device.

## 2. Sequential Multi-Role Execution (Single Physical Device)

Because a single USB Android phone cannot run two conflicting Appium sessions concurrently:
1. TestNG runs sequentially (`parallel="none"`), enforced in `BaseTest.startMobileSuite`.
2. `DriverManager` tracks `authenticatedRole` (`COMMON`, `PATIENT`, `DOCTOR`).
3. `PatientAuthWorkflow` and `DoctorAuthWorkflow` check `DriverManager.getAuthenticatedRole()` before authenticating; if switching roles in Combined Smoke or Combined Regression, the previous session logs out cleanly via `OnboardingWorkflow` before the next role logs in.

## 3. Execution Modes (`Learning` vs `Standard`)

- **Learning Mode** (`execution.mode=learning`, `action.delay.ms=1000`): Applies a centralized demonstration delay in `BasePage.applyDemonstrationDelay()` via `ExecutionUtils.applyActionDelayIfLearning()`, paired with step-by-step terminal banners.
- **Standard Mode** (`execution.mode=standard`): Zero demonstration delay (`0 ms`), relying strictly on explicit condition-based waits (`WaitUtils`) for fast CI/CD regression.
