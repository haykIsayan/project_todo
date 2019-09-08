package com.example.project_todo.presentation

import com.example.project_todo.presentation.controller.TodoController
import com.example.project_todo.viewmodel.MainViewModel
import com.example.project_todo.viewmodel.TaskViewModel

class TodosPresenter(
    private val todoController: TodoController,
    private val taskViewModel: TaskViewModel,
    private val mainViewModel: MainViewModel) {


    fun loadData() {

        taskViewModel

    }




}