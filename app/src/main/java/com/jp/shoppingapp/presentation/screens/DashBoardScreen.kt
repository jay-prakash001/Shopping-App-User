package com.jp.shoppingapp.presentation.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.jp.shoppingapp.R
import com.jp.shoppingapp.presentation.navigation.Routes
import com.jp.shoppingapp.presentation.viewModel.ShoppingAppViewModel
import com.jp.shoppingapp.utils.BannerRow
import com.jp.shoppingapp.utils.CategoryList
import com.jp.shoppingapp.utils.Feature
import com.jp.shoppingapp.utils.SearchField
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashBoardScreen(
    modifier: Modifier = Modifier,
    viewModel: ShoppingAppViewModel = hiltViewModel(),
    navController: NavHostController, firebaseAuth: FirebaseAuth
) {

    val selectedIndex = remember {
        mutableIntStateOf(0)
    }
    val title = remember {
        mutableStateOf("DashBoard")
    }
    val condition = remember {
        mutableStateOf(false)
    }
    Scaffold(topBar = {

        TopAppBar(title = {
            Text(
                text = title.value.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
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
        }, actions = {
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
        }
        )

    }, modifier = Modifier.fillMaxSize(), bottomBar = {
        BottomBar {
            selectedIndex.intValue = it
            condition.value = !condition.value

        }
    }) {


        val rotation = animateFloatAsState(
            targetValue = if (condition.value) 180f else 360f,
            animationSpec = tween(500)
        )
        Column(
            modifier = Modifier
                .graphicsLayer {
                    shadowElevation = 2f
                    rotationY = rotation.value
                    cameraDistance = 100f

                }
                .graphicsLayer {

                    shadowElevation = 2f
                    rotationY = 360 - rotation.value
                    cameraDistance = 100f


                }
                .fillMaxSize()
                .padding(it)
        ) {

            when (selectedIndex.intValue) {
                0 -> {

                    Dash(modifier = Modifier, navController = navController, viewModel = viewModel)
                    title.value = "DashBoard"
                }

                1 -> {
                    CategoriesScreen(navController = navController, viewModel = viewModel)
                    title.value = "Categories"

                }

                2 -> {
                    CartItemsList(
                        viewModel = viewModel,
                        modifier = Modifier,
                        navController = navController
                    )
                    title.value = "Cart"
                }

                3 -> {

                    OrdersComposable(navController = navController, viewModel = viewModel)
                    title.value = "Orders"

                }

                else -> {
                    ProfileScreen(
                        firebaseAuth = firebaseAuth,
                        navController = navController,
                        viewModel = viewModel
                    )
                    title.value = "Profile"
                }
            }
        }
    }
}

@Composable
fun Dash(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: ShoppingAppViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getCategories()
        viewModel.getAllProducts()
        viewModel.getBanners()
    }

    val categories = viewModel.getCategories.collectAsStateWithLifecycle().value.data
    val products = viewModel.products.collectAsStateWithLifecycle().value.data
    val banners = viewModel.bannerState.collectAsStateWithLifecycle().value.data
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        item {
            SearchField(navController = navController, viewModel = viewModel)
            Feature()
            CategoryList(
                modifier = Modifier.padding(10.dp, 0.dp),
                navController = navController,
                categories = categories
            )
            BannerRow(banners = banners, navController = navController)
        }


        item {
            ProductsList(products = products, navController = navController, viewModel = viewModel)

        }


    }
}