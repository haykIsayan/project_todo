package com.example.project_todo.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.project_todo.core.TodoRepository
import com.example.project_todo.entity.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class TodoInteractor<S>: LiveUseCase<S> {

    private val mInteractorData = MutableLiveData<Resource<S>>()

    final override fun execute(): LiveData<Resource<S>>
            = mInteractorData.apply {
        value = Resource.Pending("")
        val asyncAction = GlobalScope.async(Dispatchers.IO) { onExecute() }
        GlobalScope.launch(Dispatchers.Main) { value = asyncAction.await() }
    }

    suspend fun test(): LiveData<Resource<S>>
            = mInteractorData.apply {
        value = onExecute()
    }

    abstract suspend fun onExecute(): Resource<S>

}