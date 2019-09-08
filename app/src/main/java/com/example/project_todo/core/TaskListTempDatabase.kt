package com.example.project_todo.core

import com.example.project_todo.dummieTaskList
import com.example.project_todo.entity.TaskList

object TaskListTempDatabase {

    private val sTaskLists = mutableListOf(dummieTaskList)

    fun getTaskLists(): List<TaskList> = sTaskLists

    fun updateTaskList(taskList: TaskList) {
        sTaskLists.apply {
            set(indexOf(taskList), taskList)
        }
    }

    fun addTaskList(taskList: TaskList) {
        sTaskLists.add(taskList)
    }

}