package com.example.challenge_squad_apps.ui.utils.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.challenge_squad_apps.R
import com.example.challenge_squad_apps.webclient.dto.models.Device
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class DeleteDeviceDialog(private val device: Device) : AppCompatDialogFragment() {

    private val listener: DeleteDeviceDialogListener by lazy {
        activity as DeleteDeviceDialogListener
    }

    companion object {
        const val TAG = "DeleteDeviceDialog"
        fun newInstance(device: Device) = DeleteDeviceDialog(device)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { activity ->
            isCancelable = false

            MaterialAlertDialogBuilder(activity)
                .setTitle(R.string.dialog_remover_dispositivo)
                .setMessage(R.string.dialog_voce_tem_certeza_que_deseja_remover_este_dispositivo)
                .setPositiveButton(R.string.dialog_confirmar) { _, _ ->
                    lifecycleScope.launch {
                        listener.confirmButtonClicked(device)
                        dismiss()
                    }
                }
                .setNegativeButton(R.string.dialog_cancelar) { _, _ ->
                    dismiss()
                }
                .create()
        } ?: throw IllegalAccessException("Activity cannot be null")
    }

    interface DeleteDeviceDialogListener {
        suspend fun confirmButtonClicked(device: Device)
    }
}