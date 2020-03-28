package net.laggedhero.revolut.challenge.feature.rates.data

import io.reactivex.Single
import net.laggedhero.revolut.challenge.core.Result
import net.laggedhero.revolut.challenge.core.extension.toCurrencyCode
import net.laggedhero.revolut.challenge.domain.CurrencyCode
import net.laggedhero.revolut.challenge.feature.rates.domain.*

internal class RateRepositoryImpl(
    private val ratesApi: RatesApi
) : RateRepository {
    override fun ratesFor(currencyCode: CurrencyCode): Single<Result<Rates>> {
        return ratesApi.latestRates(currencyCode.value)
            .map<Result<Rates>> { Result.Success(it.toRates()) }
            .onErrorReturn { Result.Failure(it) }
    }

    private fun RatesDto.toRates(): Rates {
        return Rates(
            baseRate = Rate(
                currencyCode = baseCurrency.toCurrencyCode(),
                referenceRate = ReferenceRate(1F),
                appliedConversion = ConversionRate(1F)
            ),
            rates = rates.toCurrencyList()
        )
    }

    private fun Map<String, Float>.toCurrencyList(): List<Rate> {
        return map {
            Rate(
                currencyCode = it.key.toCurrencyCode(),
                referenceRate = ReferenceRate(it.value),
                appliedConversion = ConversionRate(it.value)
            )
        }
    }
}