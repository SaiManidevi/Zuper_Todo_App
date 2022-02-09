package com.zuperinterviewtest.todo.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zuperinterviewtest.todo.data.models.Todo
import com.zuperinterviewtest.todo.data.models.TodoResult
import com.zuperinterviewtest.todo.data.remote.TodoApiHelper
import com.zuperinterviewtest.todo.utils.Constants.AUTHOR
import com.zuperinterviewtest.todo.utils.DataStoreManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(
    private val todoApiHelper: TodoApiHelper,
    private val dispatcher: CoroutineDispatcher
) : DefaultTodoRepository {
    override suspend fun getAllTodos(): Response<TodoResult> = withContext(Dispatchers.IO) {
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
    override fun getTodoResults(
        pagingConfig: PagingConfig, dataStoreManager: DataStoreManager
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

    override fun getTodoResultsBySearchQuery(
        pagingConfig: PagingConfig,
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

    override fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(
            pageSize = DEFAULT_PAGE_SIZE,
            enablePlaceholders = false
        )
    }

    override suspend fun postNewTodo(newTodo: Todo): Response<Todo> {
        return withContext(dispatcher) {
            todoApiHelper.postNewTodo(newTodo = newTodo)
        }
    }

    override suspend fun updateTodoCompletedStatus(updatedTodo: Todo) {
        withContext(dispatcher) {
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