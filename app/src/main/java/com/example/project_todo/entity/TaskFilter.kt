package com.example.project_todo.entity

class TaskFilter(
    var taskCompletion: Task.TaskCompletion = Task.TaskCompletion.ALL,
    var priority: Int = 0,
    var creationDate: String = "",
    var dueDate: String = "") {


    fun isApplicable(task: Task): Boolean {

        val completionApplicable = when (taskCompletion) {
            Task.TaskCompletion.ALL -> true
            Task.TaskCompletion.TODO -> !task.isCompleted
            Task.TaskCompletion.COMPLETED -> task.isCompleted
        }

        val creationApplicable = if (creationDate.isEmpty()) { true } else { task.creationDate == creationDate }
        val dueDateApplicable = if (dueDate.isEmpty()) {true} else { task.creationDate == dueDate }
        val priorityApplicable = if (priority == 0) {true} else {task.priority == priority}

        return priorityApplicable && completionApplicable && creationApplicable && dueDateApplicable
    }
}