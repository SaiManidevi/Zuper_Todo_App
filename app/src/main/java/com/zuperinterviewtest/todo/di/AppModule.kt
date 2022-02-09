package com.zuperinterviewtest.todo.di

import com.zuperinterviewtest.todo.data.DefaultTodoRepository
import com.zuperinterviewtest.todo.data.TodoRepository
import com.zuperinterviewtest.todo.data.remote.TodoApiHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {
    @Singleton
    @Provides
    fun provideDispatchers(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun provideRepository(
        apiHelper: TodoApiHelper,
        dispatcher: CoroutineDispatcher
    ): DefaultTodoRepository = TodoRepository(todoApiHelper = apiHelper, dispatcher = dispatcher)
}