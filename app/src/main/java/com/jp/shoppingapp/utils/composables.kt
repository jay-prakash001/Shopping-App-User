package com.jp.shoppingapp.utils

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.AssignmentReturned
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.ShoppingCartCheckout
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Minimize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.jp.shoppingapp.R
import com.jp.shoppingapp.common.ID
import com.jp.shoppingapp.domain.model.OrderModel
import com.jp.shoppingapp.domain.model.ProductModel
import com.jp.shoppingapp.presentation.navigation.Routes
import com.jp.shoppingapp.presentation.viewModel.ShoppingAppViewModel
import com.jp.shoppingappadmin.domain.model.CategoryModel
import kotlinx.coroutines.delay
import java.util.Locale


@Composable
fun ShimmerEffectBox(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Box(
        modifier = modifier
            .size(100.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Gray.copy(alpha = 0.3f),
                        Color.Gray.copy(alpha = 0.7f),
                        Color.Gray.copy(alpha = 0.3f)
                    ),
                    start = Offset(translateAnim, 0f),
                    end = Offset(translateAnim + 1000f, 100f)
                )
            )
    )
}

@Composable
fun CategoryList(
    modifier: Modifier = Modifier,
    navController: NavController,
    categories: List<CategoryModel>
) {

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
    ) {
        items(categories) {
            CategoryBox(category = it) {
                navController.navigate(Routes.ProductsScreen(it.name))
            }
        }

    }

}

@Composable
fun CategoryBox(
    modifier: Modifier = Modifier,
    category: CategoryModel,
    imgSize: Dp = 60.dp,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(2.dp)
            .clickable { onClick() },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        SubcomposeAsyncImage(
            model = category.img,
            contentDescription = category.name,
            loading = {
                ShimmerEffectBox()
            },
            modifier = Modifier
                .clip(CircleShape)
                .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .size(imgSize), contentScale = ContentScale.Crop
        )
        Text(text = category.name.capitalize(Locale.ROOT), fontSize = 10.sp)
    }
}

@Composable
fun ProductsBox(
    modifier: Modifier = Modifier,
    product: ProductModel,
    onClick: () -> Unit = {}
) {


    Column(
        modifier = Modifier
            .fillMaxWidth(.5f)
            .padding(10.dp)
            .border(
                1.dp, Color(0xFFBBB9B9), RoundedCornerShape(10.dp)
            )
            .clickable {
                onClick()
            }
            .clip(RoundedCornerShape(10.dp)),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val thumbnail = if (product.images.isNotEmpty()) product.images[0] else ""
        SubcomposeAsyncImage(
            model = thumbnail,
            contentDescription = product.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(
                    RoundedCornerShape(10.dp)
                ),
            loading = {
                ShimmerEffectBox()
            }, contentScale = ContentScale.Crop
        )


        Text(
            text = product.name.capitalize(Locale.ROOT),
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp
        )
        Text(
            text = "Rs.${product.price}",
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp
        )


    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = InfiniteRepeatableSpec(
            tween(1000)

        ), label = ""
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFBBB9B9),
                Color(0xFF797777),
                Color(0xFFBBB9B9)
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )

    )
        .onGloballyPositioned {
            size = it.size
        }
}

@Composable
fun TestTransition(modifier: Modifier = Modifier, viewModel: ShoppingAppViewModel) {
    Box(modifier = modifier.fillMaxSize())
}


