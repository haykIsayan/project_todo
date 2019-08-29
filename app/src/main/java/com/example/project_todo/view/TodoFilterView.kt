package com.example.project_todo.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.HorizontalScrollView
import com.example.project_todo.R
import com.example.project_todo.entity.Todo

class TodoFilterView(context: Context, attributeSet: AttributeSet): HorizontalScrollView(context, attributeSet) {

    private lateinit var onTodoFilterSelected: (Todo.TodoFilter) -> Unit
    private lateinit var onDateSelected: () -> Unit

    init {
        View.inflate(context, R.layout.layout_todo_type, this)
    }

    fun setOnTodoFiltered(onTodoFilterSelected: (Todo.TodoFilter) -> Unit) {
        this.onTodoFilterSelected = onTodoFilterSelected
    }
}