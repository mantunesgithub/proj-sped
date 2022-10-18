
package br.com.mantunes.sped.ui.fragment.extensions

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.mostraMsg(mensagem: String) {
    Toast.makeText(
        context,
        mensagem,
        Toast.LENGTH_LONG
    ).show()
}