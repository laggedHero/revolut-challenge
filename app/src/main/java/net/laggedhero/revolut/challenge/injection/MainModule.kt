package net.laggedhero.revolut.challenge.injection

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import net.laggedhero.revolut.challenge.core.injection.fragment.DaggerFragmentFactory
import net.laggedhero.revolut.challenge.core.injection.fragment.FragmentKey
import net.laggedhero.revolut.challenge.core.injection.viewmodel.DaggerViewModelFactory
import net.laggedhero.revolut.challenge.core.injection.viewmodel.ViewModelKey
import net.laggedhero.revolut.challenge.core.provider.SchedulerProvider
import net.laggedhero.revolut.challenge.core.provider.SchedulerProviderImpl
import net.laggedhero.revolut.challenge.core.provider.StringProvider
import net.laggedhero.revolut.challenge.core.provider.StringProviderImpl
import net.laggedhero.revolut.challenge.feature.rates.CurrencyProvider
import net.laggedhero.revolut.challenge.feature.rates.CurrencyProviderImpl
import net.laggedhero.revolut.challenge.feature.rates.domain.RatesRepository
import net.laggedhero.revolut.challenge.feature.rates.ui.RatesFragment
import net.laggedhero.revolut.challenge.feature.rates.ui.RatesUiMapper
import net.laggedhero.revolut.challenge.feature.rates.ui.RatesViewModel
import javax.inject.Provider

@Module
object MainModule {

    @Provides
    @IntoMap
    @FragmentKey(RatesFragment::class)
    fun providesRatesFragment(
        viewModelFactory: ViewModelProvider.Factory,
        ratesUiMapper: RatesUiMapper
    ): Fragment {
        return RatesFragment(viewModelFactory, ratesUiMapper)
    }

    @Provides
    fun providesDaggerFragmentFactory(creator: Map<Class<out Fragment>, @JvmSuppressWildcards Provider<Fragment>>): FragmentFactory {
        return DaggerFragmentFactory(creator)
    }

    @Provides
    @IntoMap
    @ViewModelKey(RatesViewModel::class)
    fun providesRatesViewModel(
        ratesRepository: RatesRepository,
        schedulerProvider: SchedulerProvider,
        stringProvider: StringProvider,
        currencyProvider: CurrencyProvider
    ): ViewModel {
        return RatesViewModel(
            ratesRepository, schedulerProvider, stringProvider, currencyProvider
        )
    }

    @Provides
    fun providesDaggerViewModelFactory(creator: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory {
        return DaggerViewModelFactory(creator)
    }

    @Provides
    fun providesSchedulerProvider(): SchedulerProvider {
        return SchedulerProviderImpl()
    }

    @Provides
    fun providesStringProvider(
        application: Application
    ): StringProvider {
        return StringProviderImpl(application)
    }

    @Provides
    fun providesCurrencyCodeProvider(): CurrencyProvider {
        return CurrencyProviderImpl()
    }

    @Provides
    fun providesRatesUiMapper(stringProvider: StringProvider): RatesUiMapper {
        return RatesUiMapper(stringProvider)
    }
}