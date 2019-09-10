package com.example.project_todo.entity

data class Task(
    val text: String,
    val parentListTitle: String,
    val creationDate: String = "",
    val dueDate: String = "",
    var isCompleted: Boolean = false,
    val subTasks: List<String> = mutableListOf(),
    val priority: Int = 2) {

    enum class TaskCompletion {
        ALL, TODO, COMPLETED
    }
}