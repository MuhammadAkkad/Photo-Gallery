package com.muhammed.aras.presentation.common.helper

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

@FragmentScoped
class DialogHelper @Inject constructor(
    private val fragment: Fragment
) {
    fun showDialog(
        title: String,
        message: String,
        positiveButtonText: String,
        negativeButtonText: String,
        onPositiveButtonClick: () -> Unit,
        onNegativeButtonClick: () -> Unit
    ) {
        AlertDialog.Builder(fragment.context).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(positiveButtonText) { _, _ -> onPositiveButtonClick() }
            setNegativeButton(negativeButtonText) { dialog, _ ->
                onNegativeButtonClick()
                dialog.dismiss()
            }
        }.create().show()
    }
}