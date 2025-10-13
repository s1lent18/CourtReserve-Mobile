package com.aircash.courtreserve.models.model

data class CreateBookingRequest(
    val advance: Int,
    val endTime: String,
    val facilityId: Int,
    val price: Int,
    val startTime: String,
    val userId: Int
)