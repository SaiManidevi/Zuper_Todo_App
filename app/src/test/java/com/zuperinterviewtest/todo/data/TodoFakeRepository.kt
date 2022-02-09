package com.zuperinterviewtest.todo.data

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zuperinterviewtest.todo.data.models.Todo
import com.zuperinterviewtest.todo.data.models.TodoResult
import com.zuperinterviewtest.todo.utils.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

class TodoFakeRepository : DefaultTodoRepository {
    private val todoDataList: MutableList<Todo> = mutableListOf()

    override suspend fun getAllTodos(): Response<TodoResult> {
        val todoResult = TodoResult(todoDataList, todoDataList.size)
        return Response.success(todoResult)
    }

    override fun getTodoResults(
        pagingConfig: PagingConfig,
        dataStoreManager: DataStoreManager
    ): Flow<PagingData<Todo>> {
        return flowOf(PagingData.from(todoDataList))
    }

    override fun getTodoResultsBySearchQuery(
        pagingConfig: PagingConfig,
        searchTag: String
    ): Flow<PagingData<Todo>> {
        val filteredList = todoDataList.filter {
            it.tag == searchTag
        }
        return flowOf(PagingData.from(filteredList))
    }

    override fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(1)
    }

    override suspend fun postNewTodo(newTodo: Todo): Response<Todo> {
        todoDataList.add(newTodo)
        return Response.success(newTodo)
    }

    override suspend fun updateTodoCompletedStatus(updatedTodo: Todo) {
        todoDataList.filter {
            it.id == updatedTodo.id
        }.map {
            it.is_completed = !it.is_completed
        }
    }
}