package com.aircash.courtreserve.models.model

data class CourtXX(
    val closeTime: String,
    val created: String,
    val description: String,
    val id: Int,
    val location: String,
    val name: String,
    val openTime: String,
    val price: Int,
    val type: String,
    val vendor: VendorX
)