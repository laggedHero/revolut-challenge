package net.laggedhero.revolut.challenge.feature.rates

import java.util.*

class FakeCurrencyCodeProvider(
    private val current: Currency? = null
) : CurrencyCodeProvider {
    override fun current(): Currency {
        return current ?: throw Throwable(("FakeCurrencyCodeProvider: undefined currency"))
    }
}