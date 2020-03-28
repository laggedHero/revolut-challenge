package net.laggedhero.revolut.challenge.feature.rates.domain

import io.reactivex.Single
import net.laggedhero.revolut.challenge.core.Result

interface RateRepository {
    fun ratesFor(currencyCode: CurrencyCode): Single<Result<Rates>>
}