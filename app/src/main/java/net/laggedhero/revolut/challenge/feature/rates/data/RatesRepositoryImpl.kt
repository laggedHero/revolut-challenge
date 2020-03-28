package net.laggedhero.revolut.challenge.feature.rates.data

import io.reactivex.Single
import net.laggedhero.revolut.challenge.core.Result
import net.laggedhero.revolut.challenge.feature.rates.domain.*
import java.util.*

internal class RatesRepositoryImpl(
    private val ratesApi: RatesApi
) : RatesRepository {
    override fun ratesFor(currency: Currency): Single<Result<Rates>> {
        return ratesApi.latestRates(currency.currencyCode)
            .map<Result<Rates>> { Result.Success(it.toRates()) }
            .onErrorReturn { Result.Failure(it) }
    }

    private fun RatesDto.toRates(): Rates {
        return Rates(
            baseRate = Rate(
                currency = Currency.getInstance(baseCurrency),
                referenceRate = ReferenceRate(1F),
                conversionRate = ConversionRate(1F)
            ),
            rates = rates.toCurrencyList()
        )
    }

    private fun Map<String, Float>.toCurrencyList(): List<Rate> {
        return map {
            Rate(
                currency = Currency.getInstance(it.key),
                referenceRate = ReferenceRate(it.value),
                conversionRate = ConversionRate(it.value)
            )
        }
    }
}