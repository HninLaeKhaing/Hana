package com.example.hana

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class AccountActivity : AppCompatActivity() {

    private lateinit var tilFullName: TextInputLayout
    private lateinit var tilAge: TextInputLayout
    private lateinit var tilGender: TextInputLayout
    private lateinit var tilHeight: TextInputLayout
    private lateinit var tilWeight: TextInputLayout
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout

    private lateinit var etFullName: TextInputEditText
    private lateinit var etAge: TextInputEditText
    private lateinit var actGender: AutoCompleteTextView
    private lateinit var etHeight: TextInputEditText
    private lateinit var etWeight: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText

    private lateinit var btnBack: MaterialButton
    private lateinit var btnSaveAccount: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(layoutId("activity_account"))

        ViewCompat.setOnApplyWindowInsetsListener(findViewById<View>(id("accountRoot"))) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bindViews()
        setupGenderDropdown()
        loadProfile()
        setListeners()
    }

    private fun bindViews() {
        tilFullName = findViewById(id("tilFullName"))
        tilAge = findViewById(id("tilAge"))
        tilGender = findViewById(id("tilGender"))
        tilHeight = findViewById(id("tilHeight"))
        tilWeight = findViewById(id("tilWeight"))
        tilEmail = findViewById(id("tilEmail"))
        tilPassword = findViewById(id("tilPassword"))

        etFullName = findViewById(id("etFullName"))
        etAge = findViewById(id("etAge"))
        actGender = findViewById(id("actGender"))
        etHeight = findViewById(id("etHeight"))
        etWeight = findViewById(id("etWeight"))
        etEmail = findViewById(id("etEmail"))
        etPassword = findViewById(id("etPassword"))

        btnBack = findViewById(id("btnBack"))
        btnSaveAccount = findViewById(id("btnSaveAccount"))
    }

    private fun setupGenderDropdown() {
        val options = resources.getStringArray(R.array.gender_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, options)
        actGender.setAdapter(adapter)
        actGender.setOnClickListener { actGender.showDropDown() }
    }

    private fun loadProfile() {
        val profile = ProfileStore.loadProfile(this)
        if (profile == null) {
            Toast.makeText(this, R.string.user_not_found, Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        etFullName.setText(profile.fullName)
        etAge.setText(profile.age.takeIf { it > 0 }?.toString().orEmpty())
        actGender.setText(profile.gender, false)
        etHeight.setText(profile.heightCm.takeIf { it > 0 }?.toString().orEmpty())
        etWeight.setText(profile.weightKg.takeIf { it > 0 }?.toString().orEmpty())
        etEmail.setText(profile.email)
        etPassword.setText(profile.password)
    }

    private fun setListeners() {
        btnBack.setOnClickListener { finish() }
        btnSaveAccount.setOnClickListener { saveProfile() }

        etWeight.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                saveProfile()
                true
            } else {
                false
            }
        }
    }

    private fun saveProfile() {
        clearErrors()

        val fullName = etFullName.text?.toString()?.trim().orEmpty()
        val age = etAge.text?.toString()?.trim().orEmpty().toIntOrNull()
        val gender = actGender.text?.toString()?.trim().orEmpty()
        val height = etHeight.text?.toString()?.trim().orEmpty().toIntOrNull()
        val weight = etWeight.text?.toString()?.trim().orEmpty().toIntOrNull()
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
        if (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.error = getString(R.string.enter_email)
            valid = false
        }
        if (password.length < 6) {
            tilPassword.error = getString(R.string.enter_password)
            valid = false
        }

        if (!valid) return

        val safeAge = age ?: return
        val safeHeight = height ?: return
        val safeWeight = weight ?: return

        ProfileStore.saveProfile(
            this,
            UserProfile(
                fullName = fullName,
                age = safeAge,
                gender = gender,
                heightCm = safeHeight,
                weightKg = safeWeight,
                email = email,
                password = password
            )
        )

        Toast.makeText(this, getString(R.string.account_saved), Toast.LENGTH_SHORT).show()
    }

    private fun clearErrors() {
        tilFullName.error = null
        tilAge.error = null
        tilGender.error = null
        tilHeight.error = null
        tilWeight.error = null
        tilEmail.error = null
        tilPassword.error = null
    }

    private fun layoutId(name: String): Int = resources.getIdentifier(name, "layout", packageName)

    private fun id(name: String): Int = resources.getIdentifier(name, "id", packageName)
}

