package com.muhammed.surat

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast

context(androidx.fragment.app.Fragment)
class DialogHelper(
    private val title: String,
    private val message: String,
    private val positiveButton: String,
    private val negativeButton: String,
    private val onPositiveButtonClick: () -> Unit,
    private val onNegativeButtonClick: () -> Unit
) {

     fun showDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(positiveButton) { _, _ ->
                onPositiveButtonClick()
            }
            .setNegativeButton(negativeButton) { dialog, _ ->
                onNegativeButtonClick()
                dialog.dismiss()
            }
            .create()
            .show()
    }

}