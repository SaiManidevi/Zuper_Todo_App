package com.zuperinterviewtest.todo.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.DifferCallback
import androidx.paging.NullPaddedList
import androidx.paging.PagingData
import androidx.paging.PagingDataDiffer
import com.zuperinterviewtest.todo.MainCoroutineRule
import com.zuperinterviewtest.todo.data.models.Todo
import com.zuperinterviewtest.todo.data.remote.TodoApiHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TodoRepositoryTest {
    // Set up dummy data
    private val testFakeTodo: MutableList<Todo> = mutableListOf(
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

    // ApiHelper - Fake
    private lateinit var fakeApiHelper: TodoApiHelper

    // Class under test
    private lateinit var todoRepository: TodoRepository

    @ExperimentalCoroutinesApi
    @Before
    fun setUpRepo() {
        fakeApiHelper = TodoFakeApiHelperImpl()
        todoRepository = TodoRepository(fakeApiHelper, TestCoroutineDispatcher())
        runBlockingTest {
            todoRepository.postNewTodo(testFakeTodo[0])
            todoRepository.postNewTodo(testFakeTodo[1])
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testGetAllTodos() = runBlocking {
        // When - getting all todos
        val allTodos = todoRepository.getAllTodos()
        // Then - assert that inserted todos are being displayed
        assertEquals(testFakeTodo, allTodos.body()?.todo)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testPostNewTodo() = runBlocking {
        // Given - a new todo_data
        val newTodo = Todo(
            title = "Test todo 3",
            author = "sailee",
            tag = "Tag1",
            is_completed = false,
            priority = "MEDIUM",
            id = 1003
        )
        // When - new todo_data is inserted/posted
        todoRepository.postNewTodo(newTodo)
        // Then - assert that the updated todos must have the newTodo data
        testFakeTodo.add(newTodo)
        val allTodos = todoRepository.getAllTodos()
        assertEquals(testFakeTodo, allTodos.body()?.todo)
    }

    @Test
    fun testUpdateTodoCompletedStatus() = runBlocking {
        // Given - an updated todo_data
        val updatedTodo = Todo(
            title = "Test todo 2",
            author = "sailee",
            tag = "Tag2",
            is_completed = true,
            priority = "HIGH",
            id = 1002
        )
        // When - updated todo_data is provided/inserted
        todoRepository.updateTodoCompletedStatus(updatedTodo)
        testFakeTodo.filter {
            it.id == updatedTodo.id
        }.map {
            it.is_completed = !it.is_completed
        }
        // Then - assert that the update operation is successful
        val allTodos = todoRepository.getAllTodos()
        assertEquals(testFakeTodo, allTodos.body()?.todo)
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