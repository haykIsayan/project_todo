package com.example.project_todo.core

import com.example.project_todo.entity.Task

class TaskDataCore: TaskRepository {
    override suspend fun updateTask(task: Task) {

    }

    override suspend fun saveTask(task: Task): Long {
        TodoTempDatabase.addTodo(task)
        return 1L
    }

    override suspend fun getTasksByListTitle(date: String): List<Task> {
        return  TodoTempDatabase.getTodos()
    }
}