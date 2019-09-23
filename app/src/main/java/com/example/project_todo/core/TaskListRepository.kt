package com.example.project_todo.core

import com.example.project_todo.entity.TaskCollection

interface TaskListRepository {
    suspend fun getTaskCollections(): List<TaskCollection>
    suspend fun addTaskCollection(taskCollection: TaskCollection): Long
    suspend fun updateTaskCollection(taskCollection: TaskCollection)
}