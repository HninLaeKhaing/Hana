# 🎨 Hana App - Visual Architecture & Flow

## 📱 User Flow Diagram

```
┌─────────────────────────────────────────────┐
│         HANA WELLNESS APP FLOW              │
└─────────────────────────────────────────────┘

                    ┌──────────────┐
                    │   App Start  │
                    └──────┬───────┘
                           │
                    ┌──────▼───────┐
                    │ MainActivity │ (Login Screen)
                    │   (Login)    │
                    └──────┬───────┘
                           │
          ┌────────────────┼────────────────┐
          │                │                │
    ┌─────▼──────┐  ┌─────▼──────┐  ┌──────▼──────┐
    │   Email    │  │   Google   │  │   Sign Up  │
    │  + Pass    │  │  Sign-In   │  │   Button   │
    │   Login    │  │            │  │            │
    └─────┬──────┘  └─────┬──────┘  └──────┬──────┘
          │                │                │
          │         ┌──────▼──────┐        │
          │         │ SignUpActivity
          │         │ (Register)  │        │
          │         └──────┬──────┘        │
          │                │                │
          └────────────────┼────────────────┘
                           │
                    ┌──────▼───────┐
                    │ HomeActivity │ (Dashboard)
                    │ (Dashboard)  │
                    └──────┬───────┘
                           │
    ┌──────────────────────────────────────────────┐
    │          BOTTOM NAVIGATION (5 TABS)         │
    └──────────────────────────────────────────────┘
    │ Body │ Diet │ 🔘 Scanner 🔘 │ Challenge │ Acc │
    └────────────────────────────────────────────────┘
                           │
         ┌─────────────────┼─────────────────┐
         │                 │                 │
    ┌────▼─────┐     ┌─────▼──────┐    ┌────▼────┐
    │  Content │     │  Location  │    │ Logout  │
    │ Sections │     │  Display   │    │  Flow   │
    └──────────┘     └────────────┘    └────┬────┘
                                            │
                                    ┌───────▼─────┐
                                    │ SignOut &   │
                                    │ Back to     │
                                    │ MainActivity│
                                    └─────────────┘
```

---

## 🏗️ App Architecture

```
┌─────────────────────────────────────────────────┐
│            HANA WELLNESS APPLICATION            │
├─────────────────────────────────────────────────┤
│                                                 │
│  ┌──────────────────────────────────────────┐  │
│  │         USER INTERFACE LAYER             │  │
│  ├──────────────────────────────────────────┤  │
│  │ • MainActivity (Login Screen)            │  │
│  │ • SignUpActivity (Registration)          │  │
│  │ • HomeActivity (Dashboard)               │  │
│  │ • Material Design 3 Components           │  │
│  │ • 5 Bottom Navigation Tabs               │  │
│  └──────────────────────────────────────────┘  │
│           ↓ Communicates with ↓                │
│  ┌──────────────────────────────────────────┐  │
│  │      BUSINESS LOGIC LAYER                │  │
│  ├──────────────────────────────────────────┤  │
│  │ • Authentication Handler                 │  │
│  │ • Location Manager                       │  │
│  │ • Session Management                     │  │
│  │ • Data Validation                        │  │
│  └──────────────────────────────────────────┘  │
│           ↓ Communicates with ↓                │
│  ┌──────────────────────────────────────────┐  │
│  │      BACKEND SERVICES LAYER              │  │
│  ├──────────────────────────────────────────┤  │
│  │ • Firebase Authentication                │  │
│  │ • Firebase Firestore (Database)          │  │
│  │ • Google Play Services (Location)        │  │
│  │ • Google Sign-In OAuth                   │  │
│  └──────────────────────────────────────────┘  │
│                                                 │
└─────────────────────────────────────────────────┘
```

---

## 📂 File Structure Tree

```
Hana/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── java/com/example/hana/
│   │   │   ├── MainActivity.kt            ← Login
│   │   │   ├── SignUpActivity.kt          ← Register
│   │   │   └── HomeActivity.kt            ← Dashboard
│   │   └── res/
│   │       ├── layout/
│   │       │   ├── activity_main.xml      ← Login UI
│   │       │   ├── activity_sign_up.xml   ← Register UI
│   │       │   └── activity_home.xml      ← Dashboard UI
│   │       ├── values/
│   │       │   ├── strings.xml            ← Text strings
│   │       │   ├── colors.xml             ← Color palette
│   │       │   ├── arrays.xml             ← Data arrays
│   │       │   └── themes.xml             ← App styling
│   │       ├── drawable/
│   │       └── mipmap/
│   ├── google-services.json               ← Firebase config
│   └── build.gradle.kts                   ← Dependencies
├── build.gradle.kts                       ← Project config
├── gradle.properties                      ← Gradle settings
└── gradlew / gradlew.bat                  ← Build tools
```

