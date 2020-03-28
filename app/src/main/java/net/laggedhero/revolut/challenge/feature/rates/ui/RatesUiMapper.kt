package net.laggedhero.revolut.challenge.feature.rates.ui

import net.laggedhero.revolut.challenge.core.provider.StringProvider
import net.laggedhero.revolut.challenge.feature.rates.domain.Rate
import net.laggedhero.revolut.challenge.feature.rates.domain.Rates
import java.util.*

class RatesUiMapper(private val stringProvider: StringProvider) {
    fun map(rates: Rates): List<RatesListUiItem> {
        return listOf(rates.baseRate.toRatesListUiItem()) + rates.rates.map { it.toRatesListUiItem() }
    }

    private fun Rate.toRatesListUiItem(): RatesListUiItem {
        return RatesListUiItem(
            flagUrl = currency.toFlagUrl(),
            currencyCode = currency.currencyCode,
            currencyName = currency.displayName,
            currencyConversion = "%.2f".format(conversionRate.value)
        )
    }

    private fun Currency.toFlagUrl(): String {
        return FLAG_BASE_URL + currencyCode.toLowerCase(Locale.ROOT) + FLAG_FILE_EXT
    }

    companion object {
        const val FLAG_BASE_URL =
            "https://raw.githubusercontent.com/transferwise/currency-flags/master/src/flags/"
        const val FLAG_FILE_EXT = ".png"
    }
}