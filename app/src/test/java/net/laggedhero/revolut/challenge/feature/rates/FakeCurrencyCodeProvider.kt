package net.laggedhero.revolut.challenge.feature.rates

import net.laggedhero.revolut.challenge.domain.CurrencyCode

class FakeCurrencyCodeProvider(
    private val current: CurrencyCode? = null
) : CurrencyCodeProvider {
    override fun current(): CurrencyCode {
        return current ?: throw Throwable(("FakeCurrencyCodeProvider: undefined currency"))
    }
}