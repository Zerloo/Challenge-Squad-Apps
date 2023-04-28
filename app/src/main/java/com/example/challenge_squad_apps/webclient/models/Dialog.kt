package com.example.challenge_squad_apps.webclient.models

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class Dialog {

    fun showDialog(
        context: Context,
        title: String,
        message: String,
        positiveMessage: String,
        negativeMessage: String,
        positiveAction: () -> Unit,
        negativeAction: () -> Unit
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(negativeMessage) { dialog, which ->
                negativeAction()
            }
            .setPositiveButton(positiveMessage) { dialog, which ->
                positiveAction()
            }
            .show()
    }
}

