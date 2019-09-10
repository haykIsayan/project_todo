package com.example.project_todo.entity

data class Error(val throwable: Throwable): Resource.Failure()

/**
 * Failure Resources
 */

class NoTodos: Resource.Failure()

class AllCompleted: Resource.Failure()

class NoCompleted: Resource.Failure()

class NothingToShow: Resource.Failure()

/**
 * Success Resources
 */

class TaskCompleted(val task: Task, updatePosition: Int): Resource.Success<Int>(updatePosition)

class TaskDeleted(val task: Task, updatePosition: Int): Resource.Success<Int>(updatePosition)