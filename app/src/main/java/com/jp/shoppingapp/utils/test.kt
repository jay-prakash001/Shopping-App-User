package com.jp.shoppingapp.utils

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jp.shoppingapp.domain.model.ProductModel
import com.jp.shoppingapp.presentation.viewModel.ShoppingAppViewModel

@Composable
fun TestUI(modifier: Modifier = Modifier, viewModel: ShoppingAppViewModel = hiltViewModel()) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "hello")
        Button(onClick = { viewModel.addToWishList(ProductModel("10","abc",System.currentTimeMillis(),"new", 100,"alldlsk",
            emptyList())) }) {

        }
    }
}
@Preview(showSystemUi = true)
@Composable
fun QuantitySelectorPreview() {
    var quantity by remember { mutableStateOf(1) }

    QuantitySelector(
        quantity = quantity,
        onQuantityChange = { newQuantity ->
            quantity = newQuantity
        }
    )
}
