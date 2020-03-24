package net.laggedhero.revolut.challenge.core.provider

import net.laggedhero.revolut.challenge.domain.CurrencyCode

interface CurrencyStringProvider {
    fun name(currencyCode: CurrencyCode): String
}