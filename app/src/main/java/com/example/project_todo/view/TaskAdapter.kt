package com.example.project_todo.view

import android.graphics.Paint
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.project_todo.R
import com.example.project_todo.entity.Task
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView


class TaskAdapter(private val onCheckBoxClicked: (Task, Int) -> Unit): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

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
            .inflate(R.layout.layout_todo_preview, parent, false))
    }

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position])
    }

    inner class TaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val mcbComplete: MaterialCheckBox = itemView.findViewById(R.id.mcb_complete_layout_todo_preview)
        private val mtvTodoText: MaterialTextView = itemView.findViewById(R.id.mtv_todo_text_layout_todo_preview)

        fun bind(task: Task) {
            mtvTodoText.text = task.text

            if (task.isCompleted) {
                mcbComplete.isChecked = true
                mcbComplete.isEnabled = false
                mtvTodoText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                mcbComplete.isChecked = false
                mcbComplete.isEnabled = true
                mtvTodoText.paintFlags = Paint.LINEAR_TEXT_FLAG
            }

            mcbComplete.setOnClickListener { onCheckBoxClicked(task, adapterPosition) }
            itemView.setOnLongClickListener { onCheckBoxClicked(task, adapterPosition); true }
        }
    }
}