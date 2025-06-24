package com.example.dessertrelease.data.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val IS_LINEAR_LAYOUT = booleanPreferencesKey("is_linear_layout")
        const val TAG = "UserPreferencesRepo"
    }

    suspend fun saveLayoutPreferences(isLinearLayout : Boolean) {
        dataStore.edit{ preferences ->
            preferences[IS_LINEAR_LAYOUT] = isLinearLayout
        }
    }
    val isLinearLayout : Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error Reading preferences", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
        preferences[IS_LINEAR_LAYOUT] ?: true // ?and : shouldn't be having any space in between
    }
}