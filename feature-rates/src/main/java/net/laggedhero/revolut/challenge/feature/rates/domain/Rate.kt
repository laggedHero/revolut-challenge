package net.laggedhero.revolut.challenge.feature.rates.domain

data class Rate(
    val currencyCode: CurrencyCode,
    val referenceRate: ReferenceRate,
    val appliedConversion: ConversionRate
)

inline class ReferenceRate(val value: Float)

inline class ConversionRate(val value: Float)