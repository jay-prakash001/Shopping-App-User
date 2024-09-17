package com.jp.shoppingapp.presentation.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.jp.shoppingapp.R
import com.jp.shoppingapp.presentation.navigation.Routes
import com.jp.shoppingapp.presentation.navigation.SubNavigation
import com.jp.shoppingapp.presentation.viewModel.ShoppingAppViewModel
import com.jp.shoppingappadmin.ui.theme.cyan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: ShoppingAppViewModel = hiltViewModel(),
    navController: NavHostController
) {

    val email = remember {
        mutableStateOf<String>("")
    }

    val password = remember {
        mutableStateOf<String>("")
    }
    val state = viewModel.loginState.collectAsStateWithLifecycle().value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        val img = rememberAsyncImagePainter(model = R.drawable.eimg)
        Image(
            painter = img, contentDescription = "image", modifier = Modifier
                .size(200.dp)
                .align(Alignment.TopStart)
        )
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                CircularProgressIndicator()
            }
        } else if (state.error.isNotBlank()) {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                Icon(imageVector = Icons.Default.Error, contentDescription = "")
                Text(text = state.error.toString())
            }
        } else if (state.userData != null) {
            Text(text = "logged in")
            AlertDialog(onDismissRequest = { /*TODO*/ }, confirmButton = {
                Button(onClick = {
                    navController.clearBackStack(SubNavigation.LoginSignUp)
                    navController.navigate(SubNavigation.MainHome) }) {
                    Text(text = "go to home")
                }
            }, text = {

                Text(text = "go to home")
            },icon = {
                Image(imageVector = Icons.Default.Dashboard, contentDescription ="dash icon")

            })
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(imageVector = Icons.Rounded.Person, contentDescription = "person Icon")
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text(text = "Email") })
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text(text = "Password") })
                Spacer(modifier = Modifier.height(10.dp))
                val context = LocalContext.current
                Button(onClick = {

                    viewModel.login(email.value, password.value)
                    Log.d("LOGIN", "LoginScreen: ${state.userData}")
                    Toast.makeText(
                        context,
                        viewModel.signUpState.value.toString(),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }, colors = ButtonDefaults.buttonColors(containerColor = cyan)) {
                    Text(text = "Login")
                }
                TextButton(onClick = {
                    navController.navigate(Routes.SignUp)
                }) {
                    Text(text = "Create account?")
                }
            }
        }

        Image(
            painter = img,
            contentDescription = "image",
            modifier = Modifier
                .size(200.dp)
                .rotate(180f)
                .align(Alignment.BottomEnd)
        )


    }

}