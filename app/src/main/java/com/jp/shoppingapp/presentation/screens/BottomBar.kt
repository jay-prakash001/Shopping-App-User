package com.jp.shoppingapp.presentation.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GifBox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.sp
import com.jp.shoppingapp.presentation.navigation.Routes
import java.util.Locale

@Composable
fun BottomBar(modifier: Modifier = Modifier, onClick: (Int) -> Unit = {}) {
    val screens = listOf(
        BottomBarItem.DashBoard,
        BottomBarItem.Category,
        BottomBarItem.Cart,
       BottomBarItem.Orders,
        BottomBarItem.Profile
    )
    val selectedIndex = remember {
        mutableIntStateOf(0)
    }
    NavigationBar {
        screens.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex.intValue == index,
                onClick = {
                    selectedIndex.intValue = index
                    onClick(selectedIndex.intValue)
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.title, tint = if (item == BottomBarItem.WishList) {
                        Color.Red.copy(.5f)
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    })
                }, label = {
                    Text(text = item.title.capitalize(Locale.ROOT), fontSize = 10.sp)
                })
        }
    }


}

sealed class BottomBarItem(val route: Routes, val title: String, val icon: ImageVector) {
    data object DashBoard :
        BottomBarItem(route = Routes.Dashboard, title = "DashBoard", icon = Icons.Default.Dashboard)

    data object Cart :
        BottomBarItem(route = Routes.Cart, title = "Cart", icon = Icons.Default.ShoppingCart)

    data object Profile :
        BottomBarItem(route = Routes.Profile, title = "Profile", icon = Icons.Default.Person)

    data object Category :
        BottomBarItem(route = Routes.Categories, title = "Category", icon = Icons.Default.Category)
 data object WishList :
        BottomBarItem(route = Routes.WishList, title = "WishList", icon = Icons.Default.Favorite)
    data object Orders :
        BottomBarItem(route = Routes.Orders, title = "Orders", icon = Icons.Default.GifBox)

}