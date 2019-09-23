package com.example.project_todo.core

import com.example.project_todo.Constants
import com.example.project_todo.entity.Task

object TaskTempDatabase {

    private val sTaskList = mutableListOf<Task>().apply {
//        add(Task("Be the hero they deserve, but not the one they need", Constants.PROTO_COLLECTION_TITLE, Constants.PROTO_DATE, isCompleted = false))
//        add(Task("Bless that donut", Constants.PROTO_COLLECTION_TITLE, Constants.PROTO_DATE, isCompleted = false, priority = 3))
//        add(Task("Bring democracy to Cuba", Constants.PROTO_COLLECTION_TITLE, Constants.PROTO_DATE, false))
//        add(Task("Take the hobbits to Isengard", Constants.PROTO_COLLECTION_TITLE, Constants.PROTO_DATE, false))
//        add(Task("Build another Death Star", Constants.PROTO_COLLECTION_TITLE, Constants.PROTO_DATE, false))
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
