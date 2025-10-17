package com.aircash.courtreserve.models.interfaces

import com.aircash.courtreserve.models.model.SingleCourtResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GetCourtAPI {

    @GET("/user/getCourt")
    suspend fun getCourt(
        @Query("id") id: Int,
        @Header("Authorization") token : String,
    ): Response<SingleCourtResponse>
}