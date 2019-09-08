package com.example.project_todo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.project_todo.SingleLiveEvent
import com.example.project_todo.core.TaskListRepository
import com.example.project_todo.core.TaskRepository
import com.example.project_todo.domain.tasks.storage.CompleteTaskInteractor
import com.example.project_todo.domain.tasks.filtering.FilterByCompletionInteractor
import com.example.project_todo.domain.tasks.storage.GetAllTasksInteractor
import com.example.project_todo.domain.tasklists.UpdateProgressInteractor
import com.example.project_todo.domain.tasks.storage.DeleteTaskInteractor
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Task
import com.example.project_todo.entity.TaskList

class TaskViewModel(private val taskRepository: TaskRepository,
                    private val taskListRepository: TaskListRepository): BaseViewModel<List<Task>>() {

    private var currentTaskList: TaskList? = null
    private val taskListProgressData = MediatorLiveData<Resource<Float>>()
    private val mAllTasksData = MediatorLiveData<Resource<List<Task>>>()
    private val filteredTasksData = MediatorLiveData<Resource<List<Task>>>()

    private val taskInteractEvent = SingleLiveEvent<Resource<Int>>()

    var mCurrentFilter: Task.TaskFilter = Task.TaskFilter.ALL

    fun setProgressAndFetchAllTasks(taskList: TaskList) {
        if (currentTaskList == null || (currentTaskList != null && currentTaskList?.title == taskList.title)) {
            currentTaskList = taskList
            fetchAllTasks()
        }
    }

    private fun fetchAllTasks() {
        currentTaskList?.apply {
            taskListProgressData.value = Resource.Success(getProgression())
            invokeUseCase(GetAllTasksInteractor(title, taskRepository), mAllTasksData)
        }
    }

    fun updateTaskListProgress(task: Task, updateAction: UpdateProgressInteractor.UpdateAction) {
        currentTaskList?.apply {
            invokeUseCase(
                UpdateProgressInteractor(
                    task,
                    updateAction,
                    this,
                    taskListRepository
                ),
                taskListProgressData)
        }
    }

    fun completeTask(task: Task, updatePosition: Int) {
        invokeUseCase(CompleteTaskInteractor(task, updatePosition, taskRepository), taskInteractEvent)
    }

    fun deleteTask(task: Task, updatePosition: Int) {
        invokeUseCase(DeleteTaskInteractor(task, updatePosition, taskRepository), taskInteractEvent)
    }

    fun filterTasks(taskFilter: Task.TaskFilter) {
        mCurrentFilter = taskFilter
        mAllTasksData.value?.inspect({
            invokeUseCase(FilterByCompletionInteractor(it, taskFilter), filteredTasksData)
        })
    }

    fun getTaskListProgressData(): LiveData<Resource<Float>> = taskListProgressData
    fun getAllTasksData(): LiveData<Resource<List<Task>>> = mAllTasksData
    fun getFilteredTodosData(): LiveData<Resource<List<Task>>> = filteredTasksData
    fun getTaskInteractEvent(): LiveData<Resource<Int>> = taskInteractEvent
}
