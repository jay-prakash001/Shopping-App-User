package com.jp.shoppingapp.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.jp.shoppingapp.domain.model.BannerModel
import com.jp.shoppingapp.presentation.viewModel.ShoppingAppViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.random.Random


@Composable
fun BannerRow(
    modifier: Modifier = Modifier,
    viewModel: ShoppingAppViewModel = hiltViewModel(),
    banners: List<BannerModel>,
    navController: NavController
) {
    val state = viewModel.bannerState.collectAsStateWithLifecycle().value

    if (state.isLoading) {
        BannerShimmer()


    } else {
        Column(
            Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            InfiniteSlidingLazyRow(banners = banners, navController, viewModel)
        }

    }


}


@Composable
fun InfiniteSlidingLazyRow(
    banners: List<BannerModel>,
    navController: NavController,
    viewModel: ShoppingAppViewModel
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var nextIndex by remember {
        mutableStateOf(banners.size - 1)
    }
    LaunchedEffect(Unit) {
        while (true) {
            coroutineScope.launch {
                // Calculate the next index, wrapping back to 0 when the end is reached
                if (nextIndex >= banners.size - 1) {
                    nextIndex = 0
                } else {
                    nextIndex++
                }
                listState.animateScrollToItem(nextIndex)

            }
            delay(2000) // Adjust the delay as needed
        }

    }

    LazyRow(
        state = listState,
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(banners) { index, item ->
            val random = Random(index + 10)
            val red = random.nextInt(0, 100)
            val green = random.nextInt(0, 100)
            val blue = random.nextInt(0, 100)
            val randomColor = Color(red, green, blue)
            Box(modifier = Modifier.fillMaxWidth()) {
                BannerCard(banner = item, randomColor = randomColor)
            }
        }

    }
}


@Composable
fun BannerShimmer(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
            .heightIn(min = 180.dp, max = 200.dp), colors = CardDefaults.cardColors(Color.LightGray)

    ) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(), verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {


            Column(modifier = Modifier.fillMaxWidth(.5f)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(12.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shimmerEffect()
                )


            }
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .shimmerEffect()
            )


        }
    }
}

@Composable
fun BannerCard(
    banner: BannerModel = BannerModel(title = "Title", desc = "Description", img = "Image"),
    randomColor: Color
) {
    val index = remember {
        mutableStateOf(0)
    }
    Card(
        modifier = Modifier
            .padding(10.dp)
//            .fillMaxWidth()
            .widthIn(300.dp, 320.dp)
            .heightIn(min = 180.dp, max = 200.dp),
        colors = CardDefaults.cardColors(randomColor),
        elevation = CardDefaults.elevatedCardElevation(1.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(), verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {


            Column(modifier = Modifier.fillMaxWidth(.5f)) {
                Text(
                    text = banner.title.capitalize(Locale.ROOT),
                    style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp, color = Color.LightGray
                )
                Text(
                    text = banner.desc.capitalize(Locale.ROOT),
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 12.sp, color = Color.LightGray
                )

            }

            SubcomposeAsyncImage(
                model = banner.img, contentDescription = "Banner Image", modifier = Modifier
                    .size(200.dp)
                    .clip(
                        RoundedCornerShape(10.dp)
                    ), contentScale = ContentScale.Crop, loading = {
                    Box(modifier = Modifier.fillMaxSize()){
                        CircularProgressIndicator(modifier = Modifier.size(20.dp).align(Alignment.Center))
                    }
                })

        }
    }
}

