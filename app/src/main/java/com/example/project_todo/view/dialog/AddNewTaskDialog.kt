package com.example.project_todo.view.dialog

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.project_todo.R
import com.example.project_todo.entity.Task
import com.example.project_todo.view.customviews.TaskDetailView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import java.text.DateFormat
import java.util.*

class AddNewTaskDialog(context: Context, private val description: String = "",
                       private val onSaveTaskClicked: (Task) -> Unit = {}): BottomSheetDialog(context) {

    private lateinit var taskDetailView: TaskDetailView
    private lateinit var taskTextEditText: TextInputEditText
    private lateinit var taskDescriptionEditText: TextInputEditText

    private var task = Task()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState?.getParcelable<Task>("TASK")?.apply { task = this }

        findViewById<TaskDetailView>(R.id.tdv_task_details_layout_new_task)?.apply {
            taskDetailView = this
            setOnPriorityClick(::onPrioritySet)
        }

        findViewById<TextInputEditText>(R.id.et_task_text_layout_new_task)?.apply {
            taskTextEditText = this
        }

        findViewById<TextInputEditText>(R.id.et_task_description_layout_new_task)?.apply {
            setText(description)
            taskDescriptionEditText = this
        }

        findViewById<BottomNavigationView>(R.id.bnv_add_task_menu)?.apply {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.save_task -> saveTask()
                    R.id.toggle_descritpion -> toggleDescription()
                    R.id.add_deadline -> addDeadline()
                    R.id.add_image -> addImage()
                }
                true
            }
        }
    }

    override fun onSaveInstanceState() = Bundle().apply { putParcelable("TASK", task) }

    private fun saveTask() {
        if (taskTextEditText.text.toString().isEmpty()) return

        task.apply {
            text = taskTextEditText.text.toString()
            creationDate = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(Date())
            description = if (taskDescriptionEditText.visibility == View.VISIBLE) {
                taskDescriptionEditText.text.toString()
            } else { "" }
            onSaveTaskClicked(this)
        }
    }

    private fun toggleDescription() {
        taskDescriptionEditText.apply {
            visibility = if (visibility == View.GONE) { View.VISIBLE } else { View.GONE }
        }
    }

    private fun onPrioritySet(priority: Int) {
        task.priority = priority
    }

    private fun addDeadline() {
        DateAndTimeDialog(context, ::setDeadline)
            .apply {
                create()
                show()
            }
    }

    private fun addImage() {

    }

    private fun setRecurring() {

    }

    private fun setDeadline(dateString: String) {
        task.deadline = dateString
        taskDetailView.setDeadline(dateString)
    }



}