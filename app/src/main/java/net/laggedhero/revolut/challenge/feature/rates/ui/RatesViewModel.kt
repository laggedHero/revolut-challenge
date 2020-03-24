package net.laggedhero.revolut.challenge.feature.rates.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.Disposable
import net.laggedhero.revolut.challenge.R
import net.laggedhero.revolut.challenge.core.provider.SchedulerProvider
import net.laggedhero.revolut.challenge.core.provider.StringProvider
import net.laggedhero.revolut.challenge.domain.CurrencyCode
import net.laggedhero.revolut.challenge.feature.rates.domain.CurrencyRepository
import net.laggedhero.revolut.challenge.feature.rates.domain.Rates

class RatesViewModel(
    private val currencyRepository: CurrencyRepository,
    private val schedulerProvider: SchedulerProvider,
    private val stringProvider: StringProvider
) : ViewModel() {

    private val _state = MutableLiveData(RatesState(true))
    val state: LiveData<RatesState> get() = _state

    private var disposable: Disposable? = null

    init {
        disposable = currencyRepository.ratesFor(CurrencyCode.EUR)
            .subscribeOn(schedulerProvider.io())
            .map {
                RatesState(
                    loading = false,
                    rates = it
                )
            }
            .subscribe(
                { state -> _state.postValue(state) },
                {
                    val state = RatesState(
                        loading = false,
                        error = stringProvider.getString(R.string.generic_loading_error)
                    )
                    _state.postValue(state)
                }
            )
    }

    override fun onCleared() {
        disposable?.dispose()
    }

}

data class RatesState(
    val loading: Boolean,
    val rates: Rates? = null,
    val error: String? = null
)