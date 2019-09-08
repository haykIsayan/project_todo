package com.example.project_todo.entity

sealed class Resource<out S> {

    data class Success<out S>(val successData: S): Resource<S>()

    data class Pending(val pendingMessage: String): Resource<Nothing>()

    abstract class Failure: Resource<Nothing>()

    fun doOnChanged(
        onSuccess: (S) -> Unit,
        onPending: (String) -> Unit,
        onFailure: (Failure) -> Unit) {
        when (this) {
            is Success -> onSuccess(successData)
            is Pending -> onPending(pendingMessage)
            is Failure -> onFailure(this)
        }
    }
}