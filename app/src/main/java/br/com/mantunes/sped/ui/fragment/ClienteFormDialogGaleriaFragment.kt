package br.com.mantunes.sped.ui.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.DialogFragment

class ClienteFormDialogGaleriaFragment : DialogFragment() {
//    var permiteAcessoAGaleria: (permissao: String) -> Unit = {}

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Atenção")
            builder.setMessage("Precisamos do acesso a galeria do dispositivo, deseja permitir agora ?")
            builder.setNegativeButton("NÃO",
                DialogInterface.OnClickListener { dialog, id ->
                    dismiss()
                })
            builder.setPositiveButton("SIM",
                DialogInterface.OnClickListener { dialog, id ->
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", it.packageName, null)
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    dismiss()
//                    permiteAcessoAGaleria("sim")
                })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}
