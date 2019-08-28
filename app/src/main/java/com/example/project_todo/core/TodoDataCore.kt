package com.example.project_todo.core

import com.example.project_todo.entity.Todo

class TodoDataCore: TodoRepository {
    override suspend fun saveTodo(todo: Todo): Long {
        TodoTempDatabase.addTodo(todo)
        return 1L
    }

    override suspend fun getTodosByDate(date: String): List<Todo> {
        return  TodoTempDatabase.getTodos()
    }
}