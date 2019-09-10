package com.example.project_todo.domain.tasks.filtering

import com.example.project_todo.domain.LiveInteractor
import com.example.project_todo.entity.AllCompleted
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Task

class FilterByCompletionInteractor(private val taskList: List<Task>, private val taskCompletion: Task.TaskCompletion)
    : LiveInteractor<List<Task>>() {

    override suspend fun onExecute(): Resource<List<Task>> {
        return when (taskCompletion) {
            Task.TaskCompletion.ALL -> Resource.Success(taskList)
            Task.TaskCompletion.COMPLETED -> filterTasks(true)
            Task.TaskCompletion.TODO -> filterTasks(false)
        }
    }

    private fun filterTasks(isCompleted: Boolean): Resource<List<Task>> {
        val filteredList = mutableListOf<Task>()
        for (task: Task in taskList) {
            if (task.isCompleted == isCompleted) {
                filteredList.add(task)
            }
        }
        if (filteredList.isEmpty()) {
            return AllCompleted()
        }
        return Resource.Success(filteredList)
    }

}