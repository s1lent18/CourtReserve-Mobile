package com.aircash.courtreserve.viewmodels.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aircash.courtreserve.models.interfaces.user.CreateTeamAPI
import com.aircash.courtreserve.models.interfaces.user.GetSingleTeamAPI
import com.aircash.courtreserve.models.model.GetSingleTeamResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor(
    private val createTeamAPI: CreateTeamAPI,
    private val getSingleTeamAPI: GetSingleTeamAPI
) : ViewModel() {

    private val _getSingleTeamResult = MutableStateFlow<GetSingleTeamResponse?>(null)
    val getSingleTeamResult : StateFlow<GetSingleTeamResponse?> = _getSingleTeamResult

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getSingleTeam(token : String, id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = getSingleTeamAPI.getTeam(token = token, id = id)
                if (response.isSuccessful) {
                    Log.d("Check", "${response.body()}")
                    _getSingleTeamResult.value = response.body()
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Error ${response.code()}: ${response.message()}"
                    _getSingleTeamResult.value = null
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.localizedMessage}"
                _getSingleTeamResult.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}