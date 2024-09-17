package com.jp.shoppingapp.presentation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.jp.shoppingapp.presentation.navigation.Routes
import com.jp.shoppingapp.presentation.viewModel.ShoppingAppViewModel
import com.jp.shoppingapp.utils.ProductCardRow
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishListScreen(
    modifier: Modifier = Modifier,
    viewModel: ShoppingAppViewModel,
    navController: NavHostController
) {

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "WishList") },
            navigationIcon = {
                IconButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.size(50.dp),
                    colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.onPrimary)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "add to wishList",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp)
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        navController.navigate(Routes.Cart)
                    },
                    modifier = Modifier.size(50.dp),
                    colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.onPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "add to cart",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp)
                    )
                }

            })
    }, modifier = Modifier.fillMaxSize())
    {
        WishListItemList(viewModel, modifier = Modifier.padding(it), navController)
    }
}

@Composable
 fun WishListItemList(
    viewModel: ShoppingAppViewModel,
   modifier: Modifier,
    navController: NavHostController
) {
     LaunchedEffect(Unit) {
         viewModel.getProductsFromWishList()
     }
    val items = viewModel.wishListState.collectAsStateWithLifecycle().value.data
    LazyColumn(
        modifier = modifier
            .fillMaxSize()

    ) {
        items(items) {
            ProductCardRow(modifier = Modifier.padding(5.dp).border(1.dp, Color.Gray, RoundedCornerShape(10.dp)),product = it, viewModel = viewModel, navController = navController)
        }
    }
}