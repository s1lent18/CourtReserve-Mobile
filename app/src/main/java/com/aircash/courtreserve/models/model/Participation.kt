package com.aircash.courtreserve.models.model

data class Participation(
    val registeredAt: String,
    val teamId: Int,
    val teamName: String,
    val tournamentId: Int
)