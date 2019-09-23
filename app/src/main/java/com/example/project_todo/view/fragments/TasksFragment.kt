package com.example.project_todo.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.project_todo.*
import com.example.project_todo.domain.tasklists.UpdateProgressInteractor
import com.example.project_todo.entity.*
import com.example.project_todo.view.TaskAdapter
import com.example.project_todo.view.customviews.EmptyStateView
import com.example.project_todo.view.customviews.TaskBundleView
import com.example.project_todo.view.customviews.TaskFilterView
import com.example.project_todo.viewmodel.MainViewModel
import com.example.project_todo.viewmodel.TaskViewModel
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class TasksFragment : Fragment() {
    /**
     * View Models
     */
    private lateinit var mainViewModel: MainViewModel
    private lateinit var taskViewModel: TaskViewModel
    /**
     * Views & Widgets
     */
    private lateinit var taskBundleView: TaskBundleView
    private lateinit var taskFilterView: TaskFilterView
    private lateinit var emptyStateView: EmptyStateView
    private lateinit var taskRecyclerView: RecyclerView
    /**
     * Utilities
     */
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tasks_fragment, container, false)
    }

    /**
     * Instantiate Views
     */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskBundleView = view.findViewById(R.id.tbv_task_bundle_tasks_fragment)

        emptyStateView = view.findViewById(R.id.esv_empty_state_todos_fragment)
        // Setup Task Filter View
        taskFilterView = view.findViewById(R.id.tfv_todo_filter_todo_fragment)
        taskFilterView.setOnTodoFiltered(::filterTasks)
        taskFilterView.setOnDateSelected(::onDateSelected)
        taskFilterView.setOnPriorityClick(::onPrioritySelected)
        // Setup Recycler View
        taskRecyclerView = view.findViewById(R.id.rv_todos_todos_fragment)
        taskAdapter = TaskAdapter(::completeTask, ::undoTaskComplete)
        taskRecyclerView.initTaskListMode(taskAdapter, ::completeTask, ::deleteTask)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        taskFilterView.restoreState(taskViewModel.getTaskFilter())
    }

    /**
     * Instantiate ViewModels & Observe All Live Data and Live Events
     */

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        taskViewModel = getViewModel()
        mainViewModel = getSharedViewModel()

        observe(mainViewModel::getCurrentTaskListData, ::onCurrentTaskBundleSet)

        observe(mainViewModel::getSaveTaskLiveEvent, ::onTaskSaved)

        observe(taskViewModel::getTaskCollectionProgressData, ::onTaskCollectionProgressChanged)

        observe(taskViewModel::getAllTasksData)
        { if (savedInstanceState == null) onTasksDataObtained(it) }

        observe(taskViewModel::getFilteredTasksData, ::onTasksDataObtained)
        observe(taskViewModel::getTaskInteractEvent, ::onTaskInteraction)
    }

    private fun onCurrentTaskBundleSet(taskCollection: TaskCollection) {
        taskBundleView.setTaskBundle(taskCollection)
        taskViewModel.setProgressAndFetchAllTasks(taskCollection)
    }

    /**
     * Display Updated Progress
     */

    private fun onTaskCollectionProgressChanged(resource: Resource<Float>) {
        resource
            .inspectFor<Resource.Success<Float>> {
                taskBundleView.updateProgress(it.successData.toInt())
            }
            .inspectFor(::sendError)
    }

    /**
     * Task Filtering
     */

    private fun onPrioritySelected(priority: Int) {
        taskViewModel.filterTasksByPriority(priority)
    }

    private fun onDateSelected() {
        // TODO
    }

    private fun filterTasks(taskCompletion: Task.TaskCompletion)
            = taskViewModel.filterTasksByCompletion(taskCompletion)

    /**
     * On Task Action
     */

    private fun completeTask(task: Task, position: Int)
            = taskViewModel.completeTask(task, true, position)

    private fun undoTaskComplete(task: Task, position: Int)
            = taskViewModel.undoTaskComplete(task, position)

    private fun deleteTask(task: Task, position: Int)
            = taskViewModel.deleteTask(task, position)

    private fun undoTaskDelete()
            = taskViewModel.getTaskInteractEvent().currentValue {
        it.inspectFor<TaskDeleted> {taskDeleted ->
            mainViewModel.undoDeleteTask(taskDeleted.task, taskDeleted.successData)
        }
    }

    private fun onTaskInteraction(resource: Resource<Int>) {
        resource
            .inspectFor<TaskCompleted> { onTaskCompleted(it.task, it.successData) }
            .inspectFor<TaskUndoCompleted> { onTaskUndoCompleted(it.task, it.successData) }
            .inspectFor<TaskDeleted> { onTaskDeleted(it.task, it.successData) }
            .inspectFor<ErrorCompleted> { onTaskCompleteError( it)}
    }

    private fun onTaskCompleted(task: Task, updatePosition: Int) {
//        taskViewModel.updateTaskCollectionProgress(task,
//                UpdateProgressInteractor.TaskAction.TASK_COMPLETED)

        taskAdapter.updateTaskCompleted(updatePosition, taskViewModel.getTaskCompletion())
    }

    private fun onTaskUndoCompleted(task: Task, updatePosition: Int) {
//        taskViewModel.updateTaskCollectionProgress(task,
//            UpdateProgressInteractor.TaskAction.TASK_UNDO_COMPLETED)

        taskAdapter.updateTaskUndoCompleted(task, updatePosition, taskViewModel.getTaskCompletion())
    }

    private fun onTaskCompleteError(errorCompleted: ErrorCompleted) {
        taskAdapter.notifyItemChanged(errorCompleted.updatePosition)
        sendError(errorCompleted)
    }

    private fun onTaskDeleted(task: Task, updatePosition: Int) {
//        taskViewModel.updateTaskCollectionProgress(task,
//            UpdateProgressInteractor.TaskAction.TASK_DELETED)
        taskAdapter.updateTaskDeleted(updatePosition)
        showUndoDeleteSnackBar(task)
    }

    private fun onTaskSaved(resource: Resource<Task>) {
        resource.inspectFor<TaskSaved> {
            mainViewModel.disableSaveTaskEvent()
            taskViewModel.updateTaskCollectionProgress(it.successData, UpdateProgressInteractor.TaskAction.TASK_SAVED)
            taskAdapter.updateTaskSaved(it.successData, it.updatePosition)
        }
    }

    private fun showUndoDeleteSnackBar(task: Task) {
        showUndoSnackBar(task.text, resources.getString(R.string.undo_delete_action), this::undoTaskDelete)
    }

    /**
     * Update User Interface once Task has been Saved
     */



    /**
     * Display Obtained and Filtered Tasks
     */

    private fun onTasksDataObtained(resource: Resource<List<Task>>?) {
        resource?.apply {
            inspectFor<Resource.Success<List<Task>>> { displayTasks(it.successData) }
            inspectFor<Resource.Pending> { displayLoading(it.pendingMessage) }
            inspectFor<Resource.Failure> { displayEmptyState(it) }
            inspectFor<Error> { sendError(it) }
        }
    }

    private fun displayTasks(list: List<Task>) {
        taskRecyclerView.visibility = View.VISIBLE
        emptyStateView.visibility = View.GONE
        taskAdapter.setTasks(list)
    }

    private fun displayLoading(pendingMessage: String) {

    }

    /**
     * Display Empty States
     */

    private fun displayEmptyState(failure: Resource.Failure) {
        failure.apply {
            inspectFor<AllCompleted> { displayAllCompletedEmptyState() }
            inspectFor<NothingToShow> { taskRecyclerView.visibility = View.GONE }
            inspectFor<NoCompleted> { displayNoCompletedEmptyState() }
        }
    }

    private fun displayAllCompletedEmptyState() {
        taskRecyclerView.visibility = View.GONE
        emptyStateView.visibility = View.VISIBLE
        emptyStateView.invokeAllCompletedMode()
    }

    private fun displayNoCompletedEmptyState() {
        taskRecyclerView.visibility = View.GONE
    }

}
