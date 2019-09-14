package com.example.project_todo.view

import android.content.Context
import android.os.Bundle
import android.view.View
import com.example.project_todo.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import java.text.DateFormat
import java.util.*

class AddNewTaskDialog(context: Context,
                       private val onSaveTaskClicked: (text: String,
                                                       description: String,
                                                       dateString: String,
                                                       priority: Int) -> Unit): BottomSheetDialog(context) {


    private lateinit var taskTextEditText: TextInputEditText
    private lateinit var taskDescriptionEditText: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<TextInputEditText>(R.id.et_task_text_layout_new_task)?.apply {
            taskTextEditText = this
        }

        findViewById<TextInputEditText>(R.id.et_task_description_layout_new_task)?.apply {
            taskDescriptionEditText = this
        }

        findViewById<BottomNavigationView>(R.id.bnv_add_task_menu)?.apply {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.save_task -> saveTask()
                    R.id.toggle_descritpion -> toggleDescription()
                }
                true
            }
        }
    }

    private fun saveTask() {
        val date = DateFormat.getInstance().format(Date())
        val description = if (taskDescriptionEditText.visibility == View.VISIBLE) {
            taskDescriptionEditText.text.toString()
        } else { "" }

        onSaveTaskClicked(taskTextEditText.text.toString(), description, date,  2)
    }

    private fun toggleDescription() {
        taskDescriptionEditText.apply {
            visibility = if (visibility == View.GONE) { View.VISIBLE } else { View.GONE }
        }
    }



}