package com.aircash.courtreserve.view

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aircash.courtreserve.models.model.NavigationBarItems
import com.aircash.courtreserve.ui.theme.Lexend
import com.aircash.courtreserve.ui.theme.primary
import com.aircash.courtreserve.viewmodels.navigation.Screens
import com.aircash.courtreserve.viewmodels.viewmodel.TeamViewModel
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable
import com.aircash.courtreserve.viewmodels.viewmodel.UserTokenViewModel

@Composable
fun TeamPage(
    navController : NavController,
    id : Int,
    userTokenViewModel : UserTokenViewModel = hiltViewModel(),
    teamViewModel: TeamViewModel = hiltViewModel()
) {

    val insets = WindowInsets.navigationBars
    var selectedIndex by remember { mutableIntStateOf(1) }
    val navigationBarItems = remember { NavigationBarItems.entries }
    val userData = userTokenViewModel.userData.collectAsState().value
    val getSingleTeamResult = teamViewModel.getSingleTeamResult.collectAsState().value
    val bottomInsetDp = with(LocalDensity.current) { insets.getBottom(LocalDensity.current).toDp() }

    LaunchedEffect(userData) {
        if (userData != null) {
            teamViewModel.getSingleTeam(token = "Bearer ${userData.token}", id = id)
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(),
        bottomBar = {
            AnimatedNavigationBar(
                modifier = Modifier
                    .height(70.dp)
                    .offset(y = -bottomInsetDp),
                selectedIndex = selectedIndex,
                cornerRadius = shapeCornerRadius(20.dp),
                ballAnimation = Parabolic(tween(300)),
                indentAnimation = Height(tween(300)),
                barColor = primary,
                ballColor = Color.White
            ) {
                navigationBarItems.forEach { item->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .noRippleClickable {
                                item.ordinal
                                when (item) {
                                    NavigationBarItems.Home -> {
                                        navController.navigate(Screens.UserHome.route)
                                    }

                                    NavigationBarItems.Msg -> {
                                        navController.navigate(Screens.BookingPage.route)
                                    }

                                    NavigationBarItems.Logout -> {
                                        userTokenViewModel.logout()
                                        navController.navigate(Screens.UserLanding.route) {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    }

                                    NavigationBarItems.Tournament -> {}
                                    NavigationBarItems.Team -> {}
                                }
                            },

                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            modifier = Modifier.size(15.dp),
                            contentDescription = null,
                            imageVector = item.icon,
                            tint = Color.White
                        )
                        Text(
                            item.text,
                            fontSize = 12.sp,
                            fontFamily = Lexend,
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) { values ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(values)
        ) {
            ConstraintLayout (
                modifier = Modifier
                    .fillMaxSize()
                    .padding()
            ) {
                if (getSingleTeamResult == null) {
                    Box(
                       modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                else {

                }
            }
        }
    }
}