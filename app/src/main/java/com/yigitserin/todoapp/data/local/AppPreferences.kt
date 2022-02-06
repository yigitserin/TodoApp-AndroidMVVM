package com.yigitserin.todoapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "settings")

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext val context: Context,
) {

    companion object{
        val LOGIN_TOKEN_KEY = stringPreferencesKey("LOGIN_TOKEN_KEY")
    }

    suspend fun isLoggedIn(): Boolean{
        return context.dataStore.data.map { preferences ->
            preferences[LOGIN_TOKEN_KEY] != null
        }.first()
    }

    suspend fun setLoginToken(token: String){
        context.dataStore.edit { preferences ->
            preferences[LOGIN_TOKEN_KEY] = token
        }
    }
}