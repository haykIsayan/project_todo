package com.example.project_todo.entity

data class TaskList(val title: String, var progressValue: Float, var fullValue: Float) {

    fun getProgression() = ((progressValue / fullValue) * 100)

    override fun equals(other: Any?): Boolean {
        return when (other) {
            is TaskList -> title == other.title
            else -> false
        }
    }
}