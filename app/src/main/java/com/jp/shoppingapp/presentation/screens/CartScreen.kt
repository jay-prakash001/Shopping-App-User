package com.jp.shoppingapp.presentation.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.jp.shoppingapp.domain.model.OrderModel
import com.jp.shoppingapp.presentation.navigation.Routes
import com.jp.shoppingapp.presentation.viewModel.ShoppingAppViewModel
import com.jp.shoppingapp.utils.ProductCardRow
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    viewModel: ShoppingAppViewModel,
    navController: NavHostController
) {
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Cart") },
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
                        navController.navigate(Routes.WishList)
                    },
                    modifier = Modifier.size(50.dp),
                    colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.onPrimary)
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "add to wishList",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp), tint = Color.Red.copy(.5f)
                    )
                }
            })
    }, modifier = Modifier.fillMaxSize())
    {
        CartItemsList(viewModel, modifier.padding(it), navController)
    }
}

@Composable
fun CartItemsList(
    viewModel: ShoppingAppViewModel,
    modifier: Modifier,
    navController: NavHostController
) {
    LaunchedEffect(Unit) {
        viewModel.getFromCart()
    }
    val items = viewModel.cartState.collectAsStateWithLifecycle().value.data
    val totalOrderValue = remember {
        mutableStateOf(0L)
    }
    LaunchedEffect(items) {
        totalOrderValue.value = items.sumOf { it.quantity * (it.product?.price ?:0 ) }
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()

    ) {
        item {
            Card(modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = "Total Products :${items.size}, Total Order Value :${totalOrderValue.value}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
        items(items) {


            CartProductsBox(
                modifier = Modifier
                    .padding(5.dp)
                    .border(
                        1.dp, Color.Gray,
                        RoundedCornerShape(10.dp)
                    ), orderModel = it, viewModel = viewModel, navController = navController
            )
        }
    }
}

@Composable
fun CartProductsBox(
    modifier: Modifier = Modifier,
    orderModel: OrderModel,
    viewModel: ShoppingAppViewModel,
    navController: NavHostController
) {
    Column(modifier = modifier.padding(5.dp)) {
        orderModel.product?.let {
            ProductCardRow(
                product = it,
                viewModel = viewModel,
                navController = navController
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Quantity : ${orderModel.quantity}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Light
                )
                Text(
                    text = "Total Price : ${orderModel.quantity} * ${orderModel.product.price} = ${orderModel.quantity * orderModel.product.price}Rs.",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

            }
        }
    }

}

