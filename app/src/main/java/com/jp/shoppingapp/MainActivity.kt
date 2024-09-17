package com.jp.shoppingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.jp.shoppingapp.presentation.navigation.AppNavigation
import com.jp.shoppingapp.presentation.viewModel.ShoppingAppViewModel
import com.jp.shoppingappadmin.ui.theme.ShoppingAppAdminTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var viewModel: ShoppingAppViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingAppAdminTheme {
//                viewModel = hiltViewModel()
                AppNavigation(firebaseAuth = firebaseAuth, modifier = Modifier.fillMaxSize())


            }
        }
    }
}
