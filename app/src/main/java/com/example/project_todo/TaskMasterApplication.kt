package com.example.project_todo

import android.app.Application
import io.realm.Realm
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TaskMasterApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@TaskMasterApplication)
            modules(listOf(
                mainViewModelModule,
                taskViewModelModule,
                taskListsRepoModule,
                taskRepoModule,
                realmConfigModule))
        }
    }
}