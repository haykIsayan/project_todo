package com.example.project_todo.view.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.HorizontalScrollView
import com.example.project_todo.R
import com.example.project_todo.entity.Task
import com.google.android.material.button.MaterialButton

class TodoFilterView(context: Context, attributeSet: AttributeSet): HorizontalScrollView(context, attributeSet) {

    private val mbAllTodos: MaterialButton
    private val mbTodos: MaterialButton
    private val mbCompleted: MaterialButton
    private val mbSelectDate: MaterialButton

    private var lastClickedButton: MaterialButton

    private lateinit var onTodoFilterSelected: (Task.TaskFilter) -> Unit
    private lateinit var onDateSelected: () -> Unit

    init {
        View.inflate(context, R.layout.layout_todo_type, this)
        isHorizontalScrollBarEnabled = false

        mbAllTodos = findViewById(R.id.mb_all_todos_layout_todo_type)
        mbTodos = findViewById(R.id.mb_todos_layout_todo_type)
        mbCompleted = findViewById(R.id.mb_completed_layout_todo_type)
        mbSelectDate = findViewById(R.id.mb_date_layout_todo_type)

        lastClickedButton = mbAllTodos

        mbAllTodos.setOnClickListener { onFilterClicked((it as MaterialButton), Task.TaskFilter.ALL) }
        mbTodos.setOnClickListener { onFilterClicked((it as MaterialButton), Task.TaskFilter.TODO)  }
        mbCompleted.setOnClickListener { onFilterClicked((it as MaterialButton), Task.TaskFilter.COMPLETED)  }
        mbSelectDate.setOnClickListener { onDateSelected() }
    }

    private fun onFilterClicked(materialButton: MaterialButton, taskFilter: Task.TaskFilter) {
        lastClickedButton.apply {
            setTextColor(context.resources.getColor(R.color.colorPrimaryDark))
            setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
        }
        lastClickedButton = materialButton
        materialButton.apply {
            setTextColor(context.resources.getColor(R.color.colorPrimary))
            setBackgroundColor(context.resources.getColor(R.color.colorPrimaryDark))
        }
        onTodoFilterSelected(taskFilter)
    }

    fun setOnTodoFiltered(onTodoFilterSelected: (Task.TaskFilter) -> Unit) {
        this.onTodoFilterSelected = onTodoFilterSelected
    }

    fun setOnDateSelected(onDateSelected: () -> Unit) {
        this.onDateSelected = onDateSelected
    }
}