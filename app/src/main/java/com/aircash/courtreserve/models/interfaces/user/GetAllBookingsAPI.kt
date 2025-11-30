package com.aircash.courtreserve.models.interfaces.user

import com.aircash.courtreserve.models.model.GetAllBookingsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GetAllBookingsAPI {

    @GET("/booking/getAllBookings")
    suspend fun getAllBookings(
        @Query("id") id: Int,
        @Header("Authorization") token : String,
    ): Response<GetAllBookingsResponse>
}