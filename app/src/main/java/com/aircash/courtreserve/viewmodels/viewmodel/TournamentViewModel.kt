package com.aircash.courtreserve.viewmodels.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aircash.courtreserve.models.interfaces.GetAllTournamentsAPI
import com.aircash.courtreserve.models.model.GetAllTournamentsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TournamentViewModel @Inject constructor(
    private val getAllTournamentsAPI: GetAllTournamentsAPI
) : ViewModel() {

    private val _getAllTournamentsResult = MutableStateFlow<GetAllTournamentsResponse?>(null)
    val getAllTournamentsResult : StateFlow<GetAllTournamentsResponse?> = _getAllTournamentsResult

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getAllTournaments(token : String, location : String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = getAllTournamentsAPI.getAllTournaments(token = token, location = location)
                if (response.isSuccessful) {
                    Log.d("Check", "${response.body()}")
                    _getAllTournamentsResult.value = response.body()
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Error ${response.code()}: ${response.message()}"
                    _getAllTournamentsResult.value = null
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.localizedMessage}"
                _getAllTournamentsResult.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}