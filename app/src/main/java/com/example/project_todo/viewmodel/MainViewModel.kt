package com.example.project_todo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.project_todo.LiveEvent
import com.example.project_todo.SingleLiveEvent
import com.example.project_todo.core.TaskListRepository
import com.example.project_todo.core.TaskRepository
import com.example.project_todo.domain.tasklists.GetTaskListsInteractor
import com.example.project_todo.domain.tasks.SaveTaskInteractor
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Task
import com.example.project_todo.entity.TaskList

class MainViewModel(private val taskListRepository: TaskListRepository,
                    private val taskRepository: TaskRepository): BaseViewModel<List<Task>>() {

    private val saveTaskEvent = LiveEvent<Resource<Task>>()

    private val allTaskListsData = MediatorLiveData<Resource<List<TaskList>>>()
    private val currentTaskListData = MediatorLiveData<TaskList>()
    private val errorLiveData = MediatorLiveData<Throwable>()

    init {
        fetchAllTaskLists()
    }

    private fun fetchAllTaskLists() {
        invokeUseCase(GetTaskListsInteractor(taskListRepository), allTaskListsData)
    }

    fun setCurrentTaskList(taskList: TaskList) {
        currentTaskListData.value = taskList
    }

    fun sendError(throwable: Throwable) {
        errorLiveData.value = throwable
    }

    fun saveTask(text: String, dateString: String, subTasks: List<String>, priority: Int) {

        currentTaskListData.value?.apply {
            invokeUseCase(SaveTaskInteractor(
                Task(text, title, dateString, false, subTasks, priority),
                taskRepository), saveTaskEvent)
        }
    }

    fun completeSaveTaskEvent() = saveTaskEvent.complete()

    fun getAddTaskLiveEvent(): LiveData<Resource<Task>> = saveTaskEvent
    fun getCurrentTaskListData(): LiveData<TaskList> = currentTaskListData
    fun getTaskListsData(): LiveData<Resource<List<TaskList>>> = allTaskListsData
    fun getErrorData(): LiveData<Throwable> = errorLiveData

}