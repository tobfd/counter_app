package com.tobfd.counter

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsDataStore(private val context: Context) {
    private val showResetButton = booleanPreferencesKey("show_reset_button")
    private val showAnimations = booleanPreferencesKey("show_animations")

    val showResetButtonFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[showResetButton] ?: true
        }

    val showAnimationsFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[showAnimations] ?: true
        }

    suspend fun updateShowResetButton(show: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[showResetButton] = show
        }
    }

    suspend fun updateShowAnimations(show: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[showAnimations] = show
        }
    }
}
