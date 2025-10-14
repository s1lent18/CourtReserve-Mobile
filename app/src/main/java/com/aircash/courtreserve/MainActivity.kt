package com.aircash.courtreserve

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.aircash.courtreserve.ui.theme.CourtReserveTheme
import com.aircash.courtreserve.viewmodels.navigation.NavGraph
import com.aircash.courtreserve.viewmodels.navigation.Screens
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startDestination = intent.getStringExtra("startDestination") ?: Screens.Start.route

        enableEdgeToEdge()
        setContent {
            CourtReserveTheme {
                val navController = rememberNavController()
                NavGraph(
                    navController = navController,
                    startDestination = startDestination,
                )
            }
        }
    }
}