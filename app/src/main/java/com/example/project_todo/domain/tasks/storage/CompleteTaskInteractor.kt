package com.example.project_todo.domain.tasks.storage

import com.example.project_todo.core.TaskRepository
import com.example.project_todo.domain.LiveInteractor
import com.example.project_todo.entity.*

class CompleteTaskInteractor(private val task: Task,
                             private val completeTask: Boolean,
                             private val updatePosition: Int,
                             private val taskRepository: TaskRepository): LiveInteractor<Int>() {

    override suspend fun onExecute(): Resource<Int> {
        task.isCompleted = completeTask
        taskRepository.updateTask(task)
        return if (completeTask) {
            TaskCompleted(task, updatePosition)
        } else {
            TaskUndoCompleted(task, updatePosition)
        }
    }

    override fun onError(throwable: Throwable): Error {
        task.isCompleted = !completeTask
        return ErrorCompleted(updatePosition, throwable)
    }
}