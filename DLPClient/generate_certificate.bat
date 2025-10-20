@echo off
setlocal enabledelayedexpansion

title Certificate Creator and MSBuild Setup
echo.
echo ===============================================
echo   CODE SIGNING CERTIFICATE + MSBuild SETUP
echo ===============================================
echo.

set "SCRIPT_DIR=%~dp0"
set "SCRIPT_DIR=%SCRIPT_DIR:~0,-1%"  :: Remove trailing backslash

echo [INFO] Script directory: %SCRIPT_DIR%
echo.

:: Check if running as administrator (required for certificate creation)
echo [1/7] Checking administrator privileges...
net session >nul 2>&1
if %errorLevel% neq 0 (
    echo [ERROR] This script must be run as Administrator!
    echo.
    echo Please right-click and select "Run as administrator"
    echo.
    pause
    exit /b 1
)

echo [OK] Running as Administrator
echo.

:: Change to script directory (fix System32 issue)
cd /d "%SCRIPT_DIR%"
echo [INFO] Working directory: %CD%
echo.

:: Check if PowerShell is available and can create certificates
echo [2/7] Checking PowerShell certificate capabilities...
powershell -Command "Get-Command New-SelfSignedCertificate -ErrorAction SilentlyContinue" >nul 2>&1
if %errorLevel% neq 0 (
    echo [ERROR] PowerShell New-SelfSignedCertificate not available!
    echo.
    echo This requires Windows 8.1 / Windows Server 2012 R2 or later
    echo.
    pause
    exit /b 1
)

echo [OK] PowerShell certificate commands available
echo.

:: Find SignTool automatically
echo [3/7] Searching for SignTool...
set SIGNTOOL_PATH=
call :FindSignTool
if "!SIGNTOOL_PATH!"=="" (
    echo [WARNING] SignTool not found!
    echo Auto-signing will be disabled
    echo Install Windows SDK for automatic signing
) else (
    echo [OK] SignTool found: !SIGNTOOL_PATH!
)

echo.
:: Input parameters
set /p COMPANY="Enter company name (e.g., MyCompany): "
if "!COMPANY!"=="" (
    echo Company name is required!
    pause
    exit /b 1
)

:: Clean company name for filenames
set CLEAN_COMPANY=!COMPANY!
set CLEAN_COMPANY=!CLEAN_COMPANY: =_!
set CLEAN_COMPANY=!CLEAN_COMPANY:/=!
set CLEAN_COMPANY=!CLEAN_COMPANY:\=!
set CLEAN_COMPANY=!CLEAN_COMPANY::=!

:: Generate secure password
echo [4/7] Generating secure password...
set PASSWORD=
set CHARS=ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$
for /L %%i in (1,1,16) do (
    set /A RAND=!RANDOM! %% 66
    for %%j in (!RAND!) do set "PASSWORD=!PASSWORD!!CHARS:~%%j,1!"
)

:: Create certificates directory
if not exist "Certificates" (
    mkdir "Certificates"
    echo [OK] Created Certificates directory
)

echo.
echo [5/7] Creating code signing certificate using PowerShell...

:: Step 1: Create certificate using PowerShell - SINGLE LINE APPROACH
echo [INFO] Creating certificate with PowerShell...
powershell -Command "try { $cert = New-SelfSignedCertificate -Type CodeSigning -Subject 'CN=!COMPANY! Code Signing' -KeyAlgorithm RSA -KeyLength 4096 -HashAlgorithm SHA256 -CertStoreLocation 'Cert:\CurrentUser\My' -KeyExportPolicy Exportable -KeyUsage DigitalSignature -KeyUsageProperty Sign -TextExtension @('2.5.29.37={text}1.3.6.1.5.5.7.3.3') -NotAfter (Get-Date).AddYears(1); $pwd = ConvertTo-SecureString '!PASSWORD!' -AsPlainText -Force; Export-PfxCertificate -Cert $cert -FilePath 'Certificates\!CLEAN_COMPANY!.pfx' -Password $pwd; Export-Certificate -Cert $cert -FilePath 'Certificates\!CLEAN_COMPANY!.cer'; Write-Host '[OK] Certificate created!' -ForegroundColor Green; Write-Host '[OK] Thumbprint: ' $cert.Thumbprint -ForegroundColor Cyan; exit 0 } catch { Write-Host '[ERROR] ' $_.Exception.Message -ForegroundColor Red; exit 1 }"

set CERT_RESULT=%errorLevel%

if !CERT_RESULT! neq 0 (
    echo [ERROR] Certificate creation failed with error code: !CERT_RESULT!
    pause
    exit /b 1
)

:: Verify certificate files were created
if not exist "Certificates\!CLEAN_COMPANY!.pfx" (
    echo [ERROR] Certificate file was not created: Certificates\!CLEAN_COMPANY!.pfx
    pause
    exit /b 1
)

if not exist "Certificates\!CLEAN_COMPANY!.cer" (
    echo [WARNING] CER file was not created, but PFX exists
)

echo [OK] Certificate files created successfully!
echo.

:: Step 2: Create local.secrets.props
echo [6/7] Creating MSBuild configuration...
(
echo ^<?xml version="1.0" encoding="utf-8"?^>
echo ^<Project^>
echo   ^<PropertyGroup^>
echo     ^<CodeSigningEnabled^>true^</CodeSigningEnabled^>
echo     ^<CodeSigningCertificate^>Certificates\!CLEAN_COMPANY!.pfx^</CodeSigningCertificate^>
echo     ^<CodeSigningPassword^>!PASSWORD!^</CodeSigningPassword^>
echo     ^<TimestampServer^>http://timestamp.digicert.com^</TimestampServer^>
echo   ^</PropertyGroup^>
echo ^</Project^>
) > local.secrets.props

