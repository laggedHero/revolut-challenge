package net.laggedhero.revolut.challenge.feature.rates.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import net.laggedhero.revolut.challenge.core.provider.FakeSchedulerProvider
import net.laggedhero.revolut.challenge.core.provider.FakeStringProvider
import net.laggedhero.revolut.challenge.feature.rates.domain.FakeCurrencyRepository
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class RatesViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `starts with loading flagged as true`() {
        val testScheduler = TestScheduler()

        val sut = RatesViewModel(
            FakeCurrencyRepository(),
            FakeSchedulerProvider(testScheduler),
            FakeStringProvider()
        )

        Assert.assertEquals(
            RatesState(true),
            sut.state.value
        )
    }

    @Test
    fun `returns with an error message when api fails`() {
        val sut = RatesViewModel(
            FakeCurrencyRepository(
                Single.error(Throwable("Error"))
            ),
            FakeSchedulerProvider(),
            FakeStringProvider({ "Error message" })
        )

        Assert.assertEquals(
            RatesState(
                loading = false,
                error = "Error message"
            ),
            sut.state.value
        )
    }
}