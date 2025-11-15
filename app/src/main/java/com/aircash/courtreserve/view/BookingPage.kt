package com.aircash.courtreserve.view

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.StopCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aircash.courtreserve.models.model.BookingXX
import com.aircash.courtreserve.models.model.NavigationBarItems
import com.aircash.courtreserve.models.model.Status
import com.aircash.courtreserve.ui.theme.Lexend
import com.aircash.courtreserve.ui.theme.primary
import com.aircash.courtreserve.viewmodels.navigation.Screens
import com.aircash.courtreserve.viewmodels.viewmodel.BookingViewModel
import com.aircash.courtreserve.viewmodels.viewmodel.UserTokenViewModel
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable
import java.time.LocalDateTime

@Composable
fun BookingPage(
    navController : NavController,
    bookingViewModel : BookingViewModel = hiltViewModel(),
    userTokenViewModel: UserTokenViewModel = hiltViewModel()
) {
    val insets = WindowInsets.navigationBars
    var selectedIndex by remember { mutableIntStateOf(1) }
    val navigationBarItems = remember { NavigationBarItems.entries }
    val userData = userTokenViewModel.userData.collectAsState().value
    val selectedOption = remember { mutableStateOf("PENDING") }
    val bookings = bookingViewModel.getAllBookingsResult.collectAsState().value
    val bottomInsetDp = with(LocalDensity.current) { insets.getBottom(LocalDensity.current).toDp() }

    val pending = remember { mutableStateListOf<BookingXX>() }
    val expired = remember { mutableStateListOf<BookingXX>() }
    val rejected = remember { mutableStateListOf<BookingXX>() }
    val canceled = remember { mutableStateListOf<BookingXX>() }
    val confirmed = remember { mutableStateListOf<BookingXX>() }

    LaunchedEffect(userData) {
        if (userData != null) {
            bookingViewModel.getAllBookings("Bearer ${userData.token}", userData.id)
        }
    }

    LaunchedEffect(bookings) {
        Log.d("Booking Check", "$bookings")
        pending.clear()
        expired.clear()
        rejected.clear()
        canceled.clear()
        confirmed.clear()

        bookings?.bookings?.forEach { booking ->
            Log.d("Booking Check", "$booking")
            when (booking.status) {
                "PENDING" -> pending.add(booking)
                "EXPIRED" -> expired.add(booking)
                "REJECTED" -> rejected.add(booking)
                "CANCELED" -> canceled.add(booking)
                "CONFIRMED" -> confirmed.add(booking)
            }
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

                                    }

                                    NavigationBarItems.Logout -> {
                                        userTokenViewModel.logout()
                                        navController.navigate(Screens.UserLanding.route) {
                                            popUpTo(0) { inclusive = true }
                                        }
                                    }

                                    NavigationBarItems.Account -> {
                                        //navController.navigate(route = Screens.Account.route)
                                    }
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
            modifier = Modifier.fillMaxSize().padding(values),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConstraintLayout (
                modifier = Modifier
                    .fillMaxSize()
                    .padding()
            ) {
                val (name, bookingItems, bookingList) = createRefs()

                Row(
                    modifier = Modifier.constrainAs(name) {
                        top.linkTo(parent.top, margin = 50.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.percent(0.9f)
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card (
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = primary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Bookings", fontWeight = FontWeight.Bold, fontFamily = Lexend, fontSize = 17.sp)
                        }
                    }
                }

                LazyRow(
                    modifier = Modifier.constrainAs(bookingItems) {
                        top.linkTo(name.bottom, margin = 30.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                    contentPadding = PaddingValues(horizontal = 18.dp, vertical = 8.dp),
                ) {
                    items(Status.entries) { option ->
                        StatusBar(
                            status = option.name,
                            isSelected = selectedOption.value == option.name,
                            onclick = { selectedOption.value = option.name }
                        )
                        AddWidth(10.dp)
                    }
                }

                if (bookings == null) {
                    Box(
                        modifier = Modifier.constrainAs(bookingList) {
                        top.linkTo(bookingItems.bottom, margin = 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, margin = 50.dp)
                        height = Dimension.fillToConstraints
                        width = Dimension.percent(0.9f)
                        },
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                LazyColumn(
                    modifier = Modifier.constrainAs(bookingList) {
                        top.linkTo(bookingItems.bottom, margin = 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, margin = 50.dp)
                        height = Dimension.fillToConstraints
                        width = Dimension.percent(0.9f)
                    },
                ) {
                    when (selectedOption.value) {
                        "PENDING" -> items(pending) { booking ->
                            BookingCard(
                                booking = booking
                            )
                            AddHeight(10.dp)
                        }
                        "EXPIRED" -> items(expired) { booking ->
                            BookingCard(
                                booking = booking
                            )
                            AddHeight(10.dp)
                        }
                        "REJECTED" -> items(rejected) { booking ->
                            BookingCard(
                                booking = booking
                            )
                            AddHeight(10.dp)
                        }
                        "CANCELED" -> items(canceled) { booking ->
                            BookingCard(
                                booking = booking
                            )
                            AddHeight(10.dp)
                        }
                        "CONFIRMED" -> items(confirmed) { booking ->
                            BookingCard(
                                booking = booking
                            )
                            AddHeight(10.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBar(
    status : String,
    isSelected : Boolean = false,
    onclick: () -> Unit
) {
    FloatingActionButton(
        onClick = onclick,
        modifier = Modifier
            .width(100.dp)
            .height(35.dp),
        shape = RoundedCornerShape(8.dp),
        containerColor = if (isSelected) primary else Color(0xFF1A1A1A),
        contentColor = Color.White,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(status.lowercase().replaceFirstChar { it.titlecase() }, fontFamily = Lexend)
        }
    }
}

@Composable
fun BookingCard(
    booking : BookingXX
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = primary,
            contentColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .padding(start = 8.dp, top = 10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = booking.courtName, color = Color.White, fontSize = 17.sp, fontFamily = Lexend)
                Text(
                    text = LocalDateTime.parse(booking.startTime)
                        .toLocalDate()
                        .toString(),
                    color = Color.White,
                    fontSize = 9.sp,
                    fontFamily = Lexend
                )
            }

            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    when (booking.status) {
                        "PENDING" -> Icons.Default.Pending
                        "EXPIRED" -> Icons.Default.Delete
                        "CANCELED" -> Icons.Default.Cancel
                        "REJECTED" -> Icons.Default.StopCircle
                        "CONFIRMED" -> Icons.Default.AssignmentTurnedIn
                        else -> Icons.Default.Schedule
                    },
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}