package com.example.project_todo.core

import com.example.project_todo.entity.TaskList

class TaskListDataCore: TaskListRepository {
    override suspend fun updateTaskList(taskList: TaskList) {
        TaskListTempDatabase.updateTaskList(taskList)
    }

    override suspend fun getTaskLists(): List<TaskList> = TaskListTempDatabase.getTaskLists()

    override suspend fun addTaskList(taskList: TaskList) = TaskListTempDatabase.addTaskList(taskList)
}