package com.example.challenge_squad_apps.ui.utils.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.challenge_squad_apps.R
import com.example.challenge_squad_apps.webclient.exceptions.HttpResponse
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddDeviceDialog(private val backendResponse: Int) : AppCompatDialogFragment() {

    private val listener: AddDeviceDialogListener by lazy {
        activity as AddDeviceDialogListener
    }

    companion object {
        const val TAG = "AddDeviceDialog"
        fun newInstance(backendResponse: Int) = AddDeviceDialog(backendResponse)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            isCancelable = false

            val message: String = when (backendResponse) {
                HttpResponse.fromCode(backendResponse)!!.code -> getString(HttpResponse.fromCode(backendResponse)!!.message)
                else -> {
                    getString(R.string.dialog_falha_ao_cadastrar_informacoes_do_dispositivo)
                }
            }

            val title = if (backendResponse == HttpResponse.HTTP_201_CREATED.code) "Dispositivo adicionado" else ("Falha ao adicionar dispositivo")

            MaterialAlertDialogBuilder(activity)
                .setMessage(message)
                .setTitle(title)
                .setPositiveButton(R.string.dialog_ok) { _, _ ->
                    if (message == getString(R.string.HTTP_201_CREATED)) listener.confirmButtonClicked()
                    dismiss()
                }
                .create()
        } ?: throw IllegalAccessException("Activity cannot be null")
    }

    interface AddDeviceDialogListener {
        fun confirmButtonClicked()
    }
}