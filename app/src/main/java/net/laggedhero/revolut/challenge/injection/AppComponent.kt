package net.laggedhero.revolut.challenge.injection

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import net.laggedhero.revolut.challenge.MainActivity
import net.laggedhero.revolut.challenge.core.injection.CoreModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        CoreModule::class,
        MainModule::class,
        NetworkModule::class
    ]
)
interface AppComponent {

    fun inject(mainActivity: MainActivity)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}