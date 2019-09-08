package com.example.project_todo.domain.tasks.storage

import com.example.project_todo.core.TaskRepository
import com.example.project_todo.domain.LiveInteractor
import com.example.project_todo.entity.Error
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Task
import com.example.project_todo.entity.TaskCompleted

class CompleteTaskInteractor(private val task: Task, private val updatePosition: Int,
                             private val taskRepository: TaskRepository): LiveInteractor<Int>() {

    override suspend fun onExecute(): Resource<Int> {
        task.isCompleted = true
        taskRepository.updateTask(task)
        return TaskCompleted(task, updatePosition)
    }
}