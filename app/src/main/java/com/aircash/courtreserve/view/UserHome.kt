package com.aircash.courtreserve.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aircash.courtreserve.R
import com.aircash.courtreserve.models.model.CourtXXX
import com.aircash.courtreserve.ui.theme.Lexend
import com.aircash.courtreserve.viewmodels.viewmodel.CourtViewModel
import com.aircash.courtreserve.viewmodels.viewmodel.UserTokenViewModel

@Composable
fun CourtInfo(
    court: CourtXXX,
    rowHeight: Dp,
    modifier: Modifier = Modifier,
    onClick : () -> Unit
) {
    Box(
        modifier = modifier
            .clickable { onClick() }
            .height(rowHeight)
            .clip(RoundedCornerShape(20.dp))
    ) {
        Column(
            modifier = Modifier
                .offset(y = -(15).dp)
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                .align(Alignment.BottomStart)
                .fillMaxHeight(fraction = 0.35f)
                .fillMaxWidth()
                .background(Color(0xFF2b2d42))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp, horizontal = 10.dp)
            ) {
                Text(court.name, fontFamily = Lexend, color = Color.White)
                Row {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color.Yellow)
                    Text("${court.avgRating} (${court.bookingCount})", fontFamily = Lexend, color = Color.White)
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxHeight(fraction = 0.65f)
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
        ) {
            AsyncImage(
                model = stringResource(id = R.string.HomeImageUrl),
                contentDescription = court.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun UserHome(
    navController: NavController,
    userTokenViewModel: UserTokenViewModel = hiltViewModel(),
    courtViewModel: CourtViewModel = hiltViewModel()
) {
    Surface {
        val popularCourts = courtViewModel.getPopularCourtsResult.collectAsState().value
        val userData = userTokenViewModel.userData.collectAsState().value
        var searchQuery by remember { mutableStateOf("") }

        LaunchedEffect(userData) {
            Log.d("UserDataCheck", "$userData")
            if (userData != null) {
                courtViewModel.getPopularCourts(token = userData.token, location = "Karachi")
            }
        }

        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConstraintLayout (
                modifier = Modifier
                    .fillMaxSize()
                    .padding()
            ) {

                val (topImage, lazyColumn) = createRefs()

                Box (
                    modifier = Modifier
                        .constrainAs(topImage) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            height = Dimension.percent(0.4f)
                        }
                        .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
                        .fillMaxWidth()
                ) {
                    AsyncImage(
                        model = stringResource(id = R.string.HomeImageUrl),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .matchParentSize()
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Black.copy(alpha = 0.4f))
                    )

                    Column (
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Row (
                            modifier = Modifier.fillMaxWidth(fraction = 0.90f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row (
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )

                                AddWidth(5.dp)

                                Text("Karachi", fontFamily = Lexend, fontSize = 14.sp)
                            }

                            IconButton(
                                onClick = {  }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Box (
                            modifier = Modifier.fillMaxWidth(fraction = 0.9f)
                        ) {
                            Text(
                                "Hey, ${userData?.name}! Tell us where you\nwant to play today",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 32.sp,
                                fontFamily = Lexend
                            )
                        }

                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text(text = "Search...") },
                            modifier = Modifier
                                .fillMaxWidth(fraction = 0.9f)
                                .clip(RoundedCornerShape(30.dp)),
                            leadingIcon = {
                                IconButton(onClick = {  }) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Back"
                                    )
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.White.copy(alpha = 0.25f)
                            )
                        )
                    }
                }

                LazyColumn (
                    modifier = Modifier.constrainAs(lazyColumn) {
                        top.linkTo(topImage.bottom, margin = 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom, margin = 30.dp)
                        height = Dimension.fillToConstraints
                        width = Dimension.percent(0.9f)
                    }
                ) {
                    item {
                        Row{
                            Text("Most Popular", fontFamily = Lexend, fontSize = 17.sp)
                        }

                        AddHeight(15.dp)
                    }

                    item {
                        BoxWithConstraints {
                            val cardWidth = maxWidth * 0.9f
                            val rowHeight = maxHeight * 0.2f

                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(rowHeight),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp)
                            ) {
                                if (popularCourts == null) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .width(cardWidth)
                                                .height(rowHeight),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }
                                } else {
                                    items(popularCourts.courts.size) { index ->
                                        CourtInfo(
                                            court = popularCourts.courts[index],
                                            rowHeight = 200.dp,
                                            modifier = Modifier.width(cardWidth),
                                            onClick =  {
                                                Log.d("navigation Check", "${popularCourts.courts[index].id}")
                                                navController.navigate("userSingleScreen/${popularCourts.courts[index].id}")
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}