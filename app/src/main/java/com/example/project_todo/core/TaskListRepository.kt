package com.example.project_todo.core

import com.example.project_todo.entity.TaskList

interface TaskListRepository {
    suspend fun getTaskLists(): List<TaskList>
    suspend fun addTaskList(taskList: TaskList)
    suspend fun updateTaskList(taskList: TaskList)
}