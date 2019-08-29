package com.example.project_todo.domain

import com.example.project_todo.entity.AllCompleted
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Todo

class FilterTodosInteractor(private val todoList: List<Todo>, private val todoFilter: Todo.TodoFilter)
    : TodoInteractor<List<Todo>>() {

    override suspend fun onExecute(): Resource<List<Todo>> {
        return when (todoFilter) {
            Todo.TodoFilter.ALL -> Resource.Success(todoList)
            Todo.TodoFilter.COMPLETED -> filterTodos(true)
            Todo.TodoFilter.TODO -> filterTodos(false)
        }
    }

    private fun filterTodos(isCompleted: Boolean): Resource<List<Todo>> {
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