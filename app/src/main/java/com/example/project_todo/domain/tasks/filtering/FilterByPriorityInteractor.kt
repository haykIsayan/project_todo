package com.example.project_todo.domain.tasks.filtering

import com.example.project_todo.domain.LiveInteractor
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Task

class FilterByPriorityInteractor(
    private val tasks: List<Task>,
    private val priority: Int) : LiveInteractor<List<Task>>(){

    override suspend fun onExecute(): Resource<List<Task>> {
        val filteredTasks = mutableListOf<Task>()
        for (task: Task in tasks) {
            if (task.priority == priority) {
                filteredTasks.add(task)
            }
        }
        return Resource.Success(filteredTasks)
    }
}