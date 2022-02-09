package com.zuperinterviewtest.todo.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zuperinterviewtest.todo.data.TodoRepository
import com.zuperinterviewtest.todo.data.models.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TagViewViewModel @Inject constructor(private val todoRepository: TodoRepository) :
    ViewModel() {

    val allTodos: LiveData<List<Todo>> get() = _allTodos
    private val _allTodos: MutableLiveData<List<Todo>> = MutableLiveData()

    fun getAllTodos() = viewModelScope.launch {
        // call is main-safe, since within repository we are making
        // the request in Dispatchers.IO
        val response = todoRepository.getAllTodos()
        val todoList: MutableList<Todo> = (response.body()?.todo ?: emptyList()) as MutableList<Todo>
        if (todoList.isNotEmpty()) {
            todoList.sortBy {
                it.tag
            }
            Log.d("TESTE","$todoList")

            _allTodos.value = todoList
        }
    }
}