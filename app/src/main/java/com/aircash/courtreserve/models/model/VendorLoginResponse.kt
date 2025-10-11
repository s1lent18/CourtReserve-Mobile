package com.aircash.courtreserve.models.model
import kotlinx.serialization.Serializable

@Serializable
data class VendorLoginResponse(
    val vendorData: VendorData
)