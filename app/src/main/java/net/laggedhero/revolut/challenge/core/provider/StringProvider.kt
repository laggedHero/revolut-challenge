package net.laggedhero.revolut.challenge.core.provider

import androidx.annotation.StringRes

interface StringProvider {
    fun getString(@StringRes id: Int): String
}