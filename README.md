`# Hana Android App

This project now includes a real XML login screen (not an image) with working Firebase authentication actions:

- Email/password login
- Create account screen (full profile form from login)
- Forgot password reset email
- Google sign-in button wired to Firebase Auth

## Firebase setup required

1. Create a Firebase project.
2. Add an Android app with package name `com.example.hana`.
3. Enable **Authentication** providers:
   - Email/Password
   - Google
4. Download `google-services.json` and place it at:
   - `app/google-services.json`
5. In Firebase Console, add your app SHA-1/SHA-256 fingerprints for Google sign-in.

## Run

Open the project in Android Studio, sync Gradle, then run `app` on a device/emulator.

## Notes

- If `google-services.json` is missing, the app shows a clear message.
- Google sign-in depends on valid OAuth setup in Firebase and SHA fingerprints.

