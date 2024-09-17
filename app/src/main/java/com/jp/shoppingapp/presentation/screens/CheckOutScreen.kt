package com.jp.shoppingapp.presentation.screens

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.jp.shoppingapp.domain.model.OrderModel
import com.jp.shoppingapp.domain.model.OrderParentModel
import com.jp.shoppingapp.domain.model.ProductModel
import com.jp.shoppingapp.presentation.navigation.Routes
import com.jp.shoppingapp.presentation.viewModel.ShoppingAppViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckOutScreen(
    product: ProductModel,
    quantity: Int,
    navController: NavHostController,
    viewModel: ShoppingAppViewModel
) {
    val order = OrderModel(product.id, product, quantity)
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Checkout") }, navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "navigate back"
                )
            }
        })

    }) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                CartProductsBox(
                    orderModel = order,
                    viewModel = viewModel,
                    navController = navController
                )
            }
            item {
                CashOnDeliveryCard()
            }
            item {
                OrderSummaryBox(orderModel = order, quantity = quantity) {
                    viewModel.addOrder(it)
                    navController.navigateUp()
                    navController.navigate(Routes.Orders)
                }
            }

        }
    }
}

@Composable
fun CashOnDeliveryCard() {
    val condition = remember {
        mutableStateOf(false)
    }

    val fontSize =
        animateIntAsState(targetValue = if (condition.value) 30 else 10, animationSpec = tween(500))
    LaunchedEffect(key1 = Unit) {
        while (true) {
            condition.value = !condition.value
            delay(500)
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .height(40.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Cash On Delivery",
                fontWeight = FontWeight.Bold, fontSize = fontSize.value.sp,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun OrderSummaryBox(
    orderModel: OrderModel,
    quantity: Int = 1,
    onPlaceOrder: (OrderParentModel) -> Unit
) {
    var name by remember {
        mutableStateOf("")
    }
    var phone by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var pincode by remember {
        mutableStateOf("")
    }
    var address by remember {
        mutableStateOf("")
    }
    var otherDesc by remember {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SummaryTextField(name, Icons.Default.Person, "Name", singleLine = true) {
            name = it
        }
        SummaryTextField(phone, Icons.Default.Phone, "Phone", singleLine = true) {
            phone = it
        }
        SummaryTextField(email, Icons.Default.Phone, "Email", singleLine = true) {
            email = it
        }
        SummaryTextField(address, Icons.Default.LocationOn, "Address", false, maxLine = 3) {
            address = it
        }
        SummaryTextField(pincode, Icons.Default.Pin, "Pin code", singleLine = true) {
            pincode = it
        }
        SummaryTextField(otherDesc, Icons.Default.Description, "Description", false, maxLine = 3) {
            otherDesc = it
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            Button(
                onClick = {
                    val orderToPlace = OrderParentModel(
                        order = orderModel,
                        name = name,
                        contact = phone,
                        email = email,
                        address = "$address $pincode",
                        desc = otherDesc,
                        status = listOf("Order Placed"),
                        totalPrice = ((orderModel.product?.price
                            ?: 1) * quantity).toString()
                    )

                    onPlaceOrder(orderToPlace)


                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Checkout")
            }
        }
    }
}

@Composable
private fun SummaryTextField(
    value: String,
    icon: ImageVector,
    label: String, singleLine: Boolean,
    maxLine: Int = 1,
    onValueChange: (String) -> Unit

) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(it)
        }, singleLine = singleLine,
        leadingIcon = {
            Icon(imageVector = icon, contentDescription = "")
        }, maxLines = maxLine,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 0.dp)
            .fillMaxWidth(),
        label = {
            Text(text = label, fontSize = 10.sp)
        })
}


