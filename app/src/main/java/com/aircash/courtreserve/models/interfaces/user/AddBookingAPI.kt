package com.aircash.courtreserve.models.interfaces.user

import com.aircash.courtreserve.models.model.CreateBookingRequest
import com.aircash.courtreserve.models.model.CreateBookingResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AddBookingAPI {

    @POST("/booking/createBooking")
    suspend fun createBooking(
        @Header("Authorization") token : String,
        @Body createBookingRequest : CreateBookingRequest
    ) : Response<CreateBookingResponse>
}