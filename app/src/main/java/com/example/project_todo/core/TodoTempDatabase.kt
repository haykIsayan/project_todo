package com.example.project_todo.core

import com.example.project_todo.TodoUtils
import com.example.project_todo.entity.Todo

object TodoTempDatabase {

    private val sTodoList = mutableListOf<Todo>().apply {
        add(Todo("Take the hobbits to Isengard", TodoUtils.Constants.TEST_LIST_TITLE, TodoUtils.Constants.TEST_DATE, false))
        add(Todo("Bless that donut", TodoUtils.Constants.TEST_LIST_TITLE,TodoUtils.Constants.TEST_DATE, false))
        add(Todo("Bring democracy to Cuba", TodoUtils.Constants.TEST_LIST_TITLE,TodoUtils.Constants.TEST_DATE, false))
        add(Todo("Bring democracy to Cuba", TodoUtils.Constants.TEST_LIST_TITLE,TodoUtils.Constants.TEST_DATE, false))
        add(Todo("Bring democracy to Cuba", TodoUtils.Constants.TEST_LIST_TITLE,TodoUtils.Constants.TEST_DATE, true))
        add(Todo("Bring democracy to Cuba", TodoUtils.Constants.TEST_LIST_TITLE,TodoUtils.Constants.TEST_DATE, true))
        add(Todo("Bring democracy to Cuba", TodoUtils.Constants.TEST_LIST_TITLE,TodoUtils.Constants.TEST_DATE, false))
        add(Todo("Bring democracy to Cuba", TodoUtils.Constants.TEST_LIST_TITLE,TodoUtils.Constants.TEST_DATE, true))
        add(Todo("Take the hobbits to Isengard", TodoUtils.Constants.TEST_LIST_TITLE,TodoUtils.Constants.TEST_DATE, false))
        add(Todo("Bless that donut", TodoUtils.Constants.TEST_LIST_TITLE,TodoUtils.Constants.TEST_DATE, false))
    }

    fun addTodo(todo: Todo) {
        sTodoList.add(todo)
    }

    fun getTodos() = sTodoList.apply {
        shuffle()
    }
}
