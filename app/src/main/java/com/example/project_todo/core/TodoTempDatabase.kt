package com.example.project_todo.core

import com.example.project_todo.TodoUtils
import com.example.project_todo.entity.Todo

object TodoTempDatabase {

    private val sTodoList = mutableListOf<Todo>().apply {
        add(Todo("asdfa", TodoUtils.Constants.TEST_DATE, false))
        add(Todo("asdfa", TodoUtils.Constants.TEST_DATE, false))
        add(Todo("asdfa", TodoUtils.Constants.TEST_DATE, false))
    }

    fun addTodo(todo: Todo) {
        sTodoList.add(todo)
    }

    fun getTodos() = sTodoList
}
