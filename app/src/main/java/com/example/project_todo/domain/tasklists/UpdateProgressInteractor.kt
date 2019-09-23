package com.example.project_todo.domain.tasklists

import com.example.project_todo.core.TaskListRepository
import com.example.project_todo.domain.LiveInteractor
import com.example.project_todo.entity.Error
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Task
import com.example.project_todo.entity.TaskCollection

class UpdateProgressInteractor(
    private val task: Task,
    private val taskAction: TaskAction,
    private val taskCollection: TaskCollection,
    private val taskListRepository: TaskListRepository): LiveInteractor<Float>() {

    private val dulcieTaskCollection = taskCollection.run {
        TaskCollection(progressValue = progressValue, fullValue = fullValue)
    }

    override suspend fun onExecute(): Resource<Float> {

        when (taskAction) {
            TaskAction.TASK_COMPLETED -> taskCollection.progressValue += task.priority
            TaskAction.TASK_UNDO_COMPLETED -> taskCollection.progressValue -= task.priority
            TaskAction.TASK_DELETED -> {
                taskCollection.fullValue -= task.priority
                if (task.isCompleted) {
                    taskCollection.progressValue -= task.priority
                }
            }
            TaskAction.TASK_SAVED ->
                task.apply {
                    taskCollection.fullValue += priority
                    if (isCompleted) {
                        taskCollection.progressValue += priority
                    }
                }
        }
        taskListRepository.updateTaskCollection(taskCollection)
        return Resource.Success(taskCollection.getProgression())
    }

    override fun onError(throwable: Throwable): Error {
        taskCollection.apply {
            progressValue = dulcieTaskCollection.progressValue
            fullValue = dulcieTaskCollection.fullValue
        }
        return super.onError(throwable)
    }

    enum class TaskAction {
        TASK_COMPLETED, TASK_UNDO_COMPLETED, TASK_SAVED, TASK_DELETED
    }
}