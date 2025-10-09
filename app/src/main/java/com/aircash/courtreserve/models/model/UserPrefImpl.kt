package com.aircash.courtreserve.models.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.aircash.courtreserve.models.interfaces.TIMESTAMP_KEY
import com.aircash.courtreserve.models.interfaces.USER_DATA_KEY
import com.aircash.courtreserve.models.interfaces.UserPref
import com.aircash.courtreserve.models.modules.UserPreferences.USER_ROLE_KEY
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

class UserPrefImpl (private val dataStore: DataStore<Preferences>) : UserPref {

    override fun getUserData(): Flow<UserData?> {
        return dataStore.data
            .catch {
                emit(emptyPreferences())
            }.map { preferences ->
                preferences[USER_DATA_KEY]?.let {
                    try {
                        Json.decodeFromString<UserData>(it)
                    } catch (_: Exception) {
                        null
                    }
                }
            }
    }

//    override fun getVendorData(): Flow<VendorData?> {
//        return dataStore.data
//            .catch {
//                emit(emptyPreferences())
//            }.map { preferences ->
//                preferences[VENDOR_DATA_KEY]?.let {
//                    try {
//                        Json.decodeFromString<VendorData>(it)
//                    } catch (_: Exception) {
//                        null
//                    }
//                }
//            }
//    }

    override fun getTimeStamp(): Flow<String> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map {
            it[TIMESTAMP_KEY]?: ""
        }
    }

    override fun getUserRole(): Flow<String> {
        return dataStore.data.catch {
            emit(emptyPreferences())
        }.map {
            it[USER_ROLE_KEY]?: ""
        }
    }

    override suspend fun saveUserData(userData: UserData) {
        val jsonString = Json.encodeToString(userData)
        dataStore.edit { preferences ->
            preferences[USER_DATA_KEY] = jsonString
        }
    }

//    override suspend fun saveVendorData(vendorData : VendorData) {
//        val jsonString = Json.encodeToString(vendorData)
//        dataStore.edit { preferences ->
//            preferences[VENDOR_DATA_KEY] = jsonString
//        }
//    }

    override suspend fun saveTimeStamp(timestamp: String) {
        dataStore.edit {
            it[TIMESTAMP_KEY] = timestamp
        }
    }

    override suspend fun saveUserRole(role: String) {
        dataStore.edit {
            it[USER_ROLE_KEY] = role
        }
    }
}