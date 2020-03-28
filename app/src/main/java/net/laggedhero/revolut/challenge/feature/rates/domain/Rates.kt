package net.laggedhero.revolut.challenge.feature.rates.domain

data class Rates(
    val baseRate: Rate,
    val rates: List<Rate>
)