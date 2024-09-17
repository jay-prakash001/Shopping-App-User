package com.jp.shoppingapp.presentation.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.jp.shoppingapp.common.CART_COLLECTION
import com.jp.shoppingapp.common.NAME
import com.jp.shoppingapp.common.ORDER_COLLECTION
import com.jp.shoppingapp.domain.model.OrderModel
import com.jp.shoppingapp.domain.model.OrderParentModel
import com.jp.shoppingapp.domain.model.ProductModel
import com.jp.shoppingapp.domain.model.User
import com.jp.shoppingapp.domain.useCases.CreateUserUseCase
import com.jp.shoppingapp.domain.useCases.GetBannerUseCase
import com.jp.shoppingapp.domain.useCases.GetCategoriesUseCase
import com.jp.shoppingapp.domain.useCases.GetUserByIdUseCase
import com.jp.shoppingapp.domain.useCases.LoginUseCase
import com.jp.shoppingapp.domain.useCases.OrderUseCase
import com.jp.shoppingapp.domain.useCases.SearchProductUseCase
import com.jp.shoppingapp.domain.useCases.UploadMediaUseCase
import com.jp.shoppingapp.domain.useCases.WishListUseCase
import com.jp.shoppingappadmin.common.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingAppViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val loginUseCase: LoginUseCase,
    private val getUserUseCase: GetUserByIdUseCase,
    private val uploadMediaUseCase: UploadMediaUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val searchProductUseCase: SearchProductUseCase,
    private val getBannerUseCase: GetBannerUseCase,
    private val wishListUseCase: WishListUseCase,
    val firebaseAuth: FirebaseAuth,
    private val orderUseCase: OrderUseCase
) :
    ViewModel() {
    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState = _signUpState.asStateFlow()
    private val _loginState = MutableStateFlow<LoginState>(LoginState())
    val loginState = _loginState.asStateFlow()

    private val _userState = MutableStateFlow<GetUser>(GetUser())
    val userState = _userState.asStateFlow()


    private val _getCategoriesState = MutableStateFlow(GetCategoriesState())
    val getCategories = _getCategoriesState.asStateFlow()


    private val _products = MutableStateFlow(ProductsState())
    val products = _products.asStateFlow()

    private val _bannerState = MutableStateFlow(BannerState())
    val bannerState = _bannerState.asStateFlow()

    private val _wishListState = MutableStateFlow(WishListState())
    val wishListState = _wishListState.asStateFlow()

    private val _cartState = MutableStateFlow(CartState())
    val cartState = _cartState.asStateFlow()

    private val _orderState = MutableStateFlow(State(data = emptyList<OrderParentModel>()))
    val orderState = _orderState.asStateFlow()
    val isUploading = MutableStateFlow(false)

    init {
        getAllProducts()
        getCategories()
        getBanners()
        getProductsFromWishList()
        getFromCart()
        val uid = firebaseAuth.currentUser?.uid
        if (uid != null) {
            getUserByUid(uid)
        }
    }


    fun addOrder(orderParentModel: OrderParentModel) {
        viewModelScope.launch {
            orderUseCase.addOrders(orderParentModel, ORDER_COLLECTION).collectLatest {
                when (it) {
                    is ResultState.Error -> {


                    }

                    ResultState.Loading -> {


                    }

                    is ResultState.Success -> {
                        _orderState.value = State(data = listOf())

                    }
                }
            }
        }
    }

    fun getOrder() {
        viewModelScope.launch {
            orderUseCase.getOrders().collectLatest {
                when (it) {
                    is ResultState.Error -> {


                    }

                    ResultState.Loading -> {


                    }

                    is ResultState.Success -> {
                        _orderState.value = State(data = it.data)

                    }
                }
            }
        }
    }

    fun cancelOrder(orderParentModel: OrderParentModel) {
        viewModelScope.launch {
            orderUseCase.cancelOrder(orderParentModel).collectLatest {

                when (it) {
                    is ResultState.Error -> {


                    }

                    ResultState.Loading -> {


                    }

                    is ResultState.Success -> {
                        getOrder()

                    }
                }
            }


        }

    }

    fun addCart(orderModel: OrderModel) {
        viewModelScope.launch {
            orderUseCase.addOrders(orderModel, CART_COLLECTION).collectLatest {
                when (it) {
                    is ResultState.Error -> {


                    }

                    ResultState.Loading -> {


                    }

                    is ResultState.Success -> {
                        println("ADDORDER addcart ${it.data}")
                        getFromCart()
                    }
                }
            }
        }
    }

    fun deleteFromCart(orderModel: OrderModel) {
        viewModelScope.launch {
            orderUseCase.deleteFromCart(orderModel, CART_COLLECTION).collectLatest {
                when (it) {
                    is ResultState.Error -> {
                        println("ADDORDER del ${it}")


                    }

                    ResultState.Loading -> {


                    }

                    is ResultState.Success -> {
                        println("ADDORDER del ${it.data}")
                        getFromCart()
                    }
                }
            }
        }
    }

    fun getFromCart() {
        viewModelScope.launch {
            orderUseCase.getProductsFromCart(CART_COLLECTION).collectLatest {
                when (it) {
                    is ResultState.Error -> {


                    }

                    ResultState.Loading -> {

                    }

                    is ResultState.Success -> {
                        _cartState.value = _cartState.value.copy(data = it.data)
                        println("ADDORDER get ${it.data}")


                    }
                }
            }
        }
    }


    fun addToWishList(productModel: ProductModel) {
        viewModelScope.launch {
            wishListUseCase.addToWishList(productModel).collectLatest {
//                when (it) {
//                    is ResultState.Error -> TODO()
//                    ResultState.Loading -> TODO()
//                    is ResultState.Success -> TODO()
//                }
            }
        }
    }

    fun getProductsFromWishList() {
        viewModelScope.launch {
            wishListUseCase.getProductsFromWishList().collectLatest {
                when (it) {
                    is ResultState.Error -> {


                    }

                    ResultState.Loading -> {

                    }

                    is ResultState.Success -> {

                        _wishListState.value = WishListState(data = it.data)
                    }
                }
            }
        }
    }

    fun deleteProductsFromWishList(productModel: ProductModel) {
        viewModelScope.launch {
            wishListUseCase.deleteFromWishList(productModel).collectLatest {
                when (it) {
                    is ResultState.Error -> {

                    }

                    ResultState.Loading -> {

                    }

                    is ResultState.Success -> {
                        getProductsFromWishList()
                    }
                }
            }
        }
    }

    fun getBanners() {
        viewModelScope.launch {
            getBannerUseCase.getBanners().collectLatest {
                println("BANNER $it")
                when (it) {
                    is ResultState.Error -> {

                        _bannerState.value = _bannerState.value.copy(
                            error = it.exception, isLoading = false
                        )
                    }

                    ResultState.Loading -> {
                        _bannerState.value = _bannerState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _bannerState.value = _bannerState.value.copy(
                            data = it.data, isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun createUser(user: User) {
        viewModelScope.launch {
            createUserUseCase.createUser(user).collectLatest {
                when (it) {
                    is ResultState.Error -> {

                        _signUpState.value = SignUpState(error = it.exception)
                    }

                    ResultState.Loading -> {
                        _signUpState.value = SignUpState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _signUpState.value = SignUpState(success = it.data)
                    }
                }
            }
        }
    }

    fun login(email: String, password: String) {

        viewModelScope.launch {
            loginUseCase.loginWithEmailPassword(email, password).collectLatest {
                when (it) {
                    is ResultState.Error -> {
                        _loginState.value = LoginState(error = it.exception)
                    }

                    ResultState.Loading -> {
                        _loginState.value = LoginState(isLoading = true)
                    }

                    is ResultState.Success -> {

                        _loginState.value = LoginState(
                            success = it.data.toString(), userData = it.data.toString()
                        )
                    }
                }
            }


        }
    }

    fun getUserByUid(uid: String) {
        viewModelScope.launch {
            getUserUseCase.getUserById(uid).collectLatest {
                when (it) {
                    is ResultState.Error -> {

                        _userState.value = GetUser(error = it.exception)
                    }

                    ResultState.Loading -> {

                        _userState.value = GetUser(isLoading = true)
                    }

                    is ResultState.Success -> {

                        _userState.value = GetUser(userDataParent = it.data)
                    }
                }
            }
        }

    }

    fun updateUser(name: String, email: String, phone: String, uid: String, profileImg: String) {
        viewModelScope.launch {
            createUserUseCase.updateUser(
                name = name,
                email = email,
                phone = phone,
                profileImg = profileImg,
                uid = uid
            )
                .collectLatest {
                    when (it) {
                        is ResultState.Error -> {


                        }

                        ResultState.Loading -> {


                        }

                        is ResultState.Success -> {
                            getUserByUid(uid)
                        }
                    }
                }
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            getCategoriesUseCase.getCategories().collectLatest {
                when (it) {
                    is ResultState.Error -> {
                        _getCategoriesState.value = GetCategoriesState(error = it.exception)
                    }

                    ResultState.Loading -> {

                        _getCategoriesState.value = GetCategoriesState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _getCategoriesState.value = GetCategoriesState(data = it.data)
                    }
                }
                println("CAT $it")
            }
        }
    }

    fun searchCategories(name: String) {
        viewModelScope.launch {
            getCategoriesUseCase.searchCategories(name).collectLatest {
                when (it) {
                    is ResultState.Error -> {
                        _getCategoriesState.value = GetCategoriesState(error = it.exception)
                    }

                    ResultState.Loading -> {

                        _getCategoriesState.value = GetCategoriesState(isLoading = true)
                    }

                    is ResultState.Success -> {
                        _getCategoriesState.value = GetCategoriesState(data = it.data)
                    }
                }
                println("CAT $it")
            }
        }
    }

    fun uploadImage(uri: Uri, img: (String) -> Unit) {
        viewModelScope.launch {
            uploadMediaUseCase.uploadMedia(uri).collectLatest {
                when (it) {
                    is ResultState.Error -> {
                        isUploading.value = false
                    }

                    ResultState.Loading -> {
                        isUploading.value = true
                    }

                    is ResultState.Success -> {
                        isUploading.value = false
                        img(it.data)
                    }
                }
            }

        }

    }

    fun searchProducts(name: String, fieldName: String = NAME) {
        viewModelScope.launch {
            searchProductUseCase.search(name, fieldName = fieldName).collectLatest {
                when (it) {
                    is ResultState.Error -> {

                        _products.value = ProductsState(error = it.exception)
                    }

                    ResultState.Loading -> {
                        _products.value = ProductsState(isLoading = true)
                    }

                    is ResultState.Success -> {

                        _products.value = ProductsState(data = it.data)
                    }
                }
            }
        }
    }

    fun getAllProducts() {
        viewModelScope.launch {
            searchProductUseCase.getProducts().collectLatest {
                when (it) {
                    is ResultState.Error -> {

                        _products.value = ProductsState(error = it.exception)
                    }

                    ResultState.Loading -> {
                        _products.value = ProductsState(isLoading = true)
                    }

                    is ResultState.Success -> {

                        _products.value = ProductsState(data = it.data)
                    }
                }
            }
        }

    }

}
