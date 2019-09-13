package com.example.project_todo.domain.tasks.storage

import com.example.project_todo.core.TaskRepository
import com.example.project_todo.domain.LiveInteractor
import com.example.project_todo.entity.NoTasks
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Task

class GetAllTasksInteractor(
    private val parentListTitle: String,
    private val taskRepository: TaskRepository): LiveInteractor<List<Task>>() {

    override suspend fun onExecute(): Resource<List<Task>> {
        return getTasks()
    }

    private suspend fun getTasks(): Resource<List<Task>> {
        taskRepository.getTasksByListTitle(parentListTitle).apply {
            return if (isEmpty()) { NoTasks() }
            else { Resource.Success(this)}
        }
    }
}