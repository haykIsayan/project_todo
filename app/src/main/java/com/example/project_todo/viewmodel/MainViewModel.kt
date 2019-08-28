package com.example.project_todo.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.project_todo.TodoUtils
import com.example.project_todo.core.TodoDataCore
import com.example.project_todo.domain.GetTodosInteractor
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Todo
import java.util.*

class MainViewModel(application: Application): BaseViewModel<List<Todo>>(application) {

    private val mDate = Date()

    private val mTodoListData = MediatorLiveData<Resource<List<Todo>>>()

    fun fetchData() {
        val date = TodoUtils.Constants.TEST_DATE
        invokeUseCase(GetTodosInteractor(date, TodoDataCore()), mTodoListData)
    }

    fun getData(): LiveData<Resource<List<Todo>>> = mTodoListData

}