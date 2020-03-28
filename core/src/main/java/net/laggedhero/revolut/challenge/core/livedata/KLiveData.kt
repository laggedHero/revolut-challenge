package net.laggedhero.revolut.challenge.core.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

open class KLiveData<T>(value: T) : LiveData<T>(value) {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(): T {
        return super.getValue() as T
    }

    fun observe(owner: LifecycleOwner, block: (T) -> Unit) {
        super.observe(owner, Observer { block(it) })
    }
}