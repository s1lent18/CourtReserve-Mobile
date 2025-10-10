package com.aircash.courtreserve.models.interfaces

import com.aircash.courtreserve.models.model.VendorLoginRequest
import com.aircash.courtreserve.models.model.VendorLoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface VendorLoginAPI {

    @POST("/vendor/login")
    suspend fun loginVendor(
        @Body vendorLoginRequest: VendorLoginRequest
    ) : Response<VendorLoginResponse>
}