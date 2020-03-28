package net.laggedhero.revolut.challenge.core.test

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import net.laggedhero.revolut.challenge.core.provider.SchedulerProvider

class FakeSchedulerProvider(
    private val scheduler: Scheduler = Schedulers.trampoline()
) : SchedulerProvider {

    override fun io(): Scheduler {
        return scheduler
    }

    override fun computation(): Scheduler {
        return scheduler
    }

    override fun ui(): Scheduler {
        return scheduler
    }
}