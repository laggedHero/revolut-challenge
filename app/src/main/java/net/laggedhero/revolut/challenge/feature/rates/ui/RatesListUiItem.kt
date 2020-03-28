package net.laggedhero.revolut.challenge.feature.rates.ui

data class RatesListUiItem(
    val flagUrl: String,
    val currencyCode: String,
    val currencyName: String,
    val currencyConversion: String
)