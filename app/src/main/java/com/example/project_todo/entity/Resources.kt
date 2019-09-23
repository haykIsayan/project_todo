package com.example.project_todo.entity

open class Error(val throwable: Throwable): Resource.Failure()

/**
 * Failure Resources
 */

class NoTasks: Resource.Failure()

class AllCompleted: Resource.Failure()

class NoCompleted: Resource.Failure()

class NothingToShow: Resource.Failure()

class ErrorCompleted(val updatePosition: Int, throwable: Throwable): Error(throwable)

/**
 * Success Resources
 */

class TaskSaved(task: Task, var updatePosition: Int = -1): Resource.Success<Task>(task)

class TaskCompleted(val task: Task, updatePosition: Int): Resource.Success<Int>(updatePosition)

class TaskUndoCompleted(val task: Task, updatePosition: Int): Resource.Success<Int>(updatePosition)

class TaskDeleted(val task: Task, updatePosition: Int): Resource.Success<Int>(updatePosition)