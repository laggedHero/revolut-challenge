package net.laggedhero.revolut.challenge.feature.rates.data

import io.reactivex.Single
import net.laggedhero.revolut.challenge.core.Result
import net.laggedhero.revolut.challenge.domain.CurrencyCode
import net.laggedhero.revolut.challenge.feature.rates.domain.Currency
import net.laggedhero.revolut.challenge.feature.rates.domain.CurrencyConversion
import net.laggedhero.revolut.challenge.feature.rates.domain.CurrencyReferenceRate
import net.laggedhero.revolut.challenge.feature.rates.domain.Rates
import org.junit.Test

class CurrencyRepositoryImplTest {

    @Test
    fun `returns error when api fails`() {
        val error = Throwable("api error")
        val api = FakeCurrencyApi(
            latestRates = Single.error(error)
        )

        val sut = CurrencyRepositoryImpl(api)

        sut.ratesFor(CurrencyCode.EUR).test()
            .assertValue(Result.Failure(error))
            .dispose()
    }

    @Test
    fun `returns rates when api return currency rates`() {
        val currencyRatesDto = CurrencyRatesDto(
            baseCurrency = "EUR",
            rates = mapOf(
                "USD" to 1.13F
            )
        )

        val api = FakeCurrencyApi(
            latestRates = Single.just(currencyRatesDto)
        )

        val expectedRates = Rates(
            baseCurrency = Currency(
                CurrencyCode.EUR,
                CurrencyReferenceRate(1F),
                CurrencyConversion(1F)
            ),
            rates = listOf(
                Currency(
                    CurrencyCode.USD,
                    CurrencyReferenceRate(1.13F),
                    CurrencyConversion(1.13F)
                )
            )
        )

        val sut = CurrencyRepositoryImpl(api)

        sut.ratesFor(CurrencyCode.EUR).test()
            .assertValue(Result.Success(expectedRates))
            .dispose()
    }
}