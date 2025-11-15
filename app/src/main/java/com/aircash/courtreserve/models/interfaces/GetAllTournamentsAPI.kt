package com.aircash.courtreserve.models.interfaces

import com.aircash.courtreserve.models.model.GetAllTournamentsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GetAllTournamentsAPI {

    @GET("/user/getAllTournaments")
    suspend fun getAllTournaments(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("location") location: String,
        @Header("Authorization") token : String,
    ): Response<GetAllTournamentsResponse>
}