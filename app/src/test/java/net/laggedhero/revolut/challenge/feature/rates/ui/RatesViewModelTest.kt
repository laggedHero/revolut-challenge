package net.laggedhero.revolut.challenge.feature.rates.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.reactivex.Single
import io.reactivex.schedulers.TestScheduler
import net.laggedhero.revolut.challenge.core.provider.FakeSchedulerProvider
import net.laggedhero.revolut.challenge.core.provider.FakeStringProvider
import net.laggedhero.revolut.challenge.feature.rates.FakeCurrencyProvider
import net.laggedhero.revolut.challenge.feature.rates.domain.*
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit

class RatesViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `starts with initial state`() {
        val testScheduler = TestScheduler()

        val sut = RatesViewModel(
            FakeRatesRepository(),
            FakeSchedulerProvider(testScheduler),
            FakeStringProvider(),
            FakeCurrencyProvider(Currency.getInstance("EUR"))
        )

        Assert.assertEquals(
            RatesState(true, Currency.getInstance("EUR"), ConversionRate(1F)),
            sut.state.value
        )
    }

    @Test
    fun `returns with an error message when api fails`() {
        val testScheduler = TestScheduler()

        val sut = RatesViewModel(
            FakeRatesRepository { Single.error(Throwable("Error")) },
            FakeSchedulerProvider(testScheduler),
            FakeStringProvider { "Error message" },
            FakeCurrencyProvider(Currency.getInstance("EUR"))
        )

        testScheduler.advanceTimeBy(0, TimeUnit.SECONDS)

        Assert.assertEquals(
            RatesState(
                loading = false,
                selectedCurrency = Currency.getInstance("EUR"),
                appliedConversionRate = ConversionRate(1F),
                error = "Error message"
            ),
            sut.state.value
        )
    }

    @Test
    fun `api failure does not break the chain`() {
        val testScheduler = TestScheduler()

        val rates = Factory.createRates()

        var counter = 0
        val testSingleProducer = {
            when (counter) {
                1 -> {
                    counter++
                    Single.error(Throwable("Error"))
                }
                else -> {
                    counter++
                    Single.just(rates)
                }
            }
        }

        val sut = RatesViewModel(
            FakeRatesRepository(testSingleProducer),
            FakeSchedulerProvider(testScheduler),
            FakeStringProvider { "Error message" },
            FakeCurrencyProvider(Currency.getInstance("EUR"))
        )

        testScheduler.advanceTimeBy(0, TimeUnit.SECONDS)
        Assert.assertNull(sut.state.value.error)

        testScheduler.advanceTimeBy(1, TimeUnit.SECONDS)
        Assert.assertNotNull(sut.state.value.error)

        testScheduler.advanceTimeBy(2, TimeUnit.SECONDS)
        Assert.assertNull(sut.state.value.error)
    }

    @Test
    fun `returns with a rates object when api succeeds`() {
        val testScheduler = TestScheduler()

        val rates = Factory.createRates()

        val sut = RatesViewModel(
            FakeRatesRepository { Single.just(rates) },
            FakeSchedulerProvider(testScheduler),
            FakeStringProvider(),
            FakeCurrencyProvider(Currency.getInstance("EUR"))
        )

        testScheduler.advanceTimeBy(0, TimeUnit.SECONDS)
        Assert.assertEquals(rates, sut.state.value.rates)
    }

    @Test
    fun `returns with an updated rates object when applying a conversion`() {
        val testScheduler = TestScheduler()

        val rates = Factory.createRates()

        val expectedRates = Factory.createRates(
            Factory.createRate(Currency.getInstance("EUR"), 1F, 2F),
            listOf(
                Factory.createRate(Currency.getInstance("USD"), 1.13F, 2.26F)
            )
        )

        val sut = RatesViewModel(
            FakeRatesRepository { Single.just(rates) },
            FakeSchedulerProvider(testScheduler),
            FakeStringProvider(),
            FakeCurrencyProvider(Currency.getInstance("EUR"))
        )

        testScheduler.advanceTimeBy(0, TimeUnit.SECONDS)

        sut.applyConversionRate(ConversionRate(2F))

        Assert.assertEquals(expectedRates, sut.state.value.rates)
    }

    @Test
    fun `returns with an updated rates object when selecting a currency code`() {
        val testScheduler = TestScheduler()

        val eurRates = Factory.createRates()

        val usdRates = Factory.createRates(
            Factory.createRate(Currency.getInstance("USD"), 1F, 1F),
            listOf(
                Factory.createRate(Currency.getInstance("EUR"), 0.9F, 0.9F)
            )
        )

        var counter = 0
        val testSingleProducer = {
            when (counter) {
                1 -> {
                    counter++
                    Single.just(usdRates)
                }
                else -> {
                    counter++
                    Single.just(eurRates)
                }
            }
        }

        val expectedRates = Factory.createRates(
            Factory.createRate(Currency.getInstance("USD"), 1F, 1F),
            listOf(
                Factory.createRate(Currency.getInstance("EUR"), 0.9F, 0.9F)
            )
        )

        val sut = RatesViewModel(
            FakeRatesRepository(testSingleProducer),
            FakeSchedulerProvider(testScheduler),
            FakeStringProvider(),
            FakeCurrencyProvider(Currency.getInstance("EUR"))
        )

        testScheduler.advanceTimeBy(0, TimeUnit.SECONDS)

        sut.selectCurrency(Currency.getInstance("USD"))

        testScheduler.advanceTimeBy(0, TimeUnit.SECONDS)

        Assert.assertEquals(expectedRates, sut.state.value.rates)
    }

    @Test
    fun `when changing currency the conversion is kept`() {
        val testScheduler = TestScheduler()

        val eurRates = Factory.createRates()

        val usdRates = Factory.createRates(
            Factory.createRate(Currency.getInstance("USD"), 1F, 1F),
            listOf(
                Factory.createRate(Currency.getInstance("EUR"), 0.9F, 0.9F)
            )
        )

        var counter = 0
        val testSingleProducer = {
            when (counter) {
                1 -> {
                    counter++
                    Single.just(usdRates)
                }
                else -> {
                    counter++
                    Single.just(eurRates)
                }
            }
        }

        val expectedRates = Factory.createRates(
            Factory.createRate(Currency.getInstance("USD"), 1F, 2F),
            listOf(
                Factory.createRate(Currency.getInstance("EUR"), 0.9F, 1.8F)
            )
        )

        val sut = RatesViewModel(
            FakeRatesRepository(testSingleProducer),
            FakeSchedulerProvider(testScheduler),
            FakeStringProvider(),
            FakeCurrencyProvider(Currency.getInstance("EUR"))
        )

        testScheduler.advanceTimeBy(0, TimeUnit.SECONDS)

        sut.applyConversionRate(ConversionRate(2F))
        sut.selectCurrency(Currency.getInstance("USD"))

        testScheduler.advanceTimeBy(0, TimeUnit.SECONDS)

        Assert.assertEquals(expectedRates, sut.state.value.rates)
    }

    object Factory {
        fun createRates(
            baseRate: Rate = createRate(
                Currency.getInstance("EUR"), 1F, 1F
            ),
            rates: List<Rate> = listOf(
                createRate(
                    Currency.getInstance("USD"), 1.13F, 1.13F
                )
            )
        ): Rates {
            return Rates(
                baseRate = baseRate,
                rates = rates
            )
        }

        fun createRate(
            currency: Currency,
            rate: Float,
            conversion: Float
        ): Rate {
            return Rate(
                currency,
                ReferenceRate(rate),
                ConversionRate(conversion)
            )
        }
    }
}