package com.example.project_todo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.project_todo.SingleLiveEvent
import com.example.project_todo.core.TaskRepository
import com.example.project_todo.domain.tasks.CompleteTodoInteractor
import com.example.project_todo.domain.tasks.FilterByDateInteractor
import com.example.project_todo.domain.tasks.FilterTodosInteractor
import com.example.project_todo.domain.tasks.GetTodosInteractor
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Task

class TaskViewModel(private val taskRepository: TaskRepository): BaseViewModel<List<Task>>() {

    private val mAllTasksData = MediatorLiveData<Resource<List<Task>>>()
    private val mFilteredTodosData = MediatorLiveData<Resource<List<Task>>>()
    private val mCompleteTodoEvent = SingleLiveEvent<Resource<Int>>()

    private var parentListTitle = ""
    var mCurrentFilter: Task.TaskFilter = Task.TaskFilter.ALL

    fun fetchAllTask(parentListTitle: String) {
        if (parentListTitle != this.parentListTitle) {
            this.parentListTitle = parentListTitle
            invokeUseCase(GetTodosInteractor(parentListTitle, taskRepository), mAllTasksData)
        }
    }

    fun completeTask(task: Task, updatePosition: Int) {
        invokeUseCase(CompleteTodoInteractor(task, updatePosition, taskRepository), mCompleteTodoEvent)
    }

    fun filterTasks(taskFilter: Task.TaskFilter) {
        mCurrentFilter = taskFilter
        mAllTasksData.value?.apply {
            when (this) {
                is Resource.Success -> invokeUseCase(FilterTodosInteractor(successData, taskFilter), mFilteredTodosData)
            }
        }
    }

    fun filterByDate(date: String) {
        mAllTasksData.value?.apply {
            when (this) {
                is Resource.Success -> invokeUseCase(FilterByDateInteractor(successData, date), mFilteredTodosData)
            }
        }
    }

    fun getAllTasksData(): LiveData<Resource<List<Task>>> = mAllTasksData
    fun getFilteredTodosData(): LiveData<Resource<List<Task>>> = mFilteredTodosData
    fun getCompleteEvent(): LiveData<Resource<Int>> = mCompleteTodoEvent
}
