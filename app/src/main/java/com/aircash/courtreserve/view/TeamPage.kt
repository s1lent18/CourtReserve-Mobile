package com.aircash.courtreserve.view

import android.annotation.SuppressLint
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.aircash.courtreserve.models.model.AddTeamMemberRequest
import com.aircash.courtreserve.models.model.NavigationBarItems
import com.aircash.courtreserve.ui.theme.Lexend
import com.aircash.courtreserve.ui.theme.buttonDark
import com.aircash.courtreserve.ui.theme.buttonLight
import com.aircash.courtreserve.ui.theme.primary
import com.aircash.courtreserve.viewmodels.navigation.Screens
import com.aircash.courtreserve.viewmodels.viewmodel.TeamViewModel
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable
import com.aircash.courtreserve.viewmodels.viewmodel.UserTokenViewModel

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeamPage(
    id : Int,
    navController : NavController,
    teamViewModel: TeamViewModel = hiltViewModel(),
    userTokenViewModel : UserTokenViewModel = hiltViewModel()
) {
    val insets = WindowInsets.navigationBars
    var selectedIndex by remember { mutableIntStateOf(1) }
    val navigationBarItems = remember { NavigationBarItems.entries }
    val userData = userTokenViewModel.userData.collectAsState().value
    val getSingleTeamResult = teamViewModel.getSingleTeamResult.collectAsState().value
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    var notTeamAdd by remember { mutableStateOf(false) }
    val color = if (isSystemInDarkTheme()) buttonDark else buttonLight
    val (email, setEmail) = remember { mutableStateOf("") }
    val (role, setRole) = remember { mutableStateOf("Member") }
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
                if (getSingleTeamResult == null || userData == null) {
                    Box(
                       modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                else {
                    val (infoRow, teamDesign, buttonRow) = createRefs()

                    Row(
                        modifier = Modifier
                            .constrainAs(infoRow) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.percent(0.9f)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                "Hello, ${getSingleTeamResult.singleTeam.name}",
                                color = primary,
                                fontSize = 20.sp,
                                fontFamily = Lexend
                            )
                            Text(
                                "Your ${getSingleTeamResult.singleTeam.sport} Team",
                                color = Color.Gray,
                                fontSize = 15.sp,
                                fontFamily = Lexend
                            )
                        }
                    }

                    if (notTeamAdd) {
                        HorizontalUncontainedCarousel(
                            state = rememberCarouselState { getSingleTeamResult.singleTeam.members.count() },
                            modifier = Modifier
                                .constrainAs(teamDesign) {
                                    top.linkTo(infoRow.bottom, margin = 20.dp)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    bottom.linkTo(buttonRow.top, margin = 20.dp)
                                }
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(top = 16.dp, bottom = 16.dp),
                            itemWidth = screenWidth * 0.9f,
                            itemSpacing = 12.dp,
                            contentPadding = PaddingValues(start = 16.dp)
                        ) { i ->

                            val item = getSingleTeamResult.singleTeam.members[i]

                            Box(
                                modifier = Modifier
                                    .width(screenWidth * 0.9f)
                                    .height(screenHeight * 0.6f)
                                    .maskClip(MaterialTheme.shapes.extraLarge)
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(item.coverImage),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )

                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomStart)
                                        .fillMaxWidth()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    Color.Black.copy(alpha = 0.7f)
                                                )
                                            )
                                        )
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = "${item.name} | ${item.role}",
                                        fontSize = 17.sp,
                                        fontFamily = Lexend
                                    )
                                }
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.constrainAs(teamDesign) {
                                top.linkTo(infoRow.bottom, margin = 20.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(buttonRow.top, margin = 20.dp)
                            },
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row (
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text("Team Member Name", fontFamily = Lexend)
                            }

                            AddHeight(10.dp)

                            Input(
                                label = "Email",
                                value = email,
                                onValueChange = setEmail,
                                color = color
                            )

                            AddHeight(20.dp)

                            Row (
                                modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text("Role: default: ( Member )", fontFamily = Lexend)
                            }

                            AddHeight(10.dp)

                            Input(
                                label = "Role",
                                value = role,
                                onValueChange = setRole,
                                color = color
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .constrainAs(buttonRow) {
                                top.linkTo(teamDesign.top, margin = 20.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom, margin = 20.dp)
                                width = Dimension.percent(0.9f)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (notTeamAdd) {
                            Button(
                                onClick = {
                                    notTeamAdd = false
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primary,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    "Add New Members",
                                    fontSize = 17.sp,
                                    fontFamily = Lexend,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        else {
                            Button(
                                onClick = {
                                    teamViewModel.addTeamMember(
                                        token = userData.token,
                                        addTeamMemberRequest = AddTeamMemberRequest(
                                            teamId = getSingleTeamResult.singleTeam.id,
                                            userEmail = email,
                                            role = role
                                        )
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = primary,
                                    contentColor = Color.White
                                )
                            ) {
                                Text(
                                    "Add",
                                    fontSize = 17.sp,
                                    fontFamily = Lexend,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}