package com.example.project_todo.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.project_todo.SingleLiveEvent
import com.example.project_todo.core.TodoDataCore
import com.example.project_todo.domain.CompleteTodoInteractor
import com.example.project_todo.domain.FilterByDateInteractor
import com.example.project_todo.domain.FilterTodosInteractor
import com.example.project_todo.domain.GetTodosInteractor
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Todo

class TodosViewModel(application: Application) : BaseViewModel<List<Todo>>(application) {

    private val mAllTodosData = MediatorLiveData<Resource<List<Todo>>>()

    private val mFilteredTodosData = MediatorLiveData<Resource<List<Todo>>>()

    private val mCompleteTodoEvent = SingleLiveEvent<Resource<Int>>()

    var mCurrentFilter: Todo.TodoFilter = Todo.TodoFilter.ALL

    fun getAllTodosData(): LiveData<Resource<List<Todo>>> = mAllTodosData

    fun getFilteredTodosData(): LiveData<Resource<List<Todo>>> = mFilteredTodosData

    fun getCompleteEvent(): LiveData<Resource<Int>> = mCompleteTodoEvent

    fun fetchAllTodos(parentListTitle: String) {
        invokeUseCase(GetTodosInteractor(parentListTitle, TodoDataCore()), mAllTodosData)
    }

    fun completeTodo(todo: Todo, updatePosition: Int) {
        invokeUseCase(CompleteTodoInteractor(todo, updatePosition, TodoDataCore()), mCompleteTodoEvent)
    }

    fun filterTodos(todoFilter: Todo.TodoFilter) {
        mAllTodosData.value?.apply {
            when (this) {
                is Resource.Success -> invokeUseCase(FilterTodosInteractor(successData, todoFilter), mFilteredTodosData)
            }
        }
    }

    fun filterByDate(date: String) {
        mAllTodosData.value?.apply {
            when (this) {
                is Resource.Success -> invokeUseCase(FilterByDateInteractor(successData, date), mFilteredTodosData)
            }
        }
    }

}
