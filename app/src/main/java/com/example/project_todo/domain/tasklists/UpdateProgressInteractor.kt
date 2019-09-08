package com.example.project_todo.domain.tasklists

import com.example.project_todo.core.TaskListRepository
import com.example.project_todo.domain.LiveInteractor
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Task
import com.example.project_todo.entity.TaskList

class UpdateProgressInteractor(
    private val task: Task,
    private val updateAction: UpdateAction,
    private val taskList: TaskList,
    private val taskListRepository: TaskListRepository): LiveInteractor<Float>() {


    override suspend fun onExecute(): Resource<Float> {
        when (updateAction) {
            UpdateAction.TASK_COMPLETED -> taskList.progressValue += task.priority
            UpdateAction.TASK_DELETED -> {
                taskList.fullValue -= task.priority
                if (task.isCompleted) {
                    taskList.progressValue -= task.priority
                }
            }
            UpdateAction.TASK_SAVED -> taskList.fullValue += task.priority
        }

        taskListRepository.updateTaskList(taskList)
        return Resource.Success(taskList.getProgression())
    }

    enum class UpdateAction {
        TASK_COMPLETED, TASK_SAVED, TASK_DELETED
    }
}