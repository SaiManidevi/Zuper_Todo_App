package com.zuperinterviewtest.todo.data.remote

import com.zuperinterviewtest.todo.data.models.Todo
import com.zuperinterviewtest.todo.data.models.TodoResult
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

/**
 * Helpful interface to separate Repository and the Retrofit Service interface
 */
interface TodoApiHelper {
    suspend fun getAllTodos(page: Int, limit: Int, author: String): Response<TodoResult>
    suspend fun getTodos(page: Int, limit: Int, author: String): Response<TodoResult>
    suspend fun getTodosBySearchQuery(
        page: Int,
        limit: Int,
        author: String,
        searchQueryTag: String
    ): Response<TodoResult>

    suspend fun postNewTodo(newTodo: Todo): Response<Todo>

    suspend fun updateTodoCompletedStatus(todoId: Int, updatedTodo: Todo): Call<ResponseBody>
}