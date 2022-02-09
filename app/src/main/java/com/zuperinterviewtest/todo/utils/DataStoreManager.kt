package com.zuperinterviewtest.todo.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

private const val COUNT_PREFERENCES_NAME = "todo_count_preferences"
private val Context.dataStore by preferencesDataStore(COUNT_PREFERENCES_NAME)

@Singleton
class DataStoreManager constructor(@ApplicationContext appContext: Context) {
    private val dataStore = appContext.dataStore
    private val TOTAL_TODO_COUNT = intPreferencesKey("total_todos_count")

    fun getTotalTodoCount(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[TOTAL_TODO_COUNT] ?: 0
        }
    }

    suspend fun updateTotalTodoCount(totalCount: Int) {
        dataStore.edit { preferences ->
            preferences[TOTAL_TODO_COUNT] = totalCount
        }
    }

}