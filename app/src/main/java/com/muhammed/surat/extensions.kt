package com.muhammed.surat

import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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

fun String.formatDate(): String {
    val inputFormat = SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    return try {
        val date = inputFormat.parse(this)
        date?.let {
            outputFormat.format(date)
        } ?: this
    } catch (e: Exception) {
        this
    }
}
// TODO: extract date patters to constants file.
fun String.toDate(): Date? {
    val inputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return try {
        inputFormat.parse(this)
    } catch (e: Exception) {
        null
    }
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
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            collectLatest {
                action(it)
            }
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