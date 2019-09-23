package com.example.project_todo.event

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.example.project_todo.entity.Resource

open class LiveEvent<T>: MediatorLiveData<Resource<T>>() {

    final override fun observe(owner: LifecycleOwner, observer: Observer<in Resource<T>>) {
        super.observe(owner, Observer {
            value?.apply {
                if (isActive) {
                    observer.onChanged(this)

                    if (this !is Resource.Pending) { onFinished(this) }
                }
            }
        })
    }

    protected open fun onFinished(eventResult: Resource<T>) { }

    fun invoke(eventResource: Resource<T>) {
        value = eventResource
    }

    fun persistAndDisable() {
        value?.isActive = false
    }

    fun complete() {
        value = null
    }
}