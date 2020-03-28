package net.laggedhero.revolut.challenge.feature.rates.data

import io.reactivex.Single
import net.laggedhero.revolut.challenge.core.Result
import net.laggedhero.revolut.challenge.feature.rates.domain.*
import org.junit.Test

class RateRepositoryImplTest {

    @Test
    fun `returns error when api fails`() {
        val error = Throwable("api error")
        val api = FakeRatesApi(
            latestRates = Single.error(error)
        )

        val sut = RateRepositoryImpl(api)

        sut.ratesFor(CurrencyCode.EUR).test()
            .assertValue(Result.Failure(error))
            .dispose()
    }

    @Test
    fun `returns rates when api return currency rates`() {
        val currencyRatesDto = RatesDto(
            baseCurrency = "EUR",
            rates = mapOf(
                "USD" to 1.13F
            )
        )

        val api = FakeRatesApi(
            latestRates = Single.just(currencyRatesDto)
        )

        val expectedRates = Rates(
            baseRate = Rate(
                CurrencyCode.EUR,
                ReferenceRate(1F),
                ConversionRate(1F)
            ),
            rates = listOf(
                Rate(
                    CurrencyCode.USD,
                    ReferenceRate(1.13F),
                    ConversionRate(1.13F)
                )
            )
        )

        val sut = RateRepositoryImpl(api)

        sut.ratesFor(CurrencyCode.EUR).test()
            .assertValue(Result.Success(expectedRates))
            .dispose()
    }
}