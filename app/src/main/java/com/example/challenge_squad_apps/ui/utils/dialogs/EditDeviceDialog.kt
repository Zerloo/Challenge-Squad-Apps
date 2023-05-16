package com.example.challenge_squad_apps.ui.utils.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.challenge_squad_apps.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class EditDeviceDialog(private val returnBackend: Boolean) : AppCompatDialogFragment() {

    private val listener: EditDeviceDialogListener by lazy {
        activity as EditDeviceDialogListener
    }

    companion object {
        const val TAG = "EditDeviceDialog"
        fun newInstance(returnBackend: Boolean) = EditDeviceDialog(returnBackend)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            isCancelable = false

            val message: String = if (returnBackend) {
                getString(R.string.dialog_dispositivo_editado_com_sucesso)
            } else {
                getString(R.string.dialog_falha_ao_atualizar_informacoes_do_dispositivo)
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

    interface EditDeviceDialogListener{
        fun confirmButtonClicked()
    }

}