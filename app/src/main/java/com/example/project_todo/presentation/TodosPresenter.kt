package com.example.project_todo.presentation

import com.example.project_todo.presentation.controller.TodoController
import com.example.project_todo.viewmodel.MainViewModel
import com.example.project_todo.viewmodel.TodosViewModel

class TodosPresenter(
    private val todoController: TodoController,
    private val todosViewModel: TodosViewModel,
    private val mainViewModel: MainViewModel) {


    fun loadData() {

        todosViewModel

    }




}