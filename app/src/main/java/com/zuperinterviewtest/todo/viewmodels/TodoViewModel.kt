package com.zuperinterviewtest.todo.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.zuperinterviewtest.todo.data.TodoRepository
import com.zuperinterviewtest.todo.data.models.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(private val repository: TodoRepository) : ViewModel() {
    fun getAllTodos(): Flow<PagingData<Todo>> {
        Log.d("TODOTEST", "Inside getAllTodos - Viewmodel")
        return repository.getTodoResults()
    }

    val testValue: MutableLiveData<List<Todo>> = MutableLiveData()

    fun test() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.test()
            response.body()?.todo.let {
                testValue.postValue(it)
            }
        }
    }
}