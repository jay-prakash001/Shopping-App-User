package com.jp.shoppingapp.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.sharp.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.sharp.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.jp.shoppingapp.common.ID
import com.jp.shoppingapp.domain.model.OrderModel
import com.jp.shoppingapp.domain.model.OrderParentModel
import com.jp.shoppingapp.domain.model.ProductModel
import com.jp.shoppingapp.presentation.navigation.Routes
import com.jp.shoppingapp.presentation.viewModel.ShoppingAppViewModel
import com.jp.shoppingapp.utils.ProductsBox
import com.jp.shoppingapp.utils.QuantitySelector
import com.jp.shoppingapp.utils.ShowIsWishListed
import com.jp.shoppingapp.utils.shimmerEffect
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.time.Duration
import java.util.Locale
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    modifier: Modifier = Modifier,
    viewModel: ShoppingAppViewModel = hiltViewModel(),
    navController: NavHostController
) {

    val state = viewModel.products.collectAsStateWithLifecycle().value
    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBar(title = {
            Text(text = "Products")
        }, navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(imageVector = Icons.AutoMirrored.Sharp.ArrowBack, contentDescription = "back")
            }
        })
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {

            ProductsList(
                products = state.data,
                navController = navController,
                viewModel = viewModel
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    modifier: Modifier = Modifier,
    product: ProductModel,
    navController: NavHostController,
    suggestedProducts: List<ProductModel>, viewModel: ShoppingAppViewModel
) {


    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = product.name.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.ROOT
                    ) else it.toString()
                })
            },
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
        }, modifier = Modifier.fillMaxSize()

    ) {
        LazyColumn(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {

            item {
                ProductDetail(
                    product = product,
                    suggestedProducts = suggestedProducts,
                    navController = navController,
                    viewModel = viewModel
                )

            }
        }
    }

}


@Composable
private fun ProductDetail(
    product: ProductModel,
    suggestedProducts: List<ProductModel>,
    navController: NavHostController,
    viewModel: ShoppingAppViewModel
) {

    LazyRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        itemsIndexed(product.images) { index, item ->
            Box(modifier = Modifier.fillMaxWidth()) {

                SubcomposeAsyncImage(
                    model = item,
                    contentDescription = "",
                    modifier = Modifier
                        .padding(start = 10.dp, end = 10.dp)
                        .fillMaxWidth()
                        .heightIn(260.dp, 300.dp)
                        .clip(
                            RoundedCornerShape(10.dp)
                        )
                )
                Text(
                    text = "${index + 1}/${product.images.size} ", modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            MaterialTheme.colorScheme.onSurface.copy(.5f)
                        )
                        .align(
                            Alignment.BottomEnd
                        )
                        .padding(10.dp), color = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
    Divider(
        Modifier
            .padding(vertical = 10.dp)
            .fillMaxWidth()
    )

    val quantity = remember {
        mutableStateOf(1)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = product.name.capitalize(Locale.ROOT),
                fontSize = 32.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Rs." + (product.price * quantity.value).toString(),
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold
            )

        }
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(10.dp), horizontalAlignment = Alignment.End
        ) {
            ShowIsWishListed(viewModel, product)
            Text(
                text = product.category.capitalize(Locale.ROOT),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.errorContainer,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .clip(RoundedCornerShape(40.dp))
                    .background(MaterialTheme.colorScheme.scrim)
                    .padding(10.dp)
            )

        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {

        Text(
            text = product.desc.capitalize(Locale.ROOT),
            fontSize = 14.sp,
            fontWeight = FontWeight.Light
        )
        Text(
            text = "Quantity", fontSize = 16.sp, fontWeight = FontWeight.Bold
        )
        val condition =
            viewModel.cartState.collectAsStateWithLifecycle().value.data.any {
                (it.product?.id ?: 0) == product.id
            }
        val scope = rememberCoroutineScope()
        QuantitySelector(quantity = quantity.value) {

            quantity.value = it
            if (condition) {
                scope.launch {
                    viewModel.deleteFromCart(OrderModel(product = product))
                }

            }
            println("ADDED $it || ${quantity.value}")

        }
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {

            AnimatedVisibility(visible = !condition) {
                OutlinedButton(onClick = {

//                    viewModel.addToCart(product)

                    viewModel.addCart(
                        OrderModel(
                            id = product.id,
                            product = product,
                            quantity = quantity.value
                        )
                    )
                }, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth(.5f)) {
                    Text(text = "Add To Cart")

                }

            }
            AnimatedVisibility(visible = condition) {
                OutlinedButton(onClick = {
//                    viewModel.deleteProductsFromCart(product)
                    viewModel.deleteFromCart(OrderModel(product = product))
                }, shape = RoundedCornerShape(10.dp), modifier = Modifier) {
                    Text(text = "Remove From Cart")

                }

            }

            Button(
                onClick = {
                    navController.navigate(Routes.CheckOut(product.id, quantity.value))
                }, shape = RoundedCornerShape(10.dp), modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ) {
                Text(text = "Buy Now", fontWeight = FontWeight.ExtraBold)
            }
        }
    }

//    }


    Divider(modifier = Modifier.fillMaxWidth())
    ProductsList(products = suggestedProducts, navController = navController, viewModel = viewModel)

}

@Composable
fun ProductsList(
    modifier: Modifier = Modifier,
    products: List<ProductModel>,
    navController: NavHostController,
    viewModel: ShoppingAppViewModel
) {
    val height =
        animateDpAsState(targetValue = if (products.size > 10) 800.dp else if (products.isNotEmpty()) 400.dp else 0.dp)
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxWidth()
            .height(height.value),
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.Top
    ) {
        items(products) {
            ProductsBox(product = it, onClick = {
//                println("PR $it")
                viewModel.searchProducts(it.id, ID)
                navController.navigate(
                    Routes.ProductDetail(
                        it.id
                    )
                )
            })
        }

    }
}

@Composable
fun ProductsListRow(
    modifier: Modifier = Modifier,
    products: List<ProductModel>,
    onClick: () -> Unit,
    onWishList: () -> Unit = {}
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.6f)
    ) {
        items(products) {
            ProductsBox(product = it)
        }

    }
}


@Composable
fun ProductsGrid(modifier: Modifier = Modifier, products: List<ProductModel>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(products) {
            ProductsBox(product = it)
            Spacer(modifier = Modifier.height(2.dp))

        }

    }
}


@Composable
fun ProductsShimmer(modifier: Modifier = Modifier) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2)
    ) {
        items(20) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(
                            RoundedCornerShape(10.dp)
                        )
                        .shimmerEffect()
                )

            }


        }

    }
}

