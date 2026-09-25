@echo off
REM Get SHA-1 Certificate for Firebase
REM Run this in the project root directory

echo.
echo ========================================
echo Getting SHA-1 Certificate for Firebase
echo ========================================
echo.

call gradlew.bat signingReport

echo.
echo ========================================
echo SHA-1 Certificate obtained!
echo Copy the SHA1 value above and paste it
echo into Firebase Console when registering
echo your Android app.
echo ========================================
echo.
pause

