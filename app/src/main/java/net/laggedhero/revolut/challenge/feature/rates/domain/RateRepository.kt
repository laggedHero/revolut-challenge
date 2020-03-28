package net.laggedhero.revolut.challenge.feature.rates.domain

import io.reactivex.Single
import net.laggedhero.revolut.challenge.core.Result
import net.laggedhero.revolut.challenge.domain.CurrencyCode

interface RateRepository {
    fun ratesFor(currencyCode: CurrencyCode): Single<Result<Rates>>
}