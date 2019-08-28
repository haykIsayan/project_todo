package com.example.project_todo.view.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_todo.R
import com.example.project_todo.TodoUtils
import com.example.project_todo.entity.Error
import com.example.project_todo.entity.NoTodos
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Todo
import com.example.project_todo.view.TodoAdapter
import com.example.project_todo.viewmodel.MainViewModel
import com.example.project_todo.viewmodel.TodosViewModel
import com.google.android.material.tabs.TabLayout


class TodosFragment : Fragment(), TabLayout.OnTabSelectedListener {

    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mTodosViewModel: TodosViewModel

    private lateinit var tlTodoTypes: TabLayout
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

        initTabLayout(view)
        initRecyclerView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mMainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        mTodosViewModel = ViewModelProviders.of(this).get(TodosViewModel::class.java)

        mMainViewModel.getData().observe(viewLifecycleOwner, Observer {
            observeMainData(it)
        })

        mTodosViewModel.getTodoListData().observe(viewLifecycleOwner, Observer {
            onTodosFiltered(it)
        })

        mTodosViewModel.getCompletedListData().observe(viewLifecycleOwner, Observer {
            onCompletedFiltered(it)
        })
    }

    private fun initTabLayout(view: View) {
        tlTodoTypes = view.findViewById(R.id.tl_types_todos_fragment)
        tlTodoTypes.addTab(tlTodoTypes.newTab().setText("To do"))
        tlTodoTypes.addTab(tlTodoTypes.newTab().setText("Completed"))
        tlTodoTypes.addOnTabSelectedListener(this)
    }

    private fun initRecyclerView(view: View) {
        rvTodoList = view.findViewById(R.id.rv_todos_todos_fragment)
        mTodoAdapter = TodoAdapter(this::completeTodo)
        rvTodoList.layoutManager = LinearLayoutManager(this.context, RecyclerView.VERTICAL, false)
        rvTodoList.adapter = mTodoAdapter
        rvTodoList.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
    }

    private fun completeTodo(todo: Todo) {
        // todo complete todo
    }

    private fun onTodosFiltered(resource: Resource<List<Todo>>) {
        if (tlTodoTypes.selectedTabPosition != TodoUtils.Constants.TODO_POSITION) return
        displayTodos(resource)
    }

    private fun onCompletedFiltered(resource: Resource<List<Todo>>) {
        if (tlTodoTypes.selectedTabPosition != TodoUtils.Constants.COMPLETED_POSITION) return
        displayTodos(resource)
    }

    private fun displayTodos(resource: Resource<List<Todo>>?) {
        when (resource) {
            is Resource.Success -> {
                mTodoAdapter.setTodoList(resource.successData)
            }
        }
    }

    private fun observeMainData(it: Resource<List<Todo>>?) {
        when (it) {
            is Resource.Success -> onMainDataObtained(it.successData)
            is NoTodos -> {
                // todo empty list implementation
            }
            is Error -> {
                // todo error implementation
            }
        }
    }

    private fun onMainDataObtained(todos: List<Todo>) {
        rvTodoList.visibility = View.VISIBLE
        mTodosViewModel.loadTodoList(todos)
        mTodosViewModel.loadCompletedList(todos)
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        when (tab?.position) {
            TodoUtils.Constants.TODO_POSITION -> displayTodos(mTodosViewModel.getTodoListData().value)
            TodoUtils.Constants.COMPLETED_POSITION -> displayTodos(mTodosViewModel.getCompletedListData().value)
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {

    }


}
