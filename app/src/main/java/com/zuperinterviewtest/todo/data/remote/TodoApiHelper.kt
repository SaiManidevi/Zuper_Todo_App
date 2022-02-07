package com.zuperinterviewtest.todo.data.remote

import com.zuperinterviewtest.todo.data.models.TodoResult
import retrofit2.Response

interface TodoApiHelper {
    suspend fun getTodos(page: Int, limit: Int, author: String): Response<TodoResult>
}