package net.laggedhero.revolut.challenge.feature.rates

import java.util.*

interface CurrencyProvider {
    fun current(): Currency
}