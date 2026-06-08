package com.example.routesapp.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class AuthStore(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val AUTH_TOKEN = stringPreferencesKey("auth_token")
        val AUTH_USER = stringPreferencesKey("auth_user")
    }

    val tokenFlow: Flow<String?> = dataStore.data.map { it[AUTH_TOKEN] }
    val userFlow: Flow<String?> = dataStore.data.map { it[AUTH_USER] }

    suspend fun save(token: String, user: String) {
        dataStore.edit {
            it[AUTH_TOKEN] = token
            it[AUTH_USER] = user
        }
    }

    suspend fun clear() {
        dataStore.edit {
            it.remove(AUTH_TOKEN)
            it.remove(AUTH_USER)
        }
    }
}
