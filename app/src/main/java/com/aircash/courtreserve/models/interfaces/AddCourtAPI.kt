package com.aircash.courtreserve.models.interfaces

import com.aircash.courtreserve.models.model.AddCourtRequest
import com.aircash.courtreserve.models.model.AddCourtResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AddCourtAPI {

    @POST("/vendor/{id}/addCourt")
    suspend fun addCourt(
        @Path(value = "id") vendorId : Long,
        @Header("Authorization") token : String,
        @Body addCourtRequest: AddCourtRequest
    ) : Response<AddCourtResponse>
}