package com.aircash.courtreserve.models.modules

import android.app.Application
import com.aircash.courtreserve.CourtReserve
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabase(application: Application): SupabaseClient {
        val app = application as CourtReserve
        return app.supabase
    }
}
