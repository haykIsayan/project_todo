package com.example.project_todo.view.customviews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.project_todo.R
import com.google.android.material.textview.MaterialTextView

class EmptyStateView(context: Context, attributeSet: AttributeSet): ConstraintLayout(context, attributeSet) {


    private val ivCover: ImageView
    private val mtvCover: MaterialTextView

    init {
        View.inflate(context, R.layout.layout_empty_state, this)

        ivCover = findViewById(R.id.iv_cover_layout_empty_state)
        mtvCover = findViewById(R.id.mtv_message_layout_empty_state)
    }

    fun invokeAllCompletedMode() {
        ivCover.setImageResource(R.drawable.ic_action_completed)
        mtvCover.setText("All Completed")
    }

    fun invokeNoCompletedMode() {

    }


}