package com.zuperinterviewtest.todo.data.remote

import com.zuperinterviewtest.todo.data.models.Todo
import com.zuperinterviewtest.todo.data.models.TodoResult
import com.zuperinterviewtest.todo.utils.Constants.PATH_TODO
import com.zuperinterviewtest.todo.utils.Constants.QUERY_AUTHOR
import com.zuperinterviewtest.todo.utils.Constants.QUERY_LIMIT
import com.zuperinterviewtest.todo.utils.Constants.QUERY_PAGE
import com.zuperinterviewtest.todo.utils.Constants.TAG
import retrofit2.Response
import retrofit2.http.*

interface TodoApiService {
    //http://167.71.235.242:3000/todo?_page=1&_limit=1000&author=Ranjith
    @GET(PATH_TODO)
    suspend fun getAllTodos(
        @Query(QUERY_PAGE) page: Int,
        @Query(QUERY_LIMIT) limit: Int,
        @Query(QUERY_AUTHOR) author: String
    ): Response<TodoResult>

    // http://167.71.235.242:3000/todo?_page=1&_limit=15&author=Manidevi
    @GET(PATH_TODO)
    suspend fun getTodos(
        @Query(QUERY_PAGE) page: Int,
        @Query(QUERY_LIMIT) limit: Int,
        @Query(QUERY_AUTHOR) author: String
    ): Response<TodoResult>

    // http://167.71.235.242:3000/todo?
    //_page=1&_limit=1500&author=Ranjith&tag=Backend
    @GET(PATH_TODO)
    suspend fun getTodosBySearchQuery(
        @Query(QUERY_PAGE) page: Int,
        @Query(QUERY_LIMIT) limit: Int,
        @Query(QUERY_AUTHOR) author: String,
        @Query(TAG) searchQueryTag: String
    ): Response<TodoResult>

    // http://167.71.235.242:3000/todo
    @POST(PATH_TODO)
    suspend fun postNewTodo(@Body newTodo: Todo): Response<Todo>

    // http://167.71.235.242:3000/todo/1
    @PUT("todo/{id}")
    suspend fun updateTodoCompletedStatus(
        @Path("id") id: Int,
        @Body updatedTodo: Todo
    ): Response<Todo>
}