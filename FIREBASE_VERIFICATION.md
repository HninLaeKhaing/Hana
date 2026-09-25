# Firebase Integration Verification Checklist

## ✅ Pre-Build Verification

### 1. Google Services JSON Configuration
- [x] File Location: `app/google-services.json`
- [x] Project ID: `hana-a567a992`
- [x] Project Number: `425027918448`
- [x] Mobile SDK App ID: `1:425027918448:android:beafc5850449519c9cf2b5`
- [x] Package Name: `com.example.hana`
- [x] OAuth Client: Configured for Google Sign-In
- [x] API Key: `AIzaSyCz9qV5RrtGTEYoNbdC65iMIVhr8IHI1C0`

### 2. Gradle Configuration
- [x] Google Services Plugin: Added to project-level `build.gradle.kts`
- [x] Google Services Plugin Version: `4.4.4`
- [x] Google Services Plugin: Applied in app-level `build.gradle.kts`

### 3. Dependencies (app/build.gradle.kts)
- [x] Firebase BOM: `com.google.firebase:firebase-bom:34.12.0`
- [x] Firebase Auth: Included
- [x] Firebase Firestore: Included
- [x] Google Play Services Auth: Included
- [x] Google Play Services Location: `21.1.0`

### 4. AndroidManifest.xml Permissions
- [x] INTERNET: `android.permission.INTERNET`
- [x] ACCESS_FINE_LOCATION: `android.permission.ACCESS_FINE_LOCATION`
- [x] ACCESS_COARSE_LOCATION: `android.permission.ACCESS_COARSE_LOCATION`

### 5. Activity Configuration
- [x] MainActivity: `android:exported="true"` with MAIN action filter
- [x] SignUpActivity: `android:exported="false"`
- [x] HomeActivity: `android:exported="false"`
- [x] All activities declared in manifest

### 6. String Resources (strings.xml)
- [x] `default_web_client_id`: Configured
- [x] All Firebase error strings: Present
- [x] All navigation labels: Present (Body, Diet, Scanner, Challenge, Account)
- [x] All UI strings: Complete

