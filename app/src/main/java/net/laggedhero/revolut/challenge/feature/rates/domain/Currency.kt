package net.laggedhero.revolut.challenge.feature.rates.domain

import java.util.*

data class Rate(
    val currency: Currency,
    val referenceRate: ReferenceRate,
    val conversionRate: ConversionRate
)

inline class ReferenceRate(val value: Float)

inline class ConversionRate(val value: Float)