package net.laggedhero.revolut.challenge.core.injection.fragment

import androidx.fragment.app.Fragment
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
annotation class FragmentKey(val clazz: KClass<out Fragment>)