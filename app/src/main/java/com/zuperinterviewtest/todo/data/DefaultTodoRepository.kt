package com.zuperinterviewtest.todo.data

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zuperinterviewtest.todo.data.models.Todo
import com.zuperinterviewtest.todo.data.models.TodoResult
import com.zuperinterviewtest.todo.utils.DataStoreManager
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface DefaultTodoRepository {
    suspend fun getAllTodos(): Response<TodoResult>

    /**
     * Function that returns a flow encapsulated in PagingData [Todo_Data]
     * @param pagingConfig - PagingConfiguration given below in this class
     */
    fun getTodoResults(
        pagingConfig: PagingConfig = getDefaultPageConfig(), dataStoreManager: DataStoreManager
    ): Flow<PagingData<Todo>>

    fun getTodoResultsBySearchQuery(
        pagingConfig: PagingConfig = getDefaultPageConfig(),
        searchTag: String
    ): Flow<PagingData<Todo>>

    fun getDefaultPageConfig(): PagingConfig

    suspend fun postNewTodo(newTodo: Todo): Response<Todo>

    suspend fun updateTodoCompletedStatus(updatedTodo: Todo)
}