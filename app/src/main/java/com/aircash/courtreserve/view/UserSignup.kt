package com.aircash.courtreserve.view

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aircash.courtreserve.models.model.RegisterRequest
import com.aircash.courtreserve.models.network.NetworkResponse
import com.aircash.courtreserve.ui.theme.IBMPlex
import com.aircash.courtreserve.ui.theme.Lexend
import com.aircash.courtreserve.ui.theme.buttonDark
import com.aircash.courtreserve.ui.theme.buttonLight
import com.aircash.courtreserve.ui.theme.primary
import com.aircash.courtreserve.viewmodels.navigation.Screens
import com.aircash.courtreserve.viewmodels.viewmodel.UserAuthViewModel
import kotlinx.coroutines.delay

@Composable
fun UserSignup(
    navController: NavController,
    userAuthViewModel: UserAuthViewModel = hiltViewModel()
) {
    Surface {
        val (name, setName) = remember { mutableStateOf("") }
        val (email, setEmail) = remember { mutableStateOf("") }
        val (password, setPassword) = remember { mutableStateOf("") }
        val color = if (isSystemInDarkTheme()) buttonDark else buttonLight
        var passwordVisibility by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val icon = if (passwordVisibility) Icons.Default.RemoveRedEye else Icons.Default.Block
        val keyboardController = LocalSoftwareKeyboardController.current
        val registerResult = userAuthViewModel.registerResult.collectAsState()
        var requestreceived by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConstraintLayout (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 50.dp),
            ) {
                val (title, loginField) = createRefs()

                Row (
                    modifier = Modifier.constrainAs(title) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(loginField.top, margin = 50.dp)
                    }.fillMaxWidth(fraction = 0.9f).height(50.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.School,
                        contentDescription = null,
                        modifier = Modifier.size(35.dp)
                    )

                    AddWidth(10.dp)

                    Text("Court Reserve", fontFamily = IBMPlex, fontSize = 35.sp)
                }

                Column (
                    modifier = Modifier.constrainAs(loginField) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row (
                        modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text("Your email address", fontFamily = Lexend)
                    }

                    AddHeight(20.dp)

                    Input(
                        label = "ali@uni.com",
                        value = email,
                        onValueChange = setEmail,
                        color = color
                    )

                    AddHeight(20.dp)

                    Row (
                        modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text("Your name", fontFamily = Lexend)
                    }

                    AddHeight(20.dp)

                    Input(
                        label = "name",
                        value = name,
                        onValueChange = setName,
                        color = color
                    )

                    AddHeight(20.dp)

                    Row (
                        modifier = Modifier.fillMaxWidth(fraction = 0.9f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text("Your password", fontFamily = Lexend)
                    }

                    AddHeight(20.dp)

                    Input(
                        label = "password",
                        value = password,
                        onValueChange = setPassword,
                        color = color,
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    passwordVisibility = !passwordVisibility
                                }
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        },
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    )

                    AddHeight(40.dp)

                    if (!isLoading) {
                        Button(
                            modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp),
                            shape = RoundedCornerShape(20.dp),
                            onClick = {
                                if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty()) {
                                    keyboardController?.hide()
                                    isLoading = true
                                    requestreceived = true
                                    val userRegisterRequest =
                                        RegisterRequest(
                                            email = email,
                                            password = password,
                                            name = name
                                        )
                                    userAuthViewModel.userRegister(userRegisterRequest)
                                }
                                else if (email.isEmpty()) {
                                    Toast.makeText(context, "Enter Email", Toast.LENGTH_SHORT).show()
                                }
                                else if (password.isEmpty()) {
                                    Toast.makeText(context, "Enter password", Toast.LENGTH_SHORT).show()
                                }
                                else if (name.isEmpty()) {
                                    Toast.makeText(context, "Enter Name", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primary,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Login", fontFamily = Lexend)
                        }

                        AddHeight(30.dp)

                        Text("Not a user? Signup", modifier = Modifier.clickable{

                        }, color = primary, fontFamily = Lexend)
                    }
                    else {
                        Box(
                            modifier = Modifier.fillMaxWidth(fraction = 0.9f).height(50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }

                    if (email.isNotEmpty() && password.isNotEmpty() && name.isNotEmpty() && requestreceived) {
                        when (registerResult.value) {
                            is NetworkResponse.Failure -> {
                                isLoading = false
                                Toast.makeText(context, "Incorrect Credentials", Toast.LENGTH_LONG).show()
                                requestreceived = false
                            }

                            NetworkResponse.Loading -> { isLoading = true }
                            is NetworkResponse.Success -> {
                                isLoading = false
                                LaunchedEffect(Unit) {
                                    Toast.makeText(context, "Register Successful", Toast.LENGTH_SHORT).show()
                                    delay(2000)
                                    navController.navigate(route = Screens.UserLanding.route)
                                }
                            }
                            null -> {}
                        }
                    }
                }
            }
        }
    }
}