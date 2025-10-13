package com.aircash.courtreserve.models.model

data class VendorX(
    val created: String,
    val email: String,
    val id: Int,
    val location: String,
    val name: String,
    val password: String,
    val roles: List<RoleX>
)