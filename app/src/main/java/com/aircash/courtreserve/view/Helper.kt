package com.aircash.courtreserve.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.aircash.courtreserve.CourtReserve
import com.aircash.courtreserve.models.model.Content
import com.aircash.courtreserve.ui.theme.Lexend
import com.aircash.courtreserve.ui.theme.primary
import com.aircash.courtreserve.ui.theme.secondary
import com.aircash.courtreserve.viewmodels.navigation.Screens
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import io.github.jan.supabase.storage.UploadStatus
import io.github.jan.supabase.storage.storage
import io.github.jan.supabase.storage.uploadAsFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.time.Duration
import java.time.LocalTime
import java.util.Locale
import java.util.concurrent.Executor

@Composable
fun AddWidth(space: Dp) {
    Spacer(modifier = Modifier.width(space))
}

@Composable
fun AddHeight(space: Dp) {
    Spacer(modifier = Modifier.height(space))
}

@Composable
fun Input(
    label : String,
    value : String,
    onValueChange: (String) -> Unit,
    trailingIcon: (@Composable () -> Unit)? = null,
    color: Color,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    TextField(
        modifier = modifier
            .fillMaxWidth(fraction = 0.9f),
        label = {
            Text(
                label,
                fontFamily = Lexend
            )
        },
        value = value,
        onValueChange = onValueChange,
        trailingIcon = trailingIcon,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = color,
            unfocusedContainerColor = color,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            disabledLabelColor = Color.Gray,
            unfocusedLabelColor = Color.Gray,
            focusedLabelColor = Color.Gray
        ),
        shape = RoundedCornerShape(10.dp),
        textStyle = TextStyle(
            fontSize = 15.sp,
            fontFamily = Lexend
        )
    )
}

@Composable
fun Funca(
    color: Color = primary,
    text: String,
    icon : ImageVector? = null,
    tcolor: Color = Color.White,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
        .fillMaxWidth(fraction = 0.85f)
        .height(50.dp)
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = color,
            contentColor = tcolor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(start = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(icon, contentDescription = null)
                AddWidth(8.dp)
            }
            Text(text)
        }
    }
}

fun getAvailableIntervals(open: LocalTime, close: LocalTime): List<Pair<LocalTime, LocalTime>> {
    val intervals = mutableListOf<Pair<LocalTime, LocalTime>>()


    var totalMinutes = if (close.isBefore(open)) {
        Duration.between(close.plusHours(24), open).toMinutes()
    } else {
        Duration.between(open, close).toMinutes()
    }

    totalMinutes = 1440 - totalMinutes

    Log.d("Time Slots Minutes", "$totalMinutes")

    var startMinutes = 0L

    while (startMinutes + 60 <= totalMinutes) {
        val startTime = open.plusMinutes(startMinutes)
        val endTime = open.plusMinutes(startMinutes + 60)

        val normalizedStart = if (startTime.hour >= 24) startTime.minusHours(24) else startTime
        val normalizedEnd = if (endTime.hour >= 24) endTime.minusHours(24) else endTime

        intervals.add(normalizedStart to normalizedEnd)

        startMinutes += 65
    }

    Log.d("Time Slots Intervals", "$intervals")
    return intervals
}

@Composable
fun TimeSlotItem(slot: Pair<LocalTime, LocalTime>, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) Color(0xFF4CAF50) else Color.Transparent
    val borderColor = if (isSelected) Color.White else Color.Gray

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() }
            .border(1.dp, borderColor)
            .background(bgColor)
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("${slot.first} - ${slot.second}", color = Color.White)
    }
}

@Composable
fun TournamentCard(tournament : Content, navController: NavController) {
    ElevatedButton(
        onClick = {
            navController.navigate("userSingleTournamentPage/${tournament.id}")
        },
        modifier = Modifier.fillMaxWidth(fraction = 0.9f),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .size(50.dp)
                        .background(secondary),
                    contentAlignment = Alignment.Center,

                ) {
                    Text(tournament.name[0].uppercase(), color = Color.White, fontFamily = Lexend)
                }
                
                AddWidth(15.dp)

                Column (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(tournament.name, fontSize = 12.sp, fontFamily = Lexend)
                    AddHeight(5.dp)
                    Text("Venue: ${tournament.courtName}", fontSize = 12.sp, fontFamily = Lexend)
                }
            }
            AddHeight(10.dp)
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Fees: 1000", fontSize = 12.sp, fontFamily = Lexend)
                Text("Price: ${tournament.prize}", fontSize = 12.sp, fontFamily = Lexend)
            }
        }
    }
}

