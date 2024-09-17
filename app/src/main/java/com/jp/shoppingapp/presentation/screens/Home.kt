package com.jp.shoppingapp.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jp.shoppingapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(modifier: Modifier = Modifier, navController :NavHostController) {
    Scaffold(topBar = {

        TopAppBar(title = {
            Text(
                text = "DashBoard",
                fontWeight = FontWeight.SemiBold,
                fontSize = MaterialTheme.typography.titleLarge.fontSize
            )
        }, navigationIcon = {
            Image(
                painter = painterResource(id = R.drawable.shoppingbag),
                contentDescription = "app logo",
                modifier = Modifier
                    .padding(10.dp)
                    .size(50.dp)
            )
        })

    }, modifier = Modifier.fillMaxSize(), bottomBar = {
        BottomBar()
    }){
        Box(modifier = Modifier.padding(it)){
            Dash(navController = navController)
        }
    }
}