package net.laggedhero.revolut.challenge.feature.rates.ui

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import net.laggedhero.revolut.challenge.R
import net.laggedhero.revolut.challenge.core.livedata.KLiveData
import net.laggedhero.revolut.challenge.core.livedata.KMutableLiveData
import net.laggedhero.revolut.challenge.core.provider.SchedulerProvider
import net.laggedhero.revolut.challenge.core.provider.StringProvider
import net.laggedhero.revolut.challenge.domain.CurrencyCode
import net.laggedhero.revolut.challenge.feature.rates.CurrencyCodeProvider
import net.laggedhero.revolut.challenge.feature.rates.domain.CurrencyConversion
import net.laggedhero.revolut.challenge.feature.rates.domain.CurrencyRepository
import net.laggedhero.revolut.challenge.feature.rates.domain.Rates
import java.util.concurrent.TimeUnit

class RatesViewModel(
    private val currencyRepository: CurrencyRepository,
    private val schedulerProvider: SchedulerProvider,
    private val stringProvider: StringProvider,
    currencyCodeProvider: CurrencyCodeProvider
) : ViewModel() {

    private val _state: KMutableLiveData<RatesState>
    val state: KLiveData<RatesState> get() = _state

    private var disposable: Disposable? = null

    private val currencyCodeSource = BehaviorSubject.create<CurrencyCode>()
    private val appliedConversionSource = BehaviorSubject.create<CurrencyConversion>()

    init {
        val currentCurrencyCode = currencyCodeProvider.current()
        _state = KMutableLiveData(
            RatesState(
                loading = true,
                selectedCurrencyCode = currentCurrencyCode,
                appliedCurrencyConversion = CurrencyConversion(1F)
            )
        )
        currencyCodeSource.onNext(currentCurrencyCode)
        observe()
    }

    private fun observe() {
        disposable = Observable.interval(1, TimeUnit.SECONDS, schedulerProvider.computation())
            .withLatestFrom(
                currencyCodeSource,
                BiFunction<Long, CurrencyCode, Pair<CurrencyCode, (Rates) -> (CurrencyConversion) -> RatesState>> { _, currencyCode ->
                    Pair(currencyCode, curriedRatesState(currencyCode))
                }
            )
            .observeOn(schedulerProvider.io())
            .doOnEach {
                val state = _state.value.copy(loading = true)
                _state.postValue(state)
            }
            .flatMap { pair ->
                currencyRepository.ratesFor(pair.first)
                    .map { pair.second(it) }
                    .toObservable()
            }
            .withLatestFrom(
                appliedConversionSource,
                BiFunction<(CurrencyConversion) -> RatesState, CurrencyConversion, RatesState> { curriedRatesState, conversion ->
                    curriedRatesState(conversion)
                }
            )
            .observeOn(schedulerProvider.computation())
            .onErrorReturn {
                _state.value.copy(
                    loading = false,
                    error = stringProvider.getString(R.string.generic_loading_error)
                )
            }
            .subscribe { state -> _state.postValue(state) }
    }

    fun selectCurrencyCode(currencyCode: CurrencyCode) {
        disposable?.dispose()
        currencyCodeSource.onNext(currencyCode)
        observe()
    }

    fun applyCurrencyConversion(currencyConversion: CurrencyConversion) {
        appliedConversionSource.onNext(currencyConversion)
    }

    override fun onCleared() {
        disposable?.dispose()
    }

}

data class RatesState(
    val loading: Boolean,
    val selectedCurrencyCode: CurrencyCode,
    val appliedCurrencyConversion: CurrencyConversion,
    val rates: Rates? = null,
    val error: String? = null
)

private val curriedRatesState: (CurrencyCode) -> (Rates) -> (CurrencyConversion) -> RatesState =
    { currencyCode ->
        { rates ->
            { appliedConversion ->
                RatesState(
                    loading = false,
                    selectedCurrencyCode = currencyCode,
                    appliedCurrencyConversion = appliedConversion,
                    rates = rates.withConversion(appliedConversion)
                )
            }
        }
    }

private fun Rates.withConversion(appliedConversion: CurrencyConversion): Rates {
    return copy(
        baseCurrency = baseCurrency.copy(appliedConversion = appliedConversion),
        rates = rates.map {
            it.copy(
                appliedConversion = CurrencyConversion(it.referenceRate.value * appliedConversion.value)
            )
        }
    )
}