package com.example.project_todo.core

import com.example.project_todo.entity.Task

class TaskDataCore: TaskRepository {

    override suspend fun deleteTask(task: Task) {
        TaskTempDatabase.deleteTask(task)
    }

    override suspend fun updateTask(task: Task) {
        TaskTempDatabase.updateTask(task)
    }

    override suspend fun saveTask(task: Task): Long {
        TaskTempDatabase.addTodo(task)
        return 1L
    }

    override suspend fun getTasksByListTitle(date: String): List<Task> {
        return  TaskTempDatabase.getTodos()
    }
}