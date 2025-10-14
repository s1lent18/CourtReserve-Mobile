package com.aircash.courtreserve.models.modules

import android.content.Context
import com.aircash.courtreserve.R
import com.aircash.courtreserve.models.interfaces.AddBookingAPI
import com.aircash.courtreserve.models.interfaces.AddCourtAPI
import com.aircash.courtreserve.models.interfaces.AddReviewAPI
import com.aircash.courtreserve.models.interfaces.GetPopularCourtsAPI
import com.aircash.courtreserve.models.interfaces.UserLoginAPI
import com.aircash.courtreserve.models.interfaces.UserRegistrationAPI
import com.aircash.courtreserve.models.interfaces.VendorLoginAPI
import com.aircash.courtreserve.models.interfaces.VendorRegistrationAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideBaseUrl(@ApplicationContext context: Context): String {
        return context.getString(R.string.Back_End_Link)
    }

    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideUserLoginAPI(retrofit: Retrofit) : UserLoginAPI {
        return retrofit.create(UserLoginAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRegisterAPI(retrofit: Retrofit) : UserRegistrationAPI {
        return retrofit.create(UserRegistrationAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideVendorLoginAPI(retrofit: Retrofit) : VendorLoginAPI {
        return retrofit.create(VendorLoginAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideVendorRegisterAPI(retrofit: Retrofit) : VendorRegistrationAPI {
        return retrofit.create(VendorRegistrationAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideAddBookingAPI(retrofit: Retrofit) : AddBookingAPI {
        return retrofit.create(AddBookingAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideAddCourtAPI(retrofit: Retrofit) : AddCourtAPI {
        return retrofit.create(AddCourtAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideAddReviewAPI(retrofit: Retrofit) : AddReviewAPI {
        return retrofit.create(AddReviewAPI::class.java)
    }

    @Provides
    @Singleton
    fun provideGetPopularCourtsAPI(retrofit: Retrofit) : GetPopularCourtsAPI {
        return retrofit.create(GetPopularCourtsAPI::class.java)
    }
}