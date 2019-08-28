package com.example.project_todo.entity

data class Error(val throwable: Throwable): Resource.Failure()

class NoTodos: Resource.Failure()

class AllCompleted: Resource.Failure()