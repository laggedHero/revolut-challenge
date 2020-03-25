package net.laggedhero.revolut.challenge.feature.rates.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import net.laggedhero.revolut.challenge.core.provider.FakeSchedulerProvider
import net.laggedhero.revolut.challenge.core.provider.FakeStringProvider
import net.laggedhero.revolut.challenge.domain.CurrencyCode
import net.laggedhero.revolut.challenge.feature.rates.FakeCurrencyCodeProvider
import net.laggedhero.revolut.challenge.feature.rates.domain.CurrencyConversion
import net.laggedhero.revolut.challenge.feature.rates.domain.FakeCurrencyRepository
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.TimeUnit

class RatesViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `starts with initial state`() {
        val testScheduler = TestScheduler()

        val sut = RatesViewModel(
            FakeCurrencyRepository(),
            FakeSchedulerProvider(testScheduler),
            FakeStringProvider(),
            FakeCurrencyCodeProvider(CurrencyCode.EUR)
        )

        Assert.assertEquals(
            RatesState(true, CurrencyCode.EUR, CurrencyConversion(1F)),
            sut.state.value
        )
    }

    @Test
    fun `returns with an error message when api fails`() {
        val testScheduler = TestScheduler()

        val sut = RatesViewModel(
            FakeCurrencyRepository(
                Single.error(Throwable("Error"))
            ),
            FakeSchedulerProvider(testScheduler),
            FakeStringProvider({ "Error message" }),
            FakeCurrencyCodeProvider(CurrencyCode.EUR)
        )

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        Assert.assertEquals(
            RatesState(
                loading = false,
                selectedCurrencyCode = CurrencyCode.EUR,
                appliedCurrencyConversion = CurrencyConversion(1F),
                error = "Error message"
            ),
            sut.state.value
        )
    }
}