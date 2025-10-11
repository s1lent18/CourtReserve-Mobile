package com.aircash.courtreserve.models.model
import kotlinx.serialization.Serializable

@Serializable
data class VendorData(
    val createdAt: String = "",
    val email: String = "",
    val id: Int = 0,
    val name: String = "",
    val token: String = ""
)