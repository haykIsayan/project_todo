package com.example.project_todo.view.customviews

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.example.project_todo.Constants
import com.example.project_todo.R
import com.example.project_todo.entity.Task
import com.example.project_todo.view.dialog.ChangePriorityDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import java.text.DateFormat
import java.util.*

class TaskDetailView(context: Context, attributeSet: AttributeSet): LinearLayout(context, attributeSet) {

    private val detailTextView: MaterialTextView
    private val priorityButton: MaterialButton
    private val deadlineButton: MaterialButton
    private val creationDateButton: MaterialButton

    init {
        View.inflate(context, R.layout.layout_task_detail, this)
        detailTextView = findViewById(R.id.mtv_task_description_layout_task_detail)
        priorityButton = findViewById(R.id.mb_priority_layout_task_detail)
        deadlineButton = findViewById(R.id.mb_deadline_layout_task_detail)
        creationDateButton = findViewById(R.id.mb_creation_layout_task_detail)
    }

    fun setOnPriorityClick(onPriorityClick: (Int) -> Unit) {
        priorityButton.apply {
            visibility = View.VISIBLE
            backgroundTintList = ColorStateList.valueOf(Color.BLACK)
            isEnabled = true
            setOnClickListener {
                ChangePriorityDialog(context) {priority,  priorityRes ->
                    setText(priorityRes)
                    onPriorityClick(priority)
                }.show()
            }
        }
    }

    fun setTask(task: Task) {
        task.apply {
            setPriority(priority)
            setDescritpion(description)
            setDeadline(deadline)
            setCreationDate(creationDate)
        }
    }

    fun setPriority(priority: Int) {
        if (priority > 3 || priority <= 0) return

        priorityButton.visibility = View.VISIBLE
        priorityButton.setText(Constants.priorityItems[priority])
    }

    fun setDescritpion(description: String) {
        if (description.isEmpty()) return

        detailTextView.apply {
            visibility = View.VISIBLE
            text = description
        }
    }

    fun setDeadline(deadline: String?) {
        if (deadline.isNullOrEmpty()) return

        val isMissed = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT)
            .parse(deadline)?.run { this.before(Date())} ?: false

        deadlineButton.apply {
            text = "Due | $deadline"
            visibility = View.VISIBLE
            backgroundTintList = if (isMissed) {
                ColorStateList.valueOf(Color.RED)
            } else {
                ColorStateList.valueOf(Color.LTGRAY)
            }
        }
    }

    fun setCreationDate(creationDate: String) {
        if (creationDate.isEmpty()) return

        creationDateButton.apply {
            text = "Created | $creationDate"
            visibility = View.VISIBLE
        }

    }
}