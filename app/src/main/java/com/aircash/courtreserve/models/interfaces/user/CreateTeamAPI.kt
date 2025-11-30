package com.aircash.courtreserve.models.interfaces.user

import com.aircash.courtreserve.models.model.CreateTeamRequest
import com.aircash.courtreserve.models.model.GetCreateTeamResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface CreateTeamAPI {

    @POST("/team/createTeam")
    suspend fun createTeam(
        @Header("Authorization") token : String,
        @Body createTeamRequest: CreateTeamRequest
    ) : Response<GetCreateTeamResponse>
}