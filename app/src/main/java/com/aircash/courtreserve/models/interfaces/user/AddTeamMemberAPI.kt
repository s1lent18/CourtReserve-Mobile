package com.aircash.courtreserve.models.interfaces.user

import com.aircash.courtreserve.models.model.AddTeamMemberRequest
import com.aircash.courtreserve.models.model.AddTeamMemberResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AddTeamMemberAPI {

    @POST("/team/addMember")
    suspend fun addTeamMember(
        @Header("Authorization") token : String,
        @Body addTeamMemberRequest: AddTeamMemberRequest
    ) : Response<AddTeamMemberResponse>
}