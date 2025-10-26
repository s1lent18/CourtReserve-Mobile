package com.aircash.courtreserve.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.aircash.courtreserve.ui.theme.Lexend
import com.aircash.courtreserve.ui.theme.secondary
import com.aircash.courtreserve.viewmodels.viewmodel.VendorTokenViewModel

@Composable
fun VendorSinglePage(
    id : Int,
    vendorTokenViewModel: VendorTokenViewModel = hiltViewModel()
) {
    Surface {
        val vendorData = vendorTokenViewModel.vendorData.collectAsState().value

        Column(
            modifier = Modifier.fillMaxSize().padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (vendorData != null) {
                ConstraintLayout(
                    modifier = Modifier.fillMaxSize()
                ) {

                    val (text, smallStats) = createRefs()

                    Row (
                        modifier = Modifier
                            .constrainAs(text) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.percent(0.9f)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Hi ${vendorData.name}!")
                    }

                    Row (
                        modifier = Modifier
                            .constrainAs(smallStats) {
                                top.linkTo(text.bottom, margin = 20.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.percent(0.9f)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Card (
                            modifier = Modifier
                                .height(100.dp)
                                .weight(0.45f)
                        ) {
                            Column (
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Sales", fontFamily = Lexend, fontSize = 10.sp)

                                Text("Rs 4200", fontFamily = Lexend, fontSize = 17.sp)
                            }
                        }

                        Card (
                            modifier = Modifier
                                .height(100.dp)
                                .weight(0.45f),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = secondary,
                                contentColor = Color.White
                            )
                        ) {
                            Column (
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Total Visitors", fontFamily = Lexend, fontSize = 10.sp)

                                Text("4200", fontFamily = Lexend, fontSize = 17.sp)
                            }
                        }
                    }
                }
            }
            else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}