package com.aircash.courtreserve.models.model
import kotlinx.serialization.Serializable

@Serializable
data class VendorRegisterRequest(
    val email: String,
    val name: String,
    val password: String
)