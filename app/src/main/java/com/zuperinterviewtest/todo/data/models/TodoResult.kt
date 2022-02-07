package com.zuperinterviewtest.todo.data.models

import com.google.gson.annotations.SerializedName

data class TodoResult(
    @SerializedName("data")
    val todo: List<Todo>,
    val total_records: Int
)