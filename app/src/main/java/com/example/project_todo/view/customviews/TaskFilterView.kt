package com.example.project_todo.view.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.HorizontalScrollView
import androidx.appcompat.app.AlertDialog
import com.example.project_todo.R
import com.example.project_todo.entity.Task
import com.example.project_todo.entity.TaskFilter
import com.google.android.material.button.MaterialButton

class TaskFilterView(context: Context, attributeSet: AttributeSet): HorizontalScrollView(context, attributeSet) {

    private val mbAllTodos: MaterialButton
    private val mbTodos: MaterialButton
    private val mbCompleted: MaterialButton
    private val mbSelectDate: MaterialButton
    private val mbPriority: MaterialButton

    private var lastClickedButton: MaterialButton

    private val priorityItems = arrayOf(
        resources.getString(R.string.task_all_priority),
        resources.getString(R.string.task_low_priority),
        resources.getString(R.string.task_mid_priority),
        resources.getString(R.string.task_top_priority))

    private lateinit var onTodoFilterSelected: (Task.TaskCompletion) -> Unit
    private lateinit var onDateSelected: () -> Unit

    init {
        View.inflate(context, R.layout.layout_task_filter, this)
        isHorizontalScrollBarEnabled = false

        mbAllTodos = findViewById(R.id.mb_all_todos_layout_todo_type)
        mbTodos = findViewById(R.id.mb_todos_layout_todo_type)
        mbCompleted = findViewById(R.id.mb_completed_layout_todo_type)
        mbSelectDate = findViewById(R.id.mb_date_layout_todo_type)
        mbPriority = findViewById(R.id.mb_priority_layout_todo_type)

        lastClickedButton = mbAllTodos

        mbAllTodos.setOnClickListener { onFilterClicked((it as MaterialButton), Task.TaskCompletion.ALL) }
        mbTodos.setOnClickListener { onFilterClicked((it as MaterialButton), Task.TaskCompletion.TODO)  }
        mbCompleted.setOnClickListener { onFilterClicked((it as MaterialButton), Task.TaskCompletion.COMPLETED)  }
        mbSelectDate.setOnClickListener { onDateSelected() }
    }

    fun restoreState(taskFilter: TaskFilter) {
        taskFilter.apply {
            val button = when (taskCompletion) {
                Task.TaskCompletion.ALL -> mbAllTodos
                Task.TaskCompletion.TODO -> mbTodos
                Task.TaskCompletion.COMPLETED -> mbCompleted
            }
            onFilterClicked(button, taskCompletion, false)

            mbSelectDate.text = creationDate
            mbPriority.text = priorityItems[priority]


        }
    }

    private fun onFilterClicked(materialButton: MaterialButton,
                                taskCompletion: Task.TaskCompletion,
                                performClick: Boolean = true) {
        lastClickedButton.apply {
            setTextColor(context.resources.getColor(R.color.colorPrimaryDark))
            setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
        }
        lastClickedButton = materialButton
        materialButton.apply {
            setTextColor(context.resources.getColor(R.color.colorPrimary))
            setBackgroundColor(context.resources.getColor(R.color.colorPrimaryDark))
        }
        if (performClick) {
            onTodoFilterSelected(taskCompletion)
        }
    }

    fun setOnTodoFiltered(onTodoFilterSelected: (Task.TaskCompletion) -> Unit) {
        this.onTodoFilterSelected = onTodoFilterSelected
    }

    fun setOnDateSelected(onDateSelected: () -> Unit) {
        this.onDateSelected = onDateSelected
    }

    /**
     * Priority Management
     */

    fun setOnPriorityClick(onPriorityClick: (Int) -> Unit) {

        mbPriority.apply {
            visibility = View.VISIBLE

            setOnLongClickListener {
                text = priorityItems[0]
                onPriorityClick(0)
                true
            }

            setOnClickListener {
                val priorityDialogBuilder = AlertDialog.Builder(context)
                priorityDialogBuilder.setItems(priorityItems) { _, i ->
                    text = priorityItems[i]
                    onPriorityClick(i)
                }
                priorityDialogBuilder.create().show()
            }
        }
    }
}