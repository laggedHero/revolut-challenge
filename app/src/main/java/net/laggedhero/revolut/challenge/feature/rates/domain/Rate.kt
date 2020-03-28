package net.laggedhero.revolut.challenge.feature.rates.domain

import net.laggedhero.revolut.challenge.domain.CurrencyCode

data class Rate(
    val currencyCode: CurrencyCode,
    val referenceRate: ReferenceRate,
    val appliedConversion: ConversionRate
)

inline class ReferenceRate(val value: Float)

inline class ConversionRate(val value: Float)