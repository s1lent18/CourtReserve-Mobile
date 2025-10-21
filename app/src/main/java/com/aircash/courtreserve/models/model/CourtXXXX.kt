package com.aircash.courtreserve.models.model

data class CourtXXXX(
    val avgRating: Double,
    val bookedTimes: List<BookedTime>,
    val bookingCount: Int,
    val close: String,
    val description: String,
    val id: Int,
    val location: String,
    val name: String,
    val `open`: String,
    val price: Double,
    val type: String,
    val vendorId: Int
)