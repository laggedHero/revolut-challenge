package net.laggedhero.revolut.challenge.core.injection.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Provider

class DaggerViewModelFactory(
    private val creator: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val provider =
            creator[modelClass] ?: throw IllegalArgumentException("Unknown ViewModel $modelClass")
        return provider.get() as T
    }
}