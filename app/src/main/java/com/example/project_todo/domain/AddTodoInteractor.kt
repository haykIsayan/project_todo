package com.example.project_todo.domain

import com.example.project_todo.core.TodoRepository
import com.example.project_todo.entity.Error
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Todo

class AddTodoInteractor(private val todo: Todo,
                        private val todoRepository: TodoRepository): TodoInteractor<Todo>() {

    override suspend fun onExecute(): Resource<Todo> {
        return try { todoRepository.saveTodo(todo).let { Resource.Success(todo) }
        } catch (throwable: Throwable) { Error(throwable) }
    }
}