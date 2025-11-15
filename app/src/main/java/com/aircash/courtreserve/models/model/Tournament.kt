package com.aircash.courtreserve.models.model

data class Tournament(
    val courtId: Int,
    val created: String,
    val endDate: String,
    val id: Int,
    val name: String,
    val organizerId: Int,
    val prize: Int,
    val sport: String,
    val startDate: String,
    val status: String
)