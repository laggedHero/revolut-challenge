package net.laggedhero.revolut.challenge.feature.rates.injection

import dagger.Subcomponent
import net.laggedhero.revolut.challenge.feature.rates.ui.RatesFragment

@Subcomponent(
    modules = [
        FeatureRatesModule::class
    ]
)
interface FeatureRatesComponent {

    fun ratesFragment(): RatesFragment

    @Subcomponent.Builder
    interface Builder {
        fun build(): FeatureRatesComponent
    }
}