package com.example.project_todo

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_todo.core.TaskDataCore
import com.example.project_todo.core.TaskListDataCore
import com.example.project_todo.core.TaskListRepository
import com.example.project_todo.core.TaskRepository
import com.example.project_todo.entity.Task
import com.example.project_todo.viewmodel.MainViewModel
import com.example.project_todo.viewmodel.TaskViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainViewModelModule = module { viewModel { MainViewModel(get(), get()) } }

val taskListsRepoModule = module { single { TaskListDataCore() as TaskListRepository } }

val taskViewModelModule = module { viewModel { TaskViewModel(get()) } }

val taskRepoModule = module {single { TaskDataCore() as TaskRepository } }