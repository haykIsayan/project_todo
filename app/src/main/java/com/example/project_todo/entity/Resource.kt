package com.example.project_todo.entity

sealed class Resource<out S> {

    open class Success<out S>(val successData: S): Resource<S>()

    data class Pending(val pendingMessage: String): Resource<Nothing>()

    abstract class Failure: Resource<Nothing>()

    fun inspect(
        onSuccess: (S) -> Unit,
        onPending: (String) -> Unit = {},
        onFailure: (Failure) -> Unit = {},
        onError: (Throwable) -> Unit = {}) {
        when (this) {
            is Success -> onSuccess(successData)
            is Pending -> onPending(pendingMessage)
            is Error -> onError(throwable)
            is Failure -> onFailure(this)
        }
    }
}