package com.aircash.courtreserve.models.model
import kotlinx.serialization.Serializable

@Serializable
data class VendorLoginRequest(
    val email: String,
    val password: String
)