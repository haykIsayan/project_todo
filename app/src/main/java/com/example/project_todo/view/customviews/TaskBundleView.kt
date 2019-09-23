package com.example.project_todo.view.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import com.example.project_todo.R
import com.example.project_todo.entity.TaskCollection
import com.example.project_todo.initTaskProgressMode
import com.google.android.material.textview.MaterialTextView

class TaskBundleView(context: Context, attributeSet: AttributeSet): LinearLayout(context, attributeSet) {

    private val taskBundleTitle: MaterialTextView
    private val taskBundleProgress: MaterialTextView
    private val taskBundleProgressBar: SeekBar

    init {
        View.inflate(context, R.layout.layout_task_bundle, this)
        taskBundleTitle = findViewById(R.id.mtv_task_bundle_title_layout_task_bundle)
        taskBundleProgress = findViewById(R.id.mtv_task_bundle_progression_layout_task_bundle)
        taskBundleProgressBar = findViewById(R.id.sb_task_progress_layout_task_bundle)
        taskBundleProgressBar.initTaskProgressMode()
    }

    fun setTaskBundle(taskCollection: TaskCollection) {
        taskBundleTitle.text = taskCollection.title
        taskBundleProgress.text = "${taskCollection.getProgression().toInt()} %"
    }

    fun updateProgress(progress: Int) {
        taskBundleProgress.text = "$progress %"
        taskBundleProgressBar.progress = progress
    }

}