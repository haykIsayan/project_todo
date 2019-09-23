package com.example.project_todo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.project_todo.event.LiveEvent
import com.example.project_todo.core.TaskListRepository
import com.example.project_todo.core.TaskRepository
import com.example.project_todo.domain.tasklists.GetTaskListsInteractor
import com.example.project_todo.domain.tasks.storage.SaveTaskInteractor
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Task
import com.example.project_todo.entity.TaskCollection
import com.example.project_todo.currentValue
import com.example.project_todo.entity.Error
import com.example.project_todo.event.AdvancedLiveEvent
import com.example.project_todo.event.SingleLiveEvent

class MainViewModel(private val taskListRepository: TaskListRepository,
                    private val taskRepository: TaskRepository): BaseViewModel<List<Task>>() {

    private val saveTaskEvent = LiveEvent<Task>()

    private val allTaskListsData = MediatorLiveData<Resource<List<TaskCollection>>>()
    private val currentTaskListData = MediatorLiveData<TaskCollection>()
    private val errorLiveData = SingleLiveEvent<Throwable>()

    init { fetchAllTaskLists() }

    private fun fetchAllTaskLists() {
        executeUseCase(GetTaskListsInteractor(taskListRepository), allTaskListsData)
    }

    fun setCurrentTaskList(taskCollection: TaskCollection) {
        currentTaskListData.value = taskCollection
    }

    fun sendError(error: Error) { errorLiveData.value = error }

    fun saveTask(task: Task) {
        currentTaskListData.currentValue {
            task.taskCollectionTitle = it.title
            saveTask(task,-1)
        }
    }

    fun undoDeleteTask(task: Task, updatePosition: Int) {
        saveTask(task, updatePosition)
    }

    private fun saveTask(task: Task, updatePosition: Int) {
        executeUseCase(SaveTaskInteractor(task, updatePosition, taskRepository), saveTaskEvent)
    }

    fun disableSaveTaskEvent() = saveTaskEvent.persistAndDisable()

    fun getSaveTaskLiveEvent(): LiveData<Resource<Task>> = saveTaskEvent
    fun getCurrentTaskListData(): LiveData<TaskCollection> = currentTaskListData
    fun getTaskListsData(): LiveData<Resource<List<TaskCollection>>> = allTaskListsData
    fun getErrorData(): LiveData<Resource<Throwable>> = errorLiveData
}