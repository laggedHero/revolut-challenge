package net.laggedhero.revolut.challenge.core.provider

import androidx.annotation.StringRes

interface PlatformStringProvider {
    fun getString(@StringRes id: Int): String
}