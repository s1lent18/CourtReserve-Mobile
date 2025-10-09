package com.aircash.courtreserve.models.modules

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object UserPreferences {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userPref")

    val USER_ROLE_KEY = stringPreferencesKey("userRole")

    @Singleton
    @Provides
    suspend fun saveUserRole(context: Context, role: String) {
        context.dataStore.edit { pref ->
            pref[USER_ROLE_KEY] = role
        }
    }

    @Singleton
    @Provides
    fun getUserRole(context: Context): Flow<String?> {
        return context.dataStore.data.map { pref ->
            pref[USER_ROLE_KEY]
        }
    }
}