package com.aircash.courtreserve.models.interfaces


import com.aircash.courtreserve.models.model.GetVendorCourtsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GetAllCourtsAPI {

    @GET("/vendor/getVendorCourts")
    suspend fun getVendorCourts(
        @Query("id") id: Int,
        @Header("Authorization") token : String,
    ): Response<GetVendorCourtsResponse>
}