package net.laggedhero.revolut.challenge.feature.rates.data

data class CurrencyRatesDto(
    val baseCurrency: String,
    val rates: Map<String, Float>
)