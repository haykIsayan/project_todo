package com.example.project_todo.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.example.project_todo.R
import com.example.project_todo.entity.Task
import com.example.project_todo.viewmodel.MainViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import java.text.DateFormat
import java.util.*

class AddNewTaskDialog(context: Context,
                       private val onSaveTaskClicked: (text: String,
                                                       dateString: String,
                                                       subTasks: List<String>,
                                                       priority: Int) -> Unit): BottomSheetDialog(context) {


    private lateinit var taskTextEditText: TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<FloatingActionButton>(R.id.fab_save_task_layout_new_task)?.apply {
            setOnClickListener { saveTask() }
        }

        findViewById<TextInputEditText>(R.id.et_todo_text_layout_new_task)?.apply {
            taskTextEditText = this
        }


    }

    private fun saveTask() {
        val date = DateFormat.getInstance().format(Date())
        onSaveTaskClicked(taskTextEditText.text.toString(), date, mutableListOf(), 2)
    }



}