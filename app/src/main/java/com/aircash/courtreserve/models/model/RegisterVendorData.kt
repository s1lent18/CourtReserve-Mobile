package com.aircash.courtreserve.models.model
import kotlinx.serialization.Serializable

@Serializable
data class RegisterVendorData(
    val createdAt: String,
    val email: String,
    val id: Int,
    val name: String
)