package com.zuperinterviewtest.todo.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zuperinterviewtest.todo.data.models.Todo
import com.zuperinterviewtest.todo.data.remote.TodoApiHelper
import com.zuperinterviewtest.todo.utils.Constants
import com.zuperinterviewtest.todo.utils.DataStoreManager
import retrofit2.HttpException
import java.io.IOException
import kotlin.math.ceil

class TodoPagingSource(
    private val apiHelper: TodoApiHelper,
    private val dataStoreManager: DataStoreManager
) : PagingSource<Int, Todo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Todo> {
        return try {
            // If null, get the initial page i.e page = 1
            val nextPageNumber = params.key ?: DEFAULT_INIT_PAGE
            val response = apiHelper.getTodos(
                page = nextPageNumber,
                limit = LIMIT,
                author = Constants.AUTHOR
            )
            val totalRecords: Int = response.body()?.total_records ?: 0
            dataStoreManager.updateTotalTodoCount(totalRecords)
            // Compute the next page number. Since limit is 15, calculate the total_records/15 and
            // get the ceil value of the result. For eg: 48/15 = 3.2, so ceil(3.2) = 4 i.e 4 pages
            val totalPages: Int? =
                (response.body()?.total_records)?.div(LIMIT)?.toDouble()?.let { ceil(it) }?.toInt()
            val nextKey =
                totalPages?.let { if (nextPageNumber < totalPages) nextPageNumber + 1 else null }
            LoadResult.Page(
                data = response.body()?.todo ?: emptyList(),
                prevKey = null,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Todo>): Int? {
        return null
    }

    companion object {
        const val DEFAULT_INIT_PAGE = 1
        const val LIMIT = 15
    }
}