package com.example.project_todo.view.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_todo.R
import com.example.project_todo.entity.*
import com.example.project_todo.view.TodoAdapter
import com.example.project_todo.view.customviews.EmptyStateView
import com.example.project_todo.view.customviews.TodoFilterView
import com.example.project_todo.viewmodel.MainViewModel
import com.example.project_todo.viewmodel.TodosViewModel


class TodosFragment : Fragment() {

    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mTodosViewModel: TodosViewModel

    private lateinit var tfvTodoFilter: TodoFilterView

    private lateinit var esvEmptyState: EmptyStateView

    private var mCompleteDialog: AlertDialog? = null

    private lateinit var rvTodoList: RecyclerView
    private lateinit var mTodoAdapter: TodoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.todos_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tfvTodoFilter = view.findViewById(R.id.tfv_todo_filter_todo_fragment)
        esvEmptyState = view.findViewById(R.id.esv_empty_state_todos_fragment)
        tfvTodoFilter.setOnTodoFiltered(this::filterTodos)
        tfvTodoFilter.setOnDateSelected(this::onDateSelected)
        initRecyclerView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mMainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        mTodosViewModel = ViewModelProviders.of(this).get(TodosViewModel::class.java)

        mMainViewModel.getListTitleData().observe(viewLifecycleOwner, Observer { mTodosViewModel.fetchAllTodos(it) })

        mTodosViewModel.getAllTodosData().observe(viewLifecycleOwner, Observer { onTodoDataObtained(it) })
        mTodosViewModel.getFilteredTodosData().observe(viewLifecycleOwner, Observer { onTodoDataObtained(it) })
        mTodosViewModel.getCompleteEvent().observe(viewLifecycleOwner, Observer { updateCompletedTodo(it) })
    }

    private fun initRecyclerView(view: View) {
        rvTodoList = view.findViewById(R.id.rv_todos_todos_fragment)
        mTodoAdapter = TodoAdapter(this::completeTodo)
        rvTodoList.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        rvTodoList.adapter = mTodoAdapter
        rvTodoList.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
    }

    private fun onDateSelected() {
        // TODO
    }

    private fun filterTodos(todoFilter: Todo.TodoFilter) {
        mTodosViewModel.filterTodos(todoFilter)
        mTodosViewModel.mCurrentFilter = todoFilter
    }

    private fun completeTodo(todo: Todo, position: Int) {
        mCompleteDialog?.apply {
            show()
            return
        }
        context?.apply {
            mCompleteDialog = AlertDialog.Builder(this)
                .setTitle(resources.getString(R.string.complete_dialog_title))
                .setMessage(todo.text)
                .setOnCancelListener { mTodoAdapter.notifyItemChanged(position) }
                .setPositiveButton(resources.getString(R.string.complete_dialog_positive)) { _, _ ->
                    mTodosViewModel.completeTodo(todo, position)
                }
                .setNegativeButton(resources.getString(R.string.complete_dialog_negative)) { _, _ ->
                    mTodoAdapter.notifyItemChanged(position)
                }
                .create()
            mCompleteDialog?.show()
        }
    }

    private fun updateCompletedTodo(resource: Resource<Int>) {
        resource.apply {
            when (this) {
                is Resource.Success ->  mTodoAdapter.updateCompletedTodo(successData, mTodosViewModel.mCurrentFilter) }
        }
    }

    private fun onTodoDataObtained(resource: Resource<List<Todo>>?) {
        when (resource) {
            is Resource.Success -> displayTodos(resource.successData)
            is AllCompleted -> displayAllCompletedEmptyState()
            is NoCompleted -> displayNoCompletedEmptyState()
            is Error -> { mMainViewModel.sendError(resource.throwable)}
        }
    }

    private fun displayTodos(list: List<Todo>) {
        rvTodoList.visibility = View.VISIBLE
        esvEmptyState.visibility = View.GONE
        mTodoAdapter.setTodoList(list)
    }

    private fun displayAllCompletedEmptyState() {
        rvTodoList.visibility = View.GONE
        esvEmptyState.visibility = View.VISIBLE
        esvEmptyState.invokeAllCompletedMode()
    }

    private fun displayNoCompletedEmptyState() {
        rvTodoList.visibility = View.GONE
    }

}
