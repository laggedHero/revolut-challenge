package net.laggedhero.revolut.challenge.feature.rates.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import net.laggedhero.revolut.challenge.databinding.RatesListItemViewBinding

class RatesListAdapter(
    private val onSelectCurrency: (String) -> Unit,
    private val onApplyConversionRate: (String) -> Unit
) : ListAdapter<RatesListUiItem, RatesListAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RatesListItemViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            onSelectCurrency,
            onApplyConversionRate
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) holder.bind(getItem(position))
        else holder.bind(payloads[0] as Bundle)
    }

    class ViewHolder(
        private val binding: RatesListItemViewBinding,
        private val onSelectCurrency: (String) -> Unit,
        private val onApplyConversionRate: (String) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.ratesListItemCurrencyConversion.onFocusChangeListener =
                View.OnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        onSelectCurrency(binding.ratesListItemCurrencyCode.text.toString())
                    }
                }

            binding.ratesListItemCurrencyConversion.doAfterTextChanged {
                if (binding.ratesListItemCurrencyConversion.hasFocus()) {
                    onApplyConversionRate(it.toString())
                }
            }
        }

        fun bind(data: RatesListUiItem) {
            binding.ratesListItemCurrencyCode.text = data.currencyCode
            binding.ratesListItemCurrencyName.text = data.currencyName
            binding.ratesListItemCurrencyConversion.setText(data.currencyConversion)
            bindFlag(data.flagUrl)
        }

        fun bind(bundle: Bundle) {
            bundle.keySet().forEach { key ->
                if (key == KEY_FLAG_URL) {
                    bindFlag(bundle.getString(KEY_FLAG_URL, ""))
                }
                if (key == KEY_CURRENCY_NAME) {
                    binding.ratesListItemCurrencyName.text = bundle.getString(KEY_CURRENCY_NAME, "")
                }
                if (key == KEY_CURRENCY_CONVERSION) {
                    if (!binding.ratesListItemCurrencyConversion.hasFocus()) {
                        binding.ratesListItemCurrencyConversion.setText(
                            bundle.getString(KEY_CURRENCY_CONVERSION, "")
                        )
                    }
                }
            }
        }

        private fun bindFlag(flagUrl: String) {
            Glide.with(binding.root)
                .load(flagUrl)
                .circleCrop()
                .into(binding.ratesListItemFlag)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RatesListUiItem>() {
            override fun areItemsTheSame(
                oldItem: RatesListUiItem,
                newItem: RatesListUiItem
            ): Boolean {
                return oldItem.currencyCode == newItem.currencyCode
            }

            override fun getChangePayload(
                oldItem: RatesListUiItem,
                newItem: RatesListUiItem
            ): Any? {
                val bundle = Bundle()
                if (oldItem.flagUrl != newItem.flagUrl) {
                    bundle.putString(KEY_FLAG_URL, newItem.flagUrl)
                }
                if (oldItem.currencyName != newItem.currencyName) {
                    bundle.putString(KEY_CURRENCY_NAME, newItem.currencyName)
                }
                if (oldItem.currencyConversion != newItem.currencyConversion) {
                    bundle.putString(KEY_CURRENCY_CONVERSION, newItem.currencyConversion)
                }
                return if (bundle.size() == 0) null else bundle
            }

            override fun areContentsTheSame(
                oldItem: RatesListUiItem,
                newItem: RatesListUiItem
            ): Boolean {
                return oldItem == newItem
            }
        }

        const val KEY_FLAG_URL = "KEY_FLAG_URL"
        const val KEY_CURRENCY_NAME = "KEY_CURRENCY_NAME"
        const val KEY_CURRENCY_CONVERSION = "KEY_CURRENCY_CONVERSION"
    }
}

data class RatesListUiItem(
    val flagUrl: String,
    val currencyCode: String,
    val currencyName: String,
    val currencyConversion: String
)