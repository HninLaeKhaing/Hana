# Hana Wellness App - Complete Setup Guide

## Project Overview
This is an Android wellness application built with Kotlin that integrates Firebase Authentication and Google Sign-In, providing users with a personalized wellness experience.

## ✅ What's Implemented

### 1. **Firebase Authentication**
- ✅ Email/Password Login
- ✅ Email/Password Sign-Up
- ✅ Google Sign-In Integration
- ✅ Password Reset Functionality
- ✅ Error Handling for auth failures

### 2. **Navigation & UI**
- ✅ Login Screen (MainActivity)
- ✅ Sign-Up Screen (SignUpActivity)
- ✅ Home Dashboard (HomeActivity)
- ✅ Bottom Tab Navigation:
  - **Body** - Fitness section
  - **Diet** - Nutrition section
  - **Scanner** - Central action button (brown color, circular)
  - **Challenge** - Daily challenges section
  - **Account** - User profile & Logout

### 3. **Location Services**
- ✅ GPS Location Detection
- ✅ Displays City/Region Name (e.g., "Tokyo, JP")
- ✅ Location Permissions Handling

### 4. **User Experience**
- ✅ Welcome Screen with branding
- ✅ User greeting with name
- ✅ Material Design 3 components
- ✅ Edge-to-Edge layouts
- ✅ Smooth animations and transitions

---

## 📁 File Structure

```
app/
├── src/main/
│   ├── java/com/example/hana/
│   │   ├── MainActivity.kt           # Login Screen
│   │   ├── SignUpActivity.kt         # Registration Screen
│   │   └── HomeActivity.kt           # Home Dashboard
│   ├── res/
│   │   ├── layout/
│   │   │   ├── activity_main.xml     # Login UI
│   │   │   ├── activity_sign_up.xml  # Registration UI
│   │   │   └── activity_home.xml     # Home Dashboard UI
│   │   ├── values/
│   │   │   ├── strings.xml           # All text strings
│   │   │   ├── colors.xml            # Color palette
│   │   │   ├── arrays.xml            # Gender options
│   │   │   └── themes.xml            # App styling
│   │   └── drawable/
│   │       └── (app icons)
│   └── AndroidManifest.xml           # App configuration
├── google-services.json              # Firebase configuration
└── build.gradle.kts                  # Dependencies
```

---

## 🔐 Firebase Configuration

### google-services.json
The file is already configured with:
- **Project ID**: `hana-a567a992`
- **API Key**: Configured
- **OAuth Client**: Set up for Google Sign-In
- **Location**: `app/google-services.json`

### Required Dependencies
All Firebase and Google Play Services dependencies are included in `build.gradle.kts`:
- Firebase Authentication
- Firebase Firestore
- Google Play Services Location
- Google Play Services Auth

---

## 🚀 How to Build & Run

### Prerequisites
1. Android Studio installed
2. Java 11+ installed
3. Android SDK 24+ (API Level 24 minimum)

### Build Steps

1. **Open Project**
   ```
   File → Open → Select Hana project folder
   ```

2. **Sync Gradle Files**
   ```
   File → Sync Now
   ```

3. **Build APK**
   ```
   Build → Build Bundle(s)/APK(s) → Build APK(s)
   ```

4. **Run on Device/Emulator**
   ```
   Run → Run 'app'
   ```

---

## 📱 App Features

### Login Flow
1. User enters email and password
2. Click "Login ->" button
3. Firebase validates credentials
4. On success → Navigates to Home
5. On failure → Shows error message

### Google Sign-In
1. Click "Continue with Google" button
2. Google Sign-In dialog appears
3. User selects/authenticates with Google account
4. On success → Automatically creates/logs in user
5. Navigates to Home dashboard

### Sign-Up Flow
1. Click "Create New Account" on login screen
2. Fill in required fields:
   - Full Name
   - Age
   - Gender (Dropdown)
   - Height (cm)
   - Weight (kg)
   - Email
   - Password (min 6 chars)
