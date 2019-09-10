package com.example.project_todo.domain.tasks.filtering

import com.example.project_todo.domain.LiveInteractor
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Task

class FilterByDateInteractor(private val taskList: List<Task>,
                             private val dateString: String): LiveInteractor<List<Task>>() {

    override suspend fun onExecute(): Resource<List<Task>> {
        val filteredList = mutableListOf<Task>()
        for (task: Task in taskList) {
            if (task.creationDate == dateString) {
                filteredList.add(task)
            }
        }
        return Resource.Success(filteredList)
    }
}