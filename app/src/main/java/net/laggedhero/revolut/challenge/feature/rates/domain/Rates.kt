package net.laggedhero.revolut.challenge.feature.rates.domain

data class Rates(
    val baseCurrency: Currency,
    val rates: List<Currency>
)