echo [OK] Created: local.secrets.props

:: Step 3: Create Directory.Build.targets with auto SignTool detection
echo [7/7] Creating automatic signing configuration...
echo ^<?xml version="1.0" encoding="utf-8"?^> > Directory.Build.targets
echo ^<Project^> >> Directory.Build.targets
echo. >> Directory.Build.targets
echo ^<PropertyGroup^> >> Directory.Build.targets

:: Set SignTool path based on what we found
if "!SIGNTOOL_PATH!"=="" (
    echo ^<SignToolPath^>signtool.exe^</SignToolPath^> >> Directory.Build.targets
) else (
    echo ^<SignToolPath^>!SIGNTOOL_PATH!^</SignToolPath^> >> Directory.Build.targets
)

echo ^<SignToolExists Condition="Exists('$(SignToolPath)')"^>true^</SignToolExists^> >> Directory.Build.targets
echo ^<ShouldSign Condition="'$(CodeSigningEnabled)' == 'true' and '$(SignToolExists)' == 'true' and '$(Configuration)' == 'Release' and Exists('$(CodeSigningCertificate)')"^>true^</ShouldSign^> >> Directory.Build.targets
echo ^</PropertyGroup^> >> Directory.Build.targets
echo. >> Directory.Build.targets
echo ^<Target Name="SignOutput" AfterTargets="Build" Condition="'$(ShouldSign)' == 'true'"^> >> Directory.Build.targets
echo ^<Message Text="Signing: $(TargetPath)" Importance="high" /^> >> Directory.Build.targets
echo ^<Exec Command="&quot;$(SignToolPath)&quot; sign /fd SHA256 /td SHA256 /f &quot;$(CodeSigningCertificate)&quot; /p &quot;$(CodeSigningPassword)&quot; /tr &quot;$(TimestampServer)&quot; &quot;$(TargetPath)&quot;" /^> >> Directory.Build.targets
echo ^</Target^> >> Directory.Build.targets
echo. >> Directory.Build.targets
echo ^<Target Name="SignPublished" AfterTargets="Publish" Condition="'$(ShouldSign)' == 'true'"^> >> Directory.Build.targets
echo ^<Exec Command="&quot;$(SignToolPath)&quot; sign /fd SHA256 /td SHA256 /f &quot;$(CodeSigningCertificate)&quot; /p &quot;$(CodeSigningPassword)&quot; /tr &quot;$(TimestampServer)&quot; &quot;$(PublishDir)*.exe&quot;" /^> >> Directory.Build.targets
echo ^</Target^> >> Directory.Build.targets
echo. >> Directory.Build.targets
echo ^</Project^> >> Directory.Build.targets

echo [OK] Created: Directory.Build.targets

echo.
echo ===============================================
echo          SETUP COMPLETED SUCCESSFULLY
echo ===============================================
echo.
echo 📁 FILES CREATED:
echo    • Certificates\!CLEAN_COMPANY!.pfx (Certificate with private key)
echo    • Certificates\!CLEAN_COMPANY!.cer (Public key only)
echo    • local.secrets.props (MSBuild secrets)
echo    • Directory.Build.targets (Auto-signing config)
echo.
echo 🔐 SECURITY INFORMATION:
echo    • Password: !PASSWORD!
echo    • Certificate: !COMPANY! Code Signing
echo    • Valid until: 1 years from now
echo    • Store: CurrentUser\My (Personal certificate store)
echo.
echo 🔍 SIGNTOOL STATUS: !SIGNTOOL_PATH!
echo.
echo 🚀 AUTOMATIC SIGNING FEATURES:
echo    • Auto-detects SignTool location
echo    • Signs EXE/DLL files automatically on Release build
echo    • Signs all published executables
echo    • Adds timestamp for long-term validity
echo    • Uses SHA256 encryption
echo.
echo 🧪 TEST THE SETUP:
echo    1. dotnet build --configuration Release
echo    2. Check if DLPClient.exe is signed
echo    3. signtool verify /pa DLPClient.exe
echo.
echo ⚠️  IMPORTANT NOTES:
echo    • Add 'local.secrets.props' to .gitignore
echo    • Add 'Certificates/' to .gitignore  
echo    • Backup the password securely
echo    • Self-signed certs show 'Unknown Publisher'
echo    • Certificate is installed in your personal store
echo.
echo ===============================================

echo.
echo Press any key to exit...
pause >nul
goto :EOF

:: Function to find SignTool automatically
:FindSignTool
set "KITS_DIR=C:\Program Files (x86)\Windows Kits\10\bin"
if not exist "!KITS_DIR!" (
    echo [INFO] Windows Kits directory not found: !KITS_DIR!
    goto :CheckPath
)

:: Search for the highest version
for /F "delims=" %%I in ('dir "!KITS_DIR!\10.0.*" /B /AD-H /O-N 2^>nul') do (
    set "VERSION_DIR=%%I"
    
    :: Check x64
    set "TEST_PATH=!KITS_DIR!\!VERSION_DIR!\x64\signtool.exe"
    if exist "!TEST_PATH!" (
        set "SIGNTOOL_PATH=!TEST_PATH!"
        goto :EOF
    )
    
    :: Check x86
    set "TEST_PATH=!KITS_DIR!\!VERSION_DIR!\x86\signtool.exe"
    if exist "!TEST_PATH!" (
        set "SIGNTOOL_PATH=!TEST_PATH!"
        goto :EOF
    )
)

:CheckPath
:: Check if signtool is in PATH
where signtool >nul 2>&1
if %errorLevel% equ 0 (
    for /F "delims=" %%I in ('where signtool') do (
        set "SIGNTOOL_PATH=%%I"
        goto :EOF
    )
)

goto :EOF