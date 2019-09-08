package com.example.project_todo.view

import android.graphics.Paint
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.example.project_todo.R
import com.example.project_todo.entity.Task
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView


class TodoAdapter(private val onCheckBoxClicked: (Task, Int) -> Unit): RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private val mTodoList = mutableListOf<Task>()
    
    fun setTodoList(taskList: List<Task>) {
        mTodoList.clear()
        mTodoList.addAll(taskList)
        notifyDataSetChanged()
    }

    fun addTask(task: Task) {
        mTodoList.add(task)
        notifyItemInserted(itemCount)
    }

    fun updateCompletedTodo(position: Int, currentFilter: Task.TaskFilter) =
        if (currentFilter == Task.TaskFilter.TODO) {
            mTodoList.removeAt(position)
            notifyItemRemoved(position)
        } else {
            notifyItemChanged(position)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_todo_preview, parent, false))
    }

    override fun getItemCount(): Int = mTodoList.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(mTodoList[position], position)
    }

    inner class TodoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val mcbComplete: MaterialCheckBox = itemView.findViewById(R.id.mcb_complete_layout_todo_preview)
        private val mtvTodoText: MaterialTextView = itemView.findViewById(R.id.mtv_todo_text_layout_todo_preview)

        fun bind(task: Task, position: Int) {
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

            mcbComplete.setOnClickListener { onCheckBoxClicked(task, position) }
            itemView.setOnLongClickListener { onCheckBoxClicked(task, position); true }
        }
    }
}