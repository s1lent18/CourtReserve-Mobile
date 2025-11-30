package com.aircash.courtreserve

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.aircash.courtreserve.ui.theme.CourtReserveTheme
import com.aircash.courtreserve.viewmodels.navigation.NavGraph
import com.aircash.courtreserve.viewmodels.navigation.Screens
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import io.github.jan.supabase.SupabaseClient
import javax.inject.Inject

@AndroidEntryPoint
@OptIn(ExperimentalPermissionsApi::class)
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var supabase: SupabaseClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startDestination = intent.getStringExtra("startDestination") ?: Screens.Start.route

        enableEdgeToEdge()
        setContent {
            CourtReserveTheme {

                val permissions = listOf(
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )

                val permissionState = rememberMultiplePermissionsState(permissions)

                LaunchedEffect(Unit) {
                    permissionState.launchMultiplePermissionRequest()
                }

                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    startDestination = startDestination,
                    permissionState = permissionState
                )
            }
        }
    }
}