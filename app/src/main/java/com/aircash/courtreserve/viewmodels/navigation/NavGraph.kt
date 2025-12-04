package com.aircash.courtreserve.viewmodels.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aircash.courtreserve.models.modules.UserPreferences
import com.aircash.courtreserve.view.Account
import com.aircash.courtreserve.view.BookingPage
import com.aircash.courtreserve.view.SingleTournamentPage
import com.aircash.courtreserve.view.Start
import com.aircash.courtreserve.view.TeamPage
import com.aircash.courtreserve.view.UserHome
import com.aircash.courtreserve.view.UserLanding
import com.aircash.courtreserve.view.UserSignup
import com.aircash.courtreserve.view.UserSinglePage
import com.aircash.courtreserve.view.UserTournamentPage
import com.aircash.courtreserve.view.VendorHome
import com.aircash.courtreserve.view.VendorLanding
import com.aircash.courtreserve.view.VendorSignup
import com.aircash.courtreserve.view.VendorSinglePage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String,
    mediaPermission: PermissionState,
    cameraPermission: PermissionState,
    locationPermission: MultiplePermissionsState
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

        this.composable(
            route = Screens.VendorHome.route
        ) {
            VendorHome()
        }

        this.composable(
            route = Screens.UserHome.route
        ) {
            UserHome(navController = navController)
        }

        this.composable(
            route = Screens.UserSinglePage.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1

            if (id != -1) {
                UserSinglePage(id = id, navController = navController)
            }
        }

        this.composable(
            route = Screens.VendorSinglePage.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1

            if (id != -1) {
                VendorSinglePage(id = id)
            }
        }

        this.composable(
            route = Screens.BookingPage.route
        ) {
            BookingPage(navController = navController)
        }

        this.composable(
            route = Screens.UserTeamPage.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1

            if (id != -1) {
                TeamPage(
                    navController = navController,
                    id = id
                )
            }
        }

        this.composable(
            route = Screens.SingleTournamentScreen.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1

            if (id != -1) {
                SingleTournamentPage(
                    navController = navController,
                    id = id
                )
            }
        }

        this.composable(
            route = Screens.Account.route
        ) {
            Account(
                navController = navController,
                hasCameraPermission = cameraPermission.status.isGranted,
                hasMediaPermission = mediaPermission.status.isGranted,
                onRequestCameraPermission = cameraPermission::launchPermissionRequest,
                onRequestMediaPermission = mediaPermission ::launchPermissionRequest
            )
        }

        this.composable(
            route = Screens.UserSingleTournamentPage.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.IntType
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1

            if (id != -1) {
                UserTournamentPage(
                    id = id,
                    navController = navController
                )
            }
        }
    }
}