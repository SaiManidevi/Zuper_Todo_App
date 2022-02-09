package com.zuperinterviewtest.todo.data.remote

import com.zuperinterviewtest.todo.data.models.Todo
import com.zuperinterviewtest.todo.data.models.TodoResult
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class TodoApiHelperImpl @Inject constructor(private val apiService: TodoApiService) :
    TodoApiHelper {
    override suspend fun getAllTodos(page: Int, limit: Int, author: String): Response<TodoResult> {
        return apiService.getTodos(page, limit, author)
    }

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

    override suspend fun postNewTodo(newTodo: Todo): Response<Todo> {
        return apiService.postNewTodo(newTodo)
    }

    override suspend fun updateTodoCompletedStatus(
        todoId: Int,
        updatedTodo: Todo
    ): Call<ResponseBody> {
        return apiService.updateTodoCompletedStatus(
            todoID = todoId,
            updatedTodo = updatedTodo
        )
    }

}