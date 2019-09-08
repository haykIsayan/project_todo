package com.example.project_todo

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_todo.entity.Task
import com.example.project_todo.entity.TaskList
import com.example.project_todo.view.AddNewTaskDialog
import com.example.project_todo.view.MainActivity


object Constants {
    const val TEST_DATE = "June 28, 1998"
    const val TEST_LIST_TITLE = "My Tasks"

    const val TODO_POSITION = 0
    const val COMPLETED_POSITION = 1
}

val dummieTaskList = TaskList(Constants.TEST_LIST_TITLE, mutableListOf())


fun RecyclerView.init(adapter: RecyclerView.Adapter<*>) {
    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
    this.adapter = adapter
}

fun Fragment.showCompleteTaskDialog(task: Task, onPositive: () -> Unit, onNegative: () -> Unit)
        = AlertDialog.Builder(this.context!!)

    .setTitle(resources.getString(R.string.complete_dialog_title))
    .setMessage(task.text)
    .setCancelable(false)
    .setPositiveButton(resources.getString(R.string.complete_dialog_positive)) { _, _ -> onPositive() }
    .setNegativeButton(resources.getString(R.string.complete_dialog_negative)) { _, _ -> onNegative() }
    .create()
    .show()

fun MainActivity.initAddTaskDialog(onSaveTask: (String, String, List<String>, Int) -> Unit) =
    AddNewTaskDialog(this, onSaveTask)
        .apply {
            setContentView(R.layout.layout_new_task)
        }

