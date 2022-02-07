package com.zuperinterviewtest.todo.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.zuperinterviewtest.todo.data.remote.TodoApiHelper
import com.zuperinterviewtest.todo.data.models.Todo
import com.zuperinterviewtest.todo.utils.Constants
import retrofit2.HttpException
import java.io.IOException
import kotlin.math.ceil

class TodoPagingSource(private val apiHelper: TodoApiHelper) : PagingSource<Int, Todo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Todo> {
        Log.d("TODOTEST", "Inside load method")
        return try {
            Log.d("TODOTEST", "Inside load method TRY")
            // If null, get the initial page i.e page = 1
            val nextPageNumber = params.key ?: DEFAULT_INIT_PAGE
            val response = apiHelper.getTodos(
                page = nextPageNumber,
                limit = LIMIT,
                author = Constants.SAMPLE_AUTHOR
            )
            Log.d("TODOTEST", "Response: ${response.body()?.todo}")
            // Compute the next page number. Since limit is 15, calculate the total_records/15 and
            // get the ceil value of the result. For eg: 48/15 = 3.2, so ceil(3.2) = 4 i.e 4 pages
            val totalPages: Int? =
                (response.body()?.total_records)?.div(LIMIT)?.toDouble()?.let { ceil(it) }?.toInt()
            Log.d(
                "TODOTEST", "Load Total records: ${response.body()?.total_records} " +
                        "pages: $totalPages"
            )
            val nextKey =
                totalPages?.let { if (nextPageNumber < totalPages) nextPageNumber + 1 else null }
            Log.d("TODOTEST", "Next Key: $nextKey")
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