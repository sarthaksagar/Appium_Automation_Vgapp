param(
    [string]$Address = "127.0.0.1",
    [int]$Port = 4723
)

$ErrorActionPreference = "Stop"
$statusUrl = "http://${Address}:${Port}/status"

try {
    $res = Invoke-RestMethod -Uri $statusUrl -TimeoutSec 2
    if ($res.value.ready -eq $true) {
        Write-Host "[OK] Appium server is already running at $statusUrl" -ForegroundColor Green
        exit 0
    }
} catch {
    # Server not reachable yet; proceed to start
}

$projectRoot = Split-Path -Parent $PSScriptRoot
$userAppiumHome = Join-Path $env:USERPROFILE ".appium"
$localAppiumHome = Join-Path $projectRoot "temp\appium-2-home"
if ([string]::IsNullOrWhiteSpace($env:APPIUM_HOME)) {
    if (Test-Path $userAppiumHome) {
        $env:APPIUM_HOME = $userAppiumHome
    } elseif (Test-Path $localAppiumHome) {
        $env:APPIUM_HOME = $localAppiumHome
    }
}

$appiumCmd = Get-Command appium -ErrorAction SilentlyContinue | Select-Object -ExpandProperty Source
if (-not $appiumCmd -and $env:APPDATA) {
    $candidate = Join-Path $env:APPDATA "npm\appium.cmd"
    if (Test-Path $candidate) {
        $appiumCmd = $candidate
    }
}
if (-not $appiumCmd) {
    Write-Error "Appium CLI was not found on PATH or in APPDATA\npm\appium.cmd."
    exit 1
}

Write-Host "[INFO] Starting Appium server at http://${Address}:${Port} ..." -ForegroundColor Cyan
Start-Process -FilePath $appiumCmd -ArgumentList "--address", $Address, "--port", $Port -WindowStyle Minimized | Out-Null

for ($i = 0; $i -lt 20; $i++) {
    Start-Sleep -Seconds 1
    try {
        $res = Invoke-RestMethod -Uri $statusUrl -TimeoutSec 2
        if ($res.value.ready -eq $true) {
            Write-Host "[OK] Appium server ready at $statusUrl" -ForegroundColor Green
            exit 0
        }
    } catch {
        # Wait for startup
    }
}

Write-Error "Appium server did not become ready at $statusUrl within 20 seconds."
exit 1
