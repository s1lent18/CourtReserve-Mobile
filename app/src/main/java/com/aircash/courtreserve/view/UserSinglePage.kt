package com.aircash.courtreserve.view

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.aircash.courtreserve.R
import com.aircash.courtreserve.ui.theme.Lexend
import com.aircash.courtreserve.ui.theme.primary
import com.aircash.courtreserve.ui.theme.secondary
import com.aircash.courtreserve.viewmodels.viewmodel.CourtViewModel
import com.aircash.courtreserve.viewmodels.viewmodel.UserTokenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield

@Composable
fun UserSinglePage(
    id : Int,
    userTokenViewModel : UserTokenViewModel = hiltViewModel(),
    courtViewModel : CourtViewModel = hiltViewModel()
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
                            modifier = Modifier.fillMaxSize()
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
        }
    }
}