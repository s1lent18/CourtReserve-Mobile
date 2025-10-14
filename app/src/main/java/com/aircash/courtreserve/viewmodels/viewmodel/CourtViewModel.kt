package com.aircash.courtreserve.viewmodels.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aircash.courtreserve.models.interfaces.AddCourtAPI
import com.aircash.courtreserve.models.model.AddCourtRequest
import com.aircash.courtreserve.models.model.AddCourtResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourtViewModel @Inject constructor(
    private val addCourtAPI: AddCourtAPI
) : ViewModel() {

    private val _addCourtResult = MutableStateFlow<AddCourtResponse?>(null)
    val addCourtResult: StateFlow<AddCourtResponse?> = _addCourtResult

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun addCourt(vendorId : Long, request: AddCourtRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = addCourtAPI.addCourt(vendorId = vendorId, request)
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
}