package com.aircash.courtreserve.models.interfaces

import com.aircash.courtreserve.models.model.GetVendorSingleCourtResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GetVendorSingleCourtAPI {

    @GET("/vendor/getSingleCourt")
    suspend fun getVendorCourt(
        @Query("id") id: Int,
        @Header("Authorization") token : String,
    ): Response<GetVendorSingleCourtResponse>
}