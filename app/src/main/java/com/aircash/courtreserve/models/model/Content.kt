package com.aircash.courtreserve.models.model

data class Content(
    val courtId: Int,
    val courtName: String,
    val created: String,
    val endDate: String,
    val id: Int,
    val name: String,
    val organizerId: Int,
    val organizerName: String,
    val prize: Int,
    val sport: String,
    val startDate: String,
    val status: String,
    val teams: List<Any>
)