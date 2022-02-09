package com.zuperinterviewtest.todo.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.zuperinterviewtest.todo.data.TodoRepository
import com.zuperinterviewtest.todo.data.models.Todo
import com.zuperinterviewtest.todo.utils.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repository: TodoRepository, private val dataStoreManager: DataStoreManager
) : ViewModel() {

    val totalTodoCount = dataStoreManager.getTotalTodoCount().asLiveData()
    var totalCount: Int = 0

    val selectedPriority: MutableLiveData<String> = MutableLiveData("LOW")

    /**
     * Function that returns a flow of Paging data [Todo]
     */
    fun getAllTodos(): Flow<PagingData<Todo>> {
        return repository.getTodoResults(dataStoreManager = dataStoreManager)
    }

    /**
     * Function that returns a flow of Paging data [Todo]
     * of a filtered by TAG
     * @param searchTag - The Tag entered by the user
     */
    fun getAllTodosByTag(searchTag: String): Flow<PagingData<Todo>> {
        return repository.getTodoResultsBySearchQuery(searchTag = searchTag)
    }

    fun updateTodoCompletedState(clickedTodo: Todo) = viewModelScope.launch {
        clickedTodo.is_completed = !clickedTodo.is_completed
        repository.updateTodoCompletedStatus(clickedTodo)
    }

    fun postNewTodo(newTodo: Todo) = viewModelScope.launch {
        repository.postNewTodo(newTodo)
    }
}