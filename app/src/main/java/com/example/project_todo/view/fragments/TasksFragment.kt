package com.example.project_todo.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.project_todo.*
import com.example.project_todo.domain.tasklists.UpdateProgressInteractor
import com.example.project_todo.entity.*
import com.example.project_todo.view.TaskAdapter
import com.example.project_todo.view.customviews.EmptyStateView
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
    private lateinit var taskProgressSeekBar: SeekBar
    private lateinit var taskFilterView: TaskFilterView
    private lateinit var emptyStateView: EmptyStateView
    private lateinit var taskRecyclerView: RecyclerView
    /**
     * Utilities
     */
    private lateinit var taskAdapter: TaskAdapter
    private var completeTaskDialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.todos_fragment, container, false)
    }

    /**
     * Instantiate Views
     */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emptyStateView = view.findViewById(R.id.esv_empty_state_todos_fragment)
        // Setup Task Progress Bar
        taskProgressSeekBar = view.findViewById(R.id.sb_task_progress_task_fragment)
        taskProgressSeekBar.initTaskProgressMode()
        // Setup Task Filter View
        taskFilterView = view.findViewById(R.id.tfv_todo_filter_todo_fragment)
        taskFilterView.setOnTodoFiltered(this::filterTasks)
        taskFilterView.setOnDateSelected(this::onDateSelected)
        taskFilterView.setOnPriorityClick(this::onPrioritySelected)
        // Setup Recycler View
        taskRecyclerView = view.findViewById(R.id.rv_todos_todos_fragment)
        taskAdapter = TaskAdapter(this::startTaskCompletion)
        taskRecyclerView.initTaskListMode(taskAdapter, ::startTaskCompletion, ::startTaskDeletion)
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

        mainViewModel.getCurrentTaskListData()
            .observe(viewLifecycleOwner, Observer { taskViewModel.setProgressAndFetchAllTasks(it) })

        mainViewModel.getAddTaskLiveEvent().observe(viewLifecycleOwner, Observer { updateTaskSaved(it) })

        taskViewModel.getTaskListProgressData().observe(viewLifecycleOwner, Observer { onTaskListProgressChanged(it) })

        taskViewModel.getAllTasksData().observe(viewLifecycleOwner, Observer { onTasksDataObtained(it) })
        taskViewModel.getFilteredTasksData().observe(viewLifecycleOwner, Observer { onTasksDataObtained(it) })

        taskViewModel.getTaskInteractEvent().observe(viewLifecycleOwner, Observer { onTaskInteraction(it) })
    }

    /**
     * Display Updated Progress
     */

    private fun onTaskListProgressChanged(resource: Resource<Float>) {
        resource.inspect({ taskProgressSeekBar.progress = it.toInt() })
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

    private fun filterTasks(taskCompletion: Task.TaskCompletion) {
        taskViewModel.filterTasksByCompletion(taskCompletion)
    }

    /**
     * Show Task Interaction Dialogs
     */

    private fun startTaskCompletion(task: Task, position: Int) =
        showCompleteTaskDialog(task,
            { taskViewModel.completeTask(task, position) },
            { taskAdapter.notifyItemChanged(position) })

    private fun startTaskDeletion(task: Task, position: Int) {
        showDeleteTaskDialog(task,
            { taskViewModel.deleteTask(task, position) },
            { taskAdapter.notifyItemChanged(position) })
    }

    /**
     * Inspect Task Interaction Result
     */

    private fun onTaskInteraction(resource: Resource<Int>) {
        resource.inspect({
            when (resource) {
                is TaskCompleted -> updateTaskCompleted(resource.task, it)
                is TaskDeleted -> updateTaskDeleted(resource.task, it)
            }
        })
    }

    /**
     * Update User Interface once Task has been Completed
     */

    private fun updateTaskCompleted(task: Task, updatePosition: Int) {
        taskViewModel.updateTaskListProgress(task, UpdateProgressInteractor.UpdateAction.TASK_COMPLETED)
        taskAdapter.updateTaskCompleted(updatePosition, taskViewModel.mCurrentCompletion)
    }

    /**
     * Update User Interface once Task has been Deleted
     */

    private fun updateTaskDeleted(task: Task, updatePosition: Int) {
        taskViewModel.updateTaskListProgress(task, UpdateProgressInteractor.UpdateAction.TASK_DELETED)
        taskAdapter.updateTaskDeleted(updatePosition)
    }

    /**
     * Update User Interface once Task has been Saved
     */

    private fun updateTaskSaved(resource: Resource<Task>) {
        resource.inspect({
            mainViewModel.completeSaveTaskEvent()
            taskViewModel.updateTaskListProgress(it, UpdateProgressInteractor.UpdateAction.TASK_SAVED)
            taskAdapter.updateTaskSaved(it)
        })
    }

    /**
     * Display Obtained and Filtered Tasks
     */

    private fun onTasksDataObtained(resource: Resource<List<Task>>?) {
        resource?.inspect(::displayTasks, ::displayLoading, ::displayEmptyState, ::sendError)
    }

    private fun displayTasks(list: List<Task>) {
        taskRecyclerView.visibility = View.VISIBLE
        emptyStateView.visibility = View.GONE
        taskAdapter.setTodoList(list)
    }

    private fun displayLoading(pendingMessage: String) {

    }

    /**
     * Display Empty States
     */

    private fun displayEmptyState(failure: Resource.Failure) {
        when (failure) {
            is AllCompleted -> displayAllCompletedEmptyState()
            is NothingToShow ->{
                taskRecyclerView.visibility = View.GONE
            }
            is NoCompleted -> displayNoCompletedEmptyState()
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
