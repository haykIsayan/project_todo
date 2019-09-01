package com.example.project_todo.domain

import com.example.project_todo.core.TodoRepository
import com.example.project_todo.entity.Error
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Todo

class CompleteTodoInteractor(private val todo: Todo, private val updatePosition: Int,
                             private val todoRepository: TodoRepository): TodoInteractor<Int>() {

    override suspend fun onExecute(): Resource<Int> {
        return try {
            todo.isCompleted = true
            todoRepository.updateTodo(todo)
            Resource.Success(updatePosition)
        } catch (throwable: Throwable) {
            Error(throwable)
        }
    }

}