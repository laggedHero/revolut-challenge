package net.laggedhero.revolut.challenge.feature.rates.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import net.laggedhero.revolut.challenge.core.extension.toCurrencyCode
import net.laggedhero.revolut.challenge.databinding.FragmentRatesBinding
import net.laggedhero.revolut.challenge.feature.rates.domain.CurrencyConversion

class RatesFragment(
    viewModelFactory: ViewModelProvider.Factory,
    private val ratesUiMapper: RatesUiMapper
) : Fragment() {

    private val viewModel by viewModels<RatesViewModel> { viewModelFactory }

    private var _binding: FragmentRatesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: RatesListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpRecyclerView()
        observeState()
    }

    private fun setUpRecyclerView() {
        adapter = RatesListAdapter(::onSelectCurrency, ::onApplyConversion)
        binding.fragmentRatesRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.fragmentRatesRecyclerView.adapter = adapter
    }

    private fun onSelectCurrency(currency: String) {
        viewModel.selectCurrencyCode(currency.toCurrencyCode())
    }

    private fun onApplyConversion(conversion: String) {
        viewModel.applyCurrencyConversion(CurrencyConversion(conversion.toFloat()))
    }

    private fun observeState() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            // TODO: show/hide loading
            state.error?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
            state.rates?.let {
                adapter.submitList(ratesUiMapper.map(it))
            }
        }
    }
}