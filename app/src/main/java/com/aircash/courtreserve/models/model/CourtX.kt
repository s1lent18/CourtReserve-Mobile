package com.aircash.courtreserve.models.model

data class CourtX(
    val closeTime: String,
    val description: String,
    val id: Int,
    val location: String,
    val name: String,
    val openTime: String,
    val price: Int,
    val type: String
)