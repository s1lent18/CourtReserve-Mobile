package com.aircash.courtreserve.viewmodels.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aircash.courtreserve.models.interfaces.VendorLoginAPI
import com.aircash.courtreserve.models.interfaces.VendorRegistrationAPI
import com.aircash.courtreserve.models.model.VendorLoginRequest
import com.aircash.courtreserve.models.model.VendorLoginResponse
import com.aircash.courtreserve.models.model.VendorRegisterRequest
import com.aircash.courtreserve.models.model.VendorRegisterResponse
import com.aircash.courtreserve.models.network.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VendorAuthViewModel @Inject constructor(
    private val vendorLoginAPI: VendorLoginAPI,
    private val vendorRegistrationAPI: VendorRegistrationAPI
) : ViewModel() {

    private val _loginResult = MutableStateFlow<NetworkResponse<VendorLoginResponse>?>(null)
    val loginResult: StateFlow<NetworkResponse<VendorLoginResponse>?> = _loginResult

    private val _registerResult = MutableStateFlow<NetworkResponse<VendorRegisterResponse>?>(null)
    val registerResult: StateFlow<NetworkResponse<VendorRegisterResponse>?> = _registerResult

    fun vendorLogin(vendorLoginRequest: VendorLoginRequest) {

        _loginResult.value = NetworkResponse.Loading

        viewModelScope.launch {
            try {
                val response = vendorLoginAPI.loginVendor(vendorLoginRequest)
                if (response.isSuccessful && response.code() == 200) {
                    response.body()?.let {
                        _loginResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _loginResult.value = NetworkResponse.Failure("Wrong Username / Password")
                }
            } catch (e: Exception) {
                _loginResult.value = NetworkResponse.Failure("$e")
            }
        }
    }

    fun vendorRegister(vendorRegisterRequest: VendorRegisterRequest) {

        _registerResult.value = NetworkResponse.Loading

        viewModelScope.launch {
            try {
                val response = vendorRegistrationAPI.registerVendor(vendorRegisterRequest)
                if (response.isSuccessful && response.code() == 200) {
                    response.body()?.let {
                        _registerResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _registerResult.value = NetworkResponse.Failure("Wrong Username / Password")
                }
            } catch (e: Exception) {
                _registerResult.value = NetworkResponse.Failure("$e")
            }
        }
    }
}