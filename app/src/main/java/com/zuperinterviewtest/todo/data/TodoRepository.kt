package com.zuperinterviewtest.todo.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zuperinterviewtest.todo.data.remote.TodoApiHelper
import com.zuperinterviewtest.todo.data.models.Todo
import com.zuperinterviewtest.todo.data.models.TodoResult
import com.zuperinterviewtest.todo.data.remote.TodoSearchQueryPagingSource
import com.zuperinterviewtest.todo.utils.Constants
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(private val todoApiHelper: TodoApiHelper) {
    /**
     * Function that returns a flow encapsulated in PagingData [Todo]
     * @param pagingConfig - PagingConfiguration given below in this class
     */
    fun getTodoResults(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<Todo>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { TodoPagingSource(apiHelper = todoApiHelper) }
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

    companion object {
        const val DEFAULT_PAGE_SIZE = 1
    }
}