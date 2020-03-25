package net.laggedhero.revolut.challenge.feature.rates.data

import io.reactivex.Single
import net.laggedhero.revolut.challenge.core.extension.toCurrencyCode
import net.laggedhero.revolut.challenge.domain.CurrencyCode
import net.laggedhero.revolut.challenge.feature.rates.domain.*

internal class CurrencyRepositoryImpl(
    private val currencyApi: CurrencyApi
) : CurrencyRepository {
    override fun ratesFor(currencyCode: CurrencyCode): Single<Rates> {
        return currencyApi.latestRates(currencyCode.value)
            .map { it.toRates() }
    }

    private fun CurrencyRatesDto.toRates(): Rates {
        return Rates(
            baseCurrency = Currency(
                currencyCode = baseCurrency.toCurrencyCode(),
                referenceRate = CurrencyReferenceRate(1F),
                appliedConversion = CurrencyConversion(1F)
            ),
            rates = rates.toCurrencyList()
        )
    }

    private fun Map<String, Float>.toCurrencyList(): List<Currency> {
        return map {
            Currency(
                currencyCode = it.key.toCurrencyCode(),
                referenceRate = CurrencyReferenceRate(it.value),
                appliedConversion = CurrencyConversion(it.value)
            )
        }
    }
}