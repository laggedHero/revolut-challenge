package net.laggedhero.revolut.challenge.feature.rates.domain

import net.laggedhero.revolut.challenge.domain.CurrencyCode

data class Currency(
    val currencyCode: CurrencyCode,
    val referenceRate: CurrencyReferenceRate,
    val appliedConversion: CurrencyConversion
)

inline class CurrencyReferenceRate(val value: Float)

inline class CurrencyConversion(val value: Float)