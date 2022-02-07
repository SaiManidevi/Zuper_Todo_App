package com.zuperinterviewtest.todo.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.zuperinterviewtest.todo.data.remote.TodoApiHelper
import com.zuperinterviewtest.todo.data.models.Todo
import com.zuperinterviewtest.todo.data.models.TodoResult
import com.zuperinterviewtest.todo.utils.Constants
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(private val todoApiHelper: TodoApiHelper) {

    fun getTodoResults(pagingConfig: PagingConfig = getDefaultPageConfig()): Flow<PagingData<Todo>> {
        Log.d("TODOTEST", "Inside getAllTodos - Repo")
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { TodoPagingSource(apiHelper = todoApiHelper) }
        ).flow
    }

    suspend fun test(): Response<TodoResult> {
        return todoApiHelper.getTodos(
            page = 1,
            limit = TodoPagingSource.LIMIT,
            author = Constants.SAMPLE_AUTHOR
        )
    }

    private fun getDefaultPageConfig(): PagingConfig {
        return PagingConfig(
            pageSize = DEFAULT_PAGE_SIZE,
            enablePlaceholders = false
        )
    }

    companion object {
        const val DEFAULT_PAGE_SIZE = 2
    }
}