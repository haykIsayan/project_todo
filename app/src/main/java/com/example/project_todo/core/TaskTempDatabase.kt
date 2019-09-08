package com.example.project_todo.core

import com.example.project_todo.Constants
import com.example.project_todo.entity.Task

object TaskTempDatabase {

    private val sTaskList = mutableListOf<Task>().apply {
        add(Task("Be the hero they deserve, but not the one they need", Constants.TEST_LIST_TITLE, Constants.TEST_DATE, false))
        add(Task("Bless that donut", Constants.TEST_LIST_TITLE, Constants.TEST_DATE, false))
//        add(Task("Bring democracy to Cuba", Constants.TEST_LIST_TITLE, Constants.TEST_DATE, false))
//        add(Task("Take the hobbits to Isengard", Constants.TEST_LIST_TITLE, Constants.TEST_DATE, false))
//        add(Task("Build another Death Star", Constants.TEST_LIST_TITLE, Constants.TEST_DATE, false))
    }

    // QUERIES

    fun addTodo(task: Task) {
        sTaskList.add(task)
    }

    fun getTodos() = sTaskList.apply {
        shuffle()
    }

    fun deleteTask(task: Task) {
        sTaskList.remove(task)
    }

    fun updateTask(task: Task) {
        sTaskList.apply {
            set(indexOf(task), task)
        }
    }
}