@Composable
fun SearchField(navController: NavController, viewModel: ShoppingAppViewModel) {
    Box(modifier = Modifier.fillMaxWidth()) {
        val searchInput = remember {
            mutableStateOf("")
        }
        val changingLabel = remember {
            mutableStateOf(true)
        }
        val label = remember {
            mutableStateOf("Search")
        }
        LaunchedEffect(Unit) {
            while (changingLabel.value) {
                viewModel.getCategories.value.data.forEach {
                    label.value = it.name
                    delay(1000)
                }
                delay(1000)
            }
        }
        OutlinedTextField(
            value = searchInput.value,
            onValueChange = {
                searchInput.value = it
                changingLabel.value = false
                println("changing Label ${changingLabel.value}")
            },
            trailingIcon = {
                IconButton(onClick = {
                    navController.navigate(Routes.SearchProducts(searchInput.value))

                }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "search")
                }
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .height(80.dp)
                .padding(10.dp)
                .fillMaxWidth(),
            label = {
                Text(text = label.value, fontSize = 10.sp, modifier = Modifier)
            }, singleLine = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Feature(modifier: Modifier = Modifier) {
    val changeColor = remember {
        mutableStateOf(false)
    }
    LaunchedEffect(Unit) {
        while (true) {
            changeColor.value = !changeColor.value
            delay(850)

        }
    }

    val borderStroke0 =
        animateColorAsState(
            targetValue = if (changeColor.value) Color.Transparent else MaterialTheme.colorScheme.primary,
            animationSpec = tween(800)
        )
    val borderStroke1 =
        animateColorAsState(
            targetValue = if (changeColor.value) MaterialTheme.colorScheme.primary else Color.Transparent,
            animationSpec = tween(800)
        )

    Card(
        onClick = { /*TODO*/ },
        elevation = CardDefaults.elevatedCardElevation(12.dp),
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(5.dp)
            .padding(horizontal = 12.dp)
            .fillMaxWidth()
            .border(
                2.dp,
                brush = Brush.linearGradient(
                    listOf(
                        borderStroke0.value,
                        Color.Transparent,
                        borderStroke1.value,
                        borderStroke1.value
                    )
                ),
                RoundedCornerShape(14.dp)
            )
            .height(60.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.inverseOnSurface)
    ) {
        Row(
            Modifier
                .fillMaxSize()
                .padding(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            LabelCard(Icons.Default.AssignmentReturned, stringResource(id = R.string.return_days))
            Divider(
                Modifier
                    .height(30.dp)
                    .width(2.dp)
            )
            LabelCard(Icons.Default.AddCard, stringResource(id = R.string.cod))
            Divider(
                Modifier
                    .height(30.dp)
                    .width(2.dp)
            )
            LabelCard(Icons.Default.Money, stringResource(id = R.string.price))
        }
    }
}

@Composable
private fun LabelCard(icon: ImageVector, text: String) {

    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = text, lineHeight = TextUnit(15f, TextUnitType.Sp),
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCardRow(
    modifier: Modifier = Modifier,
    product: ProductModel,
    viewModel: ShoppingAppViewModel,
    navController: NavController
) {


    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(150.dp, 200.dp), onClick = {
            navController.navigate(Routes.ProductDetail(product.id))
        }, colors = CardDefaults.cardColors(Color.White.copy(.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SubcomposeAsyncImage(
                model = product.images[0], contentDescription = product.name, modifier = Modifier
                    .size(120.dp)
                    .clip(
                        RoundedCornerShape(20.dp)
                    ), contentScale = ContentScale.Crop
            )
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = product.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                    fontWeight = FontWeight.Light,
                    fontSize = 15.sp
                )
                Text(
                    text = "Rs.${product.price}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
            }
            Row {
                ShowIsWishListed(viewModel = viewModel, product = product)
                ShowIsAddedToCart(viewModel = viewModel, product = product)
            }
        }
    }
}


@Composable
fun ShowIsWishListed(
    viewModel: ShoppingAppViewModel,
    product: ProductModel
) {
    val condition =
        viewModel.wishListState.collectAsStateWithLifecycle().value.data.any { it == product }
    val wishListIcon = if (condition) {
        Icons.Default.Favorite

    } else {
        Icons.Default.FavoriteBorder
    }
    IconButton(
        onClick = {
            if (condition) {
                viewModel.deleteProductsFromWishList(product)
            } else {
                viewModel.addToWishList(product)
            }

            viewModel.getProductsFromWishList()

        },
        modifier = Modifier.size(50.dp),
        colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.onPrimary)
    ) {
        Icon(
            imageVector = wishListIcon,
            contentDescription = "add to wishList",
            tint = Color.Red.copy(.5f),
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        )
    }
}


@Composable
fun ShowIsAddedToCart(
    viewModel: ShoppingAppViewModel,
    product: ProductModel
) {
    val condition =
        viewModel.cartState.collectAsStateWithLifecycle().value.data.any { it.id == product.id }
    val wishListIcon = if (condition) {
        Icons.Default.ShoppingCart

    } else {
        Icons.Default.ShoppingCartCheckout
    }
    IconButton(
        onClick = {
            if (condition) {
                viewModel.deleteFromCart(OrderModel(id = product.id, product = product))
            } else {
//                viewModel.addToCart(product)
                viewModel.addCart(OrderModel(id = product.id, product = product))
            }

//            viewModel.getProductsFromCart()
//            viewModel.getFromCart()

        },
        modifier = Modifier.size(50.dp),
        colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.onPrimary)
    ) {
        Icon(
            imageVector = wishListIcon,
            contentDescription = "add to cart",
            tint = Color.Gray.copy(.5f),
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp)
        )
    }
}


@Composable
fun QuantitySelector(
    modifier: Modifier = Modifier,
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .height(30.dp)
            .width(100.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {

        IconButton(
            onClick = {
                if (quantity > 1) onQuantityChange(quantity - 1)
            },
            enabled = quantity > 1 // Disable button if quantity is 0
           , modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .size(30.dp)

        ) {
                Text(text = "-", color = MaterialTheme.colorScheme.onPrimary, fontSize = 20.sp)
        }

        Text(
            text = quantity.toString(),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        IconButton(
            onClick = { onQuantityChange(quantity + 1) },
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .size(30.dp)
        ) {
            Icon(imageVector = Icons.Rounded.Add, contentDescription ="increase quantity" , tint = MaterialTheme.colorScheme.onPrimary)
        }
    }
}
