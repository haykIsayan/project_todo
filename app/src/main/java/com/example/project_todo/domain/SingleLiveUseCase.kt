package com.example.project_todo.domain

import com.example.project_todo.event.SingleLiveEvent
import com.example.project_todo.entity.Resource

interface SingleLiveUseCase<T> {
    fun execute(): SingleLiveEvent<Resource<T>>
}