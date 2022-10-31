package br.com.mantunes.sped.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import br.com.mantunes.sped.R
import br.com.mantunes.sped.model.Promocao

class HomePromocaoFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.home_promocao, container, false)

    val title: TextView = view.findViewById(R.id.txt_title)
    val desc: TextView = view.findViewById(R.id.txt_desc)
    val nome: TextView = view.findViewById(R.id.txt_nome)
    val valant: TextView = view.findViewById(R.id.txt_valor_ant)
    val valatu: TextView = view.findViewById(R.id.txt_valor_atu)
    val image: ImageView = view.findViewById(R.id.img)

    arguments?.let {
      title.text = it.getString(KEY_TITLE)
      desc.text = it.getString(KEY_DESC)
      nome.text = it.getString(KEY_NOME)
      valant.text = it.getString(KEY_VALANT)
      valatu.text = it.getString(KEY_VALATU)
      image.setImageResource(it.getInt(KEY_IMAGE))
    }

    return view
  }

  companion object {

    fun newInstance(promocao: Promocao) : HomePromocaoFragment {
      val args = Bundle().apply {
        putString(KEY_TITLE, promocao.title)
        putString(KEY_NOME, promocao.nome)
        putString(KEY_DESC, promocao.desc)
        putString(KEY_VALANT, promocao.valorAnt)
        putString(KEY_VALATU, promocao.valorAtua)
        putInt(KEY_IMAGE, promocao.drawableId)
      }
      val frag = HomePromocaoFragment()
      frag.arguments = args
      return frag
    }

    const val KEY_TITLE = "title"
    const val KEY_DESC = "desc"
    const val KEY_IMAGE = "img"
    const val KEY_NOME = "nome"
    const val KEY_VALANT = "valant"
    const val KEY_VALATU = "valatu"
  }

}