---

## 🔐 Firebase Architecture

```
┌─────────────────────────────────────────────┐
│         FIREBASE PROJECT: hana-a567a992    │
├─────────────────────────────────────────────┤
│                                             │
│  ┌───────────────────────────────────────┐ │
│  │    AUTHENTICATION MODULE              │ │
│  ├───────────────────────────────────────┤ │
│  │ ┌──────────┐  ┌──────────────────┐   │ │
│  │ │ Email &  │  │  Google OAuth    │   │ │
│  │ │ Password │  │  (Web Client ID) │   │ │
│  │ └──────────┘  └──────────────────┘   │ │
│  │        ↓              ↓                │ │
│  │    Creates user account in Firebase  │ │
│  └───────────────────────────────────────┘ │
│                                             │
│  ┌───────────────────────────────────────┐ │
│  │    FIRESTORE DATABASE (Future)        │ │
│  ├───────────────────────────────────────┤ │
│  │ • User Profiles                       │ │
│  │ • Wellness Data                       │ │
│  │ • Activity History                    │ │
│  └───────────────────────────────────────┘ │
│                                             │
│  ┌───────────────────────────────────────┐ │
│  │    STORAGE (For Images/Media)         │ │
│  ├───────────────────────────────────────┤ │
│  │ • Profile Pictures (Future)           │ │
│  │ • Workout Videos (Future)             │ │
│  └───────────────────────────────────────┘ │
│                                             │
└─────────────────────────────────────────────┘
```

---

## 🎨 UI Layout - Home Activity

```
┌────────────────────────────────────────┐
│  📱 HOME DASHBOARD (HomeActivity)      │
├────────────────────────────────────────┤
│                                        │
│  [H]  Welcome, User!                   │  ← Avatar + Greeting
│       📍 Tokyo, JP                     │  ← Location
│                                        │
│  ┌──────────────────────────────────┐ │
│  │   BE IN YOUR JOURNEY             │  ← Hero Card
│  │                                  │  │ (Primary Action)
│  │   [START NOW →]                  │  │
│  └──────────────────────────────────┘ │
│                                        │
│  EXPERIENCE HANA              [VIEW ALL]│  ← Section Header
│                                        │
│  ┌──────────┐ ┌──────────────────┐   │
│  │ FITPASS  │ │ FITFEAST        │   │  ← Card Grid
│  └──────────┘ └──────────────────┘   │
│  ┌──────────┐ ┌──────────────────┐   │
│  │ FITCOACH │ │ STORE           │   │
│  └──────────┘ └──────────────────┘   │
│                                        │
│  ┌──────────────────────────────────┐ │
│  │ DAILY RITUAL                     │  ← Card Row
│  │ 15 min Morning Flow        [▶]  │  │
│  └──────────────────────────────────┘ │
│                                        │
│  ┌──────────────────┐ ┌────────────┐  │
│  │ MINDFUL BREATH   │ │ POST WORK  │  ← Card Grid
│  │ 5 MINS           │ │ STRETCH    │
│  └──────────────────┘ │ 12 MINS    │
│                       └────────────┘  │
│                                        │
│  ┌────────────────────────────────┐   │
│  │ BODY │ DIET │ 🔘 │ CHALL │ ACC│   │  ← Bottom Nav
│  │      │      │SCAN│ ENGE  │ OUT│   │
│  └────────────────────────────────┘   │
│   ↑      ↑      ↑      ↑       ↑      │
│  Brown #8B6F47, Circular, Camera icon │
│                                        │
└────────────────────────────────────────┘
```

---

## 🔄 Authentication Flow

```
LOGIN FLOW:
┌─────────┐
│ START   │
└────┬────┘
     │
     ├─→ EMAIL/PASSWORD LOGIN
     │   ├─→ Validate email format
     │   ├─→ Check password length (6+)
     │   ├─→ Firebase.auth.signInWithEmailAndPassword()
     │   ├─→ ✅ Success → Go to Home
     │   └─→ ❌ Error → Show error message
     │
     ├─→ GOOGLE SIGN-IN
     │   ├─→ Google Sign-In Activity
     │   ├─→ User selects account
     │   ├─→ Firebase.auth.signInWithCredential()
     │   ├─→ ✅ Success → Go to Home
     │   └─→ ❌ Error → Show error message
     │
     └─→ CREATE ACCOUNT
         ├─→ Go to SignUpActivity
         ├─→ Collect user data (name, age, height, etc)
         ├─→ Firebase.auth.createUserWithEmailAndPassword()
         ├─→ ✅ Success → Go to Home
         └─→ ❌ Error → Show error message


LOGOUT FLOW:
┌─────────┐
│ AT HOME │
└────┬────┘
     │
     └─→ TAP ACCOUNT TAB
         ├─→ Show toast "Logging out..."
         ├─→ Firebase.auth.signOut()
         ├─→ Clear user session
         └─→ RETURN TO LOGIN
```

