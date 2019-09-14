package com.example.project_todo.view

import android.graphics.Paint
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.project_todo.R
import com.example.project_todo.entity.Task
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textview.MaterialTextView


class TaskAdapter(private val onCompleteTask: (Task, Int) -> Unit): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private val taskList = mutableListOf<Task>()
    
    fun setTasks(taskList: List<Task>) {
        this.taskList.clear()
        this.taskList.addAll(taskList)
        notifyDataSetChanged()
    }

    fun getTask(position: Int) = taskList[position]

    fun updateTaskSaved(task: Task, updatePosition: Int) {
        if (updatePosition == -1) {
            taskList.add(task)
            notifyItemInserted(itemCount)
        } else {
            taskList.add(updatePosition, task)
            notifyItemInserted(updatePosition)
        }
    }

    fun updateTaskCompleted(position: Int, currentCompletion: Task.TaskCompletion) =
        if (currentCompletion == Task.TaskCompletion.TODO) {
            taskList.removeAt(position)
            notifyItemRemoved(position)
        } else {
            notifyItemChanged(position)
        }

    fun updateTaskUndoCompleted(task: Task, position: Int, currentCompletion: Task.TaskCompletion) {
        if (currentCompletion == Task.TaskCompletion.TODO) {
            taskList.add(position, task)
            notifyItemInserted(position)
        } else {
            notifyItemChanged(position)
        }
    }

    fun updateTaskDeleted(position: Int) {
        taskList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_task_preview, parent, false))
    }

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    inner class TaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val mrbComplete: MaterialRadioButton = itemView.findViewById(R.id.mrb_complete_layout_todo_preview)
        private val mtvTaskText: MaterialTextView = itemView.findViewById(R.id.mtv_task_text_layout_task_preview)
        private val mtvTaskDescription: MaterialTextView = itemView.findViewById(R.id.mtv_task_description_layout_task_preview)

        fun bind(task: Task) {
            mtvTaskText.text = task.text
            mtvTaskDescription.text = task.description

            if (task.isCompleted) {
                mrbComplete.isChecked = true
                mtvTaskText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                mtvTaskDescription.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                mrbComplete.isChecked = false
                mrbComplete.isEnabled = true
                mtvTaskText.paintFlags = Paint.LINEAR_TEXT_FLAG
                mtvTaskDescription.paintFlags = Paint.LINEAR_TEXT_FLAG
            }

            mrbComplete.setOnClickListener { onCompleteTask(task, adapterPosition) }
            itemView.setOnLongClickListener { onCompleteTask(task, adapterPosition); true }

            itemView.setOnClickListener {

                mtvTaskDescription.apply {
                   visibility =  if (visibility == View.GONE && task.description.isNotEmpty()) {
                        View.VISIBLE
                    } else {
                        View.GONE
                    }
                }
            }
        }
    }
}