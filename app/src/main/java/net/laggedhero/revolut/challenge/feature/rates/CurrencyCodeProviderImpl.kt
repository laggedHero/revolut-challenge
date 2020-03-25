package net.laggedhero.revolut.challenge.feature.rates

import net.laggedhero.revolut.challenge.core.extension.toCurrencyCode
import net.laggedhero.revolut.challenge.domain.CurrencyCode
import java.util.*

class CurrencyCodeProviderImpl : CurrencyCodeProvider {
    override fun current(): CurrencyCode {
        return Currency.getInstance(Locale.getDefault())
            .currencyCode.toCurrencyCode()
    }
}