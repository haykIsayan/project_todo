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
        val asyncAction = GlobalScope.async(Dispatchers.IO) { obtainResource()}
        GlobalScope.launch(Dispatchers.Main) { value = asyncAction.await() }
    }

    private suspend fun obtainResource() = try { onExecute() } catch (throwable: Throwable) { Error(throwable) }

    suspend fun test(): LiveData<Resource<S>> = MutableLiveData<Resource<S>>().apply { value = onExecute() }

    abstract suspend fun onExecute(): Resource<S>
}