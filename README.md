# VGCares Mobile Automation Framework

Enterprise Appium mobile automation framework built with Java 17, Appium 2.x, TestNG, and Maven, designed specifically for testing Android physical devices connected over USB.

---

## 📱 Hardware & Environment Prerequisites

- **Java JDK**: Version 17+ (e.g. Oracle JDK 17)
- **Node.js**: v20+ / v24+
- **Appium Server**: 2.x
- **Appium Driver**: `uiautomator2` (`appium driver install uiautomator2`)
- **Android SDK & ADB**: `platform-tools` added to your system PATH
- **Physical Device**: Android phone connected via USB with **USB Debugging** enabled and authorized

---

## 🚀 Daily Routine: Quick Start Guide

### Step 1: Connect Your Physical Device
1. Connect your Android phone to the PC with a data USB cable.
2. Verify authorization in PowerShell:
   ```powershell
   adb devices -l
   ```
   Ensure your device is listed with status `device` (e.g., `GIPV9HBMLBHITO5H device`).

### Step 2: Start Appium Server
Open a dedicated terminal window and run:
```powershell
appium --address 127.0.0.1 --port 4723
```
*Keep this terminal window running in the background.*

### Step 3: Run Automation Tests
In your project directory (`D:\My_own_project\Appium`), run:

```powershell
# Run the complete test suite
mvn clean test

# Run Smoke suite only
mvn test -DsuiteXmlFile=src/test/resources/suites/smoke.xml

# Run Regression suite
mvn test -DsuiteXmlFile=src/test/resources/suites/regression.xml

# Run a single test class
mvn test -Dtest=AppLaunchTest
mvn test -Dtest=SignUpTest
mvn test -Dtest=LoginTest
```

---

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

## 📊 Reports & Logs

- **Extent HTML Reports**: Generated in `reports/VGCares_Execution_Report_<timestamp>.html`. Includes test status, duration, device info, and Base64-embedded failure screenshots.
- **Log Files**: Stored in `logs/automation.log` with rolling file rotation.
- **Screenshots**: Automatically captured on any failure in `screenshots/`.
