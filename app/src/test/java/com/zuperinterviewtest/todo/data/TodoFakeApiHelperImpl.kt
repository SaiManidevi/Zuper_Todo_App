package com.zuperinterviewtest.todo.data

import com.zuperinterviewtest.todo.data.models.Todo
import com.zuperinterviewtest.todo.data.models.TodoResult
import com.zuperinterviewtest.todo.data.remote.TodoApiHelper
import retrofit2.Response

class TodoFakeApiHelperImpl : TodoApiHelper {
    private val todoData: MutableList<Todo> = mutableListOf()

    override suspend fun getAllTodos(page: Int, limit: Int, author: String): Response<TodoResult> {
        val todoResult = TodoResult(todoData, total_records = getTotalRecords())
        return Response.success(todoResult)
    }

    private fun getTotalRecords(): Int = todoData.size

    override suspend fun getTodos(page: Int, limit: Int, author: String): Response<TodoResult> {
        val todoResult = TodoResult(todoData, total_records = getTotalRecords())
        return Response.success(todoResult)
    }

    override suspend fun getTodosBySearchQuery(
        page: Int,
        limit: Int,
        author: String,
        searchQueryTag: String
    ): Response<TodoResult> {
        val filteredData = todoData.filter { it.tag == searchQueryTag }
        val todoResult = TodoResult(filteredData, total_records = getTotalRecords())
        return Response.success(todoResult)
    }

    override suspend fun postNewTodo(newTodo: Todo): Response<Todo> {
        todoData.add(newTodo)
        return Response.success(newTodo)
    }

    override suspend fun updateTodoCompletedStatus(todoId: Int, updatedTodo: Todo): Response<Todo> {
        todoData.filter {
            it.id == todoId
        }.map {
            it.is_completed = !it.is_completed
        }
        return Response.success(updatedTodo)
    }
}