package com.example.project_todo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.project_todo.SingleLiveEvent
import com.example.project_todo.core.TaskListRepository
import com.example.project_todo.core.TaskRepository
import com.example.project_todo.domain.tasks.storage.CompleteTaskInteractor
import com.example.project_todo.domain.tasks.filtering.FilterByCompletionInteractor
import com.example.project_todo.domain.tasks.storage.GetAllTasksInteractor
import com.example.project_todo.domain.tasklists.UpdateProgressInteractor
import com.example.project_todo.domain.tasks.filtering.FilterByPriorityInteractor
import com.example.project_todo.domain.tasks.filtering.FilterTasksInteractor
import com.example.project_todo.domain.tasks.storage.DeleteTaskInteractor
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Task
import com.example.project_todo.entity.TaskFilter
import com.example.project_todo.entity.TaskList

class TaskViewModel(private val taskRepository: TaskRepository,
                    private val taskListRepository: TaskListRepository): BaseViewModel<List<Task>>() {

    private var currentTaskList: TaskList? = null
    private val taskListProgressData = MediatorLiveData<Resource<Float>>()

    private val allTasksData = MediatorLiveData<Resource<List<Task>>>()

    private val filteredTasksData = MediatorLiveData<Resource<List<Task>>>()

    private val taskInteractEvent = SingleLiveEvent<Resource<Int>>()

    private val taskFilter = TaskFilter()

    var mCurrentCompletion: Task.TaskCompletion = Task.TaskCompletion.ALL

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
            invokeUseCase(GetAllTasksInteractor(title, taskRepository), allTasksData)
        }
    }

    /**
     * Update Task list progression after Task interaction
     */

    fun updateTaskListProgress(task: Task, updateAction: UpdateProgressInteractor.UpdateAction) {
        currentTaskList?.apply {
            invokeUseCase(
                UpdateProgressInteractor(
                    task,
                    updateAction,
                    this,
                    taskListRepository
                ),
                taskListProgressData)
        }
    }

    /**
     * Task interaction: Complete the given Task and save updatePosition for subsequent update of the RecyclerView
     */

    fun completeTask(task: Task, updatePosition: Int) {
        invokeUseCase(CompleteTaskInteractor(task, updatePosition, taskRepository), taskInteractEvent)
    }

    /**
     * Task interaction: Delete the given Task and save updatePosition for subsequent update of the RecyclerView
     */

    fun deleteTask(task: Task, updatePosition: Int) {
        invokeUseCase(DeleteTaskInteractor(task, updatePosition, taskRepository), taskInteractEvent)
    }


    /**
     * Task Filtering
     */

    fun filterTasksByCompletion(taskCompletion: Task.TaskCompletion) {
//        mCurrentCompletion = taskCompletion
//        allTasksData.value?.inspect({
//            invokeUseCase(FilterByCompletionInteractor(it, taskCompletion), filteredTasksData)
//        })
        taskFilter.taskCompletion = taskCompletion
        allTasksData.value?.inspect({
            invokeUseCase(FilterTasksInteractor(it, taskFilter), filteredTasksData)
        })
    }

    fun filterTasksByPriority(priority: Int) {
        taskFilter.priority = priority
        allTasksData.value?.inspect({
            invokeUseCase(FilterTasksInteractor(it, taskFilter), filteredTasksData)
        })
    }

    fun getTaskFilter(): TaskFilter = taskFilter

    fun getTaskListProgressData(): LiveData<Resource<Float>> = taskListProgressData
    fun getAllTasksData(): LiveData<Resource<List<Task>>> = allTasksData
    fun getFilteredTasksData(): LiveData<Resource<List<Task>>> = filteredTasksData
    fun getTaskInteractEvent(): LiveData<Resource<Int>> = taskInteractEvent
}
