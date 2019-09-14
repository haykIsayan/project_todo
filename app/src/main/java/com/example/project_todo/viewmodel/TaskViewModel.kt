package com.example.project_todo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.project_todo.event.PersistentLiveEvent
import com.example.project_todo.core.TaskListRepository
import com.example.project_todo.core.TaskRepository
import com.example.project_todo.domain.tasks.storage.CompleteTaskInteractor
import com.example.project_todo.domain.tasks.storage.GetAllTasksInteractor
import com.example.project_todo.domain.tasklists.UpdateProgressInteractor
import com.example.project_todo.domain.tasks.filtering.FilterTasksInteractor
import com.example.project_todo.domain.tasks.storage.DeleteTaskInteractor
import com.example.project_todo.entity.*

class TaskViewModel(private val taskRepository: TaskRepository,
                    private val taskListRepository: TaskListRepository): BaseViewModel<List<Task>>() {

    private var currentTaskList: TaskList? = null
    private val taskListProgressData = MediatorLiveData<Resource<Float>>()

    private val allTasksData = MediatorLiveData<Resource<List<Task>>>()

    private val filteredTasksData = MediatorLiveData<Resource<List<Task>>>()

    private val taskInteractEvent = PersistentLiveEvent<Int>()

    private val taskFilter = TaskFilter()

    private var isUndoModeActive = false

    fun setProgressAndFetchAllTasks(taskList: TaskList) {
        if (currentTaskList == null || (currentTaskList != null && currentTaskList?.title == taskList.title)) {
            currentTaskList = taskList
            fetchAllTasks()
        }
    }

    /**
     * Fetch All Tasks present in the selected Task list
     */

    private fun fetchAllTasks() {
        currentTaskList?.apply {
            taskListProgressData.value = Resource.Success(getProgression())
            executeUseCase(GetAllTasksInteractor(title, taskRepository), allTasksData)
        }
    }

    /**
     * Update Task list progression after Task interaction
     */

    fun updateTaskListProgress(task: Task, taskAction: UpdateProgressInteractor.TaskAction) {
        currentTaskList?.apply {
            executeUseCase(
                UpdateProgressInteractor(task, taskAction, this, taskListRepository), taskListProgressData)
        }
    }

    /**
     * Task interaction: Complete the given Task and save updatePosition for subsequent update of the RecyclerView
     */

    fun completeTask(task: Task, completeTask: Boolean, updatePosition: Int) {
        executeUseCase(CompleteTaskInteractor(task, completeTask, updatePosition, taskRepository), taskInteractEvent)
    }

    /**
     * Task interaction: Delete the given Task and save updatePosition for subsequent update of the RecyclerView
     */

    fun deleteTask(task: Task, updatePosition: Int) {
        executeUseCase(DeleteTaskInteractor(task, updatePosition, taskRepository), taskInteractEvent)
    }

    /**
     * Undo Task Interaction
     */

    fun undoTaskComplete(task: Task, position: Int) {
//        taskInteractEvent.value?.apply {
//            inspectFor<TaskCompleted> {
                executeUseCase(
                        CompleteTaskInteractor(task, false, position, taskRepository),
                        taskInteractEvent, taskInteractEvent::persistAndDisable)
//            }
//        }

    }


    fun undoTaskInteraction() = taskInteractEvent.complete()

    fun setUndoModeActive(undoModeActive: Boolean) {
        isUndoModeActive = undoModeActive
    }

    /**
     * Task Filtering
     */

    fun filterTasksByCompletion(taskCompletion: Task.TaskCompletion) {
        taskFilter.taskCompletion = taskCompletion
        allTasksData.value?.inspect({
            executeUseCase(FilterTasksInteractor(it, taskFilter), filteredTasksData)
        })
    }

    fun filterTasksByPriority(priority: Int) {
        taskFilter.priority = priority
        allTasksData.value?.inspect({
            executeUseCase(FilterTasksInteractor(it, taskFilter), filteredTasksData)
        })
    }

    fun getTaskFilter(): TaskFilter = taskFilter
    fun getTaskCompletion(): Task.TaskCompletion = taskFilter.taskCompletion

    fun getTaskListProgressData(): LiveData<Resource<Float>> = taskListProgressData
    fun getAllTasksData(): LiveData<Resource<List<Task>>> = allTasksData
    fun getFilteredTasksData(): LiveData<Resource<List<Task>>> = filteredTasksData
    fun getTaskInteractEvent(): LiveData<Resource.EventResource<Int>> = taskInteractEvent
}
