package com.example.project_todo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.project_todo.domain.LiveUseCase
import com.example.project_todo.entity.Resource

abstract class BaseViewModel<S>: ViewModel() {

    fun <D> invokeUseCase(useCase: LiveUseCase<D>, mediatorLiveData: MediatorLiveData<Resource<D>>) {
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
}