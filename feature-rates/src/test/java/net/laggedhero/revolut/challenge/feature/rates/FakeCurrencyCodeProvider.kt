package net.laggedhero.revolut.challenge.feature.rates

import net.laggedhero.revolut.challenge.feature.rates.domain.CurrencyCode
import net.laggedhero.revolut.challenge.feature.rates.provider.CurrencyCodeProvider

class FakeCurrencyCodeProvider(
    private val current: CurrencyCode? = null
) : CurrencyCodeProvider {
    override fun current(): CurrencyCode {
        return current ?: throw Throwable(("FakeCurrencyCodeProvider: undefined currency"))
    }
}