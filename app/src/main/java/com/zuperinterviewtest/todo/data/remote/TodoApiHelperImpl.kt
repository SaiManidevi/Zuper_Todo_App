package com.zuperinterviewtest.todo.data.remote

import com.zuperinterviewtest.todo.data.models.TodoResult
import retrofit2.Response
import javax.inject.Inject

class TodoApiHelperImpl @Inject constructor(private val apiService: TodoApiService) :
    TodoApiHelper {
    override suspend fun getTodos(page: Int, limit: Int, author: String): Response<TodoResult> {
        return apiService.getTodos(page, limit, author)
    }

    override suspend fun getTodosBySearchQuery(
        page: Int,
        limit: Int,
        author: String,
        searchQueryTag: String
    ): Response<TodoResult> {
        return apiService.getTodosBySearchQuery(page, limit, author, searchQueryTag)
    }

}