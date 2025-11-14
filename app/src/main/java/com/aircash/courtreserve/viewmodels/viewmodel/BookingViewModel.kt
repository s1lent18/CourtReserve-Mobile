package com.aircash.courtreserve.viewmodels.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aircash.courtreserve.models.interfaces.AddBookingAPI
import com.aircash.courtreserve.models.interfaces.GetAllBookingsAPI
import com.aircash.courtreserve.models.model.CreateBookingRequest
import com.aircash.courtreserve.models.model.CreateBookingResponse
import com.aircash.courtreserve.models.model.GetAllBookingsResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val addBookingAPI: AddBookingAPI,
    private val getAllBookingsAPI: GetAllBookingsAPI
) : ViewModel() {

    private val _addBookingResult = MutableStateFlow<CreateBookingResponse?>(null)
    val addBookingResult: StateFlow<CreateBookingResponse?> = _addBookingResult

    private val _getAllBookingsResult = MutableStateFlow<GetAllBookingsResponse?>(null)
    val getAllBookingsResult : StateFlow<GetAllBookingsResponse?> = _getAllBookingsResult

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun createBooking(token: String, request: CreateBookingRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = addBookingAPI.createBooking(token,request)
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

    fun getAllBookings(token : String, id : Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = getAllBookingsAPI.getAllBookings(id , token)
                if (response.isSuccessful) {
                    Log.d("Check", "${response.body()}")
                    _getAllBookingsResult.value = response.body()
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Error ${response.code()}: ${response.message()}"
                    _getAllBookingsResult.value = null
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.localizedMessage}"
                _getAllBookingsResult.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}