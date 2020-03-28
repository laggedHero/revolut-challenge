package net.laggedhero.revolut.challenge.feature.rates.injection

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import net.laggedhero.revolut.challenge.core.injection.viewmodel.DaggerViewModelFactory
import net.laggedhero.revolut.challenge.core.injection.viewmodel.ViewModelKey
import net.laggedhero.revolut.challenge.core.provider.SchedulerProvider
import net.laggedhero.revolut.challenge.core.provider.StringProvider
import net.laggedhero.revolut.challenge.feature.rates.data.RateRepositoryImpl
import net.laggedhero.revolut.challenge.feature.rates.data.RatesApi
import net.laggedhero.revolut.challenge.feature.rates.domain.RateRepository
import net.laggedhero.revolut.challenge.feature.rates.provider.CurrencyCodeProvider
import net.laggedhero.revolut.challenge.feature.rates.provider.CurrencyCodeProviderImpl
import net.laggedhero.revolut.challenge.feature.rates.provider.CurrencyStringProvider
import net.laggedhero.revolut.challenge.feature.rates.provider.CurrencyStringProviderImpl
import net.laggedhero.revolut.challenge.feature.rates.ui.RatesFragment
import net.laggedhero.revolut.challenge.feature.rates.ui.RatesUiMapper
import net.laggedhero.revolut.challenge.feature.rates.ui.RatesViewModel
import retrofit2.Retrofit
import javax.inject.Provider

@Module
object FeatureRatesModule {

    @Provides
    fun providesRatesFragment(
        viewModelFactory: ViewModelProvider.Factory,
        ratesUiMapper: RatesUiMapper
    ): RatesFragment {
        return RatesFragment(viewModelFactory, ratesUiMapper)
    }

    @Provides
    @IntoMap
    @ViewModelKey(RatesViewModel::class)
    fun providesRatesViewModel(
        rateRepository: RateRepository,
        schedulerProvider: SchedulerProvider,
        stringProvider: StringProvider,
        currencyCodeProvider: CurrencyCodeProvider
    ): ViewModel {
        return RatesViewModel(
            rateRepository, schedulerProvider, stringProvider, currencyCodeProvider
        )
    }

    @Provides
    fun providesDaggerViewModelFactory(creator: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>): ViewModelProvider.Factory {
        return DaggerViewModelFactory(creator)
    }

    @Provides
    fun providesCurrencyCodeProvider(): CurrencyCodeProvider {
        return CurrencyCodeProviderImpl()
    }

    @Provides
    fun providesCurrencyStringProvider(application: Application): CurrencyStringProvider {
        return CurrencyStringProviderImpl(application)
    }

    @Provides
    fun providesRatesUiMapper(currencyStringProvider: CurrencyStringProvider): RatesUiMapper {
        return RatesUiMapper(currencyStringProvider)
    }

    @Provides
    fun providesRatesApi(retrofit: Retrofit): RatesApi {
        return retrofit.create(RatesApi::class.java)
    }

    @Provides
    fun providesRatesRepository(ratesApi: RatesApi): RateRepository {
        return RateRepositoryImpl(ratesApi)
    }
}