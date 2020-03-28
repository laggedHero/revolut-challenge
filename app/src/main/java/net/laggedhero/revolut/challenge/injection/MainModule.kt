package net.laggedhero.revolut.challenge.injection

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import net.laggedhero.revolut.challenge.core.injection.fragment.DaggerFragmentFactory
import net.laggedhero.revolut.challenge.core.injection.fragment.FragmentKey
import net.laggedhero.revolut.challenge.feature.rates.injection.FeatureRatesComponent
import net.laggedhero.revolut.challenge.feature.rates.ui.RatesFragment
import javax.inject.Provider

@Module(
    subcomponents = [
        FeatureRatesComponent::class
    ]
)
object MainModule {

    @Provides
    @IntoMap
    @FragmentKey(RatesFragment::class)
    fun providesRatesFragment(
        featureRatesComponentBuilder: FeatureRatesComponent.Builder
    ): Fragment {
        val featureRatesComponent = featureRatesComponentBuilder.build()
        return featureRatesComponent.ratesFragment()
    }

    @Provides
    fun providesDaggerFragmentFactory(creator: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>): FragmentFactory {
        return DaggerFragmentFactory(creator)
    }
}