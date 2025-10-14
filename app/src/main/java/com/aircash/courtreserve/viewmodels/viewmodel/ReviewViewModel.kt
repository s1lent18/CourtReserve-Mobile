package com.aircash.courtreserve.viewmodels.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aircash.courtreserve.models.interfaces.AddReviewAPI
import com.aircash.courtreserve.models.model.AddReviewRequest
import com.aircash.courtreserve.models.model.AddReviewResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val addReviewAPI: AddReviewAPI
) : ViewModel() {

    private val _addReviewResult = MutableStateFlow<AddReviewResponse?>(null)
    val addReviewResult: StateFlow<AddReviewResponse?> = _addReviewResult

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun addReview(token: String, request : AddReviewRequest) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = addReviewAPI.createReview(token, request)
                if (response.isSuccessful) {
                    _addReviewResult.value = response.body()
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Error ${response.code()}: ${response.message()}"
                    _addReviewResult.value = null
                }
            } catch (e: Exception) {
                _errorMessage.value = "Network error: ${e.localizedMessage}"
                _addReviewResult.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }
}