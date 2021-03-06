package net.laggedhero.revolut.challenge.feature.rates.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RatesApi {

    @GET("latest")
    fun latestRates(@Query("base") base: String): Single<RatesDto>
}