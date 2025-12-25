@echo off
echo Starting QR Code Master build...

REM Check Gradle wrapper
if not exist gradle\wrapper\gradle-wrapper.jar (
    echo Downloading Gradle wrapper...
    powershell -Command "Invoke-WebRequest -Uri 'https://github.com/gradle/gradle/raw/v8.12.1/gradle/wrapper/gradle-wrapper.jar' -OutFile 'gradle\wrapper\gradle-wrapper.jar'"
)

REM Run Gradle build
echo Building project...
call gradlew.bat assembleDebug --info

if %errorlevel% neq 0 (
    echo Build failed with error code: %errorlevel%
    pause
    exit /b %errorlevel%
)

echo.
echo Build successful!
echo APK location: %CD%\app\build\outputs\apk\debug\app-debug.apk
pause