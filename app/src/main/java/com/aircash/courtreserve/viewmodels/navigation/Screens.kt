package com.aircash.courtreserve.viewmodels.navigation

sealed class Screens(val route: String) {
    data object UserLanding: Screens("userLandingScreen")
    data object VendorLanding : Screens("vendorLandingScreen")
    data object Start: Screens("startScreen")
    data object UserHome: Screens("userHomeScreen")
}