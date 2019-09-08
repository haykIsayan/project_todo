package com.example.project_todo.entity

data class Task(
    val text: String,
    val parentListTitle: String,
    val dateString: String,
    var isCompleted: Boolean = false,
    val subTasks: List<String> = mutableListOf(),
    val priority: Int = 2) {

    enum class TaskFilter {
        ALL, TODO, COMPLETED
    }
}