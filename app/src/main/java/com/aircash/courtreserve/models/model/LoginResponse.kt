package com.aircash.courtreserve.models.model
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val userData: UserData
)