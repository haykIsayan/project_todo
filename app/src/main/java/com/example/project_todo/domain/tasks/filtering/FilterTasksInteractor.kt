package com.example.project_todo.domain.tasks.filtering

import com.example.project_todo.domain.LiveInteractor
import com.example.project_todo.entity.*

class FilterTasksInteractor(
    private val taskList: List<Task>,
    private val taskFilter: TaskFilter): LiveInteractor<List<Task>>() {


    override suspend fun onExecute(): Resource<List<Task>> {
        val filteredList = mutableListOf<Task>()

        taskList.forEach {
            if (taskFilter.isApplicable(it)) {
                filteredList.add(it)
            }
        }
        return if (filteredList.isEmpty()) {
            sendFailure()
        } else {
            Resource.Success(filteredList)
        }
    }

    private fun sendFailure() = when (taskFilter.taskCompletion) {
        Task.TaskCompletion.ALL -> NothingToShow()
        Task.TaskCompletion.TODO -> AllCompleted()
        Task.TaskCompletion.COMPLETED -> NoCompleted()
    }


}