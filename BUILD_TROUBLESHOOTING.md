# Build & Troubleshooting Guide

## 🛠️ Build Issues & Solutions

### Issue 1: "Cannot resolve symbol"
```
Error: Cannot resolve symbol 'SomeView'
```

**Causes:**
- View ID doesn't exist in layout file
- Gradle cache is stale
- kotlin-synthetic imports deprecated

**Solutions:**
```bash
# Solution 1: Clean and rebuild
./gradlew clean
./gradlew build

# Solution 2: Invalidate caches
File → Invalidate Caches → Invalidate and Restart

# Solution 3: Verify view ID exists
# Open layout file and check android:id="@+id/..."
```

---

### Issue 2: "JAVA_HOME is not set"
```
ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
```

**Solutions:**

#### Windows PowerShell:
```powershell
# Find Java installation
Get-Command java

# Set JAVA_HOME
$env:JAVA_HOME = "C:\Program Files\Java\jdk-11"

# Verify
echo $env:JAVA_HOME
```

#### Windows Command Prompt:
```cmd
# Set JAVA_HOME
set JAVA_HOME=C:\Program Files\Java\jdk-11

# Verify
echo %JAVA_HOME%

# Build
gradlew assembleDebug
```

#### Environment Variable (Permanent):
```
1. Control Panel → System and Security → System
2. Click "Advanced system settings"
3. Click "Environment Variables"
4. Under "System variables", click "New"
   - Variable name: JAVA_HOME
   - Variable value: C:\Program Files\Java\jdk-11
5. Click OK and restart IDE/terminal
```

---

### Issue 3: "Google Services Plugin not found"
```
Error: Could not find com.google.gms:google-services:4.4.4
```

**Solution:**
```bash
# Check internet connection
# Update gradle wrapper
./gradlew wrapper --gradle-version=8.6

# Or manually update gradle/wrapper/gradle-wrapper.properties
# distributionUrl=https\://services.gradle.org/distributions/gradle-8.6-bin.zip
```

---

### Issue 4: "Unresolved reference 'tvExperience'"
```
e: file:///C:/Users/anura/AndroidStudioProjects/Hana/app/src/main/java/com/example/hana/HomeActivity.kt:123:47 
Unresolved reference 'tvExperience'.
```

**Verification Steps:**
1. Check layout file contains the view:
```xml
<!-- In activity_home.xml -->
<TextView
    android:id="@+id/tvExperience"
    ... />
```

2. Check binding in HomeActivity:
```kotlin
private lateinit var tvExperience: TextView

private fun bindViews() {
    tvExperience = findViewById(R.id.tvExperience)
}
```

3. If still failing:
```bash
./gradlew clean
./gradlew build
```

---

### Issue 5: "Firebase Config Missing"
```
Toast: "Missing Firebase config. Add app/google-services.json"
```

**Verification:**
```
app/
└── google-services.json  ← Must exist here

// Check file is valid JSON
cat app/google-services.json | python -m json.tool

// Required fields:
{
  "project_info": { ... },
  "client": [ ... ],
  "configuration_version": "1"
}
```

**Fix:**
1. Download from Firebase Console:
   ```
   Firebase Console → Project Settings → Google-services.json
   ```
2. Place in `app/` folder (NOT in `app/src/main/`)
3. Sync Gradle: `File → Sync Now`

---

### Issue 6: "Google Sign-In Failed"
```
Toast: "Google sign-in failed"
```

**Checklist:**
- [ ] Internet permission in manifest
- [ ] Google Play Services updated on device
- [ ] OAuth client configured in Firebase Console
- [ ] SHA-1 fingerprint added to Firebase
- [ ] google-services.json has oauth_client section

**Get SHA-1 Fingerprint:**
```bash
cd C:\Users\anura\AndroidStudioProjects\Hana
./gradlew signingReport

# Look for output like:
# SHA-1: AA:BB:CC:DD:EE:FF:...
```

**Add to Firebase:**
1. Open Firebase Console
2. Project Settings → Your apps → SHA certificate fingerprints
3. Add the SHA-1 value
4. Wait 15-30 minutes for propagation
5. Rebuild app

---

### Issue 7: "Compilation Failed: Multiple errors"
```
FAILURE: Build failed with an exception.
...
A failure occurred while executing org.jetbrains.kotlin.compilerRunner.GradleCompilerRunnerWithWorkers$GradleKotlinCompilerWorkAction
```

