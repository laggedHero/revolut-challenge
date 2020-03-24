package net.laggedhero.revolut.challenge.feature.rates.data

import io.reactivex.Single

class FakeCurrencyApi(
    private val latestRates: Single<CurrencyRatesDto> = Single.error(Throwable("undefined"))
) : CurrencyApi {
    override fun latestRates(base: String): Single<CurrencyRatesDto> {
        return latestRates
    }
}