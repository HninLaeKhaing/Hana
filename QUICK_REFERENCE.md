# Hana Wellness App - Quick Reference Card

## 🎯 Key URLs & Credentials

### Firebase Project
- **Project ID**: `hana-a567a992`
- **Console**: https://console.firebase.google.com/project/hana-a567a992
- **Project Number**: `425027918448`

### App Identifiers
- **Package Name**: `com.example.hana`
- **App ID**: `1:425027918448:android:beafc5850449519c9cf2b5`
- **API Key**: `AIzaSyCz9qV5RrtGTEYoNbdC65iMIVhr8IHI1C0`

---

## 📁 Important File Locations

```
Hana/
├── app/
│   ├── google-services.json          ← Firebase config
│   ├── src/main/
│   │   ├── AndroidManifest.xml       ← App manifest
│   │   ├── java/com/example/hana/
│   │   │   ├── MainActivity.kt        ← Login screen
│   │   │   ├── SignUpActivity.kt      ← Registration
│   │   │   └── HomeActivity.kt        ← Home dashboard
│   │   └── res/
│   │       ├── layout/
│   │       │   ├── activity_main.xml
│   │       │   ├── activity_sign_up.xml
│   │       │   └── activity_home.xml
│   │       └── values/
│   │           ├── strings.xml
│   │           ├── colors.xml
│   │           └── arrays.xml
│   └── build.gradle.kts              ← Dependencies
├── build.gradle.kts                  ← Project config
└── gradle.properties
```

---

## 🔑 Access Points

### Entry Point
- **Main Activity**: `com.example.hana.MainActivity`
- **Launcher**: MainActivity (login screen)

### Activity Chain
```
MainActivity (Login)
    ↓ (or) ↙
SignUpActivity (Register)  →  HomeActivity (Dashboard)
    ↓                           ↓
  [Firebase Auth]      [Location Service]
```

---

## 🎨 UI Elements

### Navigation Tabs (Bottom Bar)
| Position | Label | Icon | Action |
|----------|-------|------|--------|
| Left-1 | Body | Menu View | Scroll to experience |
| Left-2 | Diet | Menu Search | Scroll to nutrition |
| Center | Scanner | Camera | Brown button (#8B6F47) |
| Right-2 | Challenge | Menu Places | Scroll to challenges |
| Right-1 | Account | Menu Manage | Logout |

### Colors
- **Primary**: Defined in `colors.xml`
- **Scanner**: `#8B6F47` (Brown)
- **Background**: `@color/screen_background` (Beige/Cream)
- **Text**: `@color/brand_text` (Dark), `@color/body_text` (Gray)

---

## 🔐 Firebase Services

### Enabled Features
- ✅ Firebase Authentication
  - Email/Password
  - Google Sign-In
  - Password Reset
- ✅ Firebase Firestore (future use)
- ✅ Google Play Services Location

### Auth Methods
```kotlin
// Email Login
auth.signInWithEmailAndPassword(email, password)

// Google Login
auth.signInWithCredential(credential)

// Logout
auth.signOut()

// Current User
auth.currentUser
```

---

## 🛠️ Build Commands

### Build Variants
```bash
# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease

# Debug Bundle
./gradlew bundleDebug

# Release Bundle
./gradlew bundleRelease
```

### Installation
```bash
# Install debug
./gradlew installDebug

# Run app
./gradlew installDebug -Porg.gradle.java.home=/path/to/java
```

### Cleaning
```bash
# Clean build
./gradlew clean

# Clean specific task
./gradlew clean assembleDebug

# Full rebuild
./gradlew clean build
```

---

## 📱 Testing Flows

### Test Account (Email/Password)
```
Email: test@example.com
Password: Test@123456
```

### Test Google Account
- Use any Google Account
- First time automatically creates user in Firebase

### Test Locations
- Device with GPS enabled
- Emulator: Set location in Extended Controls
- Mock Location app (Android 6+)

---

## 📊 Gradle Structure

### Project Level (build.gradle.kts)
```kotlin
plugins {
    id("com.google.gms.google-services") version "4.4.4" apply false
}
```

### App Level (build.gradle.kts)
```kotlin
plugins {
    id("com.google.gms.google-services")
}

dependencies {
    implementation(platform("com.google.firebase:firebase-bom:34.12.0"))
    implementation(libs.firebase.auth)
    implementation("com.google.android.gms:play-services-location:21.1.0")
}
```

---

## 🔍 Debugging

### Logcat Filters
```bash
# Show all Firebase logs
adb logcat | grep -i firebase

# Show all app logs
adb logcat | grep -i hana

# Show authentication logs
adb logcat | grep -i auth

# Show location logs
adb logcat | grep -i location
```

### View Log in Android Studio
```
View → Tool Windows → Logcat (Ctrl+6)
```

### Firebase Console Logs
```
Firebase Console → Auth → Sign-in method → Activity
```

---

## 📋 Required Permissions

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

### Runtime Permissions
- Location permissions requested at app startup
- Users grant/deny in dialog
- App handles both cases

---

## 🎯 Common Tasks

### Add New String Resource
```xml
<!-- In strings.xml -->
<string name="my_string">Text here</string>

<!-- In Code -->
getString(R.string.my_string)
```

### Add New Color
```xml
<!-- In colors.xml -->
<color name="my_color">#RRGGBB</color>

<!-- In Code -->
ContextCompat.getColor(this, R.color.my_color)
```

### Add New Activity
```kotlin
// Create file: MyActivity.kt
class MyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my)
    }
}
```

Then add to AndroidManifest.xml:
```xml
<activity android:name=".MyActivity" android:exported="false" />
```

---

## 🚀 Performance Tips

- [x] Use lazy initialization for views
- [x] Firebase BOM for version management
- [x] ProGuard enabled (release builds)
- [x] Drawable resources cached
- [x] Location updates throttled

---

## 📞 Troubleshooting Quick Guide

| Issue | Solution |
|-------|----------|
| Build fails | `./gradlew clean`, `Sync Now` |
| Firebase error | Check google-services.json |
| Google Sign-In fails | Verify SHA-1 in Firebase Console |
| Location not updating | Grant permission, enable GPS |
| App crashes on login | Check Logcat for error details |
| UI not rendering | Clear app cache: `Settings > Apps > Hana > Storage > Clear Cache` |

---

## 🔗 External Resources

- **Firebase Docs**: https://firebase.google.com/docs
- **Android Developers**: https://developer.android.com
- **Material Design 3**: https://m3.material.io
- **Kotlin Docs**: https://kotlinlang.org/docs

---

## 📞 Development Team

- **Project**: Hana Wellness App
- **Version**: 1.0
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 36 (Android 15)
- **Language**: Kotlin
- **Build System**: Gradle 8.x (Kotlin DSL)

---

## ✅ Checklist Before Publishing

- [ ] All strings finalized
- [ ] Colors reviewed
- [ ] Icons optimized
- [ ] No hardcoded strings
- [ ] No debug logs remaining
- [ ] Permissions justified
- [ ] Privacy policy prepared
- [ ] Terms of service prepared
- [ ] SHA-1 fingerprint verified
- [ ] Firebase rules tested
- [ ] APK tested on device
- [ ] Bug fixes reviewed

---

**Last Updated**: April 11, 2026
**Status**: ✅ Production Ready

