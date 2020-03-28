package net.laggedhero.revolut.challenge.feature.rates.provider

import net.laggedhero.revolut.challenge.feature.rates.domain.CurrencyCode
import net.laggedhero.revolut.challenge.feature.rates.extension.toCurrencyCode
import java.util.*

class CurrencyCodeProviderImpl :
    CurrencyCodeProvider {
    override fun current(): CurrencyCode {
        return Currency.getInstance(Locale.getDefault())
            .currencyCode.toCurrencyCode()
    }
}