package com.aircash.courtreserve.models.model

data class GetPopularCourtsResponse(
    val courts: List<CourtXXX>,
    val currentPage: Int,
    val totalItems: Int,
    val totalPages: Int
)