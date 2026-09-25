package com.example.hana

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class SignUpActivity : AppCompatActivity() {

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

    private lateinit var btnCreateAccount: MaterialButton
    private lateinit var tvBackToLogin: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signUpRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bindViews()
        setupGenderDropdown()
        setupInputFlow()
        setListeners()
    }

    private fun bindViews() {
        tilFullName = findViewById(R.id.tilFullName)
        tilAge = findViewById(R.id.tilAge)
        tilGender = findViewById(R.id.tilGender)
        tilHeight = findViewById(R.id.tilHeight)
        tilWeight = findViewById(R.id.tilWeight)
        tilEmail = findViewById(R.id.tilEmail)
        tilPassword = findViewById(R.id.tilPassword)

        etFullName = findViewById(R.id.etFullName)
        etAge = findViewById(R.id.etAge)
        actGender = findViewById(R.id.actGender)
        etHeight = findViewById(R.id.etHeight)
        etWeight = findViewById(R.id.etWeight)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        btnCreateAccount = findViewById(R.id.btnCreateAccount)
        tvBackToLogin = findViewById(R.id.tvBackToLogin)
    }

    private fun setupGenderDropdown() {
        val options = resources.getStringArray(R.array.gender_options)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, options)
        actGender.setAdapter(adapter)
        actGender.setOnClickListener { actGender.showDropDown() }
        actGender.setOnItemClickListener { _, _, _, _ ->
            tilGender.error = null
            etHeight.requestFocus()
        }
    }

    private fun setupInputFlow() {
        etFullName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                etAge.requestFocus()
                true
            } else {
                false
            }
        }

        etAge.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                actGender.requestFocus()
                actGender.showDropDown()
                true
            } else {
                false
            }
        }

        etHeight.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                etWeight.requestFocus()
                true
            } else {
                false
            }
        }

        etWeight.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                btnCreateAccount.performClick()
                true
            } else {
                false
            }
        }
    }

    private fun setListeners() {
        btnCreateAccount.setOnClickListener {
            if (!validateForm()) return@setOnClickListener
            saveProfile()
            navigateToHome(etFullName.text?.toString()?.trim(), etEmail.text?.toString()?.trim())
        }
        tvBackToLogin.setOnClickListener { finish() }

        etFullName.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) tilFullName.error = null }
        etAge.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) tilAge.error = null }
        actGender.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) tilGender.error = null }
        etHeight.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) tilHeight.error = null }
        etWeight.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) tilWeight.error = null }
        etEmail.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) tilEmail.error = null }
        etPassword.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) tilPassword.error = null }
    }

    private fun validateForm(): Boolean {
        tilFullName.error = null
        tilAge.error = null
        tilGender.error = null
        tilHeight.error = null
        tilWeight.error = null
        tilEmail.error = null
        tilPassword.error = null

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

    private fun saveProfile() {
        ProfileStore.saveProfile(
            this,
            UserProfile(
                fullName = etFullName.text?.toString()?.trim().orEmpty(),
                age = etAge.text?.toString()?.trim().orEmpty().toInt(),
                gender = actGender.text?.toString()?.trim().orEmpty(),
                heightCm = etHeight.text?.toString()?.trim().orEmpty().toInt(),
                weightKg = etWeight.text?.toString()?.trim().orEmpty().toInt(),
                email = etEmail.text?.toString()?.trim().orEmpty(),
                password = etPassword.text?.toString().orEmpty()
            )
        )
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun navigateToHome(displayName: String?, email: String?) {
        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra(HomeActivity.EXTRA_DISPLAY_NAME, displayName)
            putExtra(HomeActivity.EXTRA_EMAIL, email)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
        finish()
    }
}

