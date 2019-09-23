package com.example.project_todo.domain.tasklists

import com.example.project_todo.core.TaskListRepository
import com.example.project_todo.domain.LiveInteractor
import com.example.project_todo.entity.Error
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.TaskCollection
import com.example.project_todo.groceryListCollection
import com.example.project_todo.myTasksCollection

class GetTaskListsInteractor(private val taskListRepository: TaskListRepository): LiveInteractor<List<TaskCollection>>() {

    override suspend fun onExecute(): Resource<List<TaskCollection>> {
        return try {
            taskListRepository.getTaskCollections().run {
                Resource.Success(if (this.isEmpty()) {
                    taskListRepository.addTaskCollection(myTasksCollection)
                    taskListRepository.addTaskCollection(groceryListCollection)
                    listOf(myTasksCollection, groceryListCollection)
                } else { this })
            }
        } catch (throwable: Throwable) {
            Error(throwable)
        }
    }
}