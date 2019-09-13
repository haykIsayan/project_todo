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
import com.example.project_todo.entity.TaskList
import com.example.project_todo.entity.TaskSaved

class MainViewModel(private val taskListRepository: TaskListRepository,
                    private val taskRepository: TaskRepository): BaseViewModel<List<Task>>() {

    private val saveTaskEvent = LiveEvent<Task>()

    private val allTaskListsData = MediatorLiveData<Resource<List<TaskList>>>()
    private val currentTaskListData = MediatorLiveData<TaskList>()
    private val errorLiveData = MediatorLiveData<Throwable>()

    init {
        fetchAllTaskLists()
    }

    private fun fetchAllTaskLists() {
        executeUseCase(GetTaskListsInteractor(taskListRepository), allTaskListsData)
    }

    fun setCurrentTaskList(taskList: TaskList) {
        currentTaskListData.value = taskList
    }

    fun sendError(throwable: Throwable) {
        errorLiveData.value = throwable
    }

    fun saveTask(text: String, dateString: String, subTasks: List<String>, priority: Int) {
        currentTaskListData.value?.apply {
            saveTask(Task(text, title, dateString, isCompleted = false, subTasks = subTasks, priority = priority))
        }
    }

    fun undoDeleteTask(task: Task, updatePosition: Int) {
        saveTask(task, updatePosition)
    }


    private fun saveTask(task: Task, updatePosition: Int = -1, onResult: () -> Unit = {}) {
        executeUseCase(SaveTaskInteractor(task, updatePosition, taskRepository), saveTaskEvent, onResult)
    }

    fun disableSaveTaskEvent() = saveTaskEvent.persistAndDisable()

    fun getSaveTaskLiveEvent(): LiveData<Resource.EventResource<Task>> = saveTaskEvent
    fun getCurrentTaskListData(): LiveData<TaskList> = currentTaskListData
    fun getTaskListsData(): LiveData<Resource<List<TaskList>>> = allTaskListsData
    fun getErrorData(): LiveData<Throwable> = errorLiveData

}