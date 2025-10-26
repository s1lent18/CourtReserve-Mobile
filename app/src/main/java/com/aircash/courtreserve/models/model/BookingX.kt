package com.aircash.courtreserve.models.model

data class BookingX(
    val advance: Int,
    val created: String,
    val endTime: String,
    val id: Int,
    val price: Int,
    val startTime: String,
    val status: String,
    val toBePaid: Int,
    val user: UserXX
)