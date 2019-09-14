package com.example.project_todo.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.project_todo.R
import com.example.project_todo.entity.Error
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.TaskList
import com.example.project_todo.initAddTaskDialog
import com.example.project_todo.viewmodel.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var addTaskFab: FloatingActionButton

    private var addTaskDialog: AddNewTaskDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel = getViewModel()

        mainViewModel.getTaskListsData().observe(this, Observer {
            it.inspect(::onTaskListsObtained, ::onPending, ::onFailure, ::onError)
        })

        mainViewModel.getSaveTaskLiveEvent().observe(this, Observer { onTaskSaved() })

        mainViewModel.getErrorData().observe(this, Observer { onError(it) })

        addTaskFab = findViewById(R.id.fab_add_task_activity_main)
        addTaskFab.setOnClickListener { addTask() }
    }

    private fun onTaskListsObtained(taskLists: List<TaskList>) {
        if (taskLists.isNotEmpty()) {
            val taskList = taskLists[0]
            mainViewModel.setCurrentTaskList(taskLists[0])
        }
    }

    private fun onPending(pendingMessage: String) {

    }

    private fun onFailure(failure: Resource.Failure) {
        when (failure) {
            is Error -> onError(failure.throwable)
        }
    }

    private fun onError(throwable: Throwable) {
        Toast.makeText(this, throwable.message, Toast.LENGTH_LONG).show()
    }

    private fun onTaskSaved() {
        addTaskDialog?.apply {
            dismiss()
        }
        addTaskDialog = null
    }

    private fun addTask() {
        if (addTaskDialog == null) {
            addTaskDialog = initAddTaskDialog(mainViewModel::saveTask)
        }
        addTaskDialog?.show()
    }
}
