package net.laggedhero.revolut.challenge.feature.rates.ui

import net.laggedhero.revolut.challenge.core.provider.StringProvider
import net.laggedhero.revolut.challenge.domain.CurrencyCode
import net.laggedhero.revolut.challenge.feature.rates.domain.Currency
import net.laggedhero.revolut.challenge.feature.rates.domain.Rates
import java.util.*

class RatesUiMapper(private val stringProvider: StringProvider) {
    fun map(rates: Rates): List<RatesListUiItem> {
        return listOf(rates.baseCurrency.toRatesListUiItem()) + rates.rates.map { it.toRatesListUiItem() }
    }

    private fun Currency.toRatesListUiItem(): RatesListUiItem {
        return RatesListUiItem(
            flagUrl = currencyCode.toFlagUrl(),
            currencyCode = currencyCode.value,
            currencyName = stringProvider.name(currencyCode),
            currencyConversion = "%.2f".format(appliedConversion.value)
        )
    }

    private fun CurrencyCode.toFlagUrl(): String {
        return FLAG_BASE_URL + value.toLowerCase(Locale.ROOT) + FLAG_FILE_EXT
    }

    companion object {
        const val FLAG_BASE_URL =
            "https://raw.githubusercontent.com/transferwise/currency-flags/master/src/flags/"
        const val FLAG_FILE_EXT = ".png"
    }
}