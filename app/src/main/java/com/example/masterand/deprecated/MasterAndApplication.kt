package com.example.masterand.deprecated

import android.app.Application

@Deprecated(message = "replaced by dagger hilt")
class MasterAndApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
