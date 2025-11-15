package com.aircash.courtreserve.view

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aircash.courtreserve.ui.theme.Lexend
import com.aircash.courtreserve.ui.theme.primary
import java.time.Duration
import java.time.LocalTime

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
fun TournamentCard() {

}