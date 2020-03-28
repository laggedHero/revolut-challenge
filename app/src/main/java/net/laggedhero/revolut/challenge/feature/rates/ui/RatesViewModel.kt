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
import net.laggedhero.revolut.challenge.feature.rates.CurrencyProvider
import net.laggedhero.revolut.challenge.feature.rates.domain.ConversionRate
import net.laggedhero.revolut.challenge.feature.rates.domain.RatesRepository
import net.laggedhero.revolut.challenge.feature.rates.domain.Rates
import java.util.*
import java.util.concurrent.TimeUnit

class RatesViewModel(
    private val ratesRepository: RatesRepository,
    private val schedulerProvider: SchedulerProvider,
    private val stringProvider: StringProvider,
    currencyProvider: CurrencyProvider
) : ViewModel() {

    private val _state: KMutableLiveData<RatesState>
    val state: KLiveData<RatesState> get() = _state

    private var disposable: Disposable? = null

    private val currencySource = BehaviorSubject.create<Currency>()
    private val appliedConversionRateSource = BehaviorSubject.create<ConversionRate>()

    init {
        val currentCurrencyCode = currencyProvider.current()
        _state = KMutableLiveData(
            RatesState(
                loading = true,
                selectedCurrency = currentCurrencyCode,
                appliedConversionRate = ConversionRate(1F)
            )
        )
        selectCurrency(currentCurrencyCode)
        applyConversionRate(ConversionRate(1F))
    }

    private fun observe() {
        disposable = Observable.combineLatest(
            timedCurrencySource()
                .doOnEach { notifyLoading() }
                .flatMap { pair -> currencyRepositorySource(pair.first, pair.second) },
            appliedConversionRateSource,
            BiFunction<Result<(ConversionRate) -> RatesState>, ConversionRate, Result<RatesState>> { result, conversion ->
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

    private fun timedCurrencySource(): Observable<Pair<Currency, (Rates) -> (ConversionRate) -> RatesState>> {
        return Observable.combineLatest(
            intervalSource(),
            currencySource,
            BiFunction<Long, Currency, Pair<Currency, (Rates) -> (ConversionRate) -> RatesState>> { _, currencyCode ->
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
        currencyCode: Currency,
        curriedState: (Rates) -> (ConversionRate) -> RatesState
    ): Observable<Result<(ConversionRate) -> RatesState>> {
        return ratesRepository.ratesFor(currencyCode)
            .map { result -> result.map { curriedState(it) } }
            .subscribeOn(schedulerProvider.io())
            .toObservable()
    }

    fun selectCurrency(currency: Currency) {
        disposable?.dispose()
        currencySource.onNext(currency)
        observe()
    }

    fun applyConversionRate(conversionRate: ConversionRate) {
        appliedConversionRateSource.onNext(conversionRate)
    }

    override fun onCleared() {
        disposable?.dispose()
    }

}

data class RatesState(
    val loading: Boolean,
    val selectedCurrency: Currency,
    val appliedConversionRate: ConversionRate,
    val rates: Rates? = null,
    val error: String? = null
)

private val curriedRatesState: (Currency) -> (Rates) -> (ConversionRate) -> RatesState =
    { currencyCode ->
        { rates ->
            { appliedConversion ->
                RatesState(
                    loading = false,
                    selectedCurrency = currencyCode,
                    appliedConversionRate = appliedConversion,
                    rates = rates.withConversion(appliedConversion)
                )
            }
        }
    }

private fun Rates.withConversion(appliedConversion: ConversionRate): Rates {
    return copy(
        baseRate = baseRate.copy(conversionRate = appliedConversion),
        rates = rates.map {
            it.copy(
                conversionRate = ConversionRate(it.referenceRate.value * appliedConversion.value)
            )
        }
    )
}