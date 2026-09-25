package com.example.hana

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

class ScannerActivity : AppCompatActivity() {

    private enum class BottomTab {
        BODY,
        DIET,
        SCANNER,
        CHALLENGE,
        ACCOUNT
    }

    private data class NutritionResult(
        val foodName: String,
        val proteinGrams: Double,
        val carbohydratesGrams: Double,
        val fatsGrams: Double,
        val hydrationMl: Double,
        val bestMealTime: String,
        val summary: String
    )

    private lateinit var tvScannerTitle: TextView
    private lateinit var tvScannerSubtitle: TextView
    private lateinit var tvScanStatus: TextView
    private lateinit var tvFoodName: TextView
    private lateinit var tvFoodSummary: TextView
    private lateinit var tvProtein: TextView
    private lateinit var tvCarbs: TextView
    private lateinit var tvFats: TextView
    private lateinit var tvHydration: TextView
    private lateinit var tvBestMealTime: TextView
    private lateinit var ivFoodPreview: ImageView
    private lateinit var progressScan: ProgressBar
    private lateinit var btnTakePhoto: MaterialButton
    private lateinit var btnAnalyze: MaterialButton
    private lateinit var btnRescan: MaterialButton
    private lateinit var cardPreview: MaterialCardView
    private lateinit var tabBody: TextView
    private lateinit var tabDiet: TextView
    private lateinit var tabScanner: MaterialButton
    private lateinit var tabChallenge: TextView
    private lateinit var tabAccount: TextView

    private var selectedTab: BottomTab = BottomTab.SCANNER
    private var currentPhotoFile: File? = null
    private var currentPhotoUri: Uri? = null
    private var isAnalyzing: Boolean = false

