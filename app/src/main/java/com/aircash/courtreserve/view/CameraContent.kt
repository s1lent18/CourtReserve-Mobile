package com.aircash.courtreserve.view

import android.Manifest
import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

private const val TAG = "CameraDebug"

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraContent(
    onPhotoCaptured: (Bitmap) -> Unit,
    lastCapturedPhoto: Bitmap? = null,
    navController: NavController
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var capturedPhoto by remember { mutableStateOf(lastCapturedPhoto) }
    val permissionState = rememberPermissionState(Manifest.permission.CAMERA)
    var final by remember { mutableStateOf(false) }

    // Create controller
    val cameraController = remember {
        Log.d(TAG, "Creating LifecycleCameraController")
        LifecycleCameraController(context).apply {
            Log.d(TAG, "Enabling use cases: IMAGE_CAPTURE + IMAGE_ANALYSIS")
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE or CameraController.IMAGE_ANALYSIS
            )
        }
    }

    // Handle permission only
    LaunchedEffect(permissionState.status) {
        Log.d(TAG, "Permission changed: granted = ${permissionState.status.isGranted}")

        if (!permissionState.status.isGranted) {
            Log.d(TAG, "Requesting CAMERA permission…")
            permissionState.launchPermissionRequest()
        }
    }

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            if (capturedPhoto == null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    ExtendedFloatingActionButton(
                        text = { Text("Take photo") },
                        onClick = { capturePhoto(context, cameraController) { bitmap ->
                            capturedPhoto = bitmap
                            onPhotoCaptured(bitmap)
                        } },
                        icon = { Icon(Icons.Default.Camera, null) }
                    )

                    AddWidth(16.dp)

                    ExtendedFloatingActionButton(
                        text = { Text("Switch Camera") },
                        onClick = { cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA },
                        icon = { Icon(Icons.Default.Cameraswitch, null) }
                    )
                }
            }
            else {
                if (!final) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        ExtendedFloatingActionButton(
                            text = { Text("Proceed to Update") },
                            onClick = {
                                final = true
                            },
                            icon = { Icon(Icons.Default.ChevronRight, null) }
                        )

                        AddWidth(16.dp)

                        ExtendedFloatingActionButton(
                            text = { Text("Retake") },
                            onClick = { capturedPhoto = null },
                            icon = { Icon(Icons.Default.Cameraswitch, null) }
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize()) {

             AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                factory = { ctx ->
                    Log.d(TAG, "PreviewView factory invoked")
                    PreviewView(ctx).apply {

                        scaleType = PreviewView.ScaleType.FILL_CENTER

                        Log.d(TAG, "Binding cameraController to lifecycle…")
                        try {
                            cameraController.bindToLifecycle(lifecycleOwner)
                            Log.d(TAG, "bindToLifecycle succeeded")
                        } catch (e: Exception) {
                            Log.e(TAG, "bindToLifecycle FAILED", e)
                        }

                        controller = cameraController
                        Log.d(TAG, "Controller set on PreviewView")
                    }
                }
            )

            if (capturedPhoto != null) {
                LastPhotoPreview(
                    modifier = Modifier.align(Alignment.BottomStart),
                    lastCapturedPhoto = capturedPhoto!!,
                    final = final,
                    navController = navController
                )
            }
        }
    }
}
