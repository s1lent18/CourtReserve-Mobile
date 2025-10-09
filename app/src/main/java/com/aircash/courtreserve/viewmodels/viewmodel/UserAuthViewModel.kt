package com.aircash.courtreserve.viewmodels.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aircash.courtreserve.models.interfaces.UserLoginAPI
import com.aircash.courtreserve.models.interfaces.UserRegistrationAPI
import com.aircash.courtreserve.models.model.LoginRequest
import com.aircash.courtreserve.models.model.LoginResponse
import com.aircash.courtreserve.models.model.RegisterRequest
import com.aircash.courtreserve.models.model.RegisterResponse
import com.aircash.courtreserve.models.network.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserAuthViewModel @Inject constructor(
    private val userLoginAPI: UserLoginAPI,
    private val userRegistrationAPI: UserRegistrationAPI
) : ViewModel() {

    private val _loginResult = MutableStateFlow<NetworkResponse<LoginResponse>?>(null)
    val loginResult: StateFlow<NetworkResponse<LoginResponse>?> = _loginResult

    private val _registerResult = MutableStateFlow<NetworkResponse< RegisterResponse>?>(null)
    val registerResult: StateFlow<NetworkResponse<RegisterResponse>?> = _registerResult

    fun userLogin(loginRequest: LoginRequest) {

        _loginResult.value = NetworkResponse.Loading

        viewModelScope.launch {
            try {
                val response = userLoginAPI.loginUser(loginRequest)
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

    fun userRegister(registerRequest: RegisterRequest) {

        _registerResult.value = NetworkResponse.Loading

        viewModelScope.launch {
            try {
                val response = userRegistrationAPI.registerUser(registerRequest)
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