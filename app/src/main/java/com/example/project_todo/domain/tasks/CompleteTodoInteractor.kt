package com.example.project_todo.domain.tasks

import com.example.project_todo.core.TaskRepository
import com.example.project_todo.domain.LiveInteractor
import com.example.project_todo.entity.Error
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Task

class CompleteTodoInteractor(private val task: Task, private val updatePosition: Int,
                             private val taskRepository: TaskRepository): LiveInteractor<Int>() {

    override suspend fun onExecute(): Resource<Int> {
        return try {
            task.isCompleted = true
            taskRepository.updateTask(task)
            Resource.Success(updatePosition)
        } catch (throwable: Throwable) {
            Error(throwable)
        }
    }

}