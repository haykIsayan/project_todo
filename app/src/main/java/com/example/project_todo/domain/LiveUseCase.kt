package com.example.project_todo.domain

import androidx.lifecycle.LiveData
import com.example.project_todo.entity.Resource

interface LiveUseCase<T> {
    fun execute(): LiveData<Resource<T>>
}