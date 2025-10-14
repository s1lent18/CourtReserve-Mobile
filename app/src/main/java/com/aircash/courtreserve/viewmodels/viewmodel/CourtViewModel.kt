package com.aircash.courtreserve.viewmodels.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aircash.courtreserve.models.interfaces.AddCourtAPI
import com.aircash.courtreserve.models.interfaces.GetPopularCourtsAPI
import com.aircash.courtreserve.models.model.AddCourtRequest
import com.aircash.courtreserve.models.model.AddCourtResponse
import com.aircash.courtreserve.models.model.GetPopularCourtsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourtViewModel @Inject constructor(
    private val addCourtAPI: AddCourtAPI,
    private val getPopularCourtsAPI: GetPopularCourtsAPI
) : ViewModel() {

    private val _addCourtResult = MutableStateFlow<AddCourtResponse?>(null)
    val addCourtResult: StateFlow<AddCourtResponse?> = _addCourtResult

    private val _getPopularCourtsResult = MutableStateFlow<GetPopularCourtsResponse?>(null)
    val getPopularCourtsResult : StateFlow<GetPopularCourtsResponse?> = _getPopularCourtsResult

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun addCourt(vendorId : Long, request: AddCourtRequest, token : String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = addCourtAPI.addCourt(vendorId = vendorId, token, request)
                if (response.isSuccessful) {
                    _addCourtResult.value = response.body()
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Error ${response.code()}: ${response.message()}"
                    _addCourtResult.value = null
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.localizedMessage}"
                _addCourtResult.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getPopularCourts(token : String, location : String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = getPopularCourtsAPI.getPopularCourts(token = token, location = location)
                if (response.isSuccessful) {
                    _getPopularCourtsResult.value = response.body()
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Error ${response.code()}: ${response.message()}"
                    _getPopularCourtsResult.value = null
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.localizedMessage}"
                _getPopularCourtsResult.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}