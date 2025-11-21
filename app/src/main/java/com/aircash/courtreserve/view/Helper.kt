package com.aircash.courtreserve.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.aircash.courtreserve.models.model.Content
import com.aircash.courtreserve.ui.theme.Lexend
import com.aircash.courtreserve.ui.theme.primary
import com.aircash.courtreserve.ui.theme.secondary
import java.time.Duration
import java.time.LocalTime
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
fun TournamentCard(tournament : Content) {
    ElevatedButton(
        onClick = {},
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

@Composable
fun LastPhotoPreview(
    modifier: Modifier = Modifier,
    lastCapturedPhoto: Bitmap
) {

    val capturedPhoto: ImageBitmap = remember(lastCapturedPhoto.hashCode()) { lastCapturedPhoto.asImageBitmap() }

    Card(
        modifier = modifier
            .size(128.dp)
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Image(
            bitmap = capturedPhoto,
            contentDescription = "Last captured photo",
            contentScale = ContentScale.Crop
        )
    }
}