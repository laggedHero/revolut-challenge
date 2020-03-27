package net.laggedhero.revolut.challenge

import android.app.Application
import net.laggedhero.revolut.challenge.injection.AppComponent
import net.laggedhero.revolut.challenge.injection.DaggerAppComponent

class RevolutChallengeApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}