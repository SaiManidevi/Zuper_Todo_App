package com.zuperinterviewtest.todo.data.models

data class Todo(
    val title: String,
    val author: String,
    val tag: String,
    var is_completed: Boolean,
    val priority: String,
    val id: Int
)