**Debug Steps:**
```bash
# Get detailed error info
./gradlew build --stacktrace

# Or for specific issues
./gradlew compileDebugKotlin --stacktrace

# Check specific file
./gradlew check -x test
```

**Common Fixes:**
```bash
# Clear kotlin cache
./gradlew clean
rm -rf .gradle
rm -rf app/build

# Rebuild
./gradlew build
```

---

### Issue 8: "API Not Found"
```
Error: Could not find method xxx()
```

**Solution - Update Dependencies:**
```kotlin
// In app/build.gradle.kts
dependencies {
    // Update to latest versions
    implementation(libs.androidx.core.ktx)  // Check libs.versions.toml
    implementation(libs.material)
}
```

Check `gradle/libs.versions.toml` for version numbers.

---

### Issue 9: "Gradle Sync Failed"
```
Error: Could not sync gradle
```

**Solutions:**
```bash
# Option 1: Invalidate cache
File → Invalidate Caches → Invalidate and Restart

# Option 2: Clean gradle
./gradlew clean

# Option 3: Delete gradle files
rm -rf .gradle
./gradlew clean

# Option 4: Update gradle wrapper
./gradlew wrapper --gradle-version=8.6
```

---

### Issue 10: "AndroidManifest.xml Errors"
```
Error: Element XXX not allowed here
```

**Common Causes:**
- Activity not defined
- Permission in wrong place
- Missing required attributes

**Check:**
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <!-- Permissions FIRST -->
    <uses-permission android:name="..." />
    
    <!-- Application SECOND -->
    <application>
        <!-- Activities INSIDE application -->
        <activity android:name="..." />
    </application>
</manifest>
```

---

## 🧪 Testing & Debugging

### Run with Debug Info
```bash
./gradlew build --debug
```

### Monitor Logcat
```bash
# In Android Studio
View → Tool Windows → Logcat

# Or command line
adb logcat
adb logcat | grep -i firebase
adb logcat | grep -i hana
```

### Test Specific Method
```kotlin
// Add debug log
Log.d("HomeActivity", "tvExperience: $tvExperience")

// Check in Logcat
D/HomeActivity: tvExperience: android.widget.TextView
```

### Device Logs
```bash
# Connect device
adb devices

# Install app
adb install -r app/build/outputs/apk/debug/app-debug.apk

# View logs
adb logcat | grep "com.example.hana"
```

---

## ⚡ Performance Issues

### Slow Build
```bash
# Enable gradle daemon
echo "org.gradle.daemon=true" >> gradle.properties

# Parallel builds
echo "org.gradle.parallel=true" >> gradle.properties

# Incremental compilation
echo "kotlin.incremental=true" >> gradle.properties
```

### Large APK Size
```
Analyze → Analyze APK
// Check for unused resources
```

---

## 🔍 Verification Checklist Before Build

- [ ] Java/JDK installed (11+)
- [ ] Android SDK installed (API 24+)
- [ ] `google-services.json` in `app/` folder
- [ ] All view IDs present in XML layouts
- [ ] All imports in Kotlin files resolve
- [ ] No syntax errors (IDE shows no red squiggles)
- [ ] gradle.properties not corrupted
- [ ] Internet connection available (for gradle download)

---

## 📋 Build Command Reference

```bash
# Clean build
./gradlew clean assembleDebug

# Build with output
./gradlew assembleDebug --info

# Build with stacktrace
./gradlew build --stacktrace

# Skip tests
./gradlew build -x test

# Force refresh dependencies
./gradlew build --refresh-dependencies

# Run specific task
./gradlew :app:compileDebugKotlin

# Check dependencies
./gradlew dependencies

# View tasks
./gradlew tasks
```

---

## 🎯 Success Indicators

### Successful Build Output
```
...
> Task :app:compileDebugKotlin
> Task :app:dexDebug
> Task :app:mergeDebugResources
> Task :app:processDebugResources
> Task :app:assembleDebug

BUILD SUCCESSFUL in XXs
```

### Successful Run
```
• App launches without crash
• Login screen displays
• No red errors in Logcat
• Firebase initialization successful
• Google Sign-In ready
```

---

## 📞 Additional Resources

- **Gradle Troubleshooting**: https://gradle.org/install/
- **Android Build Guide**: https://developer.android.com/build
- **Firebase Setup**: https://firebase.google.com/docs/android/setup
- **Kotlin Compiler**: https://kotlinlang.org/docs/reference/compiler-plugins.html

---

**Last Updated**: April 11, 2026
**Verified For**: Hana v1.0, Android SDK 24+, Java 11+

