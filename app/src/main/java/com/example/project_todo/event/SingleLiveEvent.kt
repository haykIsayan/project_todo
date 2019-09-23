package com.example.project_todo.event

import com.example.project_todo.entity.Resource

class SingleLiveEvent<T>: LiveEvent<T>() {
    override fun onFinished(eventResult: Resource<T>) {
        complete()
    }
}