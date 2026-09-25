package com.example.hana

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.card.MaterialCardView

class ChallengeActivity : AppCompatActivity() {

    private enum class BottomTab {
        BODY,
        DIET,
        SCANNER,
        CHALLENGE,
        ACCOUNT
    }

    private data class ChallengeDayPlan(
        val dayLabel: String,
        val title: String,
        val description: String,
        val sectionTitle: String,
        val taskCount: String,
        val itemTitles: List<String>,
        val itemSubtitles: List<String>
    )

    private lateinit var tvDayLabel: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvSectionTitle: TextView
    private lateinit var tvTaskCount: TextView
    private lateinit var dayToggleGroup: MaterialButtonToggleGroup
    private lateinit var cardChallenge1: MaterialCardView
    private lateinit var cardChallenge2: MaterialCardView
    private lateinit var cardChallenge3: MaterialCardView
    private lateinit var cardChallenge4: MaterialCardView
    private lateinit var cardChallenge5: MaterialCardView
    private lateinit var tvChallenge1Title: TextView
    private lateinit var tvChallenge1Subtitle: TextView
    private lateinit var tvChallenge2Title: TextView
    private lateinit var tvChallenge2Subtitle: TextView
    private lateinit var tvChallenge3Title: TextView
    private lateinit var tvChallenge3Subtitle: TextView
    private lateinit var tvChallenge4Title: TextView
    private lateinit var tvChallenge4Subtitle: TextView
    private lateinit var tvChallenge5Title: TextView
    private lateinit var tvChallenge5Subtitle: TextView
    private lateinit var tabBody: TextView
    private lateinit var tabDiet: TextView
    private lateinit var tabScanner: MaterialButton
    private lateinit var tabChallenge: TextView
    private lateinit var tabAccount: TextView

    private var selectedTab: BottomTab = BottomTab.CHALLENGE
    private var selectedDayIndex: Int = 0

    private val dayPlans = listOf(
        ChallengeDayPlan(
            dayLabel = "Monday",
            title = "Momentum Start",
            description = "Kick off the week with short, focused challenge sets.",
            sectionTitle = "Starter Challenge",
            taskCount = "5 TASKS",
            itemTitles = listOf(
                "Warm-up March",
                "Push-up Ladder",
                "Squat Hold",
                "Plank Finish",
                "Stretch Reset"
            ),
            itemSubtitles = listOf(
                "2 minutes",
                "3 rounds • 8 reps",
                "3 rounds • 30 seconds",
                "3 rounds • 20 seconds",
                "5 minutes"
            )
        ),
        ChallengeDayPlan(
            dayLabel = "Tuesday",
            title = "Power Push",
            description = "Upper body challenge for strength and control.",
            sectionTitle = "Power Challenge",
            taskCount = "5 TASKS",
            itemTitles = listOf(
                "Incline Push-ups",
                "Shoulder Taps",
                "Bench Dips",
                "Arm Circles",
                "Wall Hold"
            ),
            itemSubtitles = listOf(
                "3 rounds • 10 reps",
                "3 rounds • 20 reps",
                "3 rounds • 12 reps",
                "3 rounds • 30 seconds",
                "3 rounds • 45 seconds"
            )
        ),
        ChallengeDayPlan(
            dayLabel = "Wednesday",
            title = "Core Builder",
            description = "Core strength and balance for the middle of the week.",
            sectionTitle = "Core Challenge",
            taskCount = "5 TASKS",
            itemTitles = listOf(
                "Dead Bug",
                "Mountain Climbers",
                "Russian Twists",
                "Bicycle Crunches",
                "Hollow Hold"
            ),
            itemSubtitles = listOf(
                "3 rounds • 12 reps",
                "3 rounds • 30 seconds",
                "3 rounds • 20 reps",
                "3 rounds • 20 reps",
                "3 rounds • 20 seconds"
            )
        ),
        ChallengeDayPlan(
            dayLabel = "Thursday",
            title = "Endurance Rise",
            description = "Keep the pace steady and push your stamina.",
            sectionTitle = "Endurance Challenge",
            taskCount = "5 TASKS",
            itemTitles = listOf(
                "Jump Rope",
                "High Knees",
                "Burpees",
                "Step Jacks",
                "Recovery Breath"
            ),
            itemSubtitles = listOf(
                "3 rounds • 1 minute",
                "3 rounds • 40 seconds",
                "3 rounds • 12 reps",
                "3 rounds • 30 seconds",
                "2 minutes"
            )
        ),
        ChallengeDayPlan(
            dayLabel = "Friday",
            title = "Finish Strong",
            description = "A final push challenge to close the working week.",
            sectionTitle = "Finish Strong",
            taskCount = "5 TASKS",
            itemTitles = listOf(
                "Lunge Walk",
                "Glute Bridge",
                "Wall Sit",
                "Calf Raise Pulse",
                "Full Stretch"
            ),
            itemSubtitles = listOf(
                "3 rounds • 12 reps",
                "3 rounds • 15 reps",
                "3 rounds • 45 seconds",
                "3 rounds • 20 reps",
                "5 minutes"
            )
        ),
        ChallengeDayPlan(
            dayLabel = "Saturday",
            title = "Active Recovery",
            description = "Move gently and keep the body refreshed.",
            sectionTitle = "Recovery Challenge",
            taskCount = "5 TASKS",
            itemTitles = listOf(
                "Easy Walk",
                "Mobility Flow",
                "Hip Openers",
                "Shoulder Release",
                "Foam Roll"
            ),
            itemSubtitles = listOf(
                "15 minutes",
                "3 rounds • 5 moves",
                "3 rounds • 30 seconds",
                "3 rounds • 20 seconds",
                "5 minutes"
            )
        ),
        ChallengeDayPlan(
            dayLabel = "Sunday",
            title = "Reset & Reflect",
            description = "A gentle challenge to recharge for the next week.",
            sectionTitle = "Reset Challenge",
            taskCount = "5 TASKS",
            itemTitles = listOf(
                "Sun Salutation",
                "Breathing",
                "Gentle Stretch",
                "Walk It Out",
                "Weekly Reflection"
            ),
            itemSubtitles = listOf(
                "3 rounds • 5 flows",
                "3 rounds • 1 minute",
                "3 rounds • 30 seconds",
                "10 minutes",
                "5 minutes"
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_challenge)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.challengeRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bindViews()
        setupDayTabs()
        setupCards()
        setupTabs()
        updateTabAppearance()
    }

    private fun bindViews() {
        tvDayLabel = findViewById(R.id.tvDayLabel)
        tvTitle = findViewById(R.id.tvTitle)
        tvDescription = findViewById(R.id.tvDescription)
        tvSectionTitle = findViewById(R.id.tvSectionTitle)
        tvTaskCount = findViewById(R.id.tvTaskCount)

        cardChallenge1 = findViewById(R.id.cardChallenge1)
        cardChallenge2 = findViewById(R.id.cardChallenge2)
        cardChallenge3 = findViewById(R.id.cardChallenge3)
        cardChallenge4 = findViewById(R.id.cardChallenge4)
        cardChallenge5 = findViewById(R.id.cardChallenge5)

        tvChallenge1Title = findViewById(R.id.tvChallenge1Title)
        tvChallenge1Subtitle = findViewById(R.id.tvChallenge1Subtitle)
        tvChallenge2Title = findViewById(R.id.tvChallenge2Title)
        tvChallenge2Subtitle = findViewById(R.id.tvChallenge2Subtitle)
        tvChallenge3Title = findViewById(R.id.tvChallenge3Title)
        tvChallenge3Subtitle = findViewById(R.id.tvChallenge3Subtitle)
        tvChallenge4Title = findViewById(R.id.tvChallenge4Title)
        tvChallenge4Subtitle = findViewById(R.id.tvChallenge4Subtitle)
        tvChallenge5Title = findViewById(R.id.tvChallenge5Title)
        tvChallenge5Subtitle = findViewById(R.id.tvChallenge5Subtitle)

        dayToggleGroup = findViewById(R.id.dayToggleGroup)

        tabBody = findViewById(R.id.tabBody)
        tabDiet = findViewById(R.id.tabDiet)
        tabScanner = findViewById(R.id.tabScanner)
        tabChallenge = findViewById(R.id.tabChallenge)
        tabAccount = findViewById(R.id.tabAccount)
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
            updateChallengeContent()
        }

        dayToggleGroup.check(R.id.dayMon)
        updateChallengeContent()
    }

