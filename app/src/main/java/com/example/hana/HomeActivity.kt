package com.example.hana

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.button.MaterialButtonToggleGroup
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private enum class BottomTab {
        BODY,
        DIET,
        SCANNER,
        CHALLENGE,
        ACCOUNT
    }

    private lateinit var tvGreeting: TextView
    private lateinit var tvLocation: TextView
    private lateinit var btnViewAll: TextView
    private lateinit var btnStartNow: MaterialButton
    private lateinit var btnDailyPlay: MaterialButton
    private lateinit var tvSectionTitle: TextView
    private lateinit var dayToggleGroup: MaterialButtonToggleGroup
    private lateinit var tvDayPlanTitle: TextView
    private lateinit var tvDayPlanSubtitle: TextView
    private lateinit var cardFitpass: MaterialCardView
    private lateinit var cardFitfeast: MaterialCardView
    private lateinit var cardFitcoach: MaterialCardView
    private lateinit var cardStore: MaterialCardView
    private lateinit var cardDailyRitual: MaterialCardView
    private lateinit var cardBreathing: MaterialCardView
    private lateinit var cardStretch: MaterialCardView
    private lateinit var tabHome: TextView
    private lateinit var tabExplore: TextView
    private lateinit var tabScanner: MaterialButton
    private lateinit var tabWellness: TextView
    private lateinit var tabAccount: TextView

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val PERMISSION_REQUEST_CODE = 100
    private var selectedTab: BottomTab = BottomTab.BODY
    private var selectedDayIndex: Int = 0

    private data class DayPlan(
        val title: String,
        val subtitle: String
    )

    private val dayPlans = listOf(
        DayPlan("Monday", "Lower body reset • Glutes, quads, and mobility"),
        DayPlan("Tuesday", "Upper body push • Chest, shoulders, and triceps"),
        DayPlan("Wednesday", "Core and cardio • Stability, endurance, and flow"),
        DayPlan("Thursday", "Upper body pull • Back, biceps, and posture"),
        DayPlan("Friday", "Leg strength • Squats, lunges, and power"),
        DayPlan("Saturday", "Recovery flow • Stretching, breathing, and light movement"),
        DayPlan("Sunday", "Mindful reset • Easy movement and weekly reflection")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.homeRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        bindViews()
        populateHeader()
        requestLocationUpdates()
        setListeners()
        applyStartSection()
    }

    private fun bindViews() {
        tvGreeting = findViewById(R.id.tvGreeting)
        tvLocation = findViewById(R.id.tvLocation)
        btnViewAll = findViewById(R.id.btnViewAll)
        btnStartNow = findViewById(R.id.btnStartNow)
        btnDailyPlay = findViewById(R.id.btnDailyPlay)
        tvSectionTitle = findViewById(R.id.tvSectionTitle)
        dayToggleGroup = findViewById(R.id.dayToggleGroup)
        tvDayPlanTitle = findViewById(R.id.tvDayPlanTitle)
        tvDayPlanSubtitle = findViewById(R.id.tvDayPlanSubtitle)
        cardFitpass = findViewById(R.id.cardFitpass)
        cardFitfeast = findViewById(R.id.cardFitfeast)
        cardFitcoach = findViewById(R.id.cardFitcoach)
        cardStore = findViewById(R.id.cardStore)
        cardDailyRitual = findViewById(R.id.cardDailyRitual)
        cardBreathing = findViewById(R.id.cardBreathing)
        cardStretch = findViewById(R.id.cardStretch)
        tabHome = findViewById(R.id.tabHome)
        tabExplore = findViewById(R.id.tabExplore)
        tabScanner = findViewById(R.id.tabScanner)
        tabWellness = findViewById(R.id.tabWellness)
        tabAccount = findViewById(R.id.tabAccount)
    }

    private fun populateHeader() {
        val profile = ProfileStore.loadProfile(this)
        val displayName = profile?.fullName
            ?: intent.getStringExtra(EXTRA_DISPLAY_NAME)
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
            ?: intent.getStringExtra(EXTRA_EMAIL)
                ?.trim()
                ?.takeIf { it.isNotEmpty() }

        tvGreeting.text = displayName?.let { getString(R.string.home_welcome, it) }
            ?: getString(R.string.home_title)
        tvLocation.text = getString(R.string.home_location)
    }

    private fun setListeners() {
        configureAction(btnViewAll, getString(R.string.view_all))
        configureAction(btnStartNow, getString(R.string.start_now))
        configureAction(btnDailyPlay, getString(R.string.daily_ritual))
        configureAction(cardDailyRitual, getString(R.string.daily_ritual))
        configureAction(cardBreathing, getString(R.string.mindful_breathing))
        configureAction(cardStretch, getString(R.string.post_work_stretch))

        // Link FitCoach Card to the new Premium AI Trainer feature
        cardFitcoach.setOnClickListener {
            startActivity(Intent(this, AiTrainerActivity::class.java))
        }

        cardFitpass.setOnClickListener {
            showToast(getString(R.string.home_action_tapped, getString(R.string.fitpass)))
        }
        cardFitfeast.setOnClickListener {
            showToast(getString(R.string.home_action_tapped, getString(R.string.fitfeast)))
        }
        cardStore.setOnClickListener {
            showToast(getString(R.string.home_action_tapped, getString(R.string.store)))
        }

        setupDayTabs()

        configureTab(tabHome, BottomTab.BODY)
        configureTab(tabExplore, BottomTab.DIET)
        configureTab(tabScanner, BottomTab.SCANNER)
        configureTab(tabWellness, BottomTab.CHALLENGE)
        
        tabAccount.setOnClickListener {
            selectedTab = BottomTab.ACCOUNT
            updateTabAppearance()
            startActivity(Intent(this, AccountActivity::class.java))
        }

        selectTab(BottomTab.BODY, false)
    }

    private fun configureAction(view: View, label: String) {
        view.setOnClickListener {
            showToast(getString(R.string.home_action_tapped, label))
        }
    }

    private fun setupDayTabs() {
        dayToggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (!isChecked) return@addOnButtonCheckedListener

            selectedDayIndex = when (checkedId) {
                R.id.dayMon -> 0
                R.id.dayTue -> 1
                R.id.dayWed -> 2
                R.id.dayThu -> 3
                R.id.dayFri -> 4
                R.id.daySat -> 5
                else -> 6
            }
            updateDayPlan()
        }

        dayToggleGroup.check(R.id.dayMon)
        updateDayPlan()
    }

    private fun configureTab(view: View, tab: BottomTab) {
        view.setOnClickListener {
            selectTab(tab, true)
        }
    }

    private fun selectTab(tab: BottomTab, animate: Boolean = true) {
        selectedTab = tab
        updateTabAppearance()

        when (tab) {
            BottomTab.BODY -> if (animate) startActivity(Intent(this, BodyActivity::class.java))
            BottomTab.DIET -> if (animate) startActivity(Intent(this, DietActivity::class.java))
            BottomTab.SCANNER -> if (animate) startActivity(Intent(this, ScannerActivity::class.java))
            BottomTab.CHALLENGE -> if (animate) startActivity(Intent(this, ChallengeActivity::class.java))
            BottomTab.ACCOUNT -> startActivity(Intent(this, AccountActivity::class.java))
        }
    }

    private fun applyStartSection() {
        when (intent.getStringExtra(EXTRA_START_SECTION)) {
            SECTION_DIET -> {
                selectedTab = BottomTab.DIET
                updateTabAppearance()
                startActivity(Intent(this, DietActivity::class.java))
                finish()
            }
            SECTION_CHALLENGE -> {
                selectedTab = BottomTab.CHALLENGE
                updateTabAppearance()
                startActivity(Intent(this, ChallengeActivity::class.java))
                finish()
            }
            else -> updateTabAppearance()
        }
    }

    private fun updateDayPlan() {
        val plan = dayPlans[selectedDayIndex]
        tvDayPlanTitle.text = plan.title
        tvDayPlanSubtitle.text = plan.subtitle
    }

    private fun updateTabAppearance() {
        val activeColor = ContextCompat.getColor(this, R.color.primary_action)
        val inactiveColor = ContextCompat.getColor(this, R.color.body_text)

        fun styleTextTab(tab: TextView, isSelected: Boolean) {
            tab.setTextColor(if (isSelected) activeColor else inactiveColor)
            tab.alpha = if (isSelected) 1f else 0.72f
        }

        styleTextTab(tabHome, selectedTab == BottomTab.BODY)
        styleTextTab(tabExplore, selectedTab == BottomTab.DIET)
        styleTextTab(tabWellness, selectedTab == BottomTab.CHALLENGE)
        styleTextTab(tabAccount, selectedTab == BottomTab.ACCOUNT)

        tabScanner.scaleX = if (selectedTab == BottomTab.SCANNER) 1.08f else 1f
        tabScanner.scaleY = if (selectedTab == BottomTab.SCANNER) 1.08f else 1f
        tabScanner.alpha = if (selectedTab == BottomTab.SCANNER) 1f else 0.95f
        tabScanner.elevation = if (selectedTab == BottomTab.SCANNER) 12f else 4f
    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun requestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                PERMISSION_REQUEST_CODE
            )
        } else {
            getLocationAndDisplay()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocationAndDisplay()
            } else {
                tvLocation.text = getString(R.string.location_permission_denied)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocationAndDisplay() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                getAddressFromLocation(location.latitude, location.longitude)
            } else {
                tvLocation.text = getString(R.string.location_getting)
                // Try to get location one more time
                fusedLocationClient.lastLocation.addOnSuccessListener { updatedLocation: Location? ->
                    if (updatedLocation != null) {
                        getAddressFromLocation(updatedLocation.latitude, updatedLocation.longitude)
                    } else {
                        tvLocation.text = getString(R.string.location_unavailable)
                    }
                }
            }
        }
    }

    private fun getAddressFromLocation(latitude: Double, longitude: Double) {
        try {
            val geocoder = Geocoder(this, Locale.getDefault())
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                val locationName = buildLocationString(address)
                tvLocation.text = locationName
            } else {
                tvLocation.text = getString(
                    R.string.location_coordinates,
                    latitude,
                    longitude
                )
            }
        } catch (_: Exception) {
            tvLocation.text = getString(
                R.string.location_coordinates,
                latitude,
                longitude
            )
        }
    }

    private fun buildLocationString(address: android.location.Address): String {
        return buildString {
            append("📍 ")
            if (!address.locality.isNullOrBlank()) {
                append(address.locality)
            }
            if (!address.adminArea.isNullOrBlank()) {
                if (isNotEmpty() && !address.adminArea.isNullOrBlank()) append(", ")
                append(address.adminArea)
            }
            if (!address.countryName.isNullOrBlank()) {
                if (isNotEmpty()) append(", ")
                append(address.countryName)
            }
            if (isEmpty()) {
                append(String.format(Locale.getDefault(), "%.4f, %.4f", address.latitude, address.longitude))
            }
        }
    }

    companion object {
        const val EXTRA_DISPLAY_NAME = "extra_display_name"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_START_SECTION = "extra_start_section"
        const val SECTION_DIET = "section_diet"
        const val SECTION_CHALLENGE = "section_challenge"
    }
}
