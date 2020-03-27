package net.laggedhero.revolut.challenge.feature.rates.ui

import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import net.laggedhero.revolut.challenge.R
import net.laggedhero.revolut.challenge.core.Result
import net.laggedhero.revolut.challenge.core.livedata.KLiveData
import net.laggedhero.revolut.challenge.core.livedata.KMutableLiveData
import net.laggedhero.revolut.challenge.core.map
import net.laggedhero.revolut.challenge.core.onFailure
import net.laggedhero.revolut.challenge.core.onSuccess
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
        selectCurrencyCode(currentCurrencyCode)
        applyCurrencyConversion(CurrencyConversion(1F))
    }

    private fun observe() {
        disposable = Observable.combineLatest(
            timedCurrencySource()
                .doOnEach { notifyLoading() }
                .flatMap { pair -> currencyRepositorySource(pair.first, pair.second) },
            appliedConversionSource,
            BiFunction<Result<(CurrencyConversion) -> RatesState>, CurrencyConversion, Result<RatesState>> { result, conversion ->
                result.map { it(conversion) }
            }
        )
            .onErrorReturn { Result.Failure(it) }
            .subscribe { result ->
                result
                    .onSuccess { _state.postValue(it) }
                    .onFailure {
                        val state = _state.value.copy(
                            loading = false,
                            error = stringProvider.getString(R.string.generic_loading_error)
                        )
                        _state.postValue(state)
                    }
            }
    }

    private fun timedCurrencySource(): Observable<Pair<CurrencyCode, (Rates) -> (CurrencyConversion) -> RatesState>> {
        return Observable.combineLatest(
            intervalSource(),
            currencyCodeSource,
            BiFunction<Long, CurrencyCode, Pair<CurrencyCode, (Rates) -> (CurrencyConversion) -> RatesState>> { _, currencyCode ->
                Pair(currencyCode, curriedRatesState(currencyCode))
            }
        )
    }

    private fun intervalSource(): Observable<Long> {
        return Observable.interval(0, 1, TimeUnit.SECONDS, schedulerProvider.computation())
    }

    private fun notifyLoading() {
        val state = _state.value.copy(loading = true)
        _state.postValue(state)
    }

    private fun currencyRepositorySource(
        currencyCode: CurrencyCode,
        curriedState: (Rates) -> (CurrencyConversion) -> RatesState
    ): Observable<Result<(CurrencyConversion) -> RatesState>> {
        return currencyRepository.ratesFor(currencyCode)
            .map { result -> result.map { curriedState(it) } }
            .subscribeOn(schedulerProvider.io())
            .toObservable()
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