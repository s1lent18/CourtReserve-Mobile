package com.aircash.courtreserve.viewmodels.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aircash.courtreserve.models.interfaces.AddBookingAPI
import com.aircash.courtreserve.models.model.CreateBookingRequest
import com.aircash.courtreserve.models.model.CreateBookingResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val addBookingAPI: AddBookingAPI
) : ViewModel() {

    private val _addBookingResult = MutableStateFlow<CreateBookingResponse?>(null)
    val addBookingResult: StateFlow<CreateBookingResponse?> = _addBookingResult

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun createBooking(request: CreateBookingRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = addBookingAPI.createBooking(request)
                if (response.isSuccessful) {
                    _addBookingResult.value = response.body()
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Error ${response.code()}: ${response.message()}"
                    _addBookingResult.value = null
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.localizedMessage}"
                _addBookingResult.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }


}