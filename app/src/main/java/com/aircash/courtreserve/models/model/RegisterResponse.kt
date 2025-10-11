package com.aircash.courtreserve.models.model

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val registerUserData: RegisterUserData
)