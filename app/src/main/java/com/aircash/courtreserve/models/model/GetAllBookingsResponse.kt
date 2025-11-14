package com.aircash.courtreserve.models.model

data class GetAllBookingsResponse(
    val bookings: List<BookingXX>,
    val message: String
)