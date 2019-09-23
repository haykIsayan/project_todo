package com.example.project_todo.view.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.project_todo.Constants
import com.example.project_todo.R
import com.google.android.material.textview.MaterialTextView

class ChangePriorityDialog(context: Context,
                           private val onPriorityClick: (Int, Int) -> Unit): AlertDialog(context) {

    private lateinit var priorityTextView: MaterialTextView

    init {
        layoutInflater.inflate(R.layout.layout_change_priority, this.listView).apply {
            setView(this@apply)

            findViewById<MaterialTextView>(R.id.mtv_priority_layout_change_priority)?.apply { priorityTextView = this }

            findViewById<SeekBar>(R.id.sb_priority_layout_change_priority)?.apply {
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onStartTrackingTouch(p0: SeekBar?) { }

                    override fun onStopTrackingTouch(p0: SeekBar?) { }

                    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { setPriority(p1 + 1) }
                })
            }
        }
    }

    private fun setPriority(priority: Int) {
//        view.findViewById<MaterialTextView>(R.id.mtv_priority_layout_change_priority)?.apply {
            val priorityRes = Constants.priorityItems[priority]
            priorityTextView.setText(priorityRes)
            onPriorityClick(priority, priorityRes)
//        }
    }
}