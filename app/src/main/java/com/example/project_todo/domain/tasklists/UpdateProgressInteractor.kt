package com.example.project_todo.domain.tasklists

import com.example.project_todo.core.TaskListRepository
import com.example.project_todo.domain.LiveInteractor
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Task
import com.example.project_todo.entity.TaskList

class UpdateProgressInteractor(
    private val task: Task,
    private val taskAction: TaskAction,
    private val taskList: TaskList,
    private val taskListRepository: TaskListRepository): LiveInteractor<Float>() {


    override suspend fun onExecute(): Resource<Float> {
        when (taskAction) {
            TaskAction.TASK_COMPLETED -> taskList.progressValue += task.priority
            TaskAction.TASK_UNDO_COMPLETED -> taskList.progressValue -= task.priority
            TaskAction.TASK_DELETED -> {
                taskList.fullValue -= task.priority
                if (task.isCompleted) {
                    taskList.progressValue -= task.priority
                }
            }
            TaskAction.TASK_SAVED ->
                task.apply {
                    taskList.fullValue += priority
                    if (isCompleted) {
                        taskList.progressValue += priority
                    }
                }
        }

        taskListRepository.updateTaskList(taskList)
        return Resource.Success(taskList.getProgression())
    }



    enum class TaskAction {
        TASK_COMPLETED, TASK_UNDO_COMPLETED, TASK_SAVED, TASK_DELETED
    }
}