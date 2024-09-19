package com.muhammed.surat

import android.app.AlertDialog
import androidx.fragment.app.Fragment
import javax.inject.Inject
import dagger.hilt.android.scopes.FragmentScoped

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