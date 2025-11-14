package com.aircash.courtreserve.models.model

data class BookingXX(
    val advance: Int,
    val courtId: Int,
    val courtName: String,
    val created: String,
    val endTime: String,
    val id: Int,
    val price: Int,
    val startTime: String,
    val status: String,
    val toBePaid: Int
)