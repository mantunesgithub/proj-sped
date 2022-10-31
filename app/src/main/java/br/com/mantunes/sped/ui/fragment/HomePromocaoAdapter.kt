package br.com.mantunes.sped.ui.fragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.mantunes.sped.model.Promocao

class HomePromocaoAdapter(
  fa: HomeFragment,
  private val listaPromocoes: List<Promocao>) : FragmentStateAdapter(fa) {

  override fun getItemCount(): Int {
    return listaPromocoes.size
  }

  override fun createFragment(position: Int): Fragment {
    return HomePromocaoFragment.newInstance(listaPromocoes[position])
  }

}