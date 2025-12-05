package com.aircash.courtreserve

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

@HiltAndroidApp
class CourtReserve : Application() {

    lateinit var supabase: SupabaseClient
        private set

    override fun onCreate() {
        super.onCreate()

        supabase = createSupabaseClient(
            supabaseUrl = getString(R.string.SUPABASE_URL),
            supabaseKey = getString(R.string.SUPABASE_ANON_KEY)
        ) {
            install(Auth)
            install(Storage)
            install(Postgrest)
        }
    }
}