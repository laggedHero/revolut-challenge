package net.laggedhero.revolut.challenge.feature.rates

import java.util.*

class CurrencyCodeProviderImpl : CurrencyCodeProvider {

    override fun current(): Currency {
        return Currency.getInstance(Locale.getDefault())
    }
}