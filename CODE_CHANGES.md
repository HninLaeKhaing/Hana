# 💻 CODE CHANGES - DETAILED VIEW

## 1. MainActivity.kt - performEmailSignIn() Method

### BEFORE (Broken - No real authentication):
```kotlin
private fun performEmailSignIn() {
    val email = etEmail.text.toString().trim().takeIf { it.isNotEmpty() }
    navigateToHome(email)  // ❌ Just navigates without checking password!
}
```

### AFTER (Fixed - Real Firebase auth):
```kotlin
private fun performEmailSignIn() {
    if (!validateInputs()) {
        return
    }

    setLoading(true)
    val email = etEmail.text.toString().trim()
    val password = etPassword.text.toString()

    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
            setLoading(false)
            if (task.isSuccessful) {
                val user = auth.currentUser
                val displayName = user?.displayName ?: user?.email ?: "User"
                navigateToHome(displayName)  // ✅ Only navigate on success
            } else {
                val errorMessage = when (task.exception?.javaClass?.simpleName) {
                    "FirebaseAuthInvalidUserException" -> getString(R.string.user_not_found)
                    "FirebaseAuthInvalidCredentialsException" -> getString(R.string.invalid_password)
                    else -> task.exception?.localizedMessage ?: getString(R.string.login_failed)
                }
                showToast(errorMessage)  // ✅ Show error message
            }
        }
}
```

---

## 2. SignUpActivity.kt - Multiple Changes

### A) Added Firebase Import
```kotlin
// ADDED:
import com.google.firebase.auth.FirebaseAuth
```

### B) Added Properties
```kotlin
// ADDED:
private lateinit var auth: FirebaseAuth
private lateinit var tilEmail: TextInputLayout
private lateinit var tilPassword: TextInputLayout
private lateinit var etEmail: TextInputEditText
private lateinit var etPassword: TextInputEditText
```

### C) Initialize Firebase in onCreate()
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(R.layout.activity_sign_up)
    
    // ... existing code ...
    
    // ADDED:
    auth = FirebaseAuth.getInstance()
    
    bindViews()
    setupGenderDropdown()
    setupInputFlow()
    setListeners()
}
```

### D) Updated bindViews()
```kotlin
private fun bindViews() {
    tilFullName = findViewById(R.id.tilFullName)
    tilAge = findViewById(R.id.tilAge)
    tilGender = findViewById(R.id.tilGender)
    tilHeight = findViewById(R.id.tilHeight)
    tilWeight = findViewById(R.id.tilWeight)
    
    // ADDED:
    tilEmail = findViewById(R.id.tilEmail)
    tilPassword = findViewById(R.id.tilPassword)

    etFullName = findViewById(R.id.etFullName)
    etAge = findViewById(R.id.etAge)
    actGender = findViewById(R.id.actGender)
    etHeight = findViewById(R.id.etHeight)
    etWeight = findViewById(R.id.etWeight)
    
    // ADDED:
    etEmail = findViewById(R.id.etEmail)
    etPassword = findViewById(R.id.etPassword)

    btnCreateAccount = findViewById(R.id.btnCreateAccount)
    tvBackToLogin = findViewById(R.id.tvBackToLogin)
}
```

### E) Updated setListeners()
```kotlin
private fun setListeners() {
    btnCreateAccount.setOnClickListener {
        if (!validateForm()) return@setOnClickListener
        // CHANGED: Now calls createAccountWithFirebase() instead of just navigating
        createAccountWithFirebase()
    }
    tvBackToLogin.setOnClickListener { finish() }

    etFullName.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) tilFullName.error = null }
    etAge.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) tilAge.error = null }
    actGender.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) tilGender.error = null }
    etHeight.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) tilHeight.error = null }
    etWeight.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) tilWeight.error = null }
    // ADDED:
    etEmail.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) tilEmail.error = null }
    etPassword.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) tilPassword.error = null }
}
```

### F) Updated validateForm()
```kotlin
private fun validateForm(): Boolean {
    tilFullName.error = null
    tilAge.error = null
    tilGender.error = null
    tilHeight.error = null
    tilWeight.error = null
    // ADDED:
    tilEmail.error = null
    tilPassword.error = null

    val fullName = etFullName.text?.toString()?.trim().orEmpty()
    val age = etAge.text?.toString()?.trim().orEmpty().toIntOrNull()
    val gender = actGender.text?.toString()?.trim().orEmpty()
    val height = etHeight.text?.toString()?.trim().orEmpty().toIntOrNull()
    val weight = etWeight.text?.toString()?.trim().orEmpty().toIntOrNull()
    // ADDED:
    val email = etEmail.text?.toString()?.trim().orEmpty()
    val password = etPassword.text?.toString().orEmpty()

    var valid = true

    if (fullName.isBlank()) {
        tilFullName.error = getString(R.string.error_full_name)
        valid = false
    }

    if (age == null || age < 1 || age > 120) {
        tilAge.error = getString(R.string.error_age)
        valid = false
    }

    if (gender.isBlank()) {
        tilGender.error = getString(R.string.error_gender)
        valid = false
    }

    if (height == null || height < 50 || height > 280) {
        tilHeight.error = getString(R.string.error_height)
        valid = false
    }

    if (weight == null || weight < 20 || weight > 400) {
        tilWeight.error = getString(R.string.error_weight)
        valid = false
    }

    // ADDED:
    if (email.isBlank() || !email.contains("@")) {
        tilEmail.error = getString(R.string.enter_email)
        valid = false
    }

    if (password.length < 6) {
        tilPassword.error = getString(R.string.enter_password)
        valid = false
    }

    return valid
}
```

### G) NEW - createAccountWithFirebase() Method
```kotlin
// COMPLETELY NEW METHOD:
private fun createAccountWithFirebase() {
    btnCreateAccount.isEnabled = false
    btnCreateAccount.text = getString(R.string.auth_loading)

    val email = etEmail.text?.toString()?.trim().orEmpty()
    val password = etPassword.text?.toString().orEmpty()
    val fullName = etFullName.text?.toString()?.trim().orEmpty()

    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                showToast(getString(R.string.signup_success))
                navigateToHome(fullName)
            } else {
                btnCreateAccount.isEnabled = true
                btnCreateAccount.text = getString(R.string.create_account)
                val errorMessage = when (task.exception?.javaClass?.simpleName) {
                    "FirebaseAuthWeakPasswordException" -> getString(R.string.weak_password)
                    "FirebaseAuthUserCollisionException" -> getString(R.string.user_exists)
                    else -> task.exception?.localizedMessage ?: getString(R.string.signup_failed)
                }
                showToast(errorMessage)
            }
        }
}
```

---

## 3. activity_sign_up.xml - Added Email & Password Fields

### ADDED after weight field, before separator line:
```xml
<TextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="24dp"
    android:text="@string/email"
    android:textAllCaps="true"
    android:textColor="@color/label_text"
    android:textSize="14sp" />

