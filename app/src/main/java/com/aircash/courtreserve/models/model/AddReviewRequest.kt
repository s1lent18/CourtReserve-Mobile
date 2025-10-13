package com.aircash.courtreserve.models.model

data class AddReviewRequest(
    val facilityId: Int,
    val rating: Int,
    val review: String,
    val userId: Int
)