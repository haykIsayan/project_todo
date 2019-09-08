package com.example.project_todo.domain.tasks

import com.example.project_todo.core.TaskRepository
import com.example.project_todo.domain.LiveInteractor
import com.example.project_todo.entity.Error
import com.example.project_todo.entity.NoTodos
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Task

class GetTodosInteractor(
    private val parentListTitle: String,
    private val taskRepository: TaskRepository): LiveInteractor<List<Task>>() {

    override suspend fun onExecute(): Resource<List<Task>> {
        return try { getTodos() }
        catch (throwable: Throwable) { Error(throwable) }
    }

    private suspend fun getTodos(): Resource<List<Task>> {
        taskRepository.getTasksByListTitle(parentListTitle).apply {
            return if (isEmpty()) { NoTodos() }
            else { Resource.Success(this)}
        }
    }
}