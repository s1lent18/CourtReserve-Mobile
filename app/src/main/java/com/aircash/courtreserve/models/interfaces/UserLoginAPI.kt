package com.aircash.courtreserve.models.interfaces

import com.aircash.courtreserve.models.model.LoginRequest
import com.aircash.courtreserve.models.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserLoginAPI {

    @POST("/user/login")
    suspend fun loginUser(
        @Body loginRequest: LoginRequest
    ) : Response<LoginResponse>
}