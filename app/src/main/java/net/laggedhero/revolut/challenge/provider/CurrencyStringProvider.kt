package net.laggedhero.revolut.challenge.provider

import net.laggedhero.revolut.challenge.domain.CurrencyCode

interface CurrencyStringProvider {
    fun name(currencyCode: CurrencyCode): String
}