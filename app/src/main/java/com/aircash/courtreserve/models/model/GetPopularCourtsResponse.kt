package com.aircash.courtreserve.models.model

data class GetPopularCourtsResponse(
    val message: String,
    val size: Int,
    val content: List<CourtXXX>,
    val totalElements: Int,
    val page: Int,
    val totalPages: Int
)