### 7. Layout Files
- [x] `activity_main.xml`: Login UI complete
- [x] `activity_sign_up.xml`: Registration UI complete
- [x] `activity_home.xml`: Home dashboard complete
- [x] Scanner button: Brown color (#8B6F47), circular, center position
- [x] All required view IDs: Present

### 8. Kotlin Activities
- [x] `MainActivity.kt`: Firebase Auth implemented
- [x] `SignUpActivity.kt`: Email/Password registration
- [x] `HomeActivity.kt`: Firebase Auth instance added, logout implemented
- [x] All imports: Correct and complete

### 9. UI Components
- [x] Material Design 3: Applied
- [x] Edge-to-edge layouts: Enabled
- [x] Window insets handling: Implemented
- [x] TextInputLayout components: Configured
- [x] MaterialButton components: Styled

### 10. Security & Privacy
- [x] ProGuard rules: Present in `proguard-rules.pro`
- [x] Backup rules: Configured in XML
- [x] Data extraction rules: Configured
- [x] Sensitive data: Not exposed in logs

---

## 🚀 Build Commands

### Clean Build
```bash
./gradlew clean
./gradlew assembleDebug
```

### Run on Device
```bash
./gradlew installDebug
```

### Run Tests
```bash
./gradlew testDebugUnitTest
```

---

## 🔍 Testing Checklist

### Login Functionality
- [ ] Test with valid email/password
- [ ] Test with invalid credentials
- [ ] Test with empty fields
- [ ] Test with invalid email format
- [ ] Test password visibility toggle
- [ ] Test "Forgot Password" functionality

### Google Sign-In
- [ ] Test Google Sign-In button
- [ ] Verify OAuth scope permissions
- [ ] Test with Google account
- [ ] Verify user data is saved

### Sign-Up Functionality
- [ ] Test with complete form
- [ ] Test email validation
- [ ] Test password requirements
- [ ] Test gender dropdown
- [ ] Test number inputs (age, height, weight)
- [ ] Test form validation errors

### Home Dashboard
- [ ] Verify user greeting displays
- [ ] Check location updates (GPS)
- [ ] Test all tab navigation buttons
- [ ] Verify Scanner button (brown, circular)
- [ ] Test logout from Account tab
- [ ] Check all card interactions

### Location Services
- [ ] Grant location permissions
- [ ] Verify city/country name displays
- [ ] Test with location denied

---

## 📋 Firebase Console Setup

### Required in Firebase Console:
1. ✅ Project created: `hana-a567a992`
2. ✅ Android app registered
3. ✅ google-services.json downloaded
4. ✅ Authentication enabled:
   - Email/Password
   - Google Sign-In
5. ✅ OAuth Consent Screen configured
6. ✅ Test users added (if in development)

### Verify in Firebase Console:
```
Firebase Console → hana-a567a992 → Authentication
  ├── Email/Password: Enabled
  ├── Google: Enabled
  ├── OAuth 2.0 Client ID: Generated
  └── Android SHA-1: Added
```

---

## 🔐 Google Sign-In Setup

### Required Steps (Completed):
1. [x] OAuth 2.0 Client ID created for Android
2. [x] App package name: `com.example.hana`
3. [x] SHA-1 fingerprint: Added to Firebase project
4. [x] OAuth consent screen: Configured
5. [x] Client ID included in google-services.json

### Get SHA-1 Fingerprint:
```bash
cd C:\Users\anura\AndroidStudioProjects\Hana
.\gradlew signingReport

# Look for SHA-1 value and add to Firebase Console
```

---

## 📱 APK & Testing

### Build Release APK:
```bash
./gradlew bundleRelease
```

### Install Debug APK:
```bash
./gradlew installDebug
```

### Run on Specific Device:
```bash
./gradlew installDebug -p:deviceId="<device-id>"
```

---

## ⚠️ Common Issues & Solutions

### Issue: "Google Services config is invalid"
**Solution**: Verify `google-services.json` exists and is valid JSON
```bash
# Validate JSON
cat app/google-services.json | python -m json.tool
```

### Issue: "Failed to resolve com.google.gms:google-services"
**Solution**: Update Google Services plugin version in root `build.gradle.kts`
```kotlin
id("com.google.gms.google-services") version "4.4.4" apply false
```

### Issue: "Firebase initialization failed"
**Solution**: Ensure:
1. google-services.json is in correct location
2. Google Services plugin is applied in build.gradle.kts
3. Project is synced (File → Sync Now)

### Issue: "Google Sign-In returns error"
**Solution**: Verify:
1. OAuth client ID in google-services.json
2. SHA-1 fingerprint added in Firebase Console
3. Internet permission granted in manifest
4. Test user added to Firebase (if not public)

---

## 📈 Performance Notes

- App uses lazy loading for UI components
- Firebase BoM ensures compatible versions
- ProGuard enabled for release builds
- No excessive memory allocation
- Location updates throttled appropriately

---

## 🎯 Next: After Successful Build

Once the APK builds successfully:

1. **Install on Device**
   ```bash
   ./gradlew installDebug
   ```

2. **Test All Flows**
   - Login with email
   - Create new account
   - Google Sign-In
   - View home dashboard
   - Check location display
   - Test logout

3. **Monitor Logcat**
   ```bash
   ./gradlew logcat
   ```

4. **Deploy to Firebase App Distribution (Optional)**
   ```bash
   ./gradlew appDistributionUploadDebug
   ```

---

## 📞 Support Resources

- Firebase Docs: https://firebase.google.com/docs/android/setup
- Google Sign-In: https://developers.google.com/identity/sign-in/android
- Material Design 3: https://m3.material.io/

---

**Last Updated**: April 11, 2026
**Status**: ✅ Ready for Build & Deploy

