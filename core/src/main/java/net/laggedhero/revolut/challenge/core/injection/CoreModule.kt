package net.laggedhero.revolut.challenge.core.injection

import android.app.Application
import dagger.Module
import dagger.Provides
import net.laggedhero.revolut.challenge.core.provider.SchedulerProvider
import net.laggedhero.revolut.challenge.core.provider.SchedulerProviderImpl
import net.laggedhero.revolut.challenge.core.provider.StringProvider
import net.laggedhero.revolut.challenge.core.provider.StringProviderImpl

@Module
object CoreModule {

    @Provides
    fun providesSchedulerProvider(): SchedulerProvider {
        return SchedulerProviderImpl()
    }

    @Provides
    fun providesStringProvider(application: Application): StringProvider {
        return StringProviderImpl(application)
    }
}