package com.example.project_todo

import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
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
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel


object Constants {
    const val TEST_DATE = "June 28, 1998"
    const val TEST_LIST_TITLE = "My Tasks"
    const val TEST_GROCERY_TITLE = "Groceries"
}

val dummieTaskList = TaskList(Constants.TEST_LIST_TITLE, 0F, 0F)
val dummieGroceryList = TaskList(Constants.TEST_GROCERY_TITLE, 0F, 0F)

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

fun Fragment.sendError(throwable: Throwable) {
    getSharedViewModel<MainViewModel>().sendError(throwable)
}

fun TasksFragment.showUndoSnackBar(taskText: String, actionText: String, onAction: () -> Unit) {
    val snackBar = Snackbar.make(view!!, " ", Snackbar.LENGTH_LONG)
    snackBar.setTextColor(context?.resources!!.getColor(R.color.colorPrimary))
    snackBar.setActionTextColor(context?.resources!!.getColor(R.color.colorPrimary))
    snackBar.setText(taskText)
    snackBar.setAction(actionText) {
        onAction()
    }
    snackBar.addCallback(object  : BaseTransientBottomBar.BaseCallback<Snackbar>() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)
            if (event == BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_TIMEOUT) {
//                onDismiss()
            }
        }
    })

    snackBar.show()
}

fun SeekBar.initTaskProgressMode() {
    setOnTouchListener { _, _ -> true }
}

fun MainActivity.initAddTaskDialog(onSaveTask: (String, String, List<String>, Int) -> Unit) =
    AddNewTaskDialog(this, onSaveTask)
        .apply {
            setContentView(R.layout.layout_new_task)
        }

