# Firebase Setup Guide for Hana App

## ✅ Your build.gradle is ALREADY CONFIGURED

Your app already has:
- ✅ Google Services plugin
- ✅ Firebase Authentication (firebase-auth)
- ✅ Google Play Services Auth (play-services-auth)
- ✅ Firebase Firestore (for user data storage)

## 📋 Step-by-Step: Get Your Real google-services.json

### Step 1: Create Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click **"Create a project"** or use existing project
3. Enter project name: `hana-wellness` (or your preferred name)
4. Accept terms and create

### Step 2: Register Your Android App
1. In Firebase Console, click **"Add app"**
2. Select **Android** icon
3. Enter these details:
   - **Android package name**: `com.example.hana`
   - **App nickname** (optional): `Hana Wellness`
   - **SHA-1 certificate** (optional, needed for Google Sign-In production)

### Step 3: Get SHA-1 Certificate (Required for Google Sign-In)
Run this command in your project directory:

**Windows (PowerShell):**
```powershell
.\gradlew.bat signingReport
```

**Mac/Linux:**
```bash
./gradlew signingReport
```

Look for **SHA1** value in the output. Copy it.

### Step 4: Download google-services.json
1. After registering your app, Firebase shows you the **google-services.json** file
2. Click **"Download google-services.json"**
3. Save it to: `C:\Users\anura\AndroidStudioProjects\Hana\app\google-services.json`

### Step 5: Update Firebase Console
1. Go back to Firebase Console → Your App Settings
2. Paste your SHA-1 certificate from Step 3
3. Add Google Sign-In OAuth Client ID (if needed)

### Step 6: Enable Google Sign-In in Firebase
1. In Firebase Console → **Authentication** → **Sign-in method**
2. Enable **Google**
3. Select your OAuth app or create one

## 📝 File Structure After Setup

Your project should look like:
```
Hana/
├── app/
│   ├── google-services.json  ← Downloaded from Firebase
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/example/hana/
│   │   │   ├── MainActivity.kt (✅ Already has Google Sign-In)
│   │   │   ├── SignUpActivity.kt
│   │   │   └── HomeActivity.kt
│   │   └── res/
│   └── build.gradle.kts  (✅ Already configured)
├── build.gradle.kts  (✅ Already configured)
└── settings.gradle.kts
```

## 🔑 What Your google-services.json Should Contain

It will look like this (with YOUR actual values):
```json
{
  "project_info": {
    "project_number": "123456789012",
    "project_id": "your-firebase-project-id",
    "storage_bucket": "your-firebase-project-id.appspot.com"
  },
  "client": [
    {
      "client_info": {
        "mobilesdk_app_id": "1:123456789012:android:abcd1234efgh5678ijkl",
        "android_client_info": {
          "package_name": "com.example.hana"
        }
      },
      "oauth_client": [
        {
          "client_id": "123456789012-abcdefghijklmnopqrstuvwxyz.apps.googleusercontent.com",
          "client_type": 3
        }
      ],
      "api_key": [
        {
          "current_key": "AIzaSyBxyzAbcDeFgHiJkLmNoPqRsTuVwXyZ1234"
        }
      ],
      "services": {
        "appinvite_service": {
          "other_platform_oauth_client": [
            {
              "client_id": "123456789012-abcdefghijklmnopqrstuvwxyz.apps.googleusercontent.com",
              "client_type": 3
            }
          ]
        }
      }
    }
  ],
  "configuration_version": "1"
}
```

## ✅ Verification Checklist

After downloading and placing google-services.json:

- [ ] File exists at: `app/google-services.json`
- [ ] File contains your `project_id` (not "dummy")
- [ ] File contains `api_key` with real value (not "dummy_api_key")
- [ ] Package name is `com.example.hana`
- [ ] Firebase Console shows your Android app registered
- [ ] Google Sign-In is enabled in Firebase Authentication

## 🧪 Test the Setup

1. **Rebuild your project** in Android Studio:
   - Build → Clean Project
   - Build → Rebuild Project

2. **Run on emulator/device**

3. **Test Email Login**:
   - Email: `test@example.com`
   - Password: `123456`
   - (Create an account in Firebase Authentication first or use Sign-Up)

4. **Test Google Sign-In**:
   - Click "Continue with Google"
   - Select your Google account
   - Should navigate to Home page

## 🐛 Common Errors & Solutions

### Error: "API key not valid"
**Cause**: google-services.json has dummy values
**Solution**: Download real google-services.json from Firebase Console

### Error: "You haven't added the Google Play services..."
**Cause**: Missing SHA-1 certificate
**Solution**: Run `./gradlew signingReport` and add SHA-1 to Firebase Console

### Error: "Failed to authenticate with Google"
**Cause**: OAuth consent screen not configured
**Solution**: 
1. Go to Firebase Console → Google Cloud Console
2. Configure OAuth Consent Screen
3. Add Test Users

## 📞 Need Help?

If you need the SHA-1 certificate, run this:
```
./gradlew.bat signingReport
```

The output will show something like:
```
debugAndroidDebugKey
SHA1: AA:BB:CC:DD:EE:FF:...
```

Copy that SHA-1 value and add it to Firebase Console.

---

**Status**: Your app code is ✅ **READY**. Just need the real google-services.json from Firebase!

