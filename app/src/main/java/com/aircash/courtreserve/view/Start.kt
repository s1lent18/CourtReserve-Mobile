package com.aircash.courtreserve.view

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aircash.courtreserve.ui.theme.Lexend
import com.aircash.courtreserve.ui.theme.buttonDark
import com.aircash.courtreserve.ui.theme.buttonLight
import com.aircash.courtreserve.ui.theme.primary

@Composable
fun Start(
    onRoleSelected: (String) -> Unit
) {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "What is your Role?",
                    fontSize = 25.sp,
                    fontFamily = Lexend
                )

                AddHeight(30.dp)

                Button(
                    modifier = Modifier.fillMaxWidth(fraction = 0.85f).height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    onClick = {
                        onRoleSelected("User")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primary,
                        contentColor = Color.White
                    )
                ) {
                    Text("User", fontFamily = Lexend)
                }

                AddHeight(20.dp)

                Button(
                    modifier = Modifier.fillMaxWidth(fraction = 0.85f).height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    onClick = {
                        onRoleSelected("Vendor")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSystemInDarkTheme()) buttonDark else buttonLight,
                        contentColor = if (isSystemInDarkTheme()) Color.White else Color.Black
                    )
                ) {
                    Text("Vendor", fontFamily = Lexend)
                }
            }
        }
    }
}