package com.example.project_todo.event

import com.example.project_todo.entity.Resource

class AdvancedLiveEvent<S>: LiveEvent<S>() {
    private var observeCount = 1
    private var observeCounter = 0

    fun setObserveCount (count: Int) = apply { observeCount = count }

    override fun onFinished(eventResult: Resource<S>) {

        persistAndDisable()
    }
}