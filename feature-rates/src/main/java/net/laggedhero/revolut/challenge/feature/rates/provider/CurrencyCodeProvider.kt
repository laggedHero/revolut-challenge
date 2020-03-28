package net.laggedhero.revolut.challenge.feature.rates.provider

import net.laggedhero.revolut.challenge.feature.rates.domain.CurrencyCode

interface CurrencyCodeProvider {
    fun current(): CurrencyCode
}