package com.zuperinterviewtest.todo.di

import android.app.Application
import android.content.Context
import com.zuperinterviewtest.todo.data.remote.TodoApiHelper
import com.zuperinterviewtest.todo.data.remote.TodoApiHelperImpl
import com.zuperinterviewtest.todo.data.remote.TodoApiService
import com.zuperinterviewtest.todo.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideTodoRetrofitInstance(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.TODO_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideTodoApiService(retrofit: Retrofit): TodoApiService =
        retrofit.create(TodoApiService::class.java)

    @Singleton
    @Provides
    fun provideTodoApiHelper(todoApiHelperImpl: TodoApiHelperImpl): TodoApiHelper =
        todoApiHelperImpl
}