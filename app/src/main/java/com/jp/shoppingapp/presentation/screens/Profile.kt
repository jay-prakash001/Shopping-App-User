package com.jp.shoppingapp.presentation.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.jp.shoppingapp.R
import com.jp.shoppingapp.presentation.navigation.SubNavigation
import com.jp.shoppingapp.presentation.viewModel.ShoppingAppViewModel
import com.jp.shoppingapp.utils.ShimmerEffectBox
import com.jp.shoppingappadmin.ui.theme.cyan

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ShoppingAppViewModel = hiltViewModel(),
    firebaseAuth: FirebaseAuth,
    navController: NavHostController
) {
    LaunchedEffect(key1 = true) {
        val uid = firebaseAuth.currentUser?.uid
        if (uid != null) {
            viewModel.getUserByUid(uid)
        }
    }
    val user = viewModel.userState.collectAsStateWithLifecycle().value.userDataParent?.user
    val isEditing = remember {
        mutableStateOf(false)
    }


    if (user == null) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                trackColor = cyan.copy(alpha = .5f),
                color = cyan
            )
        }
    } else {
        val profileImg = remember {
            mutableStateOf(user.profileImg.toString())
        }
        val name = remember {
            mutableStateOf(user.name.toString())
        }
        val email = remember {
            mutableStateOf(user.email.toString())
        }
        val phone = remember {
            mutableStateOf(user.phone.toString())
        }
        val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                viewModel.uploadImage(it) {
                    profileImg.value = it
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var isRounded by remember {
                mutableStateOf(false)
            }

            val borderRadius by animateIntAsState(
                targetValue = if (isRounded) 40 else 15,

                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            val color by animateColorAsState(
                if (isRounded) Color.Gray else Color.White,
                animationSpec = tween(durationMillis = 1000, easing = EaseIn)
            )

            val imgSize by animateIntAsState(
                targetValue = if (isRounded) 180 else 200,

                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioHighBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
            Box(
                modifier = Modifier
                    .clip(
                        RoundedCornerShape((if (isRounded) borderRadius else 10).dp)
                    )
                    .size(210.dp)
                    .background(color), contentAlignment = Alignment.Center
            ) {

                if (profileImg.value != "") {
                    SubcomposeAsyncImage(model = profileImg.value,
                        contentDescription = "profileImage",
                        modifier = Modifier
                            .size(imgSize.dp)
                            .clip(
                                RoundedCornerShape(borderRadius.dp)
                            )
                            .clickable {
                                isRounded = !isRounded
                            },
                        contentScale = ContentScale.Crop,
                        loading = {
                            ShimmerEffectBox()
                        }
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.man),
                        contentDescription = "",
                        modifier = Modifier.size(100.dp)
                    )
                }
                if (isEditing.value) {

                    IconButton(
                        onClick = {
                            launcher.launch("image/*")
                        },
                        modifier = Modifier.align(Alignment.BottomEnd),
                        colors = IconButtonDefaults.iconButtonColors(
                            MaterialTheme.colorScheme.inversePrimary.copy(.7f)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Edit Button",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )

                    }
                }
            }


            OutlinedTextField(
                value = name.value,
                onValueChange = {
                    name.value = it
                },
                readOnly = !isEditing.value,
                label = {
                    Text(text = "Name")
                },
                trailingIcon = {
                    Icon(imageVector = Icons.Rounded.Person, contentDescription = "name")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = email.value.toString(),
                onValueChange = {
                    email.value = it
                },
                readOnly = !isEditing.value,
                label = {
                    Text(text = "Email")
                },
                trailingIcon = {
                    Icon(imageVector = Icons.Rounded.Email, contentDescription = "email")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = phone.value.toString(),
                onValueChange = {
                    phone.value = it
                },
                readOnly = !isEditing.value,
                label = {
                    Text(text = "Phone")
                },
                trailingIcon = {
                    Icon(imageVector = Icons.Rounded.Phone, contentDescription = "phone")
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            Spacer(modifier = Modifier.height(10.dp))



            AnimatedVisibility(visible = !isEditing.value) {
                Column(
                    modifier = Modifier.padding(10.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            isEditing.value = true
                        }, modifier = Modifier.fillMaxWidth(.5f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(text = "Edit")

                    }
                    Button(
                        onClick = {
                            firebaseAuth.signOut()
                            navController.navigate(SubNavigation.LoginSignUp)
                        },
                        modifier = Modifier.fillMaxWidth(.5f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text(text = "Log Out")

                    }
                }
            }
            AnimatedVisibility(visible = isEditing.value) {
                Button(
                    onClick = {
                        isEditing.value = false
                        firebaseAuth.currentUser?.let {
                            viewModel.updateUser(
                                name.value,
                                email.value,
                                phone.value,
                                profileImg = profileImg.value,
                                uid = it.uid
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(.5f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    enabled = name.value.isNotBlank() && email.value.isNotBlank() && phone.value.isNotBlank() && profileImg.value.isNotBlank() && !viewModel.isUploading.collectAsStateWithLifecycle().value
                ) {
                    Text(text = "Save")

                }

            }
        }

    }


}

