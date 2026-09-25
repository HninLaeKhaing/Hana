package com.example.hana

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.card.MaterialCardView

class BodyActivity : AppCompatActivity() {

    private enum class BottomTab {
        BODY,
        DIET,
        SCANNER,
        CHALLENGE,
        ACCOUNT
    }

    private data class WorkoutPlan(
        val title: String,
        val subtitle: String,
        val sectionTitle: String,
        val exerciseCount: String,
        val exerciseTitles: List<String>,
        val exerciseSubtitles: List<String>
    )

    private lateinit var tvTitle: TextView
    private lateinit var tvSubtitle: TextView
    private lateinit var tvDayPlanTitle: TextView
    private lateinit var tvDayPlanSubtitle: TextView
    private lateinit var tvSectionTitle: TextView
    private lateinit var tvExerciseCount: TextView
    private lateinit var dayToggleGroup: MaterialButtonToggleGroup
    private lateinit var btnSignOut: TextView
    private lateinit var btnMenu: TextView
    private lateinit var cardChestFocus: MaterialCardView
    private lateinit var cardInclineBench: MaterialCardView
    private lateinit var cardCableFly: MaterialCardView
    private lateinit var cardDumbbellPress: MaterialCardView
    private lateinit var cardParallelBarDips: MaterialCardView
    private lateinit var cardDeclinePushUps: MaterialCardView
    private lateinit var cardPecDeckMachine: MaterialCardView
    private lateinit var cardDumbbellPullover: MaterialCardView
    private lateinit var cardCloseGripPushups: MaterialCardView
    private lateinit var cardDiamondPushUps: MaterialCardView
    private lateinit var exerciseTitleViews: List<TextView>
    private lateinit var exerciseSubtitleViews: List<TextView>
    private lateinit var tabHome: TextView
    private lateinit var tabExplore: TextView
    private lateinit var tabScanner: MaterialButton
    private lateinit var tabWellness: TextView
    private lateinit var tabAccount: TextView

    private var selectedTab: BottomTab = BottomTab.BODY
    private var selectedDayIndex: Int = 0

