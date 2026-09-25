package com.example.hana

import android.content.Context

data class UserProfile(
    val fullName: String,
    val age: Int,
    val gender: String,
    val heightCm: Int,
    val weightKg: Int,
    val email: String,
    val password: String
)

object ProfileStore {

    private const val PREFS_NAME = "hana_profile_store"
    private const val KEY_FULL_NAME = "full_name"
    private const val KEY_AGE = "age"
    private const val KEY_GENDER = "gender"
    private const val KEY_HEIGHT = "height"
    private const val KEY_WEIGHT = "weight"
    private const val KEY_EMAIL = "email"
    private const val KEY_PASSWORD = "password"

    fun saveProfile(context: Context, profile: UserProfile) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_FULL_NAME, profile.fullName)
            .putInt(KEY_AGE, profile.age)
            .putString(KEY_GENDER, profile.gender)
            .putInt(KEY_HEIGHT, profile.heightCm)
            .putInt(KEY_WEIGHT, profile.weightKg)
            .putString(KEY_EMAIL, profile.email)
            .putString(KEY_PASSWORD, profile.password)
            .apply()
    }

    fun loadProfile(context: Context): UserProfile? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val fullName = prefs.getString(KEY_FULL_NAME, null).orEmpty().trim()
        val email = prefs.getString(KEY_EMAIL, null).orEmpty().trim()
        if (fullName.isBlank() || email.isBlank()) return null

        return UserProfile(
            fullName = fullName,
            age = prefs.getInt(KEY_AGE, 0),
            gender = prefs.getString(KEY_GENDER, null).orEmpty(),
            heightCm = prefs.getInt(KEY_HEIGHT, 0),
            weightKg = prefs.getInt(KEY_WEIGHT, 0),
            email = email,
            password = prefs.getString(KEY_PASSWORD, null).orEmpty()
        )
    }

    fun hasProfile(context: Context): Boolean = loadProfile(context) != null

    fun clear(context: Context) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }
}

