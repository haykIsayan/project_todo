package com.example.project_todo.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.project_todo.event.PersistentLiveEvent
import com.example.project_todo.domain.LiveUseCase
import com.example.project_todo.entity.Resource
import com.example.project_todo.event.LiveEvent

abstract class BaseViewModel<S>: ViewModel() {

    fun <D> executeUseCase(useCase: LiveUseCase<D>, mediatorLiveData: MediatorLiveData<Resource<D>>) {
        val useCaseData = useCase.execute()
        mediatorLiveData.apply {
            addSource(useCaseData) {
                value = it
                if (it is Resource.Success) {
                    removeSource(useCaseData)
                }
            }
        }
    }

    fun <S> executeUseCase(useCase: LiveUseCase<S>, liveEvent: LiveEvent<S>, onResult: () -> Unit = {}) {
        val useCaseData = useCase.execute()
        liveEvent.apply {
            addSource(useCaseData) {
                when (it) {
                    is Resource.EventResource -> {
                        invoke(it)
                        removeSource(useCaseData)
                        onResult()
                    }
                }
            }
        }
    }

    fun <S> executeUseCase(useCase: LiveUseCase<S>, liveEvent: PersistentLiveEvent<S>, completeEvent: Boolean = false) {
        val useCaseData = useCase.execute()
        liveEvent.apply {
            addSource(useCaseData) {
                when (it) {
                    is Resource.EventResource -> {
                        invoke(it)
                        if (completeEvent) { complete()}
                        removeSource(useCaseData)
                    }
                }
            }
        }
    }
}