---

## 📊 Data Flow

```
USER INPUT
    ↓
[View (UI)]
    ↓
[Activity]
    ↓
[Authentication Handler]
    ↓
[Firebase Auth Service]
    ↓
[Firebase Backend]
    ↓
[Response]
    ↓
[Update UI with Result]
    ↓
DISPLAY TO USER
```

---

## 🎯 Navigation Tab System

```
BOTTOM NAVIGATION (5 TABS)
─────────────────────────────────────

Position 1 (Left):
└─ BODY Tab
   └─ Icon: Menu View
   └─ Color: Primary (when active)
   └─ Action: Scroll to Body section

Position 2:
└─ DIET Tab
   └─ Icon: Menu Search
   └─ Color: Primary (when active)
   └─ Action: Scroll to Diet section

Position 3 (CENTER):
└─ 🔘 SCANNER Button (SPECIAL)
   ├─ Background Color: #8B6F47 (Brown)
   ├─ Size: 56dp diameter
   ├─ Shape: Circular (28dp radius)
   ├─ Icon: Camera
   ├─ Icon Color: White
   └─ Always elevated above other tabs

Position 4:
└─ CHALLENGE Tab (formerly "Wellness")
   └─ Icon: Menu Places
   └─ Color: Primary (when active)
   └─ Action: Scroll to Challenge section

Position 5 (Right):
└─ ACCOUNT Tab
   └─ Icon: Menu Manage
   └─ Color: Primary (when active)
   └─ Action: Logout & Return to Login
```

---

## 🔐 Security Architecture

```
┌─────────────────────────────────────────┐
│         SECURITY LAYERS                 │
├─────────────────────────────────────────┤
│                                         │
│  LAYER 1: AUTHENTICATION                │
│  ├─ Firebase Auth (Secure)              │
│  ├─ OAuth 2.0 for Google Sign-In        │
│  ├─ Password validation (min 6 chars)   │
│  └─ SSL/TLS for all connections         │
│                                         │
│  LAYER 2: PERMISSIONS                   │
│  ├─ Runtime permissions for location    │
│  ├─ Internet permission                 │
│  ├─ Manifest declaration                │
│  └─ User must grant approval            │
│                                         │
│  LAYER 3: DATA PROTECTION               │
│  ├─ No sensitive data in logs           │
│  ├─ ProGuard obfuscation (release)      │
│  ├─ Secure storage via Firebase         │
│  └─ No hardcoded credentials            │
│                                         │
│  LAYER 4: NETWORK SECURITY              │
│  ├─ HTTPS only                          │
│  ├─ API key protection                  │
│  ├─ Firebase security rules             │
│  └─ No man-in-the-middle attacks        │
│                                         │
└─────────────────────────────────────────┘
```

---

## 📈 App Lifecycle

```
LIFECYCLE EVENTS
────────────────────────────

1. onCreate()
   ├─ Initialize Firebase
   ├─ Load layout
   ├─ Bind views
   └─ Set up listeners

2. onStart()
   └─ Resume UI updates

3. onResume()
   ├─ Start location updates
   ├─ Request permissions
   └─ Refresh data

4. onPause()
   └─ Pause location updates

5. onStop()
   └─ Save state

6. onDestroy()
   ├─ Sign out (if on Account tab)
   └─ Clean up resources
```

---

## ✅ Version Diagram

```
HANA WELLNESS APP v1.0
└─────────────────────────
    ├─ Core Features
    │  ├─ ✅ Authentication (Email/Pass + Google)
    │  ├─ ✅ Dashboard with user greeting
    │  ├─ ✅ Location services
    │  ├─ ✅ 5 navigation tabs
    │  └─ ✅ Logout functionality
    │
    ├─ UI/UX
    │  ├─ ✅ Material Design 3
    │  ├─ ✅ Edge-to-edge layouts
    │  ├─ ✅ Brown scanner button
    │  ├─ ✅ Smooth animations
    │  └─ ✅ Professional styling
    │
    ├─ Backend
    │  ├─ ✅ Firebase Auth
    │  ├─ ✅ Firestore (future data)
    │  ├─ ✅ Google Play Services
    │  └─ ✅ Location API
    │
    └─ Documentation
       ├─ ✅ Complete Setup Guide
       ├─ ✅ Firebase Verification
       ├─ ✅ Troubleshooting Guide
       ├─ ✅ Quick Reference
       └─ ✅ This File!
```

---

**Visual Guide Complete!**
**For more details, see COMPLETE_SETUP_GUIDE.md**

