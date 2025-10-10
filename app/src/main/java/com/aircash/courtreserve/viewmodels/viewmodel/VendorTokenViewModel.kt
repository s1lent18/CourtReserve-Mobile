package com.aircash.courtreserve.viewmodels.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aircash.courtreserve.models.interfaces.UserPref
import com.aircash.courtreserve.models.model.VendorData
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
class VendorTokenViewModel @Inject constructor(
    private val userPref: UserPref
) : ViewModel() {

    private val sessionDurationMillis = TimeUnit.HOURS.toMillis(1)

    private val _session = MutableStateFlow(false)
    val session: StateFlow<Boolean> = _session

    val vendorData = userPref.getVendorData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null
    )

    val timeStamp = userPref.getTimeStamp().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
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

    fun saveTimeStamp(timeStamp: String) {
        viewModelScope.launch {
            userPref.saveTimeStamp(timestamp = timeStamp)
        }
    }

    fun saveVendorData(vendorData: VendorData) {
        viewModelScope.launch {
            userPref.saveVendorData(vendorData = vendorData)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userPref.saveVendorData(VendorData())
            userPref.saveTimeStamp("")
        }
    }

    fun saveVendorData(vendorData: VendorData, timeStamp: String) {
        saveVendorData(vendorData = vendorData)
        saveTimeStamp(timeStamp)
    }

    private fun startAutoLogoutTimer() {
        viewModelScope.launch {
            delay(sessionDurationMillis)
            logout()
        }
    }
}