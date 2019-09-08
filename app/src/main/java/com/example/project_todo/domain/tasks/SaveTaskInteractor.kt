package com.example.project_todo.domain.tasks

import com.example.project_todo.core.TaskRepository
import com.example.project_todo.domain.LiveInteractor
import com.example.project_todo.entity.Error
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Task

class SaveTaskInteractor(private val task: Task, private val taskRepository: TaskRepository): LiveInteractor<Task>() {

    override suspend fun onExecute(): Resource<Task> {
        return try {
            taskRepository.saveTask(task)
            Resource.Success(task)
        } catch (throwable: Throwable) {
            Error(throwable)
        }
    }

}