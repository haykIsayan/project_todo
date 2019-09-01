package com.example.project_todo.domain

import com.example.project_todo.core.TodoRepository
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Todo

class FilterByDateInteractor(private val todoList: List<Todo>,
                             private val dateString: String): TodoInteractor<List<Todo>>() {

    override suspend fun onExecute(): Resource<List<Todo>> {
        val filteredList = mutableListOf<Todo>()
        for (todo: Todo in todoList) {
            if (todo.dateString == dateString) {
                filteredList.add(todo)
            }
        }
        return Resource.Success(filteredList)
    }
}