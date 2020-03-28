package net.laggedhero.revolut.challenge.feature.rates

import java.util.*

class FakeCurrencyProvider(
    private val current: Currency? = null
) : CurrencyProvider {
    override fun current(): Currency {
        return current ?: throw Throwable(("FakeCurrencyProvider: undefined currency"))
    }
}