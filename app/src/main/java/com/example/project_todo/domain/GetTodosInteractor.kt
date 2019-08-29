package com.example.project_todo.domain

import com.example.project_todo.core.TodoRepository
import com.example.project_todo.entity.Error
import com.example.project_todo.entity.NoTodos
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Todo

class GetTodosInteractor(
    private val parentListTitle: String,
    private val todoRepository: TodoRepository): TodoInteractor<List<Todo>>() {

    override suspend fun onExecute(): Resource<List<Todo>> {
        return try { getTodos() }
        catch (throwable: Throwable) { Error(throwable) }
    }

    private suspend fun getTodos(): Resource<List<Todo>> {
        todoRepository.getTodosByListTitle(parentListTitle).apply {
            return if (isEmpty()) { NoTodos() }
            else { Resource.Success(this)}
        }
    }
}