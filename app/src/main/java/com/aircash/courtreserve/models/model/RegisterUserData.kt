package com.aircash.courtreserve.models.model
import kotlinx.serialization.Serializable

@Serializable
data class RegisterUserData(
    val createdAt: String,
    val coverImage: String,
    val email: String,
    val id: Int,
    val location: String,
    val name: String
)