package com.example.project_todo

import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_todo.entity.Task
import com.example.project_todo.entity.TaskList
import com.example.project_todo.view.AddNewTaskDialog
import com.example.project_todo.view.MainActivity
import com.example.project_todo.view.TaskAdapter
import com.example.project_todo.view.fragments.TasksFragment
import com.example.project_todo.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel


object Constants {
    const val TEST_DATE = "June 28, 1998"
    const val TEST_LIST_TITLE = "My Tasks"
}

val dummieTaskList = TaskList(Constants.TEST_LIST_TITLE, 0F, 4F)

fun RecyclerView.initTaskListMode(adapter: TaskAdapter,
                                  onCompleteSwipe: (Task, Int) -> Unit,
                                  onDeleteSwipe: (Task, Int) -> Unit) {
    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
    this.adapter = adapter

    val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val task = adapter.getTask(viewHolder.adapterPosition)
            when (direction) {
                ItemTouchHelper.RIGHT ->  onCompleteSwipe(task, viewHolder.adapterPosition)
                ItemTouchHelper.LEFT -> onDeleteSwipe(task, viewHolder.adapterPosition)
            }
        }
    }
    ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(this)
}

fun TasksFragment.showTaskInteractionDialog(task: Task, isComplete: Boolean,


                                         onPositive: () -> Unit, onNegative: () -> Unit)
        = AlertDialog.Builder(this.context!!)

    .setTitle(if (isComplete) {
        resources.getString(R.string.complete_dialog_title)
    } else {
        resources.getString(R.string.delete_dialog_title)
    })
    .setMessage(task.text)
    .setCancelable(false)
    .setPositiveButton(if (isComplete) {
        resources.getString(R.string.complete_dialog_positive)
    } else {
        resources.getString(R.string.delete_dialog_positive)
    }) { _, _ -> onPositive() }
    .setNegativeButton(resources.getString(R.string.task_dialog_negative)) { _, _ -> onNegative() }
    .create()
    .show()


fun TasksFragment.showCompleteTaskDialog(task: Task, onPositive: () -> Unit, onNegative: () -> Unit)
        = AlertDialog.Builder(this.context!!)

    .setTitle(resources.getString(R.string.complete_dialog_title))
    .setMessage(task.text)
    .setCancelable(false)
    .setPositiveButton(resources.getString(R.string.complete_dialog_positive)) { _, _ -> onPositive() }
    .setNegativeButton(resources.getString(R.string.task_dialog_negative)) { _, _ -> onNegative() }
    .create()
    .show()

fun TasksFragment.showDeleteTaskDialog(task: Task, onPositive: () -> Unit, onNegative: () -> Unit)
        = AlertDialog.Builder(this.context!!)

    .setTitle(resources.getString(R.string.delete_dialog_title))
    .setMessage(task.text)
    .setCancelable(false)
    .setPositiveButton(resources.getString(R.string.delete_dialog_positive)) { _, _ -> onPositive() }
    .setNegativeButton(resources.getString(R.string.task_dialog_negative)) { _, _ -> onNegative() }
    .create()
    .show()

fun Fragment.sendError(throwable: Throwable) {
    getSharedViewModel<MainViewModel>().sendError(throwable)
}

fun TasksFragment.initTaskProgressSeekBar() {

}

fun SeekBar.initTaskProgressMode() {
    setOnTouchListener { _, _ -> true }
}

fun MainActivity.initAddTaskDialog(onSaveTask: (String, String, List<String>, Int) -> Unit) =
    AddNewTaskDialog(this, onSaveTask)
        .apply {
            setContentView(R.layout.layout_new_task)
        }

