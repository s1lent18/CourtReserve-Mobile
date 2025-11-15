package com.aircash.courtreserve.view

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.aircash.courtreserve.viewmodels.viewmodel.CourtViewModel
import com.aircash.courtreserve.viewmodels.viewmodel.VendorTokenViewModel

@Composable
fun VendorHome(
    courtViewModel: CourtViewModel = hiltViewModel(),
    vendorTokenViewModel: VendorTokenViewModel = hiltViewModel()
) {
    Surface {
        val allCourts = courtViewModel.getAllCourtsResult.collectAsState().value
        val vendorData = vendorTokenViewModel.vendorData.collectAsState().value

        LaunchedEffect(Unit) {
            Log.d("VendorDataCheck", "$vendorData")
            if (vendorData != null) {
                courtViewModel.getAllCourts(token = vendorData.token, id = vendorData.id)
            }
        }

        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConstraintLayout (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 20.dp)
            ) {
                val (text, lazyColumn) = createRefs()

                Box(
                    modifier = Modifier
                        .constrainAs(text) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.percent(0.9f)
                        }
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(
                        text = "Hello",
                        modifier = Modifier.align(Alignment.Center),
                    )

                    IconButton(
                        onClick = {  },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Search"
                        )
                    }
                }

                if (allCourts != null) {
                    LazyColumn (
                        modifier = Modifier.constrainAs(lazyColumn) {
                            top.linkTo(text.bottom, margin = 20.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom, margin = 40.dp)
                            height = Dimension.fillToConstraints
                            width = Dimension.percent(0.9f)
                        },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        items(allCourts.court) { court ->
                            Text(court.name)
                        }
                    }
                }
            }
        }
    }
}