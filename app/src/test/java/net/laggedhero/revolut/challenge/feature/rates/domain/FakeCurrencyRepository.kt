package net.laggedhero.revolut.challenge.feature.rates.domain

import io.reactivex.Single
import net.laggedhero.revolut.challenge.core.Result
import net.laggedhero.revolut.challenge.domain.CurrencyCode

class FakeCurrencyRepository(
    private val ratesFor: () -> Single<Rates> = { Single.error(Throwable("undefined")) }
) : CurrencyRepository {
    override fun ratesFor(currencyCode: CurrencyCode): Single<Result<Rates>> {
        return ratesFor.invoke()
            .map<Result<Rates>> { Result.Success(it) }
            .onErrorReturn { Result.Failure(it) }
    }
}