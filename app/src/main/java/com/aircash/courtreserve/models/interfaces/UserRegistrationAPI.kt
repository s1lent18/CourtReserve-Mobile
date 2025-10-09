package com.aircash.courtreserve.models.interfaces

import com.aircash.courtreserve.models.model.RegisterRequest
import com.aircash.courtreserve.models.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserRegistrationAPI {

    @POST("/user/register")
    suspend fun registerUser(
        @Body registerRequest: RegisterRequest
    ) : Response<RegisterResponse>
}