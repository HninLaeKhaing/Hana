package com.example.hana

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {

    private lateinit var etEmail: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var tilEmail: TextInputLayout
    private lateinit var tilPassword: TextInputLayout
    private lateinit var btnLogin: MaterialButton
    private lateinit var btnGoogle: MaterialButton
    private lateinit var btnCreateNewAccount: MaterialButton
    private lateinit var tvForgotPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bindViews()
        setListeners()
    }

    private fun bindViews() {
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        tilEmail = findViewById(R.id.tilEmail)
        tilPassword = findViewById(R.id.tilPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnGoogle = findViewById(R.id.btnGoogle)
        btnCreateNewAccount = findViewById(R.id.btnCreateNewAccount)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)
    }

    private fun setListeners() {
        btnLogin.setOnClickListener { attemptLogin() }
        tvForgotPassword.setOnClickListener { showToast(getString(R.string.home_action_tapped, getString(R.string.forgot_password))) }
        btnCreateNewAccount.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        btnGoogle.setOnClickListener { attemptLogin() }
        etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                attemptLogin()
                true
            } else {
                false
            }
        }
    }

    private fun validateInputs(): Boolean {
        tilEmail.error = null
        tilPassword.error = null

        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()

        var valid = true
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.error = getString(R.string.enter_email)
            valid = false
        }

        if (password.length < 6) {
            tilPassword.error = getString(R.string.enter_password)
            valid = false
        }

        return valid
    }

    private fun attemptLogin() {
        if (!validateInputs()) return

        val email = etEmail.text?.toString()?.trim().orEmpty()
        val password = etPassword.text?.toString().orEmpty()
        val profile = ProfileStore.loadProfile(this)

        if (profile == null) {
            tilEmail.error = getString(R.string.user_not_found)
            return
        }

        val emailMatches = profile.email.equals(email, ignoreCase = true)
        val passwordMatches = profile.password == password

        if (!emailMatches || !passwordMatches) {
            tilPassword.error = getString(R.string.invalid_password)
            return
        }

        navigateToHome(displayName = profile.fullName, email = profile.email)
    }

    private fun navigateToHome(displayName: String? = null, email: String? = null) {
        val intent = Intent(this, HomeActivity::class.java).apply {
            putExtra(HomeActivity.EXTRA_DISPLAY_NAME, displayName)
            putExtra(HomeActivity.EXTRA_EMAIL, email)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        startActivity(intent)
        finish()
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}