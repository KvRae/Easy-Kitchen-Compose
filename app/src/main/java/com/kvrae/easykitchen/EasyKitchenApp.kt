package com.kvrae.easykitchen

import android.app.Application
import com.kvrae.easykitchen.di.dataModule
import com.kvrae.easykitchen.di.databaseModule
import com.kvrae.easykitchen.di.domainModule
import com.kvrae.easykitchen.di.networkModule
import com.kvrae.easykitchen.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class EasyKitchenApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@EasyKitchenApp)
            modules(
                networkModule,
                databaseModule,
                dataModule,
                domainModule,
                presentationModule
            )
        }
    }
}
