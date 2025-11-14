package com.aircash.courtreserve.view

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.aircash.courtreserve.R
import com.aircash.courtreserve.models.model.BookingDisplay
import com.aircash.courtreserve.models.model.CreateBookingRequest
import com.aircash.courtreserve.ui.theme.Lexend
import com.aircash.courtreserve.ui.theme.primary
import com.aircash.courtreserve.ui.theme.secondary
import com.aircash.courtreserve.viewmodels.navigation.Screens
import com.aircash.courtreserve.viewmodels.viewmodel.BookingViewModel
import com.aircash.courtreserve.viewmodels.viewmodel.CourtViewModel
import com.aircash.courtreserve.viewmodels.viewmodel.UserTokenViewModel
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserSinglePage(
    id : Int,
    navController: NavController,
    courtViewModel : CourtViewModel = hiltViewModel(),
    bookingViewModel: BookingViewModel = hiltViewModel(),
    userTokenViewModel : UserTokenViewModel = hiltViewModel()
) {
    Surface {
        val coroutineScope = rememberCoroutineScope()
        val userData = userTokenViewModel.userData.collectAsState().value
        val court = courtViewModel.getCourtResult.collectAsState().value?.court
        val images = listOf(
            R.string.HomeImageUrl,
            R.string.HomeImageUrl,
            R.string.HomeImageUrl,
            R.string.HomeImageUrl,
            R.string.HomeImageUrl
        )
        val pageCount = /*if(court?.otherImages?.isNotEmpty() == true) venue.otherImages.size else 1 */ images.size
        val pagerState = rememberPagerState(pageCount = { pageCount })
        val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        var isSheetOpen by rememberSaveable { mutableStateOf(false) }
        val addBookingResult = bookingViewModel.addBookingResult.collectAsState().value
        val dateDialogState = rememberMaterialDialogState()
        val timeDialogState = rememberMaterialDialogState()
        val (advance, setAdvance) = remember { mutableStateOf("") }
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
        var selectedSlots by remember { mutableStateOf(listOf<Pair<LocalTime, LocalTime>>()) }
        var allSlots by remember { mutableStateOf<List<Pair<LocalTime, LocalTime>>>(emptyList()) }
        var clicked by remember { mutableStateOf(false) }
        var initialPickedDate by remember { mutableStateOf(LocalDate.now()) }
        var pickedDate by remember { mutableStateOf(initialPickedDate) }

        val formattedDate by remember {
            derivedStateOf { DateTimeFormatter.ofPattern("dd MMM yyyy").format(pickedDate) }
        }

        if (court != null) {
            LaunchedEffect(/*venue.otherImages.isNotEmpty()*/ true) {
                while (true) {
                    yield()
                    delay(3000)
                    val nextPage = (pagerState.currentPage + 1) % pageCount
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            page = nextPage,
                            animationSpec = tween(durationMillis = 1200)
                        )
                    }
                }
            }
        }

        LaunchedEffect(addBookingResult) {
            delay(2000)
            clicked = false
            navController.navigate(route = Screens.UserHome.route)
        }

        val displayList = court?.bookedTimes?.map { booking ->
            val start = LocalDateTime.parse(booking.startTime, formatter)
            val end = LocalDateTime.parse(booking.endTime, formatter)
            BookingDisplay(
                date = start.format(dateFormatter),
                start = start.format(timeFormatter),
                end = end.format(timeFormatter)
            )
        }

        Log.d("Time Slots Display List: ", "$displayList")

        val blockedBookings = displayList?.map {
            Triple(
                LocalDate.parse(it.date),
                LocalTime.parse(it.start),
                LocalTime.parse(it.end)
            )
        }

        Log.d("Time Slots Blocked Bookings: ", "$blockedBookings")

        LaunchedEffect(clicked) {
            if (clicked && court != null && userData != null && selectedSlots.isNotEmpty()) {
                val totalPrice = court.price.toInt() * selectedSlots.size

                val startTime = selectedSlots.first().first
                val endTime = selectedSlots.last().second

                val endDate = if (endTime.isBefore(startTime)) pickedDate.plusDays(1) else pickedDate

                val createBookingResult = CreateBookingRequest(
                    advance = advance.toIntOrNull() ?: 0,
                    price = totalPrice,
                    facilityId = court.id,
                    userId = userData.id,
                    startTime = "$pickedDate $startTime",
                    endTime = "$endDate $endTime"
                )

                bookingViewModel.createBooking(
                    token = "Bearer ${userData.token}",
                    request = createBookingResult
                )
            }
        }

        LaunchedEffect(court, pickedDate, blockedBookings) {
            if (court != null) {
                val openingTime = LocalTime.parse(court.open)
                val closingTime = LocalTime.parse(court.close)

                Log.d("Time Slots Inside Launched", "$openingTime $closingTime")

                val totalSlots = getAvailableIntervals(openingTime, closingTime)

                Log.d("Time Slots total Slots", "$totalSlots")

                val availableSlots = totalSlots.filter { slot ->
                    val isBooked = blockedBookings?.any { (bookingDate, bookingStart, bookingEnd) ->
                        pickedDate == bookingDate && slot.first.isBefore(bookingEnd) && slot.second.isAfter(bookingStart)
                    } ?: false
                    !isBooked
                }

                allSlots = availableSlots
                Log.d("Time Slots All Slots:", "$allSlots")
            }
        }

        LaunchedEffect(userData) {
            if (userData != null) {
                courtViewModel.getCourt(token = userData.token, id = id)
            }
        }

        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConstraintLayout (
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val (imageSlides, initial, firstDivider, desc, secondDivider, time, bottomBar, thirdDivider, type) = createRefs()

                if (court == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(fraction = 0.40f)
                            .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
                            .constrainAs(imageSlides) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    ) {
                        HorizontalPager(
                            state = pagerState,
                            key = { index -> "page_$index" }
                        ) { index ->
                            Image(
                                painter = rememberAsyncImagePainter(stringResource(images[index])),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
//                            if (venue.otherImages.isNotEmpty()) {
//                                images.forEachIndexed { pageIndex, _ ->
//                                    val isSelected = pageIndex == pagerState.currentPage
//                                    Box(
//                                        modifier = Modifier
//                                            .size(8.dp)
//                                            .clip(CircleShape)
//                                            .background(if (isSelected) Color.Black else Color.Gray)
//                                            .padding(4.dp)
//                                    )
//                                    if (pageIndex != venue.otherImages.size - 1) {
//                                        AddWidth(8.dp)
//                                    }
//                                }
//                            }
                            images.forEachIndexed { pageIndex, _ ->
                                val isSelected = pageIndex == pagerState.currentPage
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) Color.Black else Color.Gray)
                                        .padding(4.dp)
                                )
                                if (pageIndex != images.size - 1) {
                                    AddWidth(8.dp)
                                }
                            }
                        }
                    }

                    Column (
                        modifier = Modifier.constrainAs(initial) {
                            top.linkTo(imageSlides.bottom, margin = 20.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.percent(0.9f)
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(court.name, fontFamily = Lexend, fontSize = 20.sp)
                        }

                        AddHeight(10.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null
                            )

                            AddWidth(4.dp)

                            Text("${court.avgRating} (${court.bookingCount} reviews)", fontFamily = Lexend)
                        }
                    }

                    Row (
                        modifier = Modifier
                            .constrainAs(firstDivider) {
                                top.linkTo(initial.bottom, margin = 15.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.percent(0.9f)
                            }
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = Color.Gray)
                    }

                    Column (
                        modifier = Modifier.constrainAs(desc) {
                            top.linkTo(firstDivider.bottom, margin = 15.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.percent(0.9f)
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Description", fontFamily = Lexend, fontWeight = FontWeight.Bold)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(court.description, fontFamily = Lexend, color = Color.Gray)
                        }
                    }

                    Row (
                        modifier = Modifier
                            .constrainAs(secondDivider) {
                                top.linkTo(desc.bottom, margin = 15.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.percent(0.9f)
                            }
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = Color.Gray)
                    }

                    Row (
                        modifier = Modifier.constrainAs(time) {
                            top.linkTo(secondDivider.bottom, margin = 20.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.percent(0.9f)
                        },
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Card (
                            modifier = Modifier
                                .weight(0.4f)
                                .height(75.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = primary
                            )
                        ) {
                            Column (
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.AccessTime,
                                    contentDescription = null
                                )

                                Text(court.open)
                            }
                        }

                        AddWidth(15.dp)

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(primary)
                                .size(30.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null
                            )
                        }

                        AddWidth(15.dp)

                        Card (
                            modifier = Modifier
                                .weight(0.4f)
                                .height(75.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = primary
                            )
                        ) {
                            Column (
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.AccessTime,
                                    contentDescription = null
                                )

                                Text(court.close)
                            }
                        }
                    }

                    Row (
                        modifier = Modifier
                            .constrainAs(thirdDivider) {
                                top.linkTo(time.bottom, margin = 15.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.percent(0.9f)
                            }
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = Color.Gray)
                    }

                    ElevatedCard (
                        modifier = Modifier.constrainAs(type) {
                            top.linkTo(thirdDivider.bottom, margin = 15.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(bottomBar.top, margin = 15.dp)
                            width = Dimension.percent(0.9f)
                            height = Dimension.fillToConstraints
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = secondary
                        ),
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 20.dp
                        )
                    ) {
                        Box (
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(court.type, fontSize = 20.sp, fontFamily = Lexend, color = Color.White)
                        }
                    }

                    ElevatedCard (
                        modifier = Modifier
                            .constrainAs(bottomBar) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom, margin = 30.dp)
                                width = Dimension.percent(0.9f)
                            }
                            .clip(RoundedCornerShape(50.dp))
                            .height(60.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = primary
                        ),
                        elevation = CardDefaults.elevatedCardElevation(20.dp)
                    ) {
                        Row (
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 15.dp, end = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column (
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(0.5f),
                                horizontalAlignment = Alignment.Start,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text("Rs.${court.price.toInt()}/Hr", fontSize = 20.sp, fontFamily = Lexend)
                            }

                            Column (
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(0.5f),
                                horizontalAlignment = Alignment.End,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box (
                                    modifier = Modifier
                                        .clickable {
                                            isSheetOpen = true
                                        }
                                        .clip(RoundedCornerShape(50.dp))
                                        .background(Color.White)
                                        .fillMaxHeight(fraction = 0.75f)
                                        .fillMaxWidth(fraction = 0.6f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("Book Now", fontFamily = Lexend, fontSize = 12.sp, color = primary)
                                }
                            }
                        }
                    }
                }
            }

            if (isSheetOpen && court != null) {
                ModalBottomSheet(
                    sheetState = bottomSheetState,
                    onDismissRequest = { isSheetOpen = false },
                    containerColor = secondary
                ) {
                    Column (
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top
                    ) {
                        if (addBookingResult == null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(fraction = 0.85f),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Funca(
                                    text = formattedDate,
                                    icon = Icons.Default.CalendarMonth,
                                    modifier = Modifier
                                        .fillMaxWidth(fraction = 0.65f)
                                        .height(50.dp)
                                )
                                Box (
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(primary)
                                ) {
                                    IconButton(
                                        onClick = { dateDialogState.show() }
                                    ) {
                                        Icon(
                                            Icons.Default.CalendarMonth,
                                            contentDescription = null,
                                            tint = Color.White
                                        )
                                    }
                                }
                                Box (
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(primary)
                                ) {
                                    IconButton(
                                        onClick = { timeDialogState.show() }
                                    ) {
                                        Icon(
                                            Icons.Default.AccessTime,
                                            contentDescription = null,
                                            tint = Color.White
                                        )
                                    }
                                }
                            }

                            AddHeight(20.dp)

                            TextField(
                                label = { Text("Advance", fontFamily = Lexend) },
                                value = advance,
                                onValueChange = setAdvance,
                                modifier = Modifier
                                    .fillMaxWidth(fraction = 0.85f)
                                    .height(50.dp),
                                leadingIcon = {
                                    Icon(
                                        Icons.Default.AttachMoney,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = primary,
                                    unfocusedContainerColor = primary,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent,
                                    disabledLabelColor = Color.White,
                                    unfocusedLabelColor = Color.White,
                                    focusedLabelColor = Color.White,
                                    disabledTextColor = Color.White,
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                                ),
                                shape = RoundedCornerShape(10.dp),
                                textStyle = TextStyle(
                                    color = Color.White,
                                    fontSize = 15.sp
                                )
                            )

                            AddHeight(20.dp)
                            if (!clicked) {
                                Button(
                                    onClick = { clicked = true },
                                    modifier = Modifier
                                        .fillMaxWidth(fraction = 0.85f)
                                        .height(50.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = primary,
                                        contentColor = Color.White
                                    ),
                                    shape = RoundedCornerShape(5.dp)
                                ) {
                                    Text("Confirm Booking", fontFamily = Lexend)
                                }
                            }
                            AddHeight(20.dp)

                            MaterialDialog (
                                dialogState = dateDialogState,
                                properties = DialogProperties(

                                ),
                                backgroundColor = primary,
                                buttons = {
                                    positiveButton(
                                        "Ok",
                                        textStyle = TextStyle(color = Color.White)
                                    )
                                }
                            ) {
                                datepicker(
                                    initialDate = LocalDate.now(),
                                    title = "Pick a Date",
                                    allowedDateValidator = { date ->
                                        date >= LocalDate.now() &&
                                                (blockedBookings?.none { it.first == date } ?: true)
                                    },
                                    colors = DatePickerDefaults.colors(
                                        headerBackgroundColor = primary,
                                        headerTextColor = Color.White,
                                        calendarHeaderTextColor = Color.White,
                                        dateActiveBackgroundColor = primary,
                                        dateActiveTextColor = Color.White,
                                        dateInactiveTextColor = Color.White
                                    )
                                ) {
                                    pickedDate = it
                                }
                            }

                            MaterialDialog (
                                dialogState = timeDialogState,
                                properties = DialogProperties(

                                ),
                                backgroundColor = primary,
                                buttons = {
                                    positiveButton(
                                        "Ok",
                                        textStyle = TextStyle(color = Color.White)
                                    )
                                }
                            ) {
                                val availableSlots = allSlots.filter { slot ->
                                    val slotStartDateTime = LocalDateTime.of(pickedDate, slot.first)
                                    val slotEndDateTime = if (slot.second.isBefore(slot.first)) {
                                        LocalDateTime.of(pickedDate.plusDays(1), slot.second)
                                    } else {
                                        LocalDateTime.of(pickedDate, slot.second)
                                    }

                                    val isBooked = blockedBookings?.any { (bookingDate, bookingStart, bookingEnd) ->
                                        val bookingStartDateTime = LocalDateTime.of(bookingDate, bookingStart)
                                        val bookingEndDateTime = if (bookingEnd.isBefore(bookingStart)) {
                                            LocalDateTime.of(bookingDate.plusDays(1), bookingEnd)
                                        } else {
                                            LocalDateTime.of(bookingDate, bookingEnd)
                                        }

                                        (slotStartDateTime < bookingEndDateTime && slotEndDateTime > bookingStartDateTime)
                                    } ?: false

                                    !isBooked
                                }

                                Log.d("Time Slots All Slots: ", "$allSlots")

                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        "Select Time Slots",
                                        color = Color.White,
                                    )
                                    AddHeight(8.dp)

                                    LazyColumn {
                                        items(availableSlots) { slot ->
                                            val isSelected = selectedSlots.contains(slot)
                                            TimeSlotItem(
                                                slot = slot,
                                                isSelected = isSelected,
                                                onClick = {
                                                    selectedSlots = if (isSelected) {
                                                        selectedSlots - slot
                                                    } else {
                                                        selectedSlots + slot
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        else {
                            AddHeight(20.dp)
                            Funca(
                                text = addBookingResult.message,
                                icon = Icons.Default.Mail,
                                modifier = Modifier
                                    .fillMaxWidth(fraction = 0.8f)
                                    .height(50.dp)
                            )
                            AddHeight(20.dp)
                        }
                    }
                }
            }
        }
    }
}