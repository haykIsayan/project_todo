package com.example.project_todo.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.project_todo.R
import com.example.project_todo.entity.*
import com.example.project_todo.init
import com.example.project_todo.showCompleteTaskDialog
import com.example.project_todo.view.TodoAdapter
import com.example.project_todo.view.customviews.EmptyStateView
import com.example.project_todo.view.customviews.TodoFilterView
import com.example.project_todo.viewmodel.MainViewModel
import com.example.project_todo.viewmodel.TaskViewModel
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel


class TasksFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var taskViewModel: TaskViewModel

    private lateinit var todoFilterView: TodoFilterView
    private lateinit var emptyStateView: EmptyStateView

    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var taskAdapter: TodoAdapter

    private var completeTaskDialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.todos_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emptyStateView = view.findViewById(R.id.esv_empty_state_todos_fragment)

        todoFilterView = view.findViewById(R.id.tfv_todo_filter_todo_fragment)
        todoFilterView.setOnTodoFiltered(this::filterTasks)
        todoFilterView.setOnDateSelected(this::onDateSelected)

        taskRecyclerView = view.findViewById(R.id.rv_todos_todos_fragment)
        taskAdapter = TodoAdapter(this::completeTask)
        taskRecyclerView.init(taskAdapter)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        taskViewModel = getViewModel()
        mainViewModel = getSharedViewModel()

        mainViewModel.getCurrentTaskListData().observe(viewLifecycleOwner, Observer {
            taskViewModel.fetchAllTask(it.title)
        })

        mainViewModel.getAddTaskLiveEvent().observe(viewLifecycleOwner, Observer {
            onTaskSaved(it)
        })

        taskViewModel.getAllTasksData().observe(viewLifecycleOwner, Observer { onTodoDataObtained(it) })
        taskViewModel.getFilteredTodosData().observe(viewLifecycleOwner, Observer { onTodoDataObtained(it) })
        taskViewModel.getCompleteEvent().observe(viewLifecycleOwner, Observer { updateCompletedTask(it) })
    }

    private fun onTaskSaved(resource: Resource<Task>) {
        when (resource) {
            is Resource.Success -> {
                mainViewModel.completeSaveTaskEvent()
                taskAdapter.addTask(resource.successData)
            }
        }
    }

    private fun onDateSelected() {
        // TODO
    }

    private fun filterTasks(taskFilter: Task.TaskFilter) {
        taskViewModel.filterTasks(taskFilter)
    }

    private fun completeTask(task: Task, position: Int) =
        showCompleteTaskDialog(task,
            { taskViewModel.completeTask(task, position) },
            { taskAdapter.notifyItemChanged(position) })

    private fun updateCompletedTask(resource: Resource<Int>) {
        resource.apply {
            when (this) {
                is Resource.Success ->  taskAdapter.updateCompletedTodo(successData, taskViewModel.mCurrentFilter) }
        }
    }

    private fun onTodoDataObtained(resource: Resource<List<Task>>?) {
        when (resource) {
            is Resource.Success -> displayTodos(resource.successData)
            is AllCompleted -> displayAllCompletedEmptyState()
            is NoCompleted -> displayNoCompletedEmptyState()
            is Error -> { mainViewModel.sendError(resource.throwable)}
        }
    }

    private fun displayTodos(list: List<Task>) {
        taskRecyclerView.visibility = View.VISIBLE
        emptyStateView.visibility = View.GONE
        taskAdapter.setTodoList(list)
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
