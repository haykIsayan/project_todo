package com.example.project_todo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.project_todo.event.AdvancedLiveEvent
import com.example.project_todo.core.TaskListRepository
import com.example.project_todo.core.TaskRepository
import com.example.project_todo.currentValue
import com.example.project_todo.domain.tasks.storage.CompleteTaskInteractor
import com.example.project_todo.domain.tasks.storage.GetAllTasksInteractor
import com.example.project_todo.domain.tasklists.UpdateProgressInteractor
import com.example.project_todo.domain.tasks.filtering.FilterTasksInteractor
import com.example.project_todo.domain.tasks.storage.DeleteTaskInteractor
import com.example.project_todo.entity.*

class TaskViewModel(private val taskRepository: TaskRepository,
                    private val taskListRepository: TaskListRepository): BaseViewModel<List<Task>>() {

    private var currentTaskCollection: TaskCollection? = null
    private val taskListProgressData = MediatorLiveData<Resource<Float>>()

    private val allTasksData = MediatorLiveData<Resource<List<Task>>>()

    private val filteredTasksData = MediatorLiveData<Resource<List<Task>>>()

    private val taskInteractEvent = AdvancedLiveEvent<Int>()

    private val taskFilter = TaskFilter()

    fun setProgressAndFetchAllTasks(taskCollection: TaskCollection) {
        if (currentTaskCollection == null ||
            (currentTaskCollection != null
                    && currentTaskCollection?.title == taskCollection.title)) {
            currentTaskCollection = taskCollection
            fetchAllTasks()
        }
    }

    /**
     * Fetch All Tasks present in the selected Task list
     */

    private fun fetchAllTasks() {
        currentTaskCollection?.apply {
            taskListProgressData.value = Resource.Success(getProgression())
            executeUseCase(GetAllTasksInteractor(title, taskRepository), allTasksData)
        }
    }

    /**
     * Update Task list progression after Task interaction
     */

    fun updateTaskCollectionProgress(task: Task, taskAction: UpdateProgressInteractor.TaskAction,
                                     onProgressUpdated: () -> Unit = {}) {
        currentTaskCollection?.apply {
            executeUseCase(
                UpdateProgressInteractor(task, taskAction, this, taskListRepository),
                taskListProgressData, onProgressUpdated)
        }
    }

    /**
     * Task interaction: Complete the given Task and save updatePosition for subsequent update of the RecyclerView
     */

    fun completeTask(task: Task, completeTask: Boolean, position: Int) {
//        executeUseCase(
//            CompleteTaskInteractor(task, completeTask, updatePosition, taskRepository), taskInteractEvent)

        executeTaskAction(task, position, UpdateProgressInteractor.TaskAction.TASK_UNDO_COMPLETED)
    }

    /**
     * Task interaction: Delete the given Task and save updatePosition for subsequent update of the RecyclerView
     */

    fun deleteTask(task: Task, position: Int) {
//        executeUseCase(DeleteTaskInteractor(task, updatePosition, taskRepository),
//            taskInteractEvent)
        executeTaskAction(task, position, UpdateProgressInteractor.TaskAction.TASK_DELETED)
    }

    /**
     * Undo Task Interaction
     */

    fun undoTaskComplete(task: Task, position: Int) {
//         executeUseCase(CompleteTaskInteractor(task, false, position, taskRepository),
//             taskInteractEvent)
        executeTaskAction(task, position, UpdateProgressInteractor.TaskAction.TASK_UNDO_COMPLETED)
    }


    private fun executeTaskAction(task: Task,
                                  position: Int,
                                  action: UpdateProgressInteractor.TaskAction) {
        updateTaskCollectionProgress(task, action) {
            when (action) {
                UpdateProgressInteractor.TaskAction.TASK_COMPLETED -> {
                    executeUseCase(CompleteTaskInteractor(task, true, position,
                            taskRepository),
                        taskInteractEvent)
                }
                UpdateProgressInteractor.TaskAction.TASK_UNDO_COMPLETED -> {
                    executeUseCase(CompleteTaskInteractor(task, false, position,
                        taskRepository),
                        taskInteractEvent)
                }
                UpdateProgressInteractor.TaskAction.TASK_DELETED -> {
                    executeUseCase(DeleteTaskInteractor(task, position, taskRepository),
                        taskInteractEvent)
                }
                UpdateProgressInteractor.TaskAction.TASK_SAVED -> {

                }
            }
        }
    }


    /**
     * Task Filtering
     */

    fun filterTasksByCompletion(taskCompletion: Task.TaskCompletion) {
        taskFilter.taskCompletion = taskCompletion

        allTasksData.currentValue {
            it.inspectForSuccess {taskList ->
                executeUseCase(FilterTasksInteractor(taskList, taskFilter), filteredTasksData)
            }
        }
    }

    fun filterTasksByPriority(priority: Int) {
        taskFilter.priority = priority
        allTasksData.currentValue {
            it.inspectForSuccess {taskList ->
                executeUseCase(FilterTasksInteractor(taskList, taskFilter), filteredTasksData)
            }
        }
    }

    fun getTaskFilter(): TaskFilter = taskFilter
    fun getTaskCompletion(): Task.TaskCompletion = taskFilter.taskCompletion

    fun getTaskCollectionProgressData(): LiveData<Resource<Float>> = taskListProgressData
    fun getAllTasksData(): LiveData<Resource<List<Task>>> = allTasksData
    fun getFilteredTasksData(): LiveData<Resource<List<Task>>> = filteredTasksData
    fun getTaskInteractEvent(): LiveData<Resource<Int>> = taskInteractEvent
}