    private fun setupCards() {
        cardChallenge1.setOnClickListener { openYouTubeSearch("${tvChallenge1Title.text} challenge workout") }
        cardChallenge2.setOnClickListener { openYouTubeSearch("${tvChallenge2Title.text} challenge workout") }
        cardChallenge3.setOnClickListener { openYouTubeSearch("${tvChallenge3Title.text} challenge workout") }
        cardChallenge4.setOnClickListener { openYouTubeSearch("${tvChallenge4Title.text} challenge workout") }
        cardChallenge5.setOnClickListener { openYouTubeSearch("${tvChallenge5Title.text} challenge workout") }
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
        tabScanner.setOnClickListener {
            selectedTab = BottomTab.SCANNER
            updateTabAppearance()
            startActivity(Intent(this, ScannerActivity::class.java))
        }
        tabChallenge.setOnClickListener {
            selectedTab = BottomTab.CHALLENGE
            updateTabAppearance()
            scrollToTop()
        }
        tabAccount.setOnClickListener {
            selectedTab = BottomTab.ACCOUNT
            updateTabAppearance()
            startActivity(Intent(this, AccountActivity::class.java))
        }
    }

    private fun updateChallengeContent() {
        val plan = dayPlans[selectedDayIndex]

        tvDayLabel.text = plan.dayLabel
        tvTitle.text = plan.title
        tvDescription.text = plan.description
        tvSectionTitle.text = plan.sectionTitle
        tvTaskCount.text = plan.taskCount

        tvChallenge1Title.text = plan.itemTitles.getOrNull(0).orEmpty()
        tvChallenge1Subtitle.text = plan.itemSubtitles.getOrNull(0).orEmpty()
        tvChallenge2Title.text = plan.itemTitles.getOrNull(1).orEmpty()
        tvChallenge2Subtitle.text = plan.itemSubtitles.getOrNull(1).orEmpty()
        tvChallenge3Title.text = plan.itemTitles.getOrNull(2).orEmpty()
        tvChallenge3Subtitle.text = plan.itemSubtitles.getOrNull(2).orEmpty()
        tvChallenge4Title.text = plan.itemTitles.getOrNull(3).orEmpty()
        tvChallenge4Subtitle.text = plan.itemSubtitles.getOrNull(3).orEmpty()
        tvChallenge5Title.text = plan.itemTitles.getOrNull(4).orEmpty()
        tvChallenge5Subtitle.text = plan.itemSubtitles.getOrNull(4).orEmpty()
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
    }

    private fun scrollToTop() {
        findViewById<android.view.View>(R.id.challengeRoot).post {
            findViewById<android.view.View>(R.id.challengeRoot).scrollY = 0
        }
    }


}

