package com.aircash.courtreserve.models.model

data class SingleTeam(
    val captainId: Int,
    val captainName: String,
    val id: Int,
    val members: List<Member>,
    val name: String,
    val sport: String
)