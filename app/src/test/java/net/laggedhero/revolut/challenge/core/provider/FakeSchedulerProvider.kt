package net.laggedhero.revolut.challenge.core.provider

import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class FakeSchedulerProvider(
    private val scheduler: Scheduler = Schedulers.trampoline()
) : SchedulerProvider {

    override fun io(): Scheduler {
        return scheduler
    }

    override fun ui(): Scheduler {
        return scheduler
    }
}