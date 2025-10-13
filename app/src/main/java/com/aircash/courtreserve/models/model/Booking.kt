package com.aircash.courtreserve.models.model

data class Booking(
    val advance: Int,
    val court: Court,
    val created: String,
    val endTime: String,
    val id: Int,
    val price: Int,
    val startTime: String,
    val status: String,
    val toBePaid: Int,
    val user: User
)