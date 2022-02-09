package com.zuperinterviewtest.todo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zuperinterviewtest.todo.data.models.Todo
import com.zuperinterviewtest.todo.databinding.ItemTagTodoBinding

var previousTag: String = "none"

class TagViewAdapter :
    ListAdapter<Todo, TagViewAdapter.TagViewAdapterViewHolder>(TodoTagViewDiffCallback()) {

    class TagViewAdapterViewHolder(private val binding: ItemTagTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(todo: Todo) {
            binding.apply {
                currentTodo = todo
                executePendingBindings()
            }
            val currentTag = todo.tag.trim()
            if (currentTag.contentEquals(previousTag, ignoreCase = true)) {
                // Same tag, so hide the tag card
                binding.itemCardTagAll.visibility = View.GONE
                // set current tag to previous tag
                previousTag = currentTag
            } else {
                // New tag, display tag card
                binding.itemCardTagAll.visibility = View.VISIBLE
                binding.itemTvTagAll.text = currentTag
                // set current tag to previous tag
                previousTag = currentTag
            }
        }

        companion object {
            fun from(parent: ViewGroup): TagViewAdapterViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemTagTodoBinding.inflate(layoutInflater, parent, false)
                return TagViewAdapterViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewAdapterViewHolder =
        TagViewAdapterViewHolder.from(parent)

    override fun onBindViewHolder(holder: TagViewAdapterViewHolder, position: Int) {
        val currentTodo = getItem(position)
        currentTodo?.let { holder.bind(todo = it) }
    }
}

private class TodoTagViewDiffCallback : DiffUtil.ItemCallback<Todo>() {
    override fun areItemsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Todo, newItem: Todo): Boolean {
        return oldItem == newItem
    }
}