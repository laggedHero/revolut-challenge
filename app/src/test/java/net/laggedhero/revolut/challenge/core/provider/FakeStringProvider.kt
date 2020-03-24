package net.laggedhero.revolut.challenge.core.provider

import net.laggedhero.revolut.challenge.domain.CurrencyCode

class FakeStringProvider(
    private val getString: (id: Int) -> String = { "getString" },
    private val name: (currencyCode: CurrencyCode) -> String = { "name" }
) : StringProvider {
    override fun getString(id: Int): String {
        return getString.invoke(id)
    }

    override fun name(currencyCode: CurrencyCode): String {
        return name.invoke(currencyCode)
    }
}