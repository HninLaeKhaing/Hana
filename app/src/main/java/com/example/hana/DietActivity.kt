package com.example.hana

import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class DietActivity : AppCompatActivity() {

    private enum class BottomTab {
        BODY,
        DIET,
        SCANNER,
        CHALLENGE,
        ACCOUNT
    }

    private lateinit var tvDayLabel: TextView
    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView
    private lateinit var dayChecks: List<CheckBox>
    private lateinit var cardBreakfast: MaterialCardView
    private lateinit var cardLunch: MaterialCardView
    private lateinit var cardSnack: MaterialCardView
    private lateinit var cardDinner: MaterialCardView
    private lateinit var tvBreakfastTitle: TextView
    private lateinit var tvBreakfastSubtitle: TextView
    private lateinit var tvLunchTitle: TextView
    private lateinit var tvLunchSubtitle: TextView
    private lateinit var tvSnackTitle: TextView
    private lateinit var tvSnackSubtitle: TextView
    private lateinit var tvDinnerTitle: TextView
    private lateinit var tvDinnerSubtitle: TextView
    private lateinit var tabBody: TextView
    private lateinit var tabDiet: TextView
    private lateinit var tabScanner: MaterialButton
    private lateinit var tabChallenge: TextView
    private lateinit var tabAccount: TextView

    private var selectedTab: BottomTab = BottomTab.DIET
    private var selectedDayIndex: Int = 0
    private var isUpdatingDayChecks: Boolean = false

    private data class DietDayPlan(
        val dayLabel: String,
        val title: String,
        val description: String,
        val breakfastTitle: String,
        val breakfastSubtitle: String,
        val lunchTitle: String,
        val lunchSubtitle: String,
        val snackTitle: String,
        val snackSubtitle: String,
        val dinnerTitle: String,
        val dinnerSubtitle: String
    )

    private val dayPlans = listOf(
        DietDayPlan(
            dayLabel = "Monday",
            title = "Curation",
            description = "Balance your nutrition with fresh meals and simple daily choices.",
            breakfastTitle = "Avocado & Poached Egg",
            breakfastSubtitle = "Sourdough toast with fresh herbs",
            lunchTitle = "Miso Glazed Salmon Bowl",
            lunchSubtitle = "Wild salmon with rice and greens",
            snackTitle = "Raw Almonds & Green Apple",
            snackSubtitle = "Simple, fiber-rich snack",
            dinnerTitle = "Grilled Harvest Platter",
            dinnerSubtitle = "Seasonal veggies, protein, and avocado"
        ),
        DietDayPlan(
            dayLabel = "Tuesday",
            title = "Protein Day",
            description = "Lean meals to support strength and recovery.",
            breakfastTitle = "Greek Yogurt Bowl",
            breakfastSubtitle = "Berries, chia, and honey",
            lunchTitle = "Chicken Quinoa Salad",
            lunchSubtitle = "Greens with avocado and seeds",
            snackTitle = "Boiled Eggs & Berries",
            snackSubtitle = "Quick protein boost",
            dinnerTitle = "Grilled Chicken Plate",
            dinnerSubtitle = "Veggies, sweet potato, and olive oil"
        ),
        DietDayPlan(
            dayLabel = "Wednesday",
            title = "Light Flow",
            description = "Easy meals for steady energy and focus.",
            breakfastTitle = "Oatmeal & Banana",
            breakfastSubtitle = "Warm oats with cinnamon",
            lunchTitle = "Tuna Rice Bowl",
            lunchSubtitle = "Cucumber, corn, and sesame",
            snackTitle = "Apple Slices & Peanut Butter",
            snackSubtitle = "Balanced midday snack",
            dinnerTitle = "Veggie Stir Fry",
            dinnerSubtitle = "Tofu with brown rice"
        ),
        DietDayPlan(
            dayLabel = "Thursday",
            title = "Green Reset",
            description = "Fresh vegetables and hydration focused meals.",
            breakfastTitle = "Spinach Omelette",
            breakfastSubtitle = "Tomato, onion, and herbs",
            lunchTitle = "Green Buddha Bowl",
            lunchSubtitle = "Broccoli, cucumber, and chickpeas",
            snackTitle = "Cucumber Sticks & Hummus",
            snackSubtitle = "Crunchy and light",
            dinnerTitle = "Baked Fish & Greens",
            dinnerSubtitle = "Lemon, herbs, and salad"
        ),
        DietDayPlan(
            dayLabel = "Friday",
            title = "Fuel Up",
            description = "Comforting meals before the weekend.",
            breakfastTitle = "Peanut Butter Toast",
            breakfastSubtitle = "Banana and seeds",
            lunchTitle = "Turkey Wrap",
            lunchSubtitle = "Lettuce, tomato, and yogurt sauce",
            snackTitle = "Mixed Nuts",
            snackSubtitle = "Small energy boost",
            dinnerTitle = "Rice & Grilled Veggies",
            dinnerSubtitle = "Simple end-of-week dinner"
        ),
        DietDayPlan(
            dayLabel = "Saturday",
            title = "Weekend Fresh",
            description = "Relaxed eating with colorful ingredients.",
            breakfastTitle = "Fruit Bowl",
            breakfastSubtitle = "Yogurt and granola",
            lunchTitle = "Pasta Salad",
            lunchSubtitle = "Pesto, olives, and tomatoes",
            snackTitle = "Dark Chocolate & Almonds",
            snackSubtitle = "Treat, but still balanced",
            dinnerTitle = "Grilled Salmon",
            dinnerSubtitle = "Asparagus and herb potatoes"
        ),
        DietDayPlan(
            dayLabel = "Sunday",
            title = "Soft Reset",
            description = "A gentle day to recharge for the week ahead.",
            breakfastTitle = "Smoothie Bowl",
            breakfastSubtitle = "Fruit, oats, and seeds",
            lunchTitle = "Soup & Sandwich",
            lunchSubtitle = "Warm, light, and filling",
            snackTitle = "Dates & Walnuts",
            snackSubtitle = "Naturally sweet snack",
            dinnerTitle = "Roasted Veg Plate",
            dinnerSubtitle = "Seasonal vegetables and grains"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(layoutId("activity_diet"))

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(id("dietRoot"))) { v, insets ->
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
        tvDayLabel = findViewById(id("tvDayLabel"))
        tvTitle = findViewById(id("tvTitle"))
        tvDescription = findViewById(id("tvDescription"))
        cardBreakfast = findViewById(id("cardBreakfast"))
        cardLunch = findViewById(id("cardLunch"))
        cardSnack = findViewById(id("cardSnack"))
        cardDinner = findViewById(id("cardDinner"))
        tvBreakfastTitle = findViewById(id("tvBreakfastTitle"))
        tvBreakfastSubtitle = findViewById(id("tvBreakfastSubtitle"))
        tvLunchTitle = findViewById(id("tvLunchTitle"))
        tvLunchSubtitle = findViewById(id("tvLunchSubtitle"))
        tvSnackTitle = findViewById(id("tvSnackTitle"))
        tvSnackSubtitle = findViewById(id("tvSnackSubtitle"))
        tvDinnerTitle = findViewById(id("tvDinnerTitle"))
        tvDinnerSubtitle = findViewById(id("tvDinnerSubtitle"))
        tabBody = findViewById(id("tabBody"))
        tabDiet = findViewById(id("tabDiet"))
        tabScanner = findViewById(id("tabScanner"))
        tabChallenge = findViewById(id("tabChallenge"))
        tabAccount = findViewById(id("tabAccount"))
    }

    private fun setupDayTabs() {
        dayChecks = listOf(
            findViewById(id("dayMon")),
            findViewById(id("dayTue")),
            findViewById(id("dayWed")),
            findViewById(id("dayThu")),
            findViewById(id("dayFri")),
            findViewById(id("daySat")),
            findViewById(id("daySun"))
        )

        dayChecks.forEachIndexed { index, checkBox ->
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isUpdatingDayChecks) return@setOnCheckedChangeListener

                if (isChecked) {
                    selectDay(index)
                } else if (selectedDayIndex == index) {
                    isUpdatingDayChecks = true
                    checkBox.isChecked = true
                    isUpdatingDayChecks = false
                }
            }
        }

        selectDay(0)
    }

    private fun selectDay(index: Int) {
        selectedDayIndex = index
        isUpdatingDayChecks = true
        dayChecks.forEachIndexed { dayIndex, checkBox ->
            checkBox.isChecked = dayIndex == index
        }
        isUpdatingDayChecks = false
        updateDietContent()
    }

    private fun setupCards() {
        cardBreakfast.setOnClickListener {
            openYouTubeSearch("${tvBreakfastTitle.text} recipe")
        }
        cardLunch.setOnClickListener {
            openYouTubeSearch("${tvLunchTitle.text} recipe")
        }
        cardSnack.setOnClickListener {
            openYouTubeSearch("${tvSnackTitle.text} healthy snack")
        }
        cardDinner.setOnClickListener {
            openYouTubeSearch("${tvDinnerTitle.text} recipe")
        }
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
        }
        tabScanner.setOnClickListener {
            selectedTab = BottomTab.SCANNER
            updateTabAppearance()
            startActivity(Intent(this, ScannerActivity::class.java))
        }
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

    private fun updateDietContent() {
        val plan = dayPlans[selectedDayIndex]

        tvDayLabel.text = plan.dayLabel
        tvTitle.text = plan.title
        tvDescription.text = plan.description

        tvBreakfastTitle.text = plan.breakfastTitle
        tvBreakfastSubtitle.text = plan.breakfastSubtitle
        tvLunchTitle.text = plan.lunchTitle
        tvLunchSubtitle.text = plan.lunchSubtitle
        tvSnackTitle.text = plan.snackTitle
        tvSnackSubtitle.text = plan.snackSubtitle
        tvDinnerTitle.text = plan.dinnerTitle
        tvDinnerSubtitle.text = plan.dinnerSubtitle
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

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun layoutId(name: String): Int = resources.getIdentifier(name, "layout", packageName)

    private fun id(name: String): Int = resources.getIdentifier(name, "id", packageName)
}

