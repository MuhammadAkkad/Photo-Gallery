package com.muhammed.surat

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Locale


fun Context.showMessage(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun String?.hyphenIfEmpty(): String {
    return if (this.isNullOrEmpty() || this == "null") "-" else this
}

fun String.formatDate(): String {
    val inputFormat = SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy MMM dd", Locale.getDefault())

    return try {
        val date = inputFormat.parse(this)
        date?.let {
            outputFormat.format(date)
        } ?: this
    } catch (e: Exception) {
        this
    }
}