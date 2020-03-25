package net.laggedhero.revolut.challenge.feature.rates

import net.laggedhero.revolut.challenge.domain.CurrencyCode

interface CurrencyCodeProvider {
    fun current(): CurrencyCode
}