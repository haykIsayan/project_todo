package com.example.project_todo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.project_todo.domain.LiveUseCase
import com.example.project_todo.entity.Resource

abstract class BaseViewModel<S>(application: Application): AndroidViewModel(application) {

    fun invokeUseCase(useCase: LiveUseCase<S>, mediatorLiveData: MediatorLiveData<Resource<S>>) {
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

    fun invokeUseCase(useCase: LiveUseCase<S>): LiveData<Resource<S>>
            = useCase.execute()
}