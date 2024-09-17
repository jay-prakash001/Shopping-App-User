package com.jp.shoppingapp.presentation.navigation


import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.firebase.auth.FirebaseAuth
import com.jp.shoppingapp.R
import com.jp.shoppingapp.common.CATEGORY
import com.jp.shoppingapp.presentation.screens.CartScreen
import com.jp.shoppingapp.presentation.screens.CategoriesScreen
import com.jp.shoppingapp.presentation.screens.CheckOutScreen
import com.jp.shoppingapp.presentation.screens.DashBoardScreen
import com.jp.shoppingapp.presentation.screens.LoginScreen
import com.jp.shoppingapp.presentation.screens.OrdersScreen
import com.jp.shoppingapp.presentation.screens.ProductDetailScreen
import com.jp.shoppingapp.presentation.screens.ProductsScreen
import com.jp.shoppingapp.presentation.screens.ProfileScreen
import com.jp.shoppingapp.presentation.screens.SearchScreen
import com.jp.shoppingapp.presentation.screens.SignUpScreen
import com.jp.shoppingapp.presentation.screens.WishListScreen
import com.jp.shoppingapp.presentation.viewModel.ShoppingAppViewModel
import com.jp.shoppingapp.utils.TestTransition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    firebaseAuth: FirebaseAuth,
    viewModel: ShoppingAppViewModel = hiltViewModel(),
    modifier: Modifier
) {
    val navController = rememberNavController()


    val startScreen = if (firebaseAuth.currentUser != null) {
        SubNavigation.MainHome
    } else {
        SubNavigation.LoginSignUp
    }
    NavHost(navController = navController, startDestination = startScreen) {

        navigation<SubNavigation.LoginSignUp>(startDestination = Routes.Login) {
            composable<Routes.SignUp> {
                SignUpScreen(navController = navController, viewModel = viewModel)
            }
            composable<Routes.Login> {
                LoginScreen(navController = navController, viewModel = viewModel)
            }

        }
        navigation<SubNavigation.MainHome>(startDestination = Routes.Dashboard) {
            composable<Routes.Dashboard>(enterTransition = {
                return@composable slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right, tween(700)
                )
            }, exitTransition = {
                return@composable slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right, tween(700)
                )
            }
            ) {
                DashBoardScreen(
                    navController = navController,
                    modifier = Modifier, viewModel = viewModel,
                    firebaseAuth = firebaseAuth
                )

            }


            composable<Routes.Categories>(
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right, tween(700)
                    )
                }, exitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right, tween(700)
                    )
                }
            ) {
                CategoriesScreen(navController = navController, viewModel = viewModel)


            }
            composable<Routes.Cart>(
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right, tween(700)
                    )
                }, exitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right, tween(700)
                    )
                }
            ) {

                CartScreen(viewModel = viewModel, navController = navController)

            }

            composable<Routes.Profile>(
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right, tween(700)
                    )
                }, exitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right, tween(700)
                    )
                }
            ) {
                ProfileScreen(
                    firebaseAuth = firebaseAuth,
                    navController = navController,
                    viewModel = viewModel
                )
//                    TestTransition(modifier = Modifier.background(Color.Blue))

            }
            composable<Routes.WishList>(
                enterTransition = {
                    return@composable slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right, tween(700)
                    )
                }, exitTransition = {
                    return@composable slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Right, tween(700)
                    )
                }
            ) {
//                TestTransition(
//                    modifier = Modifier.background(Color.Red),
//                    viewModel = viewModel
//                )
                WishListScreen(viewModel = viewModel, navController = navController)
            }

            composable<Routes.Orders> {
                OrdersScreen(viewModel = viewModel, navController = navController)

            }

        }



        composable<Routes.ProductsScreen> {
            val category = it.toRoute<Routes.ProductsScreen>().category
            viewModel.searchProducts(category, CATEGORY)
            ProductsScreen(navController = navController, viewModel = viewModel)
        }
        composable<Routes.ProductDetail> {
            val id = it.toRoute<Routes.ProductDetail>()

            val listOfProducts =
                viewModel.products.collectAsState().value.data.filter { it.id == id.id }
            if (listOfProducts.isNotEmpty()) {

                val product = listOfProducts[0]
                val suggestedProducts =
                    viewModel.products.collectAsStateWithLifecycle().value.data.filter { it ->
                        it.category == product.category && it.id != product.id
                    }
                ProductDetailScreen(
                    product = product,
                    navController = navController,
                    suggestedProducts = suggestedProducts,
                    viewModel = viewModel
                )
            }

        }
        composable<Routes.CheckOut> {
            val id = it.toRoute<Routes.CheckOut>().id
            val quantity = it.toRoute<Routes.CheckOut>().quantity
            val product =
                viewModel.products.collectAsStateWithLifecycle().value.data.filter { it.id == id }
            if (product.isNotEmpty()) {

                CheckOutScreen(
                    product = product[0],
                    quantity = quantity,
                    navController = navController,
                    viewModel = viewModel
                )
            } else {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.no_results),
                        contentDescription = "product not found"
                    )
                }
            }
        }

        composable<Routes.SearchProducts> {
            val name = it.toRoute<Routes.SearchProducts>().name
            SearchScreen(name = name, navController = navController, viewModel = viewModel)
        }


    }


}




