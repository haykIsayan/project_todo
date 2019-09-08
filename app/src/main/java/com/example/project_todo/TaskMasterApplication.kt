package com.example.project_todo

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class TaskMasterApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@TaskMasterApplication)
            modules(listOf(mainViewModelModule, taskViewModelModule, taskListsRepoModule, taskRepoModule))
        }
    }
}