package com.aircash.courtreserve.models.model

data class Member(
    val coverImage: String,
    val id: Int,
    val name: String,
    val role: String,
    val teamId: TeamId
)