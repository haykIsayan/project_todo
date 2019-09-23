package com.example.project_todo.domain.tasks.storage

import com.example.project_todo.core.TaskRepository
import com.example.project_todo.domain.LiveInteractor
import com.example.project_todo.entity.Error
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Task
import com.example.project_todo.entity.TaskSaved

class SaveTaskInteractor(private val task: Task,
                         private val updatePosition: Int = -1,
                         private val taskRepository: TaskRepository): LiveInteractor<Task>() {

    override suspend fun onExecute(): Resource<Task> {
        task.id = taskRepository.saveTask(task)
        return TaskSaved(task, updatePosition)
    }

    override fun onError(throwable: Throwable): Error {
        return super.onError(throwable)
    }
}