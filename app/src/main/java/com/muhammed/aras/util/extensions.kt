package com.muhammed.aras.util

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun Context.showMessage(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun String?.hyphenIfEmpty(): String {
    return if (this.isNullOrEmpty()) "-" else this
}

fun String.toDate(): Date? {
    val inputFormat = SimpleDateFormat(SHORT_DATE_FORMAT, Locale.getDefault())
    return try {
        inputFormat.parse(this)
    } catch (e: Exception) {
        null
    }
}

fun Date.format(): String? {
    val dateFormat = SimpleDateFormat(SHORT_DATE_FORMAT, Locale.getDefault())
    return dateFormat.format(this.time)
}

fun ImageView.load(uri: Uri?) {
    uri?.let {
        Glide.with(this.context)
            .load(it)
            .into(this)
    }
}

fun View.onClick(action: (View) -> Unit) {
    this.setOnClickListener {
        action.invoke(this)
    }
}

context(Fragment)
fun <T> Flow<T>.collectFlow(action: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        collectLatest {
            action(it)
        }
    }
}

fun TextInputEditText.onSearch(action: (String) -> Unit) {
    this.addTextChangedListener {
        if (it.toString().trim().isEmpty()) return@addTextChangedListener
        CoroutineScope(Dispatchers.Main).launch {
            delay(350)
            action(it.toString())
        }
    }
}

fun TextInputEditText.onTextEntered(action: (String) -> Unit) {
    this.doAfterTextChanged {
        if (it.toString().trim().isEmpty()) return@doAfterTextChanged
        CoroutineScope(Dispatchers.Main).launch {
            delay(350)
            action(it.toString())
        }
    }
}

fun Fragment.toggleFullScreen(isFullScreen: Boolean) {
    val window = requireActivity().window
    val controller = WindowInsetsControllerCompat(window, window.decorView)

    if (isFullScreen) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        controller.hide(WindowInsetsCompat.Type.systemBars())
    } else {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        controller.show(WindowInsetsCompat.Type.systemBars())
    }
}