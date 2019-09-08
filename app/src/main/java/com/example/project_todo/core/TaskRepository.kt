package com.example.project_todo.core

import com.example.project_todo.entity.Task

interface TaskRepository {
    suspend fun saveTask(task: Task): Long
    suspend fun getTasksByListTitle(date: String): List<Task>
    suspend fun updateTask(task: Task)
}