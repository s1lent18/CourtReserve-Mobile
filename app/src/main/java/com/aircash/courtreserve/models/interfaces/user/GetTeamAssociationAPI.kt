package com.aircash.courtreserve.models.interfaces.user

import com.aircash.courtreserve.models.model.GetTeamAssociationResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GetTeamAssociationAPI {

    @GET("/user/association")
    suspend fun getAssociation(
        @Header("Authorization") token : String,
        @Query("Id") id: Int,
    ) : Response<GetTeamAssociationResponse>
}