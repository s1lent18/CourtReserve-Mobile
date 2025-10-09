package com.aircash.courtreserve.models.model

data class RegisterRequest(
    val email: String,
    val name: String,
    val password: String
)