    private val requestCameraPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            launchCameraCapture()
        } else {
            showStatus("Camera permission is required to scan food photos.")
            showToast("Camera permission denied")
        }
    }

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            currentPhotoFile?.let { file ->
                showPreview(file)
                analyzePhoto(file)
            } ?: showStatus("Photo capture finished, but no file was created.")
        } else {
            showStatus("Camera closed. Tap Take Photo to scan a meal.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_scanner)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.scannerRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bindViews()
        setupButtons()
        setupTabs()
        updateTabAppearance()
        resetResultViews()
        openScanner()
    }

    private fun bindViews() {
        tvScannerTitle = findViewById(R.id.tvScannerTitle)
        tvScannerSubtitle = findViewById(R.id.tvScannerSubtitle)
        tvScanStatus = findViewById(R.id.tvScanStatus)
        tvFoodName = findViewById(R.id.tvFoodName)
        tvFoodSummary = findViewById(R.id.tvFoodSummary)
        tvProtein = findViewById(R.id.tvProtein)
        tvCarbs = findViewById(R.id.tvCarbs)
        tvFats = findViewById(R.id.tvFats)
        tvHydration = findViewById(R.id.tvHydration)
        tvBestMealTime = findViewById(R.id.tvBestMealTime)
        ivFoodPreview = findViewById(R.id.ivFoodPreview)
        progressScan = findViewById(R.id.progressScan)
        btnTakePhoto = findViewById(R.id.btnTakePhoto)
        btnAnalyze = findViewById(R.id.btnAnalyze)
        btnRescan = findViewById(R.id.btnRescan)
        cardPreview = findViewById(R.id.cardPreview)
        tabBody = findViewById(R.id.tabBody)
        tabDiet = findViewById(R.id.tabDiet)
        tabScanner = findViewById(R.id.tabScanner)
        tabChallenge = findViewById(R.id.tabChallenge)
        tabAccount = findViewById(R.id.tabAccount)
    }

    private fun setupButtons() {
        btnTakePhoto.setOnClickListener { openScanner() }
        btnAnalyze.setOnClickListener {
            currentPhotoFile?.let { analyzePhoto(it) }
                ?: showStatus("Take a food photo first.")
        }
        btnRescan.setOnClickListener { openScanner() }
    }

    private fun setupTabs() {
        tabBody.setOnClickListener {
            selectedTab = BottomTab.BODY
            updateTabAppearance()
            startActivity(Intent(this, BodyActivity::class.java))
        }
        tabDiet.setOnClickListener {
            selectedTab = BottomTab.DIET
            updateTabAppearance()
            startActivity(Intent(this, DietActivity::class.java))
        }
        tabScanner.setOnClickListener { openScanner() }
        tabChallenge.setOnClickListener {
            selectedTab = BottomTab.CHALLENGE
            updateTabAppearance()
            startActivity(Intent(this, ChallengeActivity::class.java))
        }
        tabAccount.setOnClickListener {
            selectedTab = BottomTab.ACCOUNT
            updateTabAppearance()
            startActivity(Intent(this, AccountActivity::class.java))
        }
    }

    private fun openScanner() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            launchCameraCapture()
        } else {
            requestCameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private fun launchCameraCapture() {
        try {
            val photoFile = File.createTempFile("food_scan_", ".jpg", cacheDir)
            currentPhotoFile = photoFile
            val photoUri = FileProvider.getUriForFile(
                this,
                "$packageName.fileprovider",
                photoFile
            )
            currentPhotoUri = photoUri
            showStatus("Camera ready. Capture any food photo.")
            takePictureLauncher.launch(photoUri)
        } catch (e: IOException) {
            showStatus("Unable to open camera: ${e.message}")
            showToast("Unable to open camera")
        }
    }

    private fun analyzePhoto(photoFile: File) {
        val apiKey = OpenRouterSecrets.API_KEY.trim()
        if (apiKey.isEmpty()) {
            showStatus("API key is missing.")
            return
        }

        setLoading(true)
        Thread {
            try {
                val result = FoodScanApi(apiKey).analyze(photoFile)
                runOnUiThread { showAnalysis(result) }
            } catch (e: Exception) {
                runOnUiThread {
                    showStatus("Scan failed: ${e.message ?: "Unknown error"}")
                    showToast("Food analysis failed")
                }
            } finally {
                runOnUiThread { setLoading(false) }
            }
        }.start()
    }

    private fun showPreview(photoFile: File) {
        val options = BitmapFactory.Options().apply { inSampleSize = 2 }
        val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath, options)
        if (bitmap != null) {
            ivFoodPreview.setImageBitmap(bitmap)
            showStatus("Photo captured. Analyzing nutrition...")
        } else {
            showStatus("Photo captured, but preview could not be loaded.")
        }
    }

    private fun showAnalysis(result: NutritionResult) {
        tvFoodName.text = result.foodName
        tvFoodSummary.text = result.summary
        tvProtein.text = formatValue(result.proteinGrams, "g")
        tvCarbs.text = formatValue(result.carbohydratesGrams, "g")
        tvFats.text = formatValue(result.fatsGrams, "g")
        tvHydration.text = formatValue(result.hydrationMl, "ml")
        tvBestMealTime.text = result.bestMealTime.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        showStatus("Nutrition details generated successfully.")
    }

    private fun resetResultViews() {
        tvFoodName.text = "No scan yet"
        tvFoodSummary.text = "Your nutrition result will appear here."
        tvProtein.text = "-- g"
        tvCarbs.text = "-- g"
        tvFats.text = "-- g"
        tvHydration.text = "-- ml"
        tvBestMealTime.text = "--"
        tvScanStatus.text = "Tap Take Photo to begin."
        ivFoodPreview.setImageResource(android.R.drawable.ic_menu_camera)
    }

    private fun setLoading(loading: Boolean) {
        isAnalyzing = loading
        progressScan.visibility = if (loading) View.VISIBLE else View.GONE
        btnTakePhoto.isEnabled = !loading
        btnAnalyze.isEnabled = !loading
        btnRescan.isEnabled = !loading
        tabScanner.isEnabled = !loading
    }

    private fun updateTabAppearance() {
        val activeColor = ContextCompat.getColor(this, R.color.primary_action)
        val inactiveColor = ContextCompat.getColor(this, R.color.body_text)

        fun styleTextTab(tab: TextView, isSelected: Boolean) {
            tab.setTextColor(if (isSelected) activeColor else inactiveColor)
            tab.alpha = if (isSelected) 1f else 0.72f
        }

        styleTextTab(tabBody, selectedTab == BottomTab.BODY)
        styleTextTab(tabDiet, selectedTab == BottomTab.DIET)
        styleTextTab(tabChallenge, selectedTab == BottomTab.CHALLENGE)
        styleTextTab(tabAccount, selectedTab == BottomTab.ACCOUNT)

        tabScanner.scaleX = if (selectedTab == BottomTab.SCANNER) 1.08f else 1f
        tabScanner.scaleY = if (selectedTab == BottomTab.SCANNER) 1.08f else 1f
        tabScanner.alpha = if (selectedTab == BottomTab.SCANNER) 1f else 0.95f
        tabScanner.elevation = if (selectedTab == BottomTab.SCANNER) 12f else 4f
    }

    private fun showStatus(message: String) {
        tvScanStatus.text = message
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun formatValue(value: Double, unit: String): String {
        val formatted = if (value % 1.0 == 0.0) {
            value.toInt().toString()
        } else {
            String.format(Locale.getDefault(), "%.1f", value)
        }
        return "$formatted $unit"
    }

    private class FoodScanApi(private val apiKey: String) {

        fun analyze(photoFile: File): NutritionResult {
            val imageBytes = photoFile.readBytes()
            val base64Image = Base64.encodeToString(imageBytes, Base64.NO_WRAP)
            val requestBody = JSONObject().apply {
                put("model", "openai/gpt-4o-mini")
                put("temperature", 0.2)
                put("max_tokens", 600)
                put("messages", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "system")
                        put(
                            "content",
                            "You are a food nutrition analyst. Return STRICT JSON only with keys: food_name, protein_g, carbohydrates_g, fats_g, hydration_ml, best_meal_time, summary. best_meal_time must be one of breakfast, lunch, snack, or dinner. Keep values realistic and concise."
                        )
                    })
                    put(JSONObject().apply {
                        put("role", "user")
                        put(
                            "content",
                            JSONArray().apply {
                                put(JSONObject().apply {
                                    put("type", "text")
                                    put(
                                        "text",
                                        "Analyze this food photo and estimate its nutrition. Include protein, carbohydrates, fats, hydration, and the best time to eat it."
                                    )
                                })
                                put(JSONObject().apply {
                                    put("type", "image_url")
                                    put(
                                        "image_url",
                                        JSONObject().apply {
                                            put("url", "data:image/jpeg;base64,$base64Image")
                                        }
                                    )
                                })
                            }
                        )
                    })
                })
            }

            val response = postJson(requestBody.toString())

            val content = response
                .optJSONArray("choices")
                ?.optJSONObject(0)
                ?.optJSONObject("message")
                ?.optString("content")
                .orEmpty()

            val parsed = parseNutritionResult(content)
            return parsed
        }

        private fun postJson(body: String): JSONObject {
            val connection = (URL("https://openrouter.ai/api/v1/chat/completions").openConnection() as HttpURLConnection).apply {
                requestMethod = "POST"
                connectTimeout = 30000
                readTimeout = 30000
                doOutput = true
                setRequestProperty("Content-Type", "application/json")
                setRequestProperty("Authorization", "Bearer $apiKey")
                setRequestProperty("HTTP-Referer", "https://hana.local")
                setRequestProperty("X-Title", "Hana Food Scanner")
            }

            connection.outputStream.use { output ->
                output.write(body.toByteArray(Charsets.UTF_8))
            }

            val responseCode = connection.responseCode
            val responseText = (if (responseCode in 200..299) {
                connection.inputStream
            } else {
                connection.errorStream
            })?.bufferedReader()?.use { it.readText() }.orEmpty()

            if (responseCode !in 200..299) {
                throw IOException("HTTP $responseCode: $responseText")
            }

            return JSONObject(responseText)
        }

        private fun parseNutritionResult(rawContent: String): NutritionResult {
            val jsonText = extractJsonObject(rawContent)
            val json = JSONObject(jsonText)
            val foodName = json.optString("food_name", "Unknown food")
            val protein = json.optDouble("protein_g", 0.0)
            val carbs = json.optDouble("carbohydrates_g", 0.0)
            val fats = json.optDouble("fats_g", 0.0)
            val hydration = json.optDouble("hydration_ml", 0.0)
            val bestMealTime = json.optString("best_meal_time", "lunch")
            val summary = json.optString("summary", "Nutrition estimate generated from the photo.")

            return NutritionResult(
                foodName = foodName,
                proteinGrams = protein,
                carbohydratesGrams = carbs,
                fatsGrams = fats,
                hydrationMl = hydration,
                bestMealTime = bestMealTime,
                summary = summary
            )
        }

        private fun extractJsonObject(text: String): String {
            val trimmed = text.trim()
            if (trimmed.startsWith("{") && trimmed.endsWith("}")) return trimmed

            val start = trimmed.indexOf('{')
            val end = trimmed.lastIndexOf('}')
            if (start >= 0 && end > start) {
                return trimmed.substring(start, end + 1)
            }

            throw IOException("Model response did not contain JSON.")
        }
    }
}

