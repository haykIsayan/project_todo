package com.example.project_todo.entity

sealed class Resource<out S> {

    open class Success<out S>(val successData: S): Resource<S>()

    data class Pending(val pendingMessage: String): Resource<Nothing>()

    abstract class Failure: Resource<Nothing>()

    open class EventResource<out S>(val successData: S, var isActive: Boolean = true): Resource<S>()

    inline fun <reified T : Resource<*> > inspectFor(onSuccess: (T) -> Unit) {
        when (this) {
            is T -> onSuccess(this as T)
        }
    }

    inline fun <reified A : Resource<*>,
            reified B : Resource<*>,
            reified C : Resource<*>> inspectFor(onTypeA: (A) -> Unit, onTypeB: (B) -> Unit, onTypeC: (C) -> Unit) {
        when (this) {
            is A -> onTypeA(this as A)
            is B -> onTypeB(this as B)
            is C -> onTypeC(this as C)
        }
    }

    fun inspect(
        onSuccess: (S) -> Unit,
        onPending: (String) -> Unit = {},
        onFailure: (Failure) -> Unit = {},
        onError: (Throwable) -> Unit = {}) {
        when (this) {
            is Success -> onSuccess(successData)
            is EventResource -> if (isActive) { onSuccess(successData) }
            is Pending -> onPending(pendingMessage)
            is Error -> onError(throwable)
            is Failure -> onFailure(this)
        }
    }
}