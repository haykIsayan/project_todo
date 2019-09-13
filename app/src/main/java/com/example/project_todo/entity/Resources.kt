package com.example.project_todo.entity

data class Error(val throwable: Throwable): Resource.Failure()

/**
 * Failure Resources
 */

class NoTasks: Resource.Failure()

class AllCompleted: Resource.Failure()

class NoCompleted: Resource.Failure()

class NothingToShow: Resource.Failure()

/**
 * Success Resources
 */

class TaskSaved(task: Task, var updatePosition: Int = -1): Resource.EventResource<Task>(task)

class TaskCompleted(val task: Task, updatePosition: Int): Resource.EventResource<Int>(updatePosition)

class TaskUndoCompleted(val task: Task, updatePosition: Int): Resource.EventResource<Int>(updatePosition)

class TaskDeleted(val task: Task, updatePosition: Int): Resource.EventResource<Int>(updatePosition)