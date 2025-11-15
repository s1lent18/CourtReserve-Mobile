package com.aircash.courtreserve.models.model

data class GetAllTournamentsResponse(
    val content: List<Content>,
    val message: String,
    val page: Int,
    val size: Int,
    val totalElements: Int,
    val totalPages: Int
)