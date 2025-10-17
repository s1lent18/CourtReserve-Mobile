package com.aircash.courtreserve.viewmodels.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aircash.courtreserve.models.interfaces.UserPref
import com.aircash.courtreserve.models.model.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class UserTokenViewModel @Inject constructor(
    private val userPref: UserPref
) : ViewModel() {

    private val sessionDurationMillis = TimeUnit.HOURS.toMillis(1)

    private val _session = MutableStateFlow(false)
    val session: StateFlow<Boolean> = _session

    val userData = userPref.getUserData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    val timeStamp = userPref.getTimeStamp().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ""
    )

    init {
        checkSession()
        startAutoLogoutTimer()
    }

    private fun checkSession() {
        viewModelScope.launch {
            val expired = isSessionExpired()
            _session.value = !expired
            if (expired) logout()
        }
    }

    private suspend fun isSessionExpired(): Boolean {
        val loginTimestamp = userPref.getTimeStamp().first().toLongOrNull() ?: return true
        return (System.currentTimeMillis() - loginTimestamp) > sessionDurationMillis
    }

    suspend fun saveUserData(userData: UserData, timeStamp: String) {
        Log.d("UserDataViewModelCheck", "$userData")
        userPref.saveUserData(userData = userData)
        userPref.saveTimeStamp(timestamp = timeStamp)
    }

    fun logout() {
        viewModelScope.launch {
            userPref.saveUserData(UserData())
            userPref.saveTimeStamp("")
        }
    }

    private fun startAutoLogoutTimer() {
        viewModelScope.launch {
            delay(sessionDurationMillis)
            logout()
        }
    }
}