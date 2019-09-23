package com.example.project_todo.core

import com.example.project_todo.myTasksCollection
import com.example.project_todo.entity.TaskCollection

object TaskListTempDatabase {

    private val sTaskLists = mutableListOf(myTasksCollection)

    fun getTaskLists(): List<TaskCollection> = sTaskLists

    fun updateTaskList(taskCollection: TaskCollection) {
        sTaskLists.apply {
            set(indexOf(taskCollection), taskCollection)
        }
    }

    fun addTaskList(taskCollection: TaskCollection) {
        sTaskLists.add(taskCollection)
    }

}