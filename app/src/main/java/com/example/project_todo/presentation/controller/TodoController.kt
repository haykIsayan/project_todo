package com.example.project_todo.presentation.controller

import com.example.project_todo.entity.Task

interface TodoController: BasePresenter {

    fun displayTodos(taskList: List<Task>)

    fun displayLoading()
}