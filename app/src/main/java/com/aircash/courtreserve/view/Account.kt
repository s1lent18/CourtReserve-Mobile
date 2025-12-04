package com.aircash.courtreserve.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.aircash.courtreserve.models.model.CameraState
import com.aircash.courtreserve.ui.theme.primary
import com.aircash.courtreserve.viewmodels.viewmodel.CameraViewModel
import com.aircash.courtreserve.viewmodels.viewmodel.UserTokenViewModel

@Composable
fun Account(
    hasCameraPermission: Boolean,
    hasMediaPermission: Boolean,
    navController : NavController,
    onRequestCameraPermission: () -> Unit,
    onRequestMediaPermission: () -> Unit,
    cameraViewModel : CameraViewModel = hiltViewModel(),
    userTokenViewModel : UserTokenViewModel = hiltViewModel()
) {
    var camera by remember { mutableStateOf(false) }
    val userData = userTokenViewModel.userData.collectAsState().value
    val cameraState : CameraState by cameraViewModel.state.collectAsStateWithLifecycle()

    if (userData != null) {
        if (!camera) {
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(),
                bottomBar = {  }
            ) { values ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(values),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ConstraintLayout (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding()
                    ) {
                        val (displayBox) = createRefs()

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
                                .background(primary)
                                .constrainAs(displayBox) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    height = Dimension.percent(0.46f)
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                userData.coverImage.let {

                                    val painter = rememberAsyncImagePainter(model = userData.coverImage)

                                    val imageState = painter.state

                                    LaunchedEffect(painter.state) {
                                        when (painter.state) {
                                            is AsyncImagePainter.State.Loading -> Log.d("ImageState", "Loading...")
                                            is AsyncImagePainter.State.Success -> Log.d("ImageState", "Image loaded successfully")
                                            is AsyncImagePainter.State.Error -> Log.d("ImageState", "Error loading image")
                                            AsyncImagePainter.State.Empty -> {}
                                        }
                                    }

                                    Box(
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .clickable { camera = true }
                                            .size(100.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painter,
                                            contentDescription = "Profile Picture",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )

                                        if (imageState is AsyncImagePainter.State.Loading) {
                                            CircularProgressIndicator(
                                                color = Color.White,
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .align(Alignment.Center)
                                            )
                                        }
                                    }
                                }

                                AddHeight(20.dp)

                                Text(
                                    text = "Hello, ${userData.name}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

        } else {
            if (hasCameraPermission && hasMediaPermission) {
                Log.d("CameraDebug", "Check")
                CameraContent(
                    onPhotoCaptured = cameraViewModel::storePhotoInGallery,
                    lastCapturedPhoto = cameraState.capturedImage
                )
            } else {
                Column {
                    if (!hasMediaPermission) {
                        Text("Media permission is required")
                        Button(onClick = { onRequestMediaPermission() }) {
                            Text("Grant Media Permission")
                        }
                    }
                    if (!hasCameraPermission) {
                        Text("Camera permission is required")
                        Button(onClick = { onRequestCameraPermission() }) {
                            Text("Grant Camera Permission")
                        }
                    }
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}