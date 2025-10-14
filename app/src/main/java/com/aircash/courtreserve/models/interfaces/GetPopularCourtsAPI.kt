package com.aircash.courtreserve.models.interfaces

import com.aircash.courtreserve.models.model.GetPopularCourtsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface GetPopularCourtsAPI {

    @GET("/user/getPopularCourts")
    suspend fun getPopularCourts(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("location") location: String,
        @Header("Authorization") token : String,
    ): Response<GetPopularCourtsResponse>
}