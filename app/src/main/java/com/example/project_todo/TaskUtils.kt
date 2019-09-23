package com.example.project_todo

import android.widget.SeekBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_todo.entity.Task
import com.example.project_todo.entity.TaskCollection
import com.example.project_todo.view.dialog.AddNewTaskDialog
import com.example.project_todo.view.MainActivity
import com.example.project_todo.view.TaskAdapter
import com.example.project_todo.view.fragments.TasksFragment
import com.example.project_todo.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.getSharedViewModel
import android.view.View
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import com.example.project_todo.entity.Error
import io.realm.Realm
import io.realm.RealmModel
import io.realm.exceptions.RealmException


object Constants {
    // Prototypes for Testing
    const val PROTO_DATE = "June 28, 1998"
    const val PROTO_COLLECTION_TITLE = "My Tasks"
    const val PROTO_TASK_TEXT = "Lorem ipsum dolor sit amet"
    const val PROTO_TASK_DESCRIPTION = "Excepteur sint occaecat cupidatat non proident."
    const val PROTO_GROCERY_TITLE = "Groceries"
    // EXTRAS
    const val TASK_EXTRA = "Task.Extra"
    // REQUESTS
    const val REQUEST_SET_ALARM = 1998
    // Error System Messages
    const val REALM_UPDATE_EXCEPTION_MESSAGE = "Realm Object to be updated does not exist"

    val priorityItems = arrayOf(
        R.string.task_all_priority,
        R.string.task_low_priority,
        R.string.task_mid_priority,
        R.string.task_top_priority)
}

val protoTask = Task(
    text = Constants.PROTO_TASK_TEXT,
    description = Constants.PROTO_TASK_DESCRIPTION,
    creationDate = Constants.PROTO_DATE,
    taskCollectionTitle = Constants.PROTO_COLLECTION_TITLE)

val myTasksCollection = TaskCollection(title = Constants.PROTO_COLLECTION_TITLE, progressValue = 0F, fullValue = 0F)


val groceryListCollection = TaskCollection(title = Constants.PROTO_GROCERY_TITLE, progressValue = 0F, fullValue = 0F)

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

fun Fragment.sendError(error: Error) {
    getSharedViewModel<MainViewModel>().sendError(error)
}


fun <T> Fragment.observe(liveData: LiveData<T>, onChanged: (T) -> Unit ) {
    liveData.observe(viewLifecycleOwner, Observer(onChanged))
}

fun <T> Fragment.observe(onData: () -> LiveData<T>, onChanged: (T) -> Unit) {
    onData().observe(viewLifecycleOwner, Observer(onChanged))
}


fun <T> MainActivity.observe(onData: () -> LiveData<T>, onChanged: (T) -> Unit) {
    onData().observe(this, Observer(onChanged))
}


fun TasksFragment.showUndoSnackBar(taskText: String, actionText: String, onAction: () -> Unit) {
    Snackbar.make(view!!, taskText, Snackbar.LENGTH_LONG).apply {
        setAnchorView(R.id.fab_add_task_activity_main)
        resources.apply {
            setTextColor(getColor(R.color.colorPrimary, newTheme()))
            setActionTextColor(getColor(R.color.colorPrimary, newTheme()))
            view.background = getDrawable(R.drawable.layout_snackbar_background, newTheme())
        }
        (view.layoutParams as CoordinatorLayout.LayoutParams).apply {
            marginStart = 40
            marginEnd = 40
        }
        setAction(actionText) { onAction() }
        show()
    }
}

fun SeekBar.initTaskProgressMode() {
    setOnTouchListener { _, _ -> true }
}

fun MainActivity.initAddTaskDialog(onSaveTask: (Task) -> Unit, onShow: () -> Unit, onDismiss: () -> Unit) =
    AddNewTaskDialog(this, onSaveTaskClicked = onSaveTask)
        .apply {
            setContentView(R.layout.layout_new_task)
            setOnShowListener { onShow() }
            setOnDismissListener { onDismiss() }
            setOnCancelListener { onDismiss() }
        }

fun <T> LiveData<T>.currentValue(onValue: (T) -> Unit) {
    value?.apply {
        onValue(this)
    }
}

fun <E: RealmModel> Realm.createObjectIncPrimaryKey(clazz: Class<E>, keyFieldName: String): E {
    return where(clazz).max(keyFieldName).run {
        if (this == null) 1  else toInt() + 1
    }.run { createObject(clazz, this) }
}

fun Realm.updateTask(task: Task) {
    where(Task::class.java)
        .equalTo(Task.PRIMARY_KEY_FIELD_NAME, task.id)
        .findAll().run {
            if (isNotEmpty() && first() != null) {
                first()!!.copy(task)
                commitTransaction()
            } else {
                commitTransaction()
                throw RealmException(Constants.REALM_UPDATE_EXCEPTION_MESSAGE)
            }
        }
}

//fun Realm.updateTaskCollection(taskCollection: TaskCollection) {
//    where(Task::class.java)
//        .equalTo(Task.PRIMARY_KEY_FIELD_NAME, task.id)
//        .findFirst().run {
//            if (this != null) {
//                this.copy(task)
//                commitTransaction()
//            } else {
//                throw RealmException(Constants.REALM_UPDATE_EXCEPTION_MESSAGE)
//            }
//        }
//}

//fun slideUp(view: View) {
//    view.setVisibility(View.VISIBLE)
//    val animate = TranslateAnimation(
//        0f, // fromXDelta
//        0f, // toXDelta
//        view.getHeight(), // fromYDelta
//        0f
//    )                // toYDelta
//    animate.duration = 500
//    animate.fillAfter = true
//    view.startAnimation(animate)
//}
//
//// slide the view from its current position to below itself
//fun slideDown(view: View) {
//    val animate = TranslateAnimation(
//        0f, // fromXDelta
//        0f, // toXDelta
//        0f, // fromYDelta
//        view.getHeight()
//    ) // toYDelta
//    animate.duration = 500
//    animate.fillAfter = true
//    view.startAnimation(animate)
//}

fun View.slideUpGone() {
    val animate = TranslateAnimation(
        0f, // fromXDelta
        0f, // toXDelta
        height.toFloat(), // fromYDelta
        0f
    )
    animate.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(p0: Animation?) {}

        override fun onAnimationEnd(p0: Animation?) { this@slideUpGone.visibility = View.GONE }

        override fun onAnimationStart(p0: Animation?) {}
    })
    animate.duration = 300
    animate.fillAfter = true
    startAnimation(animate)
}

fun View.slideDownVisible() {
    visibility = View.VISIBLE
    val animate = TranslateAnimation(
        0f, // fromXDelta
        0f, // toXDelta
        0f, // fromYDelta
        height.toFloat()
    ) // toYDelta

    animate.duration = 300
    animate.fillAfter = true
    startAnimation(animate)
}
