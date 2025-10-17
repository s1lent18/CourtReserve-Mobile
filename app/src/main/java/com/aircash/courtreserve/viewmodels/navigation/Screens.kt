package com.aircash.courtreserve.viewmodels.navigation

sealed class Screens(val route: String) {
    data object Start: Screens("startScreen")
    data object UserLanding: Screens("userLandingScreen")
    data object UserSignup : Screens("userSignupScreen")
    data object UserHome: Screens("userHomeScreen")
    data object UserSinglePage : Screens("userSingleScreen/{id}")
    data object VendorLanding : Screens("vendorLandingScreen")
    data object VendorSignup : Screens("vendorSignupScreen")
    data object VendorHome : Screens("vendorHomeScreen")
}