<com.google.android.material.textfield.TextInputLayout
    android:id="@+id/tilEmail"
    style="@style/Widget.Material3.TextInputLayout.FilledBox"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="8dp"
    app:boxBackgroundColor="@color/input_background"
    app:boxCornerRadiusBottomEnd="12dp"
    app:boxCornerRadiusBottomStart="12dp"
    app:boxCornerRadiusTopEnd="12dp"
    app:boxCornerRadiusTopStart="12dp"
    app:boxStrokeWidth="0dp"
    app:boxStrokeWidthFocused="0dp"
    app:errorEnabled="true"
    app:hintEnabled="false">

    <com.google.android.material.textfield.TextInputEditText
        android:id="@+id/etEmail"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="@string/email_placeholder"
        android:imeOptions="actionNext"
        android:inputType="textEmailAddress"
        android:minHeight="64dp"
        android:paddingStart="16dp"
        android:paddingTop="8dp"
        android:paddingEnd="16dp"
        android:paddingBottom="8dp"
        android:textColor="@color/brand_text"
        android:textColorHint="@color/placeholder_text" />
</com.google.android.material.textfield.TextInputLayout>

<TextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="24dp"
    android:text="@string/password"
    android:textAllCaps="true"
    android:textColor="@color/label_text"
    android:textSize="14sp" />

<com.google.android.material.textfield.TextInputLayout
    android:id="@+id/tilPassword"
    style="@style/Widget.Material3.TextInputLayout.FilledBox"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="8dp"
    app:boxBackgroundColor="@color/input_background"
    app:boxCornerRadiusBottomEnd="12dp"
    app:boxCornerRadiusBottomStart="12dp"
    app:boxCornerRadiusTopEnd="12dp"
    app:boxCornerRadiusTopStart="12dp"
    app:boxStrokeWidth="0dp"
    app:boxStrokeWidthFocused="0dp"
    app:errorEnabled="true"
    app:hintEnabled="false">

    <com.google.android.material.textfield.TextInputEditText
        android:id="@+id/etPassword"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="@string/password_placeholder"
        android:imeOptions="actionDone"
        android:inputType="textPassword"
        android:minHeight="64dp"
        android:paddingStart="16dp"
        android:paddingTop="8dp"
        android:paddingEnd="16dp"
        android:paddingBottom="8dp"
        android:textColor="@color/brand_text"
        android:textColorHint="@color/placeholder_text" />
</com.google.android.material.textfield.TextInputLayout>
```

---

## 4. strings.xml - Added 8 New Strings

```xml
<string name="email">Email</string>
<string name="user_not_found">User not found. Please create an account.</string>
<string name="invalid_password">Invalid password. Please try again.</string>
<string name="login_failed">Login failed. Please try again.</string>
<string name="weak_password">Password is too weak. Use at least 6 characters.</string>
<string name="user_exists">Email already exists. Please use a different email.</string>
<string name="signup_failed">Signup failed. Please try again.</string>
<string name="create_account">Create Account</string>
```

---

## Summary of Changes

| Type | File | Lines Changed | Reason |
|------|------|---------------|--------|
| Logic | MainActivity.kt | ~25 | Proper Firebase auth |
| Logic | SignUpActivity.kt | ~60 | Firebase account creation |
| UI | activity_sign_up.xml | ~80 | Email & password fields |
| Config | strings.xml | +8 | Error messages |

**Total: ~173 lines of code changes** ✅

