package net.laggedhero.revolut.challenge.feature.rates.domain

import io.reactivex.Single
import net.laggedhero.revolut.challenge.core.Result
import java.util.*

interface CurrencyRepository {
    fun ratesFor(currency: Currency): Single<Result<Rates>>
}