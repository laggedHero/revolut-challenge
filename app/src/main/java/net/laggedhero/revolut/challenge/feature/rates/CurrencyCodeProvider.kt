package net.laggedhero.revolut.challenge.feature.rates

import java.util.*

interface CurrencyCodeProvider {
    fun current(): Currency
}