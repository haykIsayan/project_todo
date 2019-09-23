package com.example.project_todo.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.project_todo.entity.Error
import com.example.project_todo.entity.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

abstract class LiveInteractor<S>: LiveUseCase<S> {

    private val mInteractorData = MutableLiveData<Resource<S>>()

    final override fun execute(): LiveData<Resource<S>> = MutableLiveData<Resource<S>>().apply {
        value = Resource.Pending("")
        val asyncAction = GlobalScope.async(Dispatchers.IO) { fetchResource()}
        GlobalScope.launch(Dispatchers.Main) { postValue(asyncAction.await()) }
    }

    private suspend fun fetchResource() = try {
        onExecute()
    } catch (throwable: Throwable) {
        onError(throwable)
    }

    suspend fun test(): LiveData<Resource<S>> = MutableLiveData<Resource<S>>().apply { value = fetchResource() }

    protected abstract suspend fun onExecute(): Resource<S>

    protected open fun onError(throwable: Throwable) = Error(throwable)
}