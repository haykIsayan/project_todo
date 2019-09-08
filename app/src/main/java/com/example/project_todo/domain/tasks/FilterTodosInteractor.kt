package com.example.project_todo.domain.tasks

import com.example.project_todo.domain.LiveInteractor
import com.example.project_todo.entity.AllCompleted
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Task

class FilterTodosInteractor(private val taskList: List<Task>, private val taskFilter: Task.TaskFilter)
    : LiveInteractor<List<Task>>() {

    override suspend fun onExecute(): Resource<List<Task>> {
        return when (taskFilter) {
            Task.TaskFilter.ALL -> Resource.Success(taskList)
            Task.TaskFilter.COMPLETED -> filterTodos(true)
            Task.TaskFilter.TODO -> filterTodos(false)
        }
    }

    private fun filterTodos(isCompleted: Boolean): Resource<List<Task>> {
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