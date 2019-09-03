package com.example.project_todo.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.project_todo.TodoUtils
import com.example.project_todo.core.TodoDataCore
import com.example.project_todo.domain.GetTodosInteractor
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Todo
import java.util.*

class MainViewModel(application: Application): BaseViewModel<List<Todo>>(application) {

    private val mListTitleData = MutableLiveData<String>()
    private val mErrorLiveData = MutableLiveData<Throwable>()

    fun setListTitle(parentListTitle: String) {
        mListTitleData.value = parentListTitle
    }

    fun sendError(throwable: Throwable) {
        mErrorLiveData.value = throwable
    }

    fun getListTitleData(): LiveData<String> = mListTitleData
    fun getErrorData(): LiveData<Throwable> = mErrorLiveData
}