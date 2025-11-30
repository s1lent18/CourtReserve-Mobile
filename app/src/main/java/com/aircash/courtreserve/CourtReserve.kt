package com.aircash.courtreserve

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.postgrest.Postgrest

@HiltAndroidApp
class CourtReserve : Application() {

    lateinit var supabase: SupabaseClient
        private set

    override fun onCreate() {
        super.onCreate()

        supabase = createSupabaseClient(
            supabaseUrl = R.string.SUPABASE_URL.toString(),
            supabaseKey = R.string.SUPABASE_ANON_KEY.toString()
        ) {
            install(Storage)
            install(Postgrest)
        }
    }
}