package br.com.mantunes.sped

import android.app.Application
import br.com.mantunes.sped.di.daoModule
import br.com.mantunes.sped.di.testeDatabaseModule
import br.com.mantunes.sped.di.uiModule
import br.com.mantunes.sped.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppApplication)
            modules(
                listOf(
                    testeDatabaseModule,
                    daoModule,
                    uiModule,
                    viewModelModule
                )
            )
        }
    }
}