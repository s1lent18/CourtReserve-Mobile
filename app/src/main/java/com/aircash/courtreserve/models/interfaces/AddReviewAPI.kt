package com.aircash.courtreserve.models.interfaces

import com.aircash.courtreserve.models.model.AddReviewRequest
import com.aircash.courtreserve.models.model.AddReviewResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AddReviewAPI {

    @POST("/user/createReview")
    suspend fun createReview(
        @Header("Authorization") token : String,
        @Body addReviewRequest: AddReviewRequest
    ) : Response<AddReviewResponse>
}