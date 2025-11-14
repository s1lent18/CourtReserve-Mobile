package com.aircash.courtreserve.models.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavigationBarItems(val text: String, val icon: ImageVector) {
    Home(text = "Home", icon = Icons.Default.Home),
    Msg(text = "Booking", icon = Icons.AutoMirrored.Filled.Message),
    Account(text = "Account", icon = Icons.Default.AccountCircle),
    Logout(text = "Logout", icon = Icons.AutoMirrored.Filled.ArrowBack),
}