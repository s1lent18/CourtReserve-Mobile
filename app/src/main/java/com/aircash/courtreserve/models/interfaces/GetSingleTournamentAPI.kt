package com.aircash.courtreserve.models.interfaces

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import com.aircash.courtreserve.models.model.GetSingleTournamentResponse


interface GetSingleTournamentAPI {

    @GET("/user/getSingleTournament")
    suspend fun getTournament(
        @Query("Id") id: Int,
        @Header("Authorization") token : String,
    ): Response<GetSingleTournamentResponse>
}