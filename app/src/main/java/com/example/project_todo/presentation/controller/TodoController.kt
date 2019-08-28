package com.example.project_todo.presentation.controller

import com.example.project_todo.entity.Todo

interface TodoController: BasePresenter {

    fun displayTodos(todoList: List<Todo>)

    fun displayLoading()
}