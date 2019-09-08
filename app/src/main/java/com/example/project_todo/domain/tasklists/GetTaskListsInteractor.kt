package com.example.project_todo.domain.tasklists

import com.example.project_todo.core.TaskListRepository
import com.example.project_todo.domain.LiveInteractor
import com.example.project_todo.entity.Error
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.TaskList

class GetTaskListsInteractor(private val taskListRepository: TaskListRepository): LiveInteractor<List<TaskList>>() {

    override suspend fun onExecute(): Resource<List<TaskList>> {
        return try {
            Resource.Success(taskListRepository.getTaskLists())
        } catch (throwable: Throwable) {
            Error(throwable)
        }
    }
}