    private val dayPlans = listOf(
        WorkoutPlan(
            title = "Chest Focus",
            subtitle = "Lower body reset • Glutes, quads, and mobility",
            sectionTitle = "Chest Focus",
            exerciseCount = "10 EXERCISES",
            exerciseTitles = listOf(
                "Standard Push-ups",
                "Incline Bench Press",
                "Cable Chest Fly",
                "Dumbbell Press",
                "Parallel Bar Dips",
                "Decline Push-ups",
                "Pec Deck Machine",
                "Dumbbell Pullover",
                "Close-grip Pushups",
                "Diamond Push-ups"
            ),
            exerciseSubtitles = listOf(
                "3 SETS • 8 REPS",
                "3 SETS • 10 REPS",
                "3 SETS • 12 REPS",
                "3 SETS • 12 REPS",
                "3 SETS • MAX REPS",
                "3 SETS • 12 REPS",
                "3 SETS • 8 REPS",
                "3 SETS • 10 REPS",
                "3 SETS • 12 REPS",
                "3 SETS • 10 REPS"
            )
        ),
        WorkoutPlan(
            title = "Shoulder Focus",
            subtitle = "Upper body press • Delts, stability, and posture",
            sectionTitle = "Shoulder Focus",
            exerciseCount = "10 EXERCISES",
            exerciseTitles = listOf(
                "Overhead Press",
                "Lateral Raises",
                "Front Raises",
                "Face Pulls",
                "Arnold Press",
                "Shrugs",
                "Reverse Flys",
                "Upright Rows",
                "Push Press",
                "Pike Push-ups"
            ),
            exerciseSubtitles = listOf(
                "4 SETS • 8 REPS",
                "3 SETS • 15 REPS",
                "3 SETS • 12 REPS",
                "4 SETS • 15 REPS",
                "3 SETS • 10 REPS",
                "3 SETS • 12 REPS",
                "3 SETS • 15 REPS",
                "3 SETS • 12 REPS",
                "4 SETS • 6 REPS",
                "3 SETS • MAX REPS"
            )
        ),
        WorkoutPlan(
            title = "Core Flow",
            subtitle = "Core and cardio • Stability, endurance, and flow",
            sectionTitle = "Core Flow",
            exerciseCount = "10 EXERCISES",
            exerciseTitles = listOf(
                "Plank",
                "Dead Bug",
                "Mountain Climbers",
                "Bird Dog",
                "Russian Twists",
                "Hollow Hold",
                "Leg Raises",
                "Side Plank",
                "Bicycle Crunches",
                "Burpees"
            ),
            exerciseSubtitles = listOf(
                "3 SETS • 30 SECONDS",
                "3 SETS • 12 REPS",
                "3 SETS • 30 SECONDS",
                "3 SETS • 10 REPS",
                "3 SETS • 20 REPS",
                "3 SETS • 20 SECONDS",
                "3 SETS • 12 REPS",
                "3 SETS • 30 SECONDS",
                "3 SETS • 20 REPS",
                "3 SETS • 12 REPS"
            )
        ),
        WorkoutPlan(
            title = "Back Strength",
            subtitle = "Upper body pull • Back, biceps, and posture",
            sectionTitle = "Back Strength",
            exerciseCount = "10 EXERCISES",
            exerciseTitles = listOf(
                "Lat Pulldown",
                "Seated Row",
                "Face Pulls",
                "Single-arm Row",
                "Pull-ups",
                "Reverse Flys",
                "Chest Supported Row",
                "Bicep Curls",
                "Hammer Curls",
                "Superman Hold"
            ),
            exerciseSubtitles = listOf(
                "3 SETS • 10 REPS",
                "3 SETS • 12 REPS",
                "3 SETS • 15 REPS",
                "3 SETS • 10 REPS",
                "4 SETS • MAX REPS",
                "3 SETS • 15 REPS",
                "3 SETS • 10 REPS",
                "3 SETS • 12 REPS",
                "3 SETS • 12 REPS",
                "3 SETS • 30 SECONDS"
            )
        ),
        WorkoutPlan(
            title = "Leg Strength",
            subtitle = "Leg strength • Squats, lunges, and power",
            sectionTitle = "Leg Strength",
            exerciseCount = "10 EXERCISES",
            exerciseTitles = listOf(
                "Squats",
                "Lunges",
                "Romanian Deadlift",
                "Glute Bridge",
                "Leg Press",
                "Step-ups",
                "Hamstring Curl",
                "Calf Raises",
                "Wall Sit",
                "Jump Squats"
            ),
            exerciseSubtitles = listOf(
                "4 SETS • 8 REPS",
                "3 SETS • 10 REPS",
                "3 SETS • 10 REPS",
                "3 SETS • 15 REPS",
                "3 SETS • 12 REPS",
                "3 SETS • 10 REPS",
                "3 SETS • 12 REPS",
                "3 SETS • 20 REPS",
                "3 SETS • 45 SECONDS",
                "3 SETS • 12 REPS"
            )
        ),
        WorkoutPlan(
            title = "Recovery Flow",
            subtitle = "Recovery flow • Stretching, breathing, and light movement",
            sectionTitle = "Recovery Flow",
            exerciseCount = "10 EXERCISES",
            exerciseTitles = listOf(
                "Yoga Flow",
                "Child's Pose",
                "Cat Cow",
                "Hamstring Stretch",
                "Hip Opener",
                "Thoracic Twist",
                "Breath Work",
                "Neck Stretch",
                "Ankle Mobility",
                "Foam Rolling"
            ),
            exerciseSubtitles = listOf(
                "3 SETS • 60 SECONDS",
                "3 SETS • 30 SECONDS",
                "3 SETS • 12 REPS",
                "3 SETS • 30 SECONDS",
                "3 SETS • 30 SECONDS",
                "3 SETS • 10 REPS",
                "3 SETS • 60 SECONDS",
                "3 SETS • 20 SECONDS",
                "3 SETS • 20 REPS",
                "5 MINUTES"
            )
        ),
        WorkoutPlan(
            title = "Mindful Reset",
            subtitle = "Mindful reset • Easy movement and weekly reflection",
            sectionTitle = "Mindful Reset",
            exerciseCount = "10 EXERCISES",
            exerciseTitles = listOf(
                "Walk",
                "Sun Salutation",
                "Gentle Stretch",
                "Breath Hold",
                "Meditation",
                "Hip Opener",
                "Shoulder Circles",
                "Spine Twist",
                "Neck Release",
                "Reflection"
            ),
            exerciseSubtitles = listOf(
                "15 MINUTES",
                "3 SETS • 5 ROUNDS",
                "3 SETS • 30 SECONDS",
                "3 SETS • 5 BREATHS",
                "10 MINUTES",
                "3 SETS • 30 SECONDS",
                "3 SETS • 10 REPS",
                "3 SETS • 10 REPS",
                "3 SETS • 20 SECONDS",
                "5 MINUTES"
            )
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(layoutId("activity_body"))

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(id("bodyRoot"))) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bindViews()
        setupDayTabs()
        setupActions()
        updateTabAppearance()
    }

