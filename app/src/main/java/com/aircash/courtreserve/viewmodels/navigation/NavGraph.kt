package com.aircash.courtreserve.viewmodels.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aircash.courtreserve.models.modules.UserPreferences
import com.aircash.courtreserve.view.Start
import com.aircash.courtreserve.view.UserLanding
import com.aircash.courtreserve.view.UserSignup
import com.aircash.courtreserve.view.VendorLanding
import com.aircash.courtreserve.view.VendorSignup
import kotlinx.coroutines.launch

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable(Screens.Start.route) {
            Start { selectedRole ->
                scope.launch {
                    UserPreferences.saveUserRole(context, selectedRole)
                }
                navController.navigate(
                    if (selectedRole == "User") Screens.UserLanding.route
                    else Screens.VendorLanding.route
                ) {
                    popUpTo(Screens.Start.route) { inclusive = true }
                }
            }
        }

        this.composable(
            route = Screens.UserLanding.route
        ) {
            UserLanding(navController = navController)
        }

        this.composable(
            route = Screens.UserSignup.route
        ) {
            UserSignup(navController = navController)
        }

        this.composable(
            route = Screens.VendorLanding.route
        ) {
            VendorLanding(navController = navController)
        }

        this.composable(
            route = Screens.VendorSignup.route
        ) {
            VendorSignup(navController = navController)
        }
    }
}