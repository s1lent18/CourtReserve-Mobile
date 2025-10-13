package com.aircash.courtreserve.models.interfaces

import com.aircash.courtreserve.models.model.CreateBookingRequest
import com.aircash.courtreserve.models.model.CreateBookingResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AddBookingAPI {

    @POST("/user/createBooking")
    suspend fun createBooking(
        @Body createBookingRequest : CreateBookingRequest
    ) : Response<CreateBookingResponse>
}