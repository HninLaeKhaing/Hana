package com.example.hana

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri

fun Context.openYouTubeSearch(query: String) {
    val encodedQuery = Uri.encode(query.trim())
    val youtubeUrl = "https://www.youtube.com/results?search_query=$encodedQuery"
    val intent = Intent(Intent.ACTION_VIEW, youtubeUrl.toUri())

    try {
        startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        val browserIntent = Intent(Intent.ACTION_VIEW, youtubeUrl.toUri())
        startActivity(browserIntent)
    }
}

