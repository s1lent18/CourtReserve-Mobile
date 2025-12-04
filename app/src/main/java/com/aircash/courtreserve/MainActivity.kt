package com.aircash.courtreserve

import android.Manifest
import android.os.Build
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
import com.google.accompanist.permissions.rememberPermissionState
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

                val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)

                val mediaPermission = rememberPermissionState(
                    if (Build.VERSION.SDK_INT >= 33)
                        Manifest.permission.READ_MEDIA_IMAGES
                    else
                        Manifest.permission.READ_EXTERNAL_STORAGE
                )

                val locationPermissions = rememberMultiplePermissionsState(
                    listOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )

                val navController = rememberNavController()

                NavGraph(
                    navController = navController,
                    startDestination = startDestination,
                    mediaPermission = mediaPermission,
                    locationPermission = locationPermissions,
                    cameraPermission = cameraPermission
                )
            }
        }
    }
}