package com.example.challenge_squad_apps.ui.utils.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.challenge_squad_apps.R
import com.example.challenge_squad_apps.ui.activities.AddDeviceActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddDeviceDialog(private val returnBackend: Boolean) : AppCompatDialogFragment() {

    private val listener: AddDeviceDialogListener by lazy {
        activity as AddDeviceDialogListener
    }

    companion object {
        const val TAG = "AddDeviceDialog"
        fun newInstance(returnBackend: Boolean) = AddDeviceDialog(returnBackend)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            isCancelable = false

            val message: String = if (returnBackend) {
                getString(R.string.dialog_dispositivo_cadastrado_com_sucesso)
            } else {
                getString(R.string.dialog_falha_ao_cadastrar_informacoes_do_dispositivo)
            }

            MaterialAlertDialogBuilder(activity)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_ok) { _, _ ->
                    listener.confirmButtonClicked()
                    dismiss()
                }
                .create()
        } ?: throw IllegalAccessException("Activity cannot be null")
    }

    interface AddDeviceDialogListener {
        fun confirmButtonClicked()
    }
}