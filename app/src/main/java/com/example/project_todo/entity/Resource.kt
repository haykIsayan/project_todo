package com.example.project_todo.entity

sealed class Resource<out S>(var isActive: Boolean = true) {

    open class Success<out S>(val successData: S): Resource<S>()

    data class Pending(val pendingMessage: String): Resource<Nothing>()

    abstract class Failure: Resource<Nothing>()

    fun inspectForSuccess(onSuccess: (S) -> Unit) {
        when (this) {
            is Success -> onSuccess(successData)
        }
    }

    inline fun <reified T: Resource<*>> inspectFor(onSuccess: (T) -> Unit) = apply {
        if (this is T){
            onSuccess(this as T)
        }
    }
}