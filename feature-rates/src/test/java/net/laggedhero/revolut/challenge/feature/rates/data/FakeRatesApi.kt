package net.laggedhero.revolut.challenge.feature.rates.data

import io.reactivex.Single

class FakeRatesApi(
    private val latestRates: Single<RatesDto> = Single.error(Throwable("undefined"))
) : RatesApi {
    override fun latestRates(base: String): Single<RatesDto> {
        return latestRates
    }
}