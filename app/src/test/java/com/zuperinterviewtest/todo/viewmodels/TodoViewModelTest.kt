package com.zuperinterviewtest.todo.viewmodels

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.asLiveData
import androidx.paging.DifferCallback
import androidx.paging.NullPaddedList
import androidx.paging.PagingData
import androidx.paging.PagingDataDiffer
import androidx.test.core.app.ApplicationProvider
import com.zuperinterviewtest.todo.MainCoroutineRule
import com.zuperinterviewtest.todo.data.DefaultTodoRepository
import com.zuperinterviewtest.todo.data.TodoFakeRepository
import com.zuperinterviewtest.todo.data.models.Todo
import com.zuperinterviewtest.todo.getOrAwaitValue
import com.zuperinterviewtest.todo.utils.DataStoreManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TodoViewModelTest {
    // Set up dummy data
    private val testFakeTodoData: MutableList<Todo> = mutableListOf(
        Todo(
            title = "Test todo 1",
            author = "sailee",
            tag = "Tag1",
            is_completed = false,
            priority = "LOW",
            id = 1001
        ),
        Todo(
            title = "Test todo 2",
            author = "sailee",
            tag = "Tag2",
            is_completed = false,
            priority = "HIGH",
            id = 1002
        )
    )

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Repository
    private lateinit var fakeRepository: DefaultTodoRepository

    // Class under test - TodoViewModel
    private lateinit var todoViewModel: TodoViewModel

    @ExperimentalCoroutinesApi
    @Before
    fun setUpRepo() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        fakeRepository = TodoFakeRepository()
        todoViewModel = TodoViewModel(fakeRepository, DataStoreManager(context))
        runBlockingTest {
            fakeRepository.postNewTodo(testFakeTodoData[0])
            fakeRepository.postNewTodo(testFakeTodoData[1])
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetAllTodos() = runBlockingTest {
        // Given
        val initialData = flowOf(PagingData.from(testFakeTodoData))
        // When
        val allTodosFromViewModel = todoViewModel.getAllTodos()
        // Then
        val inputTodoLiveData = initialData.asLiveData().getOrAwaitValue()
        val resultTodoLiveData = allTodosFromViewModel.asLiveData().getOrAwaitValue()
        assertEquals(inputTodoLiveData.collectData(), resultTodoLiveData.collectData())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetAllTodosByTag() = runBlockingTest {
        // Given
        val searchTag = "Tag2"
        val filteredList = testFakeTodoData.filter { it.tag == searchTag }
        val initialData = flowOf(PagingData.from(filteredList))
        // When
        val allTodosByTag = todoViewModel.getAllTodosByTag(searchTag)
        // Then
        val inputTodoByTag = initialData.asLiveData().getOrAwaitValue()
        val resultTodoByTag = allTodosByTag.asLiveData().getOrAwaitValue()
        assertEquals(inputTodoByTag.collectData(), resultTodoByTag.collectData())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testUpdateTodoCompletedState() = runBlockingTest {
        // Goal is to change the completed status of todo_data at index 0
        // Given
        // 1. A copy of the input fake data
        // 2. Change the completed status of todo_data at index 0 in the COPY
        val testInputDataCopy = testFakeTodoData
        val todoIndexToUpdate = 0
        testInputDataCopy[todoIndexToUpdate].is_completed =
            !testInputDataCopy[todoIndexToUpdate].is_completed
        // When - Changing completed status of todo_data at index 0 via viewmodel
        val todoToChange: Todo = testFakeTodoData[todoIndexToUpdate]
        todoViewModel.updateTodoCompletedState(todoToChange)
        // Then - Ensure that the change is properly updated
        val allTodosFromViewModel = todoViewModel.getAllTodos()
        val resultUpdatedTodo = allTodosFromViewModel.asLiveData().getOrAwaitValue().collectData()
        assertEquals(
            testInputDataCopy[todoIndexToUpdate].is_completed,
            resultUpdatedTodo[todoIndexToUpdate].is_completed
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testPostNewTodo() = runBlockingTest {
        // Given - a new todo_data to be added
        val newTodo = Todo(
            title = "Test todo 3",
            author = "sailee",
            tag = "Tag1",
            is_completed = true,
            priority = "MEDIUM",
            id = 1003
        )
        testFakeTodoData.add(newTodo)
        // When - new todo_data is posted
        todoViewModel.postNewTodo(newTodo)
        // Then - assert that the new todo_data is added
        val resultUpdatedTodo =
            todoViewModel.getAllTodos().asLiveData().getOrAwaitValue().collectData()
        assertEquals(testFakeTodoData, resultUpdatedTodo)
    }

    /**
     * Helper extension function to use for unit test for getting PagingData's value
     * Ref: [https://stackoverflow.com/questions/63522656/what-is-the-correct-way-to-check-the-data-from-a-pagingdata-object-in-android-un]
     */
    @ExperimentalCoroutinesApi
    suspend fun <T : Any> PagingData<T>.collectData(): List<T> {
        val items = mutableListOf<T>()
        val dif = object : PagingDataDiffer<T>(dcb, TestCoroutineDispatcher()) {

            override suspend fun presentNewList(
                previousList: NullPaddedList<T>,
                newList: NullPaddedList<T>,
                lastAccessedIndex: Int,
                onListPresentable: () -> Unit
            ): Int? {
                for (idx in 0 until newList.size)
                    items.add(newList.getFromStorage(idx))
                onListPresentable()
                return null
            }
        }
        dif.collectFrom(this)
        return items
    }

    private val dcb = object : DifferCallback {
        override fun onChanged(position: Int, count: Int) {}
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
    }
}