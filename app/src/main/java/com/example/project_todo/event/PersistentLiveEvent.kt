package com.example.project_todo.event

import com.example.project_todo.entity.Resource

class PersistentLiveEvent<S>: LiveEvent<S>() {
    override fun onFinished(eventResult: Resource.EventResource<S>) {
        persistAndDisable()
    }
}