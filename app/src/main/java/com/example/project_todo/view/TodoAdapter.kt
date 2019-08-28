package com.example.project_todo.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project_todo.R
import com.example.project_todo.entity.Todo
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textview.MaterialTextView

class TodoAdapter(private val onCheckBoxClicked: (Todo) -> Unit): RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    private val mTodoList = mutableListOf<Todo>()
    
    fun setTodoList(todoList: List<Todo>) {
        mTodoList.clear()
        mTodoList.addAll(todoList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        return TodoViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_todo_preview, parent, false))
    }

    override fun getItemCount(): Int = mTodoList.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        holder.bind(mTodoList[position])
    }

    inner class TodoViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val mcbComplete: MaterialCheckBox = itemView.findViewById(R.id.mcb_complete_layout_todo_preview)
        private val mtvTodoText: MaterialTextView = itemView.findViewById(R.id.mtv_todo_text_layout_todo_preview)

        fun bind(todo: Todo) {
            mtvTodoText.text = todo.text

            if (todo.isCompleted) {
                mcbComplete.isChecked = true
                mcbComplete.isEnabled = false
            }

            mcbComplete.setOnClickListener { onCheckBoxClicked(todo) }
        }
    }
}