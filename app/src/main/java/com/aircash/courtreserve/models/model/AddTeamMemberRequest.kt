package com.aircash.courtreserve.models.model

data class AddTeamMemberRequest(
    val role: String,
    val teamId: Int,
    val userEmail: String
)