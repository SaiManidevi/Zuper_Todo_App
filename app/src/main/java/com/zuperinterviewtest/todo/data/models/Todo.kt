package com.zuperinterviewtest.todo.data.models

data class Todo(
    val author: String,
    val id: Int,
    val is_completed: Boolean,
    val priority: String,
    val tag: String,
    val title: String
)