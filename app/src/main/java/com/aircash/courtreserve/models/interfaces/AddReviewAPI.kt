package com.aircash.courtreserve.models.interfaces

import com.aircash.courtreserve.models.model.AddReviewRequest
import com.aircash.courtreserve.models.model.AddReviewResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AddReviewAPI {

    @POST("/user/createReview")
    suspend fun createReview(
        @Body addReviewRequest: AddReviewRequest
    ) : Response<AddReviewResponse>
}