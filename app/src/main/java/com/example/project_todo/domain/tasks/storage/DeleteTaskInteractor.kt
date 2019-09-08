package com.example.project_todo.domain.tasks.storage

import com.example.project_todo.core.TaskRepository
import com.example.project_todo.domain.LiveInteractor
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Task
import com.example.project_todo.entity.TaskDeleted

class DeleteTaskInteractor(private val task: Task,
                           private val updatePosition: Int,
                           private val taskRepository: TaskRepository)
    : LiveInteractor<Int>(){

    override suspend fun onExecute(): Resource<Int> {
        taskRepository.deleteTask(task)
        return TaskDeleted(task, updatePosition)
    }
}