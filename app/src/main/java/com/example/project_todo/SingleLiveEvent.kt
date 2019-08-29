package com.example.project_todo

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer

class SingleLiveEvent<T>: MediatorLiveData<T>() {

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer {
            value?.apply {
                observer.onChanged(this)
                value = null
            }

        })
    }

    fun invoke(data: T) {
        value = data
    }
}