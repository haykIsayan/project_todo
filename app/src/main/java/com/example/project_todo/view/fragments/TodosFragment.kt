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


class TodosFragment : Fragment() {

    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mTodosViewModel: TodosViewModel

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
        initRecyclerView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mMainViewModel = ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        mTodosViewModel = ViewModelProviders.of(this).get(TodosViewModel::class.java)

        mMainViewModel.getListTitleData().observe(viewLifecycleOwner, Observer {
            mTodosViewModel.getAllTodos(it)
        })

        mTodosViewModel.getAllTodosData().observe(viewLifecycleOwner, Observer {
            displayTodos(it)
        })
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

    private fun displayTodos(resource: Resource<List<Todo>>?) {
        when (resource) {
            is Resource.Success -> {
                rvTodoList.visibility = View.VISIBLE
                mTodoAdapter.setTodoList(resource.successData)
            }
        }
    }
}
