package com.example.project_todo.entity

sealed class Resource<out S> {

    data class Success<out S>(val successData: S): Resource<S>()

    data class Pending(val pendingMessage: String): Resource<Nothing>()

    abstract class Failure: Resource<Nothing>()

}