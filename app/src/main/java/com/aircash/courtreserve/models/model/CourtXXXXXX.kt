package com.aircash.courtreserve.models.model

data class CourtXXXXXX(
    val bookings: List<BookingX>,
    val closeTime: String,
    val createdAt: String,
    val description: String,
    val id: Int,
    val location: String,
    val name: String,
    val openTime: String,
    val price: Int,
    val type: String
)