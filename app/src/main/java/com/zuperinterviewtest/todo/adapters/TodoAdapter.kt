package com.zuperinterviewtest.todo.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.zuperinterviewtest.todo.data.models.Todo
import com.zuperinterviewtest.todo.databinding.ItemTodoBinding
import com.zuperinterviewtest.todo.viewmodels.TodoViewModel

class TodoAdapter(private val todoViewModel: TodoViewModel) :
    PagingDataAdapter<Todo, TodoAdapter.TodoAdapterViewHolder>(TodoDiffCallback()) {

    override fun onBindViewHolder(holder: TodoAdapterViewHolder, position: Int) {
        val currentTodo = getItem(position)
        currentTodo?.let { holder.bind(currentTodo = it, todoViewModel = todoViewModel) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapterViewHolder =
        TodoAdapterViewHolder.from(parent)

    class TodoAdapterViewHolder(private val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(currentTodo: Todo, todoViewModel: TodoViewModel) {
            binding.apply {
                viewmodel = todoViewModel
                todo = currentTodo
                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): TodoAdapterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTodoBinding.inflate(layoutInflater, parent, false)
                return TodoAdapterViewHolder(binding)
            }
        }
    }
}

private class TodoDiffCallback : DiffUtil.ItemCallback<Todo>() {
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem == newItem
    }

}