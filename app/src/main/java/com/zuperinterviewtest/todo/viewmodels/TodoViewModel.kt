package com.zuperinterviewtest.todo.viewmodels

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import com.zuperinterviewtest.todo.data.TodoRepository
import com.zuperinterviewtest.todo.data.models.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(private val repository: TodoRepository) : ViewModel() {

    /**
     * Function that returns a flow of Paging data [Todo]
     */
    fun getAllTodos(): Flow<PagingData<Todo>> {
        return repository.getTodoResults()
    }

    /**
     * Function that returns a flow of Paging data [Todo]
     * of a filtered by TAG
     * @param searchTag - The Tag entered by the user
     */
    fun getAllTodosByTag(searchTag: String): Flow<PagingData<Todo>> {
        return repository.getTodoResultsBySearchQuery(searchTag = searchTag)
    }
}