package net.laggedhero.revolut.challenge.feature.rates.data

import io.reactivex.Single
import net.laggedhero.revolut.challenge.core.Result
import net.laggedhero.revolut.challenge.feature.rates.domain.*
import java.util.*

internal class CurrencyRepositoryImpl(
    private val currencyApi: CurrencyApi
) : CurrencyRepository {
    override fun ratesFor(currency: Currency): Single<Result<Rates>> {
        return currencyApi.latestRates(currency.currencyCode)
            .map<Result<Rates>> { Result.Success(it.toRates()) }
            .onErrorReturn { Result.Failure(it) }
    }

    private fun CurrencyRatesDto.toRates(): Rates {
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