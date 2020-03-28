package net.laggedhero.revolut.challenge.feature.rates

import java.util.*

class CurrencyProviderImpl : CurrencyProvider {

    override fun current(): Currency {
        return Currency.getInstance(Locale.getDefault())
    }
}