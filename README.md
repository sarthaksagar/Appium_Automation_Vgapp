# VGCares Mobile Automation Framework

Enterprise Appium mobile automation framework built with Java 17, Appium 2.x, TestNG, and Maven, designed specifically for testing Android physical devices connected over USB.

---

## Windows setup

Use JDK 17, Maven, the Android SDK platform tools, and a USB-connected Android phone with USB debugging enabled. Install the VG Cares QA APK before testing. Accept the phone's debugging authorization prompt and leave the device awake and unlocked while tests run.

Appium 2.16.2 works with UiAutomator2 4.2.9. From the project folder, install the driver once in a workspace-specific Appium home.

```powershell
$env:APPIUM_HOME = Join-Path $PWD 'temp\appium-2-home'
$env:Path = (Join-Path $env:ANDROID_HOME 'platform-tools') + ';' + $env:Path
$appium = Join-Path $env:APPDATA 'npm\appium.cmd'
& $appium driver install uiautomator2@4.2.9
& $appium driver list --installed
```

On Windows, the mobile suite starts Appium at `http://127.0.0.1:4723` if the connection is refused, using the workspace Appium home by default. It waits for readiness and stops the server it started after the suite. Appium warnings appear in `logs/automation.log`. An already-running server is reused. For a custom Appium URL, start its server before launching the suite.

To check the USB connection, run the following in the test terminal.

```powershell
& (Join-Path $env:ANDROID_HOME 'platform-tools\adb.exe') devices -l
```

If several ADB devices are listed, set VGCARES_UDID to the intended USB phone's serial in the test process environment. Appium settings live in src/main/resources/config.properties. A system property or the corresponding VGCARES_ environment variable can override a setting. For example, VGCARES_APPIUM_SERVER_URL overrides appiumServerUrl, including a custom base path when needed.

## QA test data

- Set VGCARES_DOCTOR_EMAIL and VGCARES_DOCTOR_PASSWORD in the test process environment using an authorized QA doctor account and your local secret manager.
- Set VGCARES_RESET_EMAIL to an authorized QA mailbox for reset and resend cases. Set VGCARES_SIGNUP_EMAIL_DOMAIN to a controlled QA domain for generated signup addresses.
- Use a dedicated QA account and test fixtures for flows that save settings, upload files, submit requests, or change server-side state.
- Enable failure screenshots with -DcaptureScreenshots=true when the device and account contain only disposable QA data.

## Test execution

Run these stages from the project folder. Resolve any failure in the current stage before moving on.

```powershell
mvn.cmd dependency:tree
mvn.cmd clean test-compile
mvn.cmd test '-Dtest=*UnitTest'
mvn.cmd test '-Dtest=AppLaunchTest'
mvn.cmd test '-Dtest=LoginTest#testDynamicLiveLoginToDashboard'
mvn.cmd test '-DsuiteXmlFile=src/test/resources/suites/smoke.xml'
mvn.cmd test '-Dtest=SettingsPreferencesTest'
mvn.cmd clean test
```

Class filters support other modules, including DocumentsUploadAndValidationTest and HomeTest. TestNG opens one session for each serial mobile suite and closes it at suite teardown. When Appium or the phone is unavailable, TestNG records the failed prerequisite and blocks the UI tests. First-run onboarding and biometric setup can require a device or test account in a specific initial state.

## 🏗️ Framework Architecture

```text
Appium/
├── pom.xml                                  <-- Maven dependencies, Surefire, Java 17
├── testng.xml                               <-- Master suite
├── .gitignore                               <-- Clean repository rules
├── README.md                                <-- Documentation
│
├── src/
│   ├── main/
│   │   ├── java/com/vgcares/automation/
│   │   │   ├── config/ConfigManager.java    <-- System/Env/Properties loader
│   │   │   ├── constants/FrameworkConstants.java
│   │   │   ├── driver/                      <-- ThreadLocal AndroidDriver & Factory
│   │   │   │   ├── DriverFactory.java
│   │   │   │   └── DriverManager.java
│   │   │   ├── pages/                       <-- Page Object Model (POM)
│   │   │   │   ├── BasePage.java
│   │   │   │   ├── LoginPage.java
│   │   │   │   └── SignUpPage.java
│   │   │   ├── reports/ExtentReportManager.java
│   │   │   └── utils/                       <-- Device, gestures, waits, screenshots, JSON
│   │   │       ├── DeviceUtils.java
│   │   │       ├── GestureUtils.java
│   │   │       ├── JsonUtils.java
│   │   │       ├── ScreenshotUtils.java
│   │   │       └── WaitUtils.java
│   │   │
│   │   └── resources/
│   │       ├── config.properties            <-- Real device configurations
│   │       └── log4j2.xml                   <-- Structured logging
│   │
│   └── test/
│       ├── java/com/vgcares/automation/
│       │   ├── base/BaseTest.java           <-- Setup & Teardown lifecycle
│       │   ├── dataproviders/AuthDataProvider.java
│       │   ├── listeners/TestListener.java  <-- ExtentReports & failure screenshots
│       │   └── tests/
│       │       ├── app/AppLaunchTest.java   <-- Real device smoke verification
│       │       └── auth/
│       │           ├── LoginTest.java
│       │           └── SignUpTest.java
│       │
│       └── resources/
│           ├── suites/
│           │   ├── smoke.xml
│           │   └── regression.xml
│           └── testdata/auth-data.json      <-- Externalized test data
│
├── reports/                                 <-- Extent HTML reports (auto-generated)
├── screenshots/                             <-- Failure screenshots (auto-generated)
└── logs/                                    <-- Execution logs (auto-generated)
```

---

## Reports and logs

- Extent HTML reports are written under reports/. They include configuration failures and individual test results.
- TestNG and Surefire results are written under target/surefire-reports/.
- The framework writes rolling logs to logs/automation.log. Failure screenshots are available when explicitly enabled and a driver session is active.
