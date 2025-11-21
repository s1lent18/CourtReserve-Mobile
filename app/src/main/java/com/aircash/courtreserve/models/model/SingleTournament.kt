package com.aircash.courtreserve.models.model

data class SingleTournament(
    val courtId: Int,
    val courtName: String,
    val endDate: String,
    val id: Int,
    val name: String,
    val prize: Int,
    val sport: String,
    val startDate: String,
    val status: String,
    val tournamentTeams: List<TournamentTeam>,
    val userId: Int,
    val userName: String
)