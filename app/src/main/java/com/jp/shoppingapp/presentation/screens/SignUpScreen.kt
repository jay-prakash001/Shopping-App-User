package com.jp.shoppingapp.presentation.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import com.jp.shoppingapp.R
import com.jp.shoppingapp.domain.model.User
import com.jp.shoppingapp.presentation.viewModel.ShoppingAppViewModel
import com.jp.shoppingappadmin.ui.theme.cyan

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: ShoppingAppViewModel = hiltViewModel(),
    navController: NavHostController
) {

    val name = remember {
        mutableStateOf<String>("")
    }
    val email = remember {
        mutableStateOf<String>("")
    }
    val phone = remember {
        mutableStateOf<String>("")
    }
    val password = remember {
        mutableStateOf<String>("")
    }
    val confirmPassword = remember {
        mutableStateOf<String>("")
    }
    val profileImg = remember {
        mutableStateOf("")
    }
    val galleryLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
        uri->
        if (uri != null) {
            viewModel.uploadImage(uri){
                profileImg.value = it
            }
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surface)) {
        val img = rememberAsyncImagePainter(model = R.drawable.eimg)

        Image(
            painter = img, contentDescription = "image", modifier = Modifier
                .size(200.dp)
                .align(Alignment.TopStart)
        )
        val state = viewModel.signUpState.collectAsStateWithLifecycle().value

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                CircularProgressIndicator()
            }
        } else if (state.error.isNotBlank()) {

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                Icon(imageVector = Icons.Default.Error, contentDescription = "")
                Text(text = state.error.toString())
            }
        }
        else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = {
                    galleryLauncher.launch("image/*")
                }) {
                    if (profileImg.value.isBlank()){
                        Icon(imageVector = Icons.Rounded.Person, contentDescription = "")
                    }else{
                        SubcomposeAsyncImage(model = profileImg.value, contentDescription = "profile Image")

                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text(text = "Name") })
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text(text = "Email") })
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = phone.value,
                    onValueChange = { phone.value = it },
                    label = { Text(text = "Phone no.") })
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text(text = "Password") })
                Spacer(modifier = Modifier.height(10.dp))
                TextField(
                    value = confirmPassword.value,
                    onValueChange = { confirmPassword.value = it },
                    label = { Text(text = "Confirm Password") })

                Spacer(modifier = Modifier.height(10.dp))
                val context = LocalContext.current
                Button(onClick = {
                    viewModel.createUser(
                        user = User(
                            name = name.value,
                           email =  email.value,
                           phone =  phone.value,
                           password =  password.value,
                            profileImg = profileImg.value
                        )
                    )
                    Toast.makeText(
                        context,
                        viewModel.signUpState.value.toString(),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }, colors = ButtonDefaults.buttonColors(containerColor = cyan)) {
                    Text(text = "Sign Up")
                }
                
                TextButton(onClick = {
                    navController.navigateUp()
                }) {
                    Text(text = "Already have an account?")
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