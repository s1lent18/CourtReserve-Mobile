package com.aircash.courtreserve.models.model

data class VendorRegisterRequest(
    val email: String,
    val name: String,
    val password: String
)