package com.jp.shoppingapp.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.jp.shoppingapp.domain.model.OrderParentModel
import com.jp.shoppingapp.presentation.viewModel.ShoppingAppViewModel

@Composable
fun OrdersComposable(
    modifier: Modifier = Modifier,
    viewModel: ShoppingAppViewModel = hiltViewModel(),
    navController: NavHostController
) {
    LaunchedEffect(Unit) {
        viewModel.getOrder()
    }
    val state = viewModel.orderState.collectAsStateWithLifecycle().value
    val orders = state.data
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(orders!!) {
            OrderProductCard(
                orderParentModel = it!!,
                viewModel = viewModel,
                navController = navController
            )
        }
    }
}

@Composable
fun OrderProductCard(
    modifier: Modifier = Modifier,
    orderParentModel: OrderParentModel,
    viewModel: ShoppingAppViewModel,
    navController: NavHostController
) {

    var isExpanded by remember {
        mutableStateOf(false)
    }
    val isCancelled = orderParentModel.status.contains("Cancelled")
    val isDelivered = orderParentModel.status.contains("Delivered")

    val color = animateColorAsState(
        targetValue = if (isCancelled) Color.Red else if (isDelivered) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary,
        animationSpec = tween(1000)
    )

    Column(
        modifier = Modifier
            .padding(1.dp)
            .fillMaxWidth()
            .background(if (isCancelled || isDelivered) color.value.copy(.1f) else MaterialTheme.colorScheme.background)
            .border(
                2.dp,
                if (isCancelled || isDelivered) color.value else Color.LightGray,
                RoundedCornerShape(10.dp)
            ),
        verticalArrangement = Arrangement.Top, horizontalAlignment = Alignment.End
    ) {
        orderParentModel.order?.let {
            CartProductsBox(
                orderModel = it,
                viewModel = viewModel,
                navController = navController
            )

            Button(
                onClick = {

                    viewModel.cancelOrder(orderParentModel = orderParentModel)


                },
                enabled = !(orderParentModel.status.contains("Cancelled") || orderParentModel.status.contains(
                    "Delivered"
                )),

                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.padding(horizontal = 10.dp)
            ) {
                Text(text = "Cancel")
            }
            Row(
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Status")
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "see all")
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = if (isCancelled || isDelivered) Arrangement.SpaceBetween else Arrangement.spacedBy(
                        6.dp
                    )
                ) {
                    orderParentModel.status.forEach {

                        Text(
                            text = it,
                            fontSize = if (isCancelled) 10.sp else 8.sp,
                            color = MaterialTheme.colorScheme.onError,
                            modifier = Modifier
                                .clip(
                                    RoundedCornerShape(50.dp)
                                )
                                .background(color = color.value)

                        )
                    }
                }
                Slider(
                    value = (orderParentModel.status.size - 1).toFloat(),
                    onValueChange = {},
                    valueRange = 0f..if (isCancelled || isDelivered) 1f else 5f,
                    modifier = Modifier,
                    colors = SliderDefaults.colors(
                        activeTrackColor = color.value,
                        thumbColor = color.value
                    )
                )

            }
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: ShoppingAppViewModel
) {

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Orders") }, navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "navigate back"
                )
            }
        })
    }) {
        OrdersComposable(
            modifier = modifier.padding(it),
            navController = navController,
            viewModel = viewModel
        )

    }
}


