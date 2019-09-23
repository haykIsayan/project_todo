package com.example.project_todo.view

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.project_todo.*
import com.example.project_todo.entity.Error
import com.example.project_todo.entity.Resource
import com.example.project_todo.entity.Task
import com.example.project_todo.entity.TaskCollection
import com.example.project_todo.view.dialog.AddNewTaskDialog
import com.example.project_todo.viewmodel.MainViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm
import org.koin.androidx.viewmodel.ext.android.getViewModel
import java.text.DateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var addTaskFab: FloatingActionButton
    private lateinit var bottomAppBar: BottomAppBar

    private var addTaskDialog: AddNewTaskDialog? = null

    private lateinit var navigationController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationController = Navigation.findNavController(this, R.id.nav_host_fragment)
        mainViewModel = getViewModel()

        observe(mainViewModel::getTaskListsData, ::onTaskListDataObtained)
        observe(mainViewModel::getSaveTaskLiveEvent, ::onTaskSaved)
        observe(mainViewModel::getErrorData) {resource ->
            resource.inspectFor<Error> { onError(it) }
        }

        addTaskFab = findViewById<FloatingActionButton>(R.id.fab_add_task_activity_main)
            .apply { setOnClickListener { addTask() } }

        bottomAppBar = findViewById<BottomAppBar>(R.id.bab_main_menu_activity_main)
            .apply {
                setOnMenuItemClickListener { onRecordTaskClick().run { true } }
                setNavigationOnClickListener { showTaskCollectionsDialog() }
            }
    }

    private fun onTaskListDataObtained(resource: Resource<List<TaskCollection>>) {
        resource
            .inspectFor<Resource.Success<List<TaskCollection>>> {
                onTaskCollectionsObtained(it.successData)
            }
            .inspectFor<Resource.Pending> { onPending(it.pendingMessage) }
            .inspectFor<Resource.Failure> { onFailure(it) }
            .inspectFor<Error> { onError(it) }
    }

    private fun onTaskCollectionsObtained(taskCollections: List<TaskCollection>) {
        if (taskCollections.isNotEmpty()) {
            mainViewModel.setCurrentTaskList(taskCollections[0])
        }
    }

    private fun onPending(pendingMessage: String) {

    }

    private fun onFailure(failure: Resource.Failure) {
        failure.inspectFor<Error> { onError(it) }
    }

    private fun onError(error: Error) {
        Toast.makeText(this, error.throwable.message, Toast.LENGTH_LONG).show()

    }

    private fun onSaveTaskClicked(task: Task) {
        task.apply {
            mainViewModel.saveTask(this)
            // todo work in progress
            if (deadline.isNotEmpty()) {
                DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT)
                    .parse(deadline)?.apply { enableAlarm(this, task) }
            }
        }
    }

    private fun onTaskSaved(resource: Resource<Task>) {
        resource.inspectFor<Error> {
            onError(it)
            return
        }
        addTaskDialog?.apply {
            dismiss()
        }
        addTaskDialog = null
    }

    private fun addTask() {
        if (addTaskDialog == null) {
            addTaskDialog = initAddTaskDialog(::onSaveTaskClicked, {
//                toggleBottomBarAndButton(false)
            }, {
//                toggleBottomBarAndButton(true)
            })
        }
        addTaskDialog?.show()
    }

    private fun showTaskCollectionsDialog() {
        findNavController(R.id.nav_host_fragment).navigate(R.id.destination_task_collections)
    }

    private fun enableAlarm(date: Date, task: Task) {
        (getSystemService(Context.ALARM_SERVICE) as AlarmManager).apply {
            val intent = Intent(this@MainActivity, AlarmReceiver::class.java)
            intent.putExtra(Constants.TASK_EXTRA, task)
            val pendingIntent = PendingIntent
                .getBroadcast(this@MainActivity, Constants.REQUEST_SET_ALARM, intent, 0)
            setExact(AlarmManager.RTC_WAKEUP, date.time, pendingIntent)
        }
    }

    private fun disableAlarm(date: Date) {

    }

    private fun onRecordTaskClick() {

    }

    private fun onSearchTasksClick() {

    }

    private fun toggleBottomBarAndButton(visible: Boolean = true) {
        val visibility = if (visible) { View.VISIBLE } else { View.GONE }
        addTaskFab.visibility = visibility
        bottomAppBar.visibility = visibility
    }

    override fun onDestroy() {
        super.onDestroy()
        Realm.getDefaultInstance().close()
    }
}
