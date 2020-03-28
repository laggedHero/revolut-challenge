package net.laggedhero.revolut.challenge.feature.rates.data

data class RatesDto(
    val baseCurrency: String,
    val rates: Map<String, Float>
)