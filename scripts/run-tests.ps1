$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot
Set-Location $projectRoot

function Read-DotEnvMap {
    $map = @{}
    $envFile = Join-Path $projectRoot ".env"
    if (Test-Path $envFile) {
        Get-Content $envFile | ForEach-Object {
            $line = $_.Trim()
            if ($line -and -not $line.StartsWith("#") -and $line.Contains("=")) {
                $idx = $line.IndexOf("=")
                $k = $line.Substring(0, $idx).Trim()
                $v = $line.Substring($idx + 1).Trim().Trim('"').Trim("'")
                if ($k) { $map[$k] = $v }
            }
        }
    }
    return $map
}

function Has-ConfigValue([string]$primaryKey, [string]$secondaryKey, [hashtable]$dotEnv) {
    foreach ($key in @($primaryKey, $secondaryKey)) {
        if (-not [string]::IsNullOrWhiteSpace([Environment]::GetEnvironmentVariable($key))) {
            return $true
        }
        if ($dotEnv.ContainsKey($key) -and -not [string]::IsNullOrWhiteSpace($dotEnv[$key])) {
            return $true
        }
    }
    return $false
}

function Find-AdbExecutable {
    $adb = Get-Command adb -ErrorAction SilentlyContinue | Select-Object -ExpandProperty Source
    if ($adb) { return $adb }
    if ($env:ANDROID_HOME) {
        $cand = Join-Path $env:ANDROID_HOME "platform-tools\adb.exe"
        if (Test-Path $cand) { return $cand }
    }
    $defaultAdb = Join-Path $env:LOCALAPPDATA "Android\Sdk\platform-tools\adb.exe"
    if (Test-Path $defaultAdb) { return $defaultAdb }
    return $null
}

function Find-MavenExecutable {
    $mvn = Get-Command mvn.cmd -ErrorAction SilentlyContinue | Select-Object -ExpandProperty Source
    if ($mvn) { return $mvn }
    $mvn = Get-Command mvn -ErrorAction SilentlyContinue | Select-Object -ExpandProperty Source
    if ($mvn) { return $mvn }
    $downloadsMvn = Get-ChildItem -Path (Join-Path $env:USERPROFILE "Downloads") -Filter "mvn.cmd" -Recurse -ErrorAction SilentlyContinue | Select-Object -First 1 -ExpandProperty FullName
    if ($downloadsMvn) { return $downloadsMvn }
    $m2Mvn = Get-ChildItem -Path (Join-Path $env:USERPROFILE ".m2\wrapper") -Filter "mvn.cmd" -Recurse -ErrorAction SilentlyContinue | Select-Object -First 1 -ExpandProperty FullName
    if ($m2Mvn) { return $m2Mvn }
    return $null
}

