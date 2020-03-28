package net.laggedhero.revolut.challenge.feature.rates.data

import io.reactivex.Single
import net.laggedhero.revolut.challenge.core.Result
import net.laggedhero.revolut.challenge.feature.rates.domain.ConversionRate
import net.laggedhero.revolut.challenge.feature.rates.domain.Rate
import net.laggedhero.revolut.challenge.feature.rates.domain.Rates
import net.laggedhero.revolut.challenge.feature.rates.domain.ReferenceRate
import org.junit.Test
import java.util.*

class RatesRepositoryImplTest {

    @Test
    fun `returns error when api fails`() {
        val error = Throwable("api error")
        val api = FakeRatesApi(
            latestRates = Single.error(error)
        )

        val sut = RatesRepositoryImpl(api)

        sut.ratesFor(Currency.getInstance("EUR")).test()
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
                Currency.getInstance("EUR"),
                ReferenceRate(1F),
                ConversionRate(1F)
            ),
            rates = listOf(
                Rate(
                    Currency.getInstance("USD"),
                    ReferenceRate(1.13F),
                    ConversionRate(1.13F)
                )
            )
        )

        val sut = RatesRepositoryImpl(api)

        sut.ratesFor(Currency.getInstance("EUR")).test()
            .assertValue(Result.Success(expectedRates))
            .dispose()
    }
}