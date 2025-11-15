package com.aircash.courtreserve.models.model

data class CreateTournamentRequest(
    val courtId: Int,
    val endDate: String,
    val name: String,
    val prize: Int,
    val startDate: String
)