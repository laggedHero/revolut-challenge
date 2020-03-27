package net.laggedhero.revolut.challenge.feature.rates.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import net.laggedhero.revolut.challenge.databinding.FragmentRatesBinding

class RatesFragment(
    viewModelFactory: ViewModelProvider.Factory
) : Fragment() {

    private val viewModel by viewModels<RatesViewModel> { viewModelFactory }

    private var _binding: FragmentRatesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRatesBinding.inflate(inflater, container, false)
        return binding.root
    }
}