package com.example.project_todo.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.project_todo.domain.LiveUseCase
import com.example.project_todo.entity.Resource

abstract class BaseViewModel<S>: ViewModel() {

    fun <T> executeUseCase(
        useCase: LiveUseCase<T>,
        mediatorLiveData: MediatorLiveData<Resource<T>>,
        onResult: () -> Unit = {}) {

        val useCaseData = useCase.execute()
        mediatorLiveData.apply {
            addSource(useCaseData) {
                value = it
                if (it !is Resource.Pending) {
                    onResult()
                    removeSource(useCaseData)
                }
            }
        }
    }
}