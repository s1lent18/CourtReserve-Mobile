package com.aircash.courtreserve.models.interfaces.user

import com.aircash.courtreserve.models.model.GetPopularCourtsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GetPopularCourtsAPI {

    @GET("/court/getPopularCourts")
    suspend fun getPopularCourts(
        @Query("location") location: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Header("Authorization") token: String
    ): Response<GetPopularCourtsResponse>
}