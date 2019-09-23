package com.example.project_todo

import com.example.project_todo.core.TaskDataCore
import com.example.project_todo.core.TaskCollectionDataCore
import com.example.project_todo.core.TaskListRepository
import com.example.project_todo.core.TaskRepository
import com.example.project_todo.viewmodel.MainViewModel
import com.example.project_todo.viewmodel.TaskViewModel
import io.realm.Realm
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainViewModelModule = module { viewModel { MainViewModel(get(), get()) } }

val taskListsRepoModule = module { single { TaskCollectionDataCore() as TaskListRepository } }

val taskViewModelModule = module { viewModel { TaskViewModel(get(), get()) } }

val taskRepoModule = module { single { TaskDataCore(get()) as TaskRepository } }

val realmConfigModule = module { single {  Realm.getDefaultConfiguration() } }
