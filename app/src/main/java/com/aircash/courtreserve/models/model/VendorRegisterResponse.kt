package com.aircash.courtreserve.models.model
import kotlinx.serialization.Serializable

@Serializable
data class VendorRegisterResponse(
    val registerVendorData: RegisterVendorData
)