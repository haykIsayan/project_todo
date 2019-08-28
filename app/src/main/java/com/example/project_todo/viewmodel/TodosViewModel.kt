package com.example.project_todo.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.project_todo.domain.FilterTodosInteractor
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Todo

class TodosViewModel(application: Application) : BaseViewModel<List<Todo>>(application) {

    private val mTodoList = MediatorLiveData<Resource<List<Todo>>>()
    private val mCompletedList = MediatorLiveData< Resource<List<Todo>>>()

    fun loadTodoList(todosList: List<Todo>) =
        invokeUseCase(FilterTodosInteractor(todosList, false), mTodoList)


    fun loadCompletedList(todosList: List<Todo>) =
        invokeUseCase(FilterTodosInteractor(todosList, true), mCompletedList)

    fun getTodoListData(): LiveData<Resource<List<Todo>>> = mTodoList
    fun getCompletedListData(): LiveData<Resource<List<Todo>>> = mCompletedList
}
