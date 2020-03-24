package net.laggedhero.revolut.challenge.feature.rates.domain

import io.reactivex.Single
import net.laggedhero.revolut.challenge.domain.CurrencyCode

class FakeCurrencyRepository(
    private val ratesFor: Single<Rates> = Single.error(Throwable("undefined"))
) : CurrencyRepository {
    override fun ratesFor(currencyCode: CurrencyCode): Single<Rates> {
        return ratesFor
    }
}