    private fun bindViews() {
        tvTitle = findViewById(id("tvTitle"))
        tvSubtitle = findViewById(id("tvSubtitle"))
        tvDayPlanTitle = findViewById(id("tvDayPlanTitle"))
        tvDayPlanSubtitle = findViewById(id("tvDayPlanSubtitle"))
        tvSectionTitle = findViewById(id("tvSectionTitle"))
        tvExerciseCount = findViewById(id("tvExerciseCount"))
        dayToggleGroup = findViewById(id("dayToggleGroup"))
        btnSignOut = findViewById(id("btnSignOut"))
        btnMenu = findViewById(id("btnMenu"))

        cardChestFocus = findViewById(id("cardChestFocus"))
        cardInclineBench = findViewById(id("cardInclineBench"))
        cardCableFly = findViewById(id("cardCableFly"))
        cardDumbbellPress = findViewById(id("cardDumbbellPress"))
        cardParallelBarDips = findViewById(id("cardParallelBarDips"))
        cardDeclinePushUps = findViewById(id("cardDeclinePushUps"))
        cardPecDeckMachine = findViewById(id("cardPecDeckMachine"))
        cardDumbbellPullover = findViewById(id("cardDumbbellPullover"))
        cardCloseGripPushups = findViewById(id("cardCloseGripPushups"))
        cardDiamondPushUps = findViewById(id("cardDiamondPushUps"))

        exerciseTitleViews = listOf(
            findViewById(id("tvExercise1Title")),
            findViewById(id("tvExercise2Title")),
            findViewById(id("tvExercise3Title")),
            findViewById(id("tvExercise4Title")),
            findViewById(id("tvExercise5Title")),
            findViewById(id("tvExercise6Title")),
            findViewById(id("tvExercise7Title")),
            findViewById(id("tvExercise8Title")),
            findViewById(id("tvExercise9Title")),
            findViewById(id("tvExercise10Title"))
        )
        exerciseSubtitleViews = listOf(
            findViewById(id("tvExercise1Subtitle")),
            findViewById(id("tvExercise2Subtitle")),
            findViewById(id("tvExercise3Subtitle")),
            findViewById(id("tvExercise4Subtitle")),
            findViewById(id("tvExercise5Subtitle")),
            findViewById(id("tvExercise6Subtitle")),
            findViewById(id("tvExercise7Subtitle")),
            findViewById(id("tvExercise8Subtitle")),
            findViewById(id("tvExercise9Subtitle")),
            findViewById(id("tvExercise10Subtitle"))
        )

        tabHome = findViewById(id("tabHome"))
        tabExplore = findViewById(id("tabExplore"))
        tabScanner = findViewById(id("tabScanner"))
        tabWellness = findViewById(id("tabWellness"))
        tabAccount = findViewById(id("tabAccount"))

        updateDayPlan()
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
        dayToggleGroup.check(id("dayMon"))
    }

