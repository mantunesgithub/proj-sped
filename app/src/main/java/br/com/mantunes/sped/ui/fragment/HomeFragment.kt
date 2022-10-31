package br.com.mantunes.sped.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import br.com.mantunes.sped.R
import br.com.mantunes.sped.databinding.HomeFragmentBinding
import br.com.mantunes.sped.di.criaPromocoes
import br.com.mantunes.sped.model.Promocao
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {
    private lateinit var _binding: HomeFragmentBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var homePromocaoAdapter: HomePromocaoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabLayout = view.findViewById(R.id.tab_layout)
        viewPager2 = view.findViewById(R.id.view_pager)

        val listaPromocoes = criaPromocoes()

        homePromocaoAdapter = HomePromocaoAdapter(this, listaPromocoes)
        viewPager2.adapter = homePromocaoAdapter
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = "Promoção #${position + 1}"
        }.attach()

        _binding.homeHeaderBtnComprar.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_mainActivity)
        }
    }
}