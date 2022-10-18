package br.com.mantunes.sped.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.mantunes.sped.R
import br.com.mantunes.sped.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {
    private lateinit var _binding : HomeFragmentBinding

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
//        setHasOptionsMenu(false)

        _binding.homeHeaderBtnComprar.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_mainActivity)
        }
    }
}