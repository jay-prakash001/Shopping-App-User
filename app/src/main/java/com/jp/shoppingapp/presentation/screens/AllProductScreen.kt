package com.jp.shoppingapp.presentation.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jp.shoppingapp.presentation.viewModel.ShoppingAppViewModel

@Composable
fun AllProductScreen(
    modifier: Modifier = Modifier,
    viewModel: ShoppingAppViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.getAllProducts()
    }
    val products = viewModel.products.collectAsStateWithLifecycle().value
    if (products.isLoading) {
        ProductsShimmer()

    } else if (products.data.isNotEmpty()) {
        ProductsGrid(products = products.data)
    }else{
        Text(text = products.error)
    }
}