    private fun setupActions() {
        btnMenu.setOnClickListener { showToast("Menu tapped") }
        btnSignOut.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            })
            finish()
        }

        configureExercise(cardChestFocus, exerciseTitleViews[0])
        configureExercise(cardInclineBench, exerciseTitleViews[1])
        configureExercise(cardCableFly, exerciseTitleViews[2])
        configureExercise(cardDumbbellPress, exerciseTitleViews[3])
        configureExercise(cardParallelBarDips, exerciseTitleViews[4])
        configureExercise(cardDeclinePushUps, exerciseTitleViews[5])
        configureExercise(cardPecDeckMachine, exerciseTitleViews[6])
        configureExercise(cardDumbbellPullover, exerciseTitleViews[7])
        configureExercise(cardCloseGripPushups, exerciseTitleViews[8])
        configureExercise(cardDiamondPushUps, exerciseTitleViews[9])

        tabHome.setOnClickListener {
            selectedTab = BottomTab.BODY
            updateTabAppearance()
            scrollToTop()
        }
        configureTab(tabExplore, BottomTab.DIET)
        configureTab(tabScanner, BottomTab.SCANNER)
        configureTab(tabWellness, BottomTab.CHALLENGE)
        tabAccount.setOnClickListener {
            selectedTab = BottomTab.ACCOUNT
            updateTabAppearance()
            startActivity(Intent(this, AccountActivity::class.java))
        }

        selectTab(BottomTab.BODY)
    }

    private fun configureExercise(view: View, labelView: TextView) {
        view.setOnClickListener {
            openYouTubeSearch("${labelView.text} workout tutorial")
        }
    }

    private fun openYouTubeSearch(query: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/results?search_query=$query"))
        startActivity(intent)
    }

    private fun configureTab(view: View, tab: BottomTab) {
        view.setOnClickListener { selectTab(tab) }
    }

    private fun selectTab(tab: BottomTab) {
        selectedTab = tab
        updateTabAppearance()

        when (tab) {
            BottomTab.BODY -> scrollToTop()
            BottomTab.DIET -> startActivity(Intent(this, DietActivity::class.java))
            BottomTab.SCANNER -> startActivity(Intent(this, ScannerActivity::class.java))
            BottomTab.CHALLENGE -> startActivity(Intent(this, ChallengeActivity::class.java))
            BottomTab.ACCOUNT -> startActivity(Intent(this, AccountActivity::class.java))
        }
    }

    private fun updateDayPlan() {
        val plan = dayPlans[selectedDayIndex]
        tvTitle.text = plan.title
        tvSubtitle.text = plan.subtitle
        tvDayPlanTitle.text = plan.title
        tvDayPlanSubtitle.text = plan.subtitle
        tvSectionTitle.text = plan.sectionTitle
        tvExerciseCount.text = plan.exerciseCount

        exerciseTitleViews.forEachIndexed { index, textView ->
            textView.text = plan.exerciseTitles.getOrNull(index).orEmpty()
        }
        exerciseSubtitleViews.forEachIndexed { index, textView ->
            textView.text = plan.exerciseSubtitles.getOrNull(index).orEmpty()
        }
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

    private fun scrollToTop() {
        findViewById<View>(id("bodyRoot")).post {
            findViewById<View>(id("bodyRoot")).scrollY = 0
        }
    }

    private fun layoutId(name: String): Int = resources.getIdentifier(name, "layout", packageName)

    private fun id(name: String): Int = resources.getIdentifier(name, "id", packageName)


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