Clear-Host
Write-Host "========================================" -ForegroundColor Cyan
Write-Host " VG CARES MOBILE AUTOMATION FRAMEWORK" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "SELECT USER ROLE" -ForegroundColor Yellow
Write-Host ""
Write-Host "1. Patient"
Write-Host "2. Doctor"
Write-Host "3. Patient and Doctor"
Write-Host ""
$roleChoice = Read-Host "Enter choice (1-3)"
if ($roleChoice -notin @("1", "2", "3")) {
    Write-Host "[ERROR] Invalid role selection. Please enter 1, 2, or 3." -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "SELECT TEST SUITE" -ForegroundColor Yellow
Write-Host ""
Write-Host "1. Smoke Testing"
Write-Host "2. Regression Testing"
if ($roleChoice -eq "2") {
    Write-Host "3. Doctor Auth Complete (Sign Up + Login + Dynamic Login at End)"
}
Write-Host ""
$suitePrompt = if ($roleChoice -eq "2") { "Enter choice (1-3)" } else { "Enter choice (1-2)" }
$suiteChoice = Read-Host $suitePrompt
$validSuites = if ($roleChoice -eq "2") { @("1", "2", "3") } else { @("1", "2") }
if ($suiteChoice -notin $validSuites) {
    Write-Host "[ERROR] Invalid suite selection." -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "SELECT EXECUTION MODE" -ForegroundColor Yellow
Write-Host ""
Write-Host "1. Standard Mode (Fast)"
Write-Host "2. Learning Mode (Step-by-step delay)"
Write-Host ""
Write-Host "DEVICE TYPE: Physical Android Device" -ForegroundColor Gray
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
$modeChoice = Read-Host "Enter choice (1-2)"
if ($modeChoice -notin @("1", "2")) {
    Write-Host "[ERROR] Invalid execution mode selection. Please enter 1 or 2." -ForegroundColor Red
    exit 1
}

$suiteMap = @{
    "1-1" = "src/test/resources/suites/patient/patient-smoke.xml"
    "1-2" = "src/test/resources/suites/patient/patient-regression.xml"
    "2-1" = "src/test/resources/suites/doctor/doctor-smoke.xml"
    "2-2" = "src/test/resources/suites/doctor/doctor-regression.xml"
    "2-3" = "src/test/resources/suites/doctor/doctor-auth-complete.xml"
    "3-1" = "src/test/resources/suites/combined/combined-smoke.xml"
    "3-2" = "src/test/resources/suites/combined/combined-regression.xml"
}
$selectedSuite = $suiteMap["${roleChoice}-${suiteChoice}"]
$selectedMode = if ($modeChoice -eq "2") { "learning" } else { "standard" }

Write-Host ""
Write-Host "[1/4] Validating required role credentials..." -ForegroundColor Cyan
$dotEnv = Read-DotEnvMap
$needPatient = $roleChoice -in @("1", "3")
$needDoctor = $roleChoice -in @("2", "3")
if ($needPatient) {
    $hasPatEmail = Has-ConfigValue "VGCARES_PATIENT_EMAIL" "QA_PATIENT_EMAIL" $dotEnv
    $hasPatPass = Has-ConfigValue "VGCARES_PATIENT_PASSWORD" "QA_PATIENT_PASSWORD" $dotEnv
    if (-not ($hasPatEmail -and $hasPatPass)) {
        $patEmailInput = Read-Host "QA Patient Email not in .env (press Enter to skip dynamic patient tests)"
        if (-not [string]::IsNullOrWhiteSpace($patEmailInput)) {
            $secPass = Read-Host "QA Patient Password" -AsSecureString
            $bstr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($secPass)
            try {
                $env:QA_PATIENT_EMAIL = $patEmailInput.Trim()
                $env:QA_PATIENT_PASSWORD = [Runtime.InteropServices.Marshal]::PtrToStringAuto($bstr)
            } finally {
                [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($bstr)
            }
        }
    }
}
if ($needDoctor) {
    $hasDocEmail = Has-ConfigValue "VGCARES_DOCTOR_EMAIL" "QA_DOCTOR_EMAIL" $dotEnv
    $hasDocPass = Has-ConfigValue "VGCARES_DOCTOR_PASSWORD" "QA_DOCTOR_PASSWORD" $dotEnv
    if (-not ($hasDocEmail -and $hasDocPass)) {
        $docEmailInput = Read-Host "QA Doctor Email not in .env (press Enter to skip dynamic doctor tests)"
        if (-not [string]::IsNullOrWhiteSpace($docEmailInput)) {
            $secPass = Read-Host "QA Doctor Password" -AsSecureString
            $bstr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($secPass)
            try {
                $env:QA_DOCTOR_EMAIL = $docEmailInput.Trim()
                $env:QA_DOCTOR_PASSWORD = [Runtime.InteropServices.Marshal]::PtrToStringAuto($bstr)
            } finally {
                [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($bstr)
            }
        }
    }
}
Write-Host "[OK] Credential check complete." -ForegroundColor Green

Write-Host "[2/4] Detecting connected physical Android device..." -ForegroundColor Cyan
$adbExe = Find-AdbExecutable
if (-not $adbExe) {
    Write-Host "[ERROR] adb.exe not found. Ensure Android SDK platform-tools is installed." -ForegroundColor Red
    exit 1
}
$adbLines = & $adbExe devices -l | Where-Object { $_ -match "\sdevice(\s|$)" -and $_ -notmatch "^emulator-" }
if (-not $adbLines) {
    Write-Host "[ERROR] No physical Android USB device detected. Connect your phone with USB Debugging enabled." -ForegroundColor Red
    exit 1
}
Write-Host "[OK] Physical Android device detected: $($adbLines | Select-Object -First 1)" -ForegroundColor Green

Write-Host "[3/4] Checking Appium server readiness..." -ForegroundColor Cyan
& (Join-Path $PSScriptRoot "start-appium.ps1")
if ($LASTEXITCODE -ne 0) {
    exit $LASTEXITCODE
}

$mvnExe = Find-MavenExecutable
if (-not $mvnExe) {
    Write-Host "[ERROR] Maven (mvn.cmd) not found." -ForegroundColor Red
    exit 1
}

Write-Host "[4/4] Executing Maven suite: $selectedSuite (mode=$selectedMode)" -ForegroundColor Cyan
& $mvnExe clean test "-DsuiteXmlFile=$selectedSuite" "-Dexecution.mode=$selectedMode"
$exitCode = $LASTEXITCODE

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
if ($exitCode -eq 0) {
    Write-Host " EXECUTION STATUS : PASSED" -ForegroundColor Green
} else {
    Write-Host " EXECUTION STATUS : FAILED (Exit Code: $exitCode)" -ForegroundColor Red
}
Write-Host " REPORTS FOLDER   : $(Join-Path $projectRoot 'target\reports')" -ForegroundColor Gray
Write-Host " LOGS FILE        : $(Join-Path $projectRoot 'target\logs\automation.log')" -ForegroundColor Gray
Write-Host "========================================" -ForegroundColor Cyan

exit $exitCode
