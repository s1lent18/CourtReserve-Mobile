package com.aircash.courtreserve.models.interfaces

import com.aircash.courtreserve.models.model.VendorRegisterRequest
import com.aircash.courtreserve.models.model.VendorRegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface VendorRegistrationAPI {

    @POST("/vendor/register")
    suspend fun registerVendor(
        @Body vendorRegisterRequest: VendorRegisterRequest
    ) : Response<VendorRegisterResponse>
}