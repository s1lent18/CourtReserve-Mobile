package com.aircash.courtreserve.models.interfaces

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.aircash.courtreserve.models.model.UserData
import kotlinx.coroutines.flow.Flow

val USER_DATA_KEY: Preferences.Key<String> = stringPreferencesKey("userData")
val VENDOR_DATA_KEY: Preferences.Key<String> = stringPreferencesKey("vendorData")
val TIMESTAMP_KEY = stringPreferencesKey("timestamp")

interface UserPref {

    //fun getVendorData(): Flow<TeacherData?>
    fun getUserData(): Flow<UserData?>
    fun getUserRole(): Flow<String>
    fun getTimeStamp(): Flow<String>

    //suspend fun saveVendorData(teacherData: TeacherData)
    suspend fun saveUserData(userData: UserData)
    suspend fun saveUserRole(role: String)
    suspend fun saveTimeStamp(timestamp: String)
}