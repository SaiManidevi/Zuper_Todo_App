package com.zuperinterviewtest.todo.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zuperinterviewtest.todo.data.remote.TodoApiHelper
import com.zuperinterviewtest.todo.data.models.Todo
import com.zuperinterviewtest.todo.data.models.TodoResult
import com.zuperinterviewtest.todo.utils.Constants
import com.zuperinterviewtest.todo.utils.Constants.AUTHOR
import com.zuperinterviewtest.todo.utils.DataStoreManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(
    private val todoApiHelper: TodoApiHelper
) {
    suspend fun getAllTodos(): Response<TodoResult> = withContext(Dispatchers.IO) {
        todoApiHelper.getTodos(
            page = INITIAL_PAGE,
            limit = LIMIT_TAG_VIEW,
            author = AUTHOR
        )
    }

    /**
     * Function that returns a flow encapsulated in PagingData [Todo]
     * @param pagingConfig - PagingConfiguration given below in this class
     */
    fun getTodoResults(
        pagingConfig: PagingConfig = getDefaultPageConfig(), dataStoreManager: DataStoreManager
    ): Flow<PagingData<Todo>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                TodoPagingSource(
                    apiHelper = todoApiHelper,
                    dataStoreManager = dataStoreManager
                )
            }
        ).flow
    }

    fun getTodoResultsBySearchQuery(
        pagingConfig: PagingConfig = getDefaultPageConfig(),
        searchTag: String
    ): Flow<PagingData<Todo>> {
        return Pager(config = pagingConfig,
            pagingSourceFactory = {
                TodoSearchQueryPagingSource(
                    apiHelper = todoApiHelper,
                    searchQuery = searchTag
                )
            }
        ).flow
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(
            pageSize = DEFAULT_PAGE_SIZE,
            enablePlaceholders = false
        )
    }

    suspend fun postNewTodo(newTodo: Todo): Response<Todo> {
        return withContext(Dispatchers.IO) {
            todoApiHelper.postNewTodo(newTodo = newTodo)
        }
    }

    suspend fun updateTodoCompletedStatus(updatedTodo: Todo) {
        withContext(Dispatchers.IO) {
            todoApiHelper.updateTodoCompletedStatus(
                todoId = updatedTodo.id,
                updatedTodo = updatedTodo
            )
        }
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 1
        const val INITIAL_PAGE = 1
        const val LIMIT_TAG_VIEW = 1000
    }
}