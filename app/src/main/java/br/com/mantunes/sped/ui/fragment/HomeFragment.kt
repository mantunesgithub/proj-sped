package br.com.mantunes.sped.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import br.com.mantunes.sped.R
import br.com.mantunes.sped.databinding.HomeFragmentBinding
import br.com.mantunes.sped.di.criaProductCarousel
import br.com.mantunes.sped.di.criaPromocoes
import br.com.mantunes.sped.model.ProductCarousel
import br.com.mantunes.sped.ui.recyclerview.adapter.ProductAdapter
import br.com.mantunes.sped.ui.viewmodel.LoginViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.android.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private lateinit var _binding: HomeFragmentBinding
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var homePromocaoAdapter: HomePromocaoAdapter
    private val controlador by lazy {
        findNavController()
    }
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)

        val listCarousel = criaProductCarousel()

        val adapter = ProductAdapter(listCarousel)

        _binding.apply {
            carouselRecyclerciew.adapter = adapter
            carouselRecyclerciew.set3DItem(true)
            carouselRecyclerciew.setAlpha(true)
            carouselRecyclerciew.setInfinite(true)
        }
        return _binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.i("TAG", "onView: chegou no onViewCreated")
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