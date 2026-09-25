# 🚀 Quick Firebase Setup - 5 Minutes

## What You Need to Do:

### 1️⃣ Get SHA-1 Certificate (2 minutes)

Open PowerShell in your project folder and run:
```powershell
.\gradlew.bat signingReport
```

Wait for it to complete, then look for the SHA1 line:
```
debugAndroidDebugKey
  SHA1: AA:BB:CC:DD:EE:FF:00:11:22:33:44:55:66:77:88:99:AA:BB:CC:DD
```

**COPY THIS SHA-1 VALUE** ✂️

---

### 2️⃣ Create Firebase Project (2 minutes)

1. Go to: https://console.firebase.google.com/
2. Click **"Create a project"**
3. Enter project name: `hana-wellness`
4. Click through the steps
5. When done, you'll see your project dashboard

---

### 3️⃣ Register Your Android App (1 minute)

1. In Firebase Console, click the **Android icon** (or "+ Add app")
2. Fill in:
   - **Package name**: `com.example.hana`
   - **App nickname**: `Hana Wellness` (optional)
   - **SHA-1**: Paste the value from Step 1
3. Click **"Register app"**

---

### 4️⃣ Download google-services.json (1 minute)

1. Firebase will show you a download button
2. Click **"Download google-services.json"**
3. This will download a file to your Downloads folder

---

### 5️⃣ Place google-services.json in Your Project

1. Open your Downloads folder
2. Copy the `google-services.json` file
3. Navigate to: `C:\Users\anura\AndroidStudioProjects\Hana\app\`
4. Paste it there (it should replace the template file)

---

### 6️⃣ Enable Google Sign-In in Firebase (1 minute)

1. In Firebase Console, go to **Authentication**
2. Click **"Sign-in method"**
3. Find **"Google"** and click it
4. Toggle **"Enable"** ✅
5. Save

---

### 7️⃣ Add Users to Firebase (Optional for Testing)

1. In Firebase Console → **Authentication** → **Users**
2. Click **"Add user"**
3. Enter test email and password:
   - Email: `test@example.com`
   - Password: `Test@123456`

---

### 8️⃣ Rebuild Your Project

In Android Studio:
1. **Build** → **Clean Project**
2. **Build** → **Rebuild Project**
3. Wait for it to complete (green checkmark)

---

## ✅ Done! Now Test It

1. **Run your app** on emulator/device
2. **Test Email Login**:
   - Enter: `test@example.com` / `Test@123456`
   - Should go to Home page ✅

3. **Test Google Sign-In**:
   - Click "Continue with Google"
   - Select your Google account
   - Should go to Home page ✅

---

## 🐛 If You Get "API Key Not Valid" Error

It means your google-services.json still has placeholder values. Make sure:
- ✅ You downloaded the REAL file from Firebase Console
- ✅ File is in: `app/google-services.json`
- ✅ It's NOT a template (check it has your project_id, not "YOUR_PROJECT_ID")

---

## 📱 Create Test User in Firebase

If you want to test email login:

1. Firebase Console → **Authentication** → **Users**
2. Click **"Add user"**
3. Email: `demo@hana.com`
4. Password: `Demo@1234`
5. Click **"Add user"**

Then in your app:
- Email: `demo@hana.com`
- Password: `Demo@1234`

---

## 🎉 All Done!

Your app is now connected to Firebase! 

**Features working:**
- ✅ Email & Password Login
- ✅ Google Sign-In
- ✅ User Registration
- ✅ Password Reset
- ✅ Home Page with user greeting
- ✅ Bottom navigation with Scanner button

Enjoy! 🚀

