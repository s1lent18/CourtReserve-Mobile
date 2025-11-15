package com.aircash.courtreserve.models.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PeopleAlt
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.ui.graphics.vector.ImageVector

enum class NavigationBarItems(val text: String, val icon: ImageVector) {
    Home(text = "Home", icon = Icons.Default.Home),
    Msg(text = "Booking", icon = Icons.AutoMirrored.Filled.Message),
    Tournament(text = "Tournament", icon = Icons.Default.SportsSoccer),
    Team(text = "Team", icon = Icons.Default.PeopleAlt),
    Logout(text = "Logout", icon = Icons.AutoMirrored.Filled.ArrowBack),
}