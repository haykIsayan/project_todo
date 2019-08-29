package com.example.project_todo.entity

data class Todo(
    val text: String,
    val parentListTitle: String,
    val dateString: String,
    val isCompleted: Boolean = false) {

    enum class TodoFilter {
        ALL, TODO, COMPLETED
    }
}