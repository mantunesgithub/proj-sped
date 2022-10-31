package br.com.mantunes.sped.ui.fragment.extensions

import android.os.Handler
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager

fun Fragment.mostraMsg(mensagem: String) {
    Toast.makeText(
        context,
        mensagem,
        Toast.LENGTH_LONG
    ).show()
}

