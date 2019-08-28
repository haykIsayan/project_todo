package com.example.project_todo.core

import com.example.project_todo.entity.Todo

interface TodoRepository {
    suspend fun saveTodo(todo: Todo): Long
    suspend fun getTodosByDate(date: String): List<Todo>
}