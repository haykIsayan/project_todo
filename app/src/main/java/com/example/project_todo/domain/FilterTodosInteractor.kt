package com.example.project_todo.domain

import com.example.project_todo.entity.AllCompleted
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Todo

class FilterTodosInteractor(private val todoList: List<Todo>, private val isCompleted: Boolean)
    : TodoInteractor<List<Todo>>() {

    override suspend fun onExecute(): Resource<List<Todo>> {
        val filteredList = mutableListOf<Todo>()
        for (todo: Todo in todoList) {
            if (todo.isCompleted == isCompleted) {
                filteredList.add(todo)
            }
        }
        if (filteredList.isEmpty()) {
            return AllCompleted()
        }
        return Resource.Success(filteredList)
    }


}