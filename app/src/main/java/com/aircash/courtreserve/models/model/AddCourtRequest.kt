package com.aircash.courtreserve.models.model

data class AddCourtRequest(
    val closeTime: String,
    val description: String,
    val location: String,
    val name: String,
    val openTime: String,
    val price: Int,
    val type: String
)