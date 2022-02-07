package com.zuperinterviewtest.todo.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.zuperinterviewtest.todo.R

@BindingAdapter("priorityImage")
fun bindPriorityCircle(view: ImageView, priority: String?) {
    priority?.let {
        view.setImageResource(
            when (priority) {
                "LOW" -> R.drawable.circle_solid_priority_low
                "MEDIUM" -> R.drawable.circle_solid_priority_medium
                "HIGH" -> R.drawable.circle_solid_priority_high
                else -> R.drawable.circle_solid_priority_low
            }
        )
    } ?: view.setImageResource(R.drawable.circle_solid_priority_low)
}

@BindingAdapter("completedStatus")
fun bindTodoCompletedStatus(view: ImageView, isCompleted: Boolean) {
    view.setImageResource(
        if (isCompleted) R.drawable.ic_checked else R.drawable.circle_plain
    )
    view.contentDescription = if (isCompleted) "Todo checked" else "Todo not checked"
}