@Composable
fun TeamMember(
    text: String,
    image: String,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .height(200.dp)
            .width(150.dp)
            .background(primary)
            .clickable {
                onClick()
            }
        ,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .padding(20.dp)
                .fillMaxWidth(fraction = 0.7f),
            contentAlignment = Alignment.TopCenter
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = image),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .border(2.dp, Color.White, CircleShape)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = text,
            fontFamily = Lexend,
            color = Color.White,
            modifier = Modifier.padding(5.dp),
            fontSize = 13.sp
        )
    }
}

fun capturePhoto(
    context: Context,
    cameraController: LifecycleCameraController,
    onPhotoCaptured: (Bitmap) -> Unit
) {
    val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

    cameraController.takePicture(mainExecutor, object : ImageCapture.OnImageCapturedCallback() {
        override fun onCaptureSuccess(image: ImageProxy) {
            val correctedBitmap: Bitmap = image
                .toBitmap()
                //.rotateBitmap(image.imageInfo.rotationDegrees)

            onPhotoCaptured(correctedBitmap)
            image.close()
        }

        override fun onError(exception: ImageCaptureException) {
            Log.e("CameraContent", "Error capturing image", exception)
        }
    })
}

fun imageBitmapToJpeg(image: ImageBitmap): ByteArray {
    val bitmap = image.asAndroidBitmap()
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
    return stream.toByteArray()
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LastPhotoPreview(
    final: Boolean,
    modifier: Modifier = Modifier,
    lastCapturedPhoto: Bitmap,
    navController: NavController
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var containerSize by remember { mutableStateOf(IntSize.Zero) }
    val capturedPhoto: ImageBitmap = remember(lastCapturedPhoto.hashCode()) { lastCapturedPhoto.asImageBitmap() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val supabase = (context.applicationContext as CourtReserve).supabase
    var isUploading by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { size ->
                containerSize = size
            }
    ) {
        Image(
            bitmap = capturedPhoto,
            contentDescription = "Image to crop",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (final) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(containerSize) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            val cropWidth = containerSize.width * 0.75f
                            val cropHeight = containerSize.height * 0.35f

                            val maxX = (containerSize.width - cropWidth) / 2
                            val maxY = (containerSize.height - cropHeight) / 2

                            offsetX = (offsetX + dragAmount.x).coerceIn(-maxX, maxX)
                            offsetY = (offsetY + dragAmount.y).coerceIn(-maxY, maxY)
                        }
                    }
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val canvasWidth = size.width
                    val canvasHeight = size.height

                    val cropWidth = canvasWidth * 0.75f
                    val cropHeight = canvasHeight * 0.35f
                    val centerX = canvasWidth / 2 + offsetX
                    val centerY = canvasHeight / 2 + offsetY
                    val cornerRadius = 24f

                    val outerPath = Path().apply {
                        addRect(Rect(0f, 0f, canvasWidth, canvasHeight))
                    }

                    val innerPath = Path().apply {
                        addRoundRect(
                            RoundRect(
                                rect = Rect(
                                    left = centerX - cropWidth / 2,
                                    top = centerY - cropHeight / 2,
                                    right = centerX + cropWidth / 2,
                                    bottom = centerY + cropHeight / 2
                                ),
                                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                            )
                        )
                    }

                    val overlayPath = Path().apply {
                        op(outerPath, innerPath, PathOperation.Difference)
                    }

                    drawPath(
                        path = overlayPath,
                        color = Color.Black.copy(alpha = 0.6f)
                    )

                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(centerX - cropWidth / 2, centerY - cropHeight / 2),
                        size = Size(cropWidth, cropHeight),
                        cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                        style = Stroke(width = 3f)
                    )

                    val cornerSize = 30f
                    val cornerOffset = 8f
                    val corners = listOf(
                        Offset(centerX - cropWidth / 2 + cornerOffset, centerY - cropHeight / 2 + cornerOffset), // Top-left
                        Offset(centerX + cropWidth / 2 - cornerOffset, centerY - cropHeight / 2 + cornerOffset), // Top-right
                        Offset(centerX - cropWidth / 2 + cornerOffset, centerY + cropHeight / 2 - cornerOffset), // Bottom-left
                        Offset(centerX + cropWidth / 2 - cornerOffset, centerY + cropHeight / 2 - cornerOffset)  // Bottom-right
                    )

                    drawLine(
                        color = Color.White,
                        start = corners[0],
                        end = corners[0] + Offset(cornerSize, 0f),
                        strokeWidth = 5f
                    )
                    drawLine(
                        color = Color.White,
                        start = corners[0],
                        end = corners[0] + Offset(0f, cornerSize),
                        strokeWidth = 5f
                    )

                    drawLine(
                        color = Color.White,
                        start = corners[1],
                        end = corners[1] + Offset(-cornerSize, 0f),
                        strokeWidth = 5f
                    )
                    drawLine(
                        color = Color.White,
                        start = corners[1],
                        end = corners[1] + Offset(0f, cornerSize),
                        strokeWidth = 5f
                    )

                    drawLine(
                        color = Color.White,
                        start = corners[2],
                        end = corners[2] + Offset(cornerSize, 0f),
                        strokeWidth = 5f
                    )
                    drawLine(
                        color = Color.White,
                        start = corners[2],
                        end = corners[2] + Offset(0f, -cornerSize),
                        strokeWidth = 5f
                    )

                    drawLine(
                        color = Color.White,
                        start = corners[3],
                        end = corners[3] + Offset(-cornerSize, 0f),
                        strokeWidth = 5f
                    )
                    drawLine(
                        color = Color.White,
                        start = corners[3],
                        end = corners[3] + Offset(0f, -cornerSize),
                        strokeWidth = 5f
                    )
                }
            }
        }

        if (final) {
            if (isUploading) {
                LinearWavyProgressIndicator(
                    progress = { uploadProgress },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 50.dp),
                    color = Color.White,
                    trackColor = primary
                )
            } else {
                Button(
                    onClick = {
                        scope.launch {
                            isUploading = true
                            uploadProgress = 0f
                            try {
                                val bucket = supabase.storage.from("images")
                                val imageBytes = imageBitmapToJpeg(capturedPhoto)

                                bucket.uploadAsFlow(
                                    path = "img_${System.currentTimeMillis()}.jpg",
                                    data = imageBytes
                                ).collect { status ->
                                    when (status) {
                                        is UploadStatus.Progress -> {
                                            uploadProgress =
                                                status.totalBytesSend.toFloat() / status.contentLength
                                        }

                                        is UploadStatus.Success -> {
                                            Toast.makeText(context, "Upload successful!", Toast.LENGTH_SHORT).show()
                                            navController.navigate(Screens.Account.route) {
                                                popUpTo(Screens.Account.route) { inclusive = true }
                                            }
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_LONG).show()
                                Log.d("CameraDebug", "${e.message}")
                            } finally {
                                isUploading = false
                            }
                        }
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 50.dp)
                        .height(50.dp)
                        .fillMaxWidth(0.9f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primary
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Text(
                        text = "Upload",
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}

//fun ImageBitmap.toAndroidBitmap(): Bitmap =
//    this.asAndroidBitmap()
//
//fun uriToBytes(context: Context, uri: Uri): ByteArray {
//    return context.contentResolver.openInputStream(uri)?.use {
//        it.readBytes()
//    } ?: ByteArray(0)
//}
//
//suspend fun uploadImage(context: Context, imageBitmap: ImageBitmap): String {
//    val bitmap = imageBitmap.toAndroidBitmap()
//
//    val stream = ByteArrayOutputStream()
//    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//    val bytes = stream.toByteArray()
//
//    return uploadImageBytes(context, bytes)
//}
//
//suspend fun uploadImage(context: Context, uri: Uri): String {
//    val bytes = uriToBytes(context, uri)
//    return uploadImageBytes(context, bytes)
//}
//
//private fun uploadImageBytes(context: Context, bytes: ByteArray): String {
//    val supabase = (context.applicationContext as CourtReserve).supabase
//
//    val fileName = "img_${System.currentTimeMillis()}.jpg"
//    val bucket = "images"
//
////    return supabase.storage.from(bucket).upload(
////        path = fileName,
////        data = bytes
////    )
//
//    return ""
//}

fun getPublicUrl(context: Context, path: String): String {
    val supabase = (context.applicationContext as CourtReserve).supabase
    return supabase.storage
        .from("images")
        .publicUrl(path)
}

suspend fun deleteImage(context: Context, path: String) {
    val supabase = (context.applicationContext as CourtReserve).supabase
    supabase.storage
        .from("images")
        .delete(paths = listOf(path))
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(onPermissionGranted: () -> Unit) {
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(key1 = permissionState.status.isGranted) {
        if (permissionState.status.isGranted) {
            onPermissionGranted()
        }
    }

    if (!permissionState.status.isGranted) {
        SideEffect { permissionState.launchPermissionRequest() }
    }
}

fun getCityFromLocation(context: Context, latitude: Double, longitude: Double): String {
    return try {
        val geocoder = Geocoder(context, Locale("en"))
        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
        val city = addresses?.firstOrNull()?.locality
        if (!city.isNullOrEmpty()) city else "Unknown"
    } catch (_: Exception) {
        "Unknown"
    }
}