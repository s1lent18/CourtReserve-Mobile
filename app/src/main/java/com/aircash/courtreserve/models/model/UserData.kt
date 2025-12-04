package com.aircash.courtreserve.models.model
import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val createdAt: String = "",
    val coverImage: String = "",
    val email: String = "",
    val id: Int = 0,
    val location: String = "",
    val name: String = "",
    val token: String = ""
)