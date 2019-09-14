package com.example.project_todo.entity

data class Task(
    val text: String,
    val description: String = "",
    val parentListTitle: String,
    val creationDate: String = "",
    val dueDate: String = "",
    var isCompleted: Boolean = false,
    val priority: Int = 2) {

    enum class TaskCompletion {
        ALL, TODO, COMPLETED
    }
}