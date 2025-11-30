package com.aircash.courtreserve.models.interfaces.user

import com.aircash.courtreserve.models.model.GetSingleTeamResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GetSingleTeamAPI {

    @GET("/team/getSingleTeam")
    suspend fun getTeam(
        @Query("Id") id: Int,
        @Header("Authorization") token : String,
    ): Response<GetSingleTeamResponse>
}