package net.laggedhero.revolut.challenge.core.provider

import android.app.Application
import android.content.Context

internal class StringProviderImpl(
    application: Application,
    currencyStringProvider: CurrencyStringProvider
) : StringProvider, CurrencyStringProvider by currencyStringProvider {

    private val context: Context = application.applicationContext

    override fun getString(id: Int): String {
        return context.getString(id)
    }
}