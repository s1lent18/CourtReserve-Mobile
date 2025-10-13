package com.aircash.courtreserve.models.model

data class Review(
    val court: CourtXX,
    val created: String,
    val id: Int,
    val rating: Int,
    val review: String,
    val user: UserX
)