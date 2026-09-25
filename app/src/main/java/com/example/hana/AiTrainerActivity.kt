package com.example.hana

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import java.util.Locale

class AiTrainerActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var llLevelSelector: LinearLayout
    private lateinit var clTrainerView: ConstraintLayout
    private lateinit var btnBeginner: MaterialButton
    private lateinit var btnIntermediate: MaterialButton
    private lateinit var btnExpert: MaterialButton
    private lateinit var ivAvatarGraphic: ImageView
    private lateinit var tvAvatarInstruction: TextView
    private lateinit var tvFeedbackText: TextView
    private lateinit var vScanLine: View
    private lateinit var btnSimulateCorrection: MaterialButton
    private lateinit var btnResetTrainer: MaterialButton

    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    private val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            startTrainingFlow()
        } else {
            Toast.makeText(this, "Camera permission required for AI tracking", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ai_trainer)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.trainerRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tts = TextToSpeech(this, this)

        bindViews()
        setupListeners()
        startScanAnimation()
    }

    private fun bindViews() {
        llLevelSelector = findViewById(R.id.llLevelSelector)
        clTrainerView = findViewById(R.id.clTrainerView)
        btnBeginner = findViewById(R.id.btnBeginner)
        btnIntermediate = findViewById(R.id.btnIntermediate)
        btnExpert = findViewById(R.id.btnExpert)
        ivAvatarGraphic = findViewById(R.id.ivAvatarGraphic)
        tvAvatarInstruction = findViewById(R.id.tvAvatarInstruction)
        tvFeedbackText = findViewById(R.id.tvFeedbackText)
        vScanLine = findViewById(R.id.vScanLine)
        btnSimulateCorrection = findViewById(R.id.btnSimulateCorrection)
        btnResetTrainer = findViewById(R.id.btnResetTrainer)
    }

    private fun setupListeners() {
        btnBeginner.setOnClickListener { checkCameraAndStart("Beginner") }
        btnIntermediate.setOnClickListener { checkCameraAndStart("Intermediate") }
        btnExpert.setOnClickListener { checkCameraAndStart("Expert") }

        btnSimulateCorrection.setOnClickListener {
            simulateAiCorrection()
        }

        btnResetTrainer.setOnClickListener {
            finish()
        }
    }

    private fun checkCameraAndStart(level: String) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startTrainingFlow()
        } else {
            requestCameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private fun startTrainingFlow() {
        llLevelSelector.visibility = View.GONE
        clTrainerView.visibility = View.VISIBLE
        
        speak("Initializing Premium AI Trainer. Please stand in front of the camera.")
        tvFeedbackText.text = "AI System: Aligning skeletal tracking..."
    }

    private fun simulateAiCorrection() {
        tvFeedbackText.text = "CORRECTION: Lower your hips!"
        tvAvatarInstruction.text = "AI Model: Hips too high"
        ivAvatarGraphic.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_red_light))
        
        speak("Your hips are too high. Please lower them for a perfect squat.")
        
        vScanLine.postDelayed({
            ivAvatarGraphic.setColorFilter(ContextCompat.getColor(this, R.color.primary_action))
            tvAvatarInstruction.text = "AI Model: Keep your back straight"
            tvFeedbackText.text = "Posture Corrected. Well done."
        }, 4000)
    }

    private fun startScanAnimation() {
        val animation = TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0f,
            Animation.RELATIVE_TO_PARENT, 0.8f
        ).apply {
            duration = 2000
            repeatCount = Animation.INFINITE
            repeatMode = Animation.REVERSE
        }
        vScanLine.startAnimation(animation)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts?.language = Locale.US
            isTtsReady = true
        }
    }

    private fun speak(text: String) {
        if (isTtsReady) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    override fun onDestroy() {
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }
}