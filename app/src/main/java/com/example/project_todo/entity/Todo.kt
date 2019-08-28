package com.example.project_todo.entity

data class Todo(
    val text: String,
    val dateString: String,
    val isCompleted: Boolean = false)