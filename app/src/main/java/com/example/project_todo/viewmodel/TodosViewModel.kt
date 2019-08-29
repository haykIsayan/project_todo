package com.example.project_todo.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.project_todo.core.TodoDataCore
import com.example.project_todo.domain.FilterTodosInteractor
import com.example.project_todo.domain.GetTodosInteractor
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Todo

class TodosViewModel(application: Application) : BaseViewModel<List<Todo>>(application) {

    private val mAllTodosData = MediatorLiveData<Resource<List<Todo>>>()

    private val mFilteredTodosData = MediatorLiveData<Resource<List<Todo>>>()

    fun getAllTodosData(): LiveData<Resource<List<Todo>>> = mAllTodosData

    fun getFilteredTodosData(): LiveData<Resource<List<Todo>>> = mFilteredTodosData

    fun getAllTodos(parentListTitle: String) {
        invokeUseCase(GetTodosInteractor(parentListTitle, TodoDataCore()), mAllTodosData)
    }

    fun filterTodos(todoList: List<Todo>,
                    todoFilter: Todo.TodoFilter) {
        invokeUseCase(FilterTodosInteractor(todoList, todoFilter), mFilteredTodosData)
    }

    fun filterByDate() {

    }

}
