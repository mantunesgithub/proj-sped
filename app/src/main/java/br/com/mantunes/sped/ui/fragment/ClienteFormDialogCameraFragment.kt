package br.com.mantunes.sped.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import br.com.mantunes.sped.R
import br.com.mantunes.sped.databinding.ClienteFormDialogCamera2Binding
import kotlinx.android.synthetic.main.activity_main.*

class ClienteFormDialogCameraFragment : DialogFragment() {
    private lateinit var _bindingDialog: ClienteFormDialogCamera2Binding
    var quandoEscolheFoto: (foto: String) -> Unit = {}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindingDialog = ClienteFormDialogCamera2Binding.inflate(inflater, container, false)
        return _bindingDialog.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
            _bindingDialog.clienteFormCadastroPfBtnClose.setOnClickListener {
                dismiss()
            }
            _bindingDialog.clienteFormCadastroPfBtnCamera.setOnClickListener {
                quandoEscolheFoto(getString(R.string.foto))
                dismiss()
            }
            _bindingDialog.clienteFormCadastroPfBtnGaleria.setOnClickListener {
                quandoEscolheFoto(getString(R.string.galeria))
                dismiss()
            }
    }
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        return activity?.let {
//            val builder = AlertDialog.Builder(it)
//            // Get the layout inflater
//            val inflater = requireActivity().layoutInflater;
//            // Inflate and set the layout for the dialog
//            // Pass null as the parent view because its going in the dialog layout
//
//            builder.setView(inflater.inflate(R.layout.cliente_form_dialog_camera2, null))
//                // Add action buttons
//
//            builder.setPositiveButton(getString(R.string.button_positive_foto) ,
//                    DialogInterface.OnClickListener { dialog, id ->
//                           quandoEscolheFoto(getString(R.string.foto))
//                    })
//                .setNegativeButton(getString(R.string.button_negative_galeria),
//                    DialogInterface.OnClickListener { dialog, id ->
//                        quandoEscolheFoto(getString(R.string.galeria))
//                    })
//                .setNeutralButton(getString(R.string.button_neutral_sair),
//                    DialogInterface.OnClickListener { dialog, id ->
//                        dismiss()
//                    })
//            builder.create()
//        } ?: throw IllegalStateException("Activity cannot be null")
//    }
}