3. Click "Create Account"
4. Firebase creates user account
5. Navigates to Home with user's name

### Home Dashboard
- **Header**: Greeting with user's name
- **Location**: Displays current city/country
- **Content Sections**:
  - Journey Card (motivational message)
  - Experience Hana (main content area)
  - Cards: FitPass, FitFeast, FitCoach, Store
  - Daily Ritual (15-min morning flow)
  - Mindful Breathing (5 mins)
  - Post-Work Stretch (12 mins)

### Bottom Navigation
- **Body Tab**: Scrolls to experience section
- **Diet Tab**: Scrolls to nutrition section
- **Scanner Tab**: Central brown button (action point)
- **Challenge Tab**: Scrolls to challenges section
- **Account Tab**: Logs out user and returns to login

---

## 🎨 UI Design Details

### Colors
- **Primary**: Defined in `colors.xml`
- **Background**: Soft beige/cream
- **Text**: Dark brown for headers, medium gray for body
- **Scanner Button**: #8B6F47 (Brown)
- **Accents**: Orange/warm tones

### Typography
- **Headers**: Bold, 20-34sp
- **Body**: Regular, 13-16sp
- **Labels**: Bold uppercase, 11-14sp

### Components
- Material Card Views (elevated panels)
- Material Buttons (primary actions)
- Text Input Layouts (form fields)
- Nested Scroll View (main container)
- Linear/Constraint Layouts (structure)

---

## 🔌 Permissions

The app requests:
- `android.permission.INTERNET` - For Firebase communication
- `android.permission.ACCESS_FINE_LOCATION` - For precise GPS location
- `android.permission.ACCESS_COARSE_LOCATION` - For approximate location

Users grant these during app first use.

---

## 📊 Data Flow

```
Login/SignUp
    ↓
Firebase Auth
    ↓
MainActivity/SignUpActivity
    ↓
HomeActivity
    ↓
Location Services & Content Display
```

---

## 🐛 Troubleshooting

### Build Errors
- **"Cannot resolve symbol"**: Run `Build → Clean Project` then `Sync Now`
- **"No matching variant"**: Update gradle version in `gradle.properties`
- **Missing google-services.json**: Ensure file is in `app/` folder

### Runtime Errors
- **Firebase Config Missing**: google-services.json not found or invalid
- **Google Sign-In Failed**: Check OAuth configuration in Firebase Console
- **Location Permission Denied**: User rejected permission request

### Gradle Build Issues
```bash
# Clean and rebuild
./gradlew clean
./gradlew build --stacktrace
```

---

## 🔄 Next Steps / Enhancements

Potential features to add:
1. User Profile Page with profile picture
2. Wellness History & Statistics
3. Push Notifications
4. Social Sharing
5. Meal Tracking Database
6. Fitness Tracker Integration
7. Video Content Library
8. In-app Messaging

---

## 📝 Configuration Files Summary

### build.gradle.kts (Project Level)
- Google Services Plugin configured
- Android Gradle Plugin configured

### build.gradle.kts (App Level)
- Kotlin DSL syntax
- Firebase BOM for dependency management
- All required libraries included

### AndroidManifest.xml
- 3 Activities configured
- Permissions declared
- MainActivity as launcher

### google-services.json
- Firebase project credentials
- OAuth client configuration
- API keys configured

---

## 📞 Support

If you encounter issues:
1. Check Firebase Console (console.firebase.google.com)
2. Verify google-services.json is valid
3. Ensure Java 11+ is installed
4. Check Android SDK version (API 24+)
5. Review Logcat for detailed error messages

---

## ✨ Project Status

✅ Authentication System: Complete
✅ Navigation Framework: Complete
✅ UI/UX Design: Complete
✅ Location Services: Complete
✅ Firebase Integration: Complete
✅ Google Sign-In: Configured

🎉 **App is ready for development of feature content!**

