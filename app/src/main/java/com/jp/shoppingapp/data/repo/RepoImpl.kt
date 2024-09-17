package com.jp.shoppingapp.data.repo

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.jp.shoppingapp.common.BANNER_COLLECTION
import com.jp.shoppingapp.common.CATEGORY_COLLECTION
import com.jp.shoppingapp.common.ID
import com.jp.shoppingapp.common.NAME
import com.jp.shoppingapp.common.ORDER_COLLECTION
import com.jp.shoppingapp.common.PRODUCT_COLLECTION
import com.jp.shoppingapp.common.PROFILE_IMG
import com.jp.shoppingapp.common.USER_COLLECTION
import com.jp.shoppingapp.domain.model.BannerModel
import com.jp.shoppingapp.domain.model.OrderModel
import com.jp.shoppingapp.domain.model.OrderParentModel
import com.jp.shoppingapp.domain.model.ProductModel
import com.jp.shoppingapp.domain.model.User
import com.jp.shoppingapp.domain.model.UserDataParent
import com.jp.shoppingapp.domain.repo.Repo
import com.jp.shoppingappadmin.common.ResultState
import com.jp.shoppingappadmin.domain.model.CategoryModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RepoImpl @Inject constructor(
    var firebaseAuth: FirebaseAuth,
    private var firebaseFirestore: FirebaseFirestore,
    private var firebaseStorage: FirebaseStorage
) : Repo {
    override fun registerUserWithEmailAndPassword(user: User): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                        firebaseFirestore.collection(USER_COLLECTION)
                            .document(it.result.user?.uid.toString()).set(user)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {
                                    trySend(ResultState.Success("User registered successfully"))

                                } else {
                                    trySend(ResultState.Error(it.exception?.localizedMessage.toString()))

                                }
                            }
                    } else {
                        trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                    }
                }
            awaitClose {
                close()
            }
        }

    override fun loginWithEmailPassword(
        email: String,
        password: String
    ): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    trySend(ResultState.Success(firebaseAuth.currentUser?.email!!))
                } else {
                    trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                }

            }
            awaitClose {
                close()
            }
        }

    override suspend fun updateUser(
        name: String,
        email: String,
        phone: String,
        uid: String,
        profileImg: String
    ): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection(USER_COLLECTION).document(uid)
            .update("name", name, "email", email, "phone", phone, "profileImg", profileImg)
            .addOnSuccessListener {
                trySend(ResultState.Success("Updated Successfully"))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage))
            }
        awaitClose { close() }
    }

    override fun getUserByUid(uid: String): Flow<ResultState<UserDataParent>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection(USER_COLLECTION).document(uid).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val user = it.result.toObject(User::class.java)
                val userDataParent = UserDataParent(it.result.id, user)
                if (user != null) {
                    trySend(ResultState.Success(userDataParent))

                } else {
                    trySend(ResultState.Error("User not found"))
                }

            } else {
                trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
            }
        }
        awaitClose {
            close()
        }
    }

    override suspend fun uploadMedia(uri: Uri): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseStorage.reference.child(PROFILE_IMG + "/${System.currentTimeMillis()}").putFile(uri)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    it.result.storage.downloadUrl.addOnSuccessListener {
                        trySend(ResultState.Success(it.toString()))

                    }
                } else {
                    trySend(ResultState.Error(it.exception?.localizedMessage.toString()))
                }
            }
        awaitClose {
            close()
        }
    }

    override fun getCategories(): Flow<ResultState<List<CategoryModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection(CATEGORY_COLLECTION).get()
            .addOnSuccessListener { querySnapShot ->
                val categories = mutableListOf<CategoryModel>()
                querySnapShot.documents.forEach { document ->
                    val category = document.toObject(CategoryModel::class.java)
                    if (category != null) {
                        categories.add(category)
                    }

                }
                trySend(ResultState.Success(categories))

            }.addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage))
            }
        awaitClose {
            close()
        }

    }

    override fun searchProducts(
        name: String,
        fieldName: String
    ): Flow<ResultState<List<ProductModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseFirestore.collection(PRODUCT_COLLECTION).whereEqualTo(fieldName, name).get()
                .addOnSuccessListener { querySnapShot ->
                    val products = mutableListOf<ProductModel>()

                    querySnapShot.documents.forEach {
                        val document = it.toObject(ProductModel::class.java)
                        if (document != null) {
                            products.add(document)
                        }
                    }
                    trySend(ResultState.Success(products))

                }.addOnFailureListener {
                    trySend(ResultState.Error(it.localizedMessage))
                }
            awaitClose {
                close()
            }
        }

    override fun searchCategories(name: String): Flow<ResultState<List<CategoryModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseFirestore.collection(CATEGORY_COLLECTION).whereEqualTo(NAME, name).get()
                .addOnSuccessListener { querySnapShot ->
                    val categories = mutableListOf<CategoryModel>()

                    querySnapShot.documents.forEach {
                        val document = it.toObject(CategoryModel::class.java)
                        if (document != null) {
                            categories.add(document)
                        }
                    }
                    trySend(ResultState.Success(categories))

                }.addOnFailureListener {
                    trySend(ResultState.Error(it.localizedMessage))
                }
            awaitClose {
                close()
            }
        }

    override fun getAllProducts(): Flow<ResultState<List<ProductModel>>> = callbackFlow {
        trySend(ResultState.Loading)

        firebaseFirestore.collection(PRODUCT_COLLECTION).get().addOnSuccessListener {
            val products = mutableListOf<ProductModel>()
            it.documents.forEach {
                val product = it.toObject(ProductModel::class.java)
                if (product != null) {
                    products.add(product)
                }
            }
            trySend(ResultState.Success(products))


        }.addOnFailureListener {
            trySend(ResultState.Error(it.localizedMessage))
        }
        awaitClose {
            close()
        }
    }

    override fun getBanners(): Flow<ResultState<List<BannerModel>>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection(BANNER_COLLECTION).get().addOnSuccessListener {
            val banners = mutableListOf<BannerModel>()

            it.documents.forEach {
                val banner = it.toObject(BannerModel::class.java)
                if (banner != null) {
                    banners.add(banner)

                }
            }
            trySend(ResultState.Success(banners))
        }.addOnFailureListener {
            trySend(ResultState.Error(it.localizedMessage))
        }
        awaitClose {
            close()
        }
    }

    override suspend fun addToWishList(
        productModel: ProductModel,
        collection: String
    ): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection(USER_COLLECTION)
            .document(firebaseAuth.currentUser?.uid.toString()).collection(
                collection
            ).add(productModel)
            .addOnSuccessListener {
                trySend(ResultState.Success("Success"))
            }.addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage))
            }
        awaitClose {
            close()
        }
    }

    override suspend fun deleteFromWishList(
        productModel: ProductModel,
        collection: String
    ): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection(USER_COLLECTION)
            .document(firebaseAuth.currentUser?.uid.toString()).collection(
                collection
            )
            .whereEqualTo(ID, productModel.id).get().addOnSuccessListener { querySnapShot ->
                if (!querySnapShot.isEmpty) {
                    querySnapShot.documents[0].reference.delete().addOnSuccessListener {
                        trySend(ResultState.Success("Removed From Cart"))
                    }.addOnFailureListener {
                        trySend(ResultState.Error(it.localizedMessage.toString()))
                    }
                } else {
                    trySend(ResultState.Error("Error"))

                }

            }.addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage.toString()))

            }
        awaitClose { close() }
    }

    override fun getProductsFromWishList(collection: String): Flow<ResultState<List<ProductModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseFirestore.collection(USER_COLLECTION)
                .document(firebaseAuth.currentUser?.uid.toString()).collection(
                    collection
                ).get().addOnSuccessListener {
                    val products = mutableListOf<ProductModel>()
                    it.documents.forEach {
                        val product = it.toObject(ProductModel::class.java)
                        if (product != null) {
                            products.add(product)
                        }
                    }
                    trySend(ResultState.Success(products))


                }.addOnFailureListener {
                    trySend(ResultState.Error(it.localizedMessage))
                }
            awaitClose {
                close()
            }
        }

    override suspend fun <T : Any> addOrder(
        orderModel: T,
        collection: String
    ): Flow<ResultState<String>> = callbackFlow {
        println("ADDORDER $orderModel")

        trySend(ResultState.Loading)
        firebaseFirestore.collection(USER_COLLECTION)
            .document(firebaseAuth.currentUser?.uid.toString()).collection(
                collection
            ).add(orderModel)
            .addOnSuccessListener {
                println("ADDORDER $it")
                trySend(
                    ResultState.Success("Success")

                )
            }.addOnFailureListener {
                println("ADDORDER $it")
                trySend(ResultState.Error(it.localizedMessage))
            }
        awaitClose {
            close()
        }
    }

    override suspend fun deleteOrder(
        orderModel: OrderParentModel,
        collection: String
    ): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        firebaseFirestore.collection(USER_COLLECTION)
            .document(firebaseAuth.currentUser?.uid.toString()).collection(
                collection
            )
            .whereEqualTo(ID, orderModel.id).get().addOnSuccessListener { querySnapShot ->
                if (!querySnapShot.isEmpty) {
                    querySnapShot.documents[0].reference.delete().addOnSuccessListener {
                        trySend(ResultState.Success("Removed From Cart"))
                    }.addOnFailureListener {
                        trySend(ResultState.Error(it.localizedMessage.toString()))
                    }
                } else {
                    trySend(ResultState.Error("Error"))

                }

            }.addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage.toString()))

            }
        awaitClose { close() }
    }

    override suspend fun deleteCart(
        orderModel: OrderModel,
        collection: String
    ): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        println("ADDORDER delRepo0 ${orderModel}")

        firebaseFirestore.collection(USER_COLLECTION)
            .document(firebaseAuth.currentUser?.uid.toString()).collection(
                collection
            )
            .whereEqualTo(ID, orderModel.product?.id).get().addOnSuccessListener { it ->
                if (!it.isEmpty) {
                    val doc = it.documents[0].reference
                    doc.delete().addOnSuccessListener {
                        trySend(ResultState.Success("Removed From Cart"))

                    }.addOnFailureListener {

                        trySend(ResultState.Error(it.localizedMessage.toString()))
                    }
                }
            }.addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage.toString()))
            }
        awaitClose {
            close()
        }
    }

    override suspend fun updateOrderStatus(
        orderParentModel: OrderParentModel,
        status: String
    ): Flow<ResultState<String>> =
        callbackFlow {
            trySend(ResultState.Loading)

            firebaseFirestore.collection(USER_COLLECTION).document(
                firebaseAuth.currentUser?.uid.toString()
            ).collection(ORDER_COLLECTION).whereEqualTo(
                ID, orderParentModel.id
            ).get().addOnSuccessListener {
                val doc = it.documents[0].reference
                val statusList = mutableListOf<String>()
                orderParentModel.status.forEach { it ->
                    statusList.add(it)
                }
                statusList.add(status)
                doc.update("status", statusList).addOnSuccessListener {
                    trySend(ResultState.Success("Success"))
                }.addOnFailureListener {
                    trySend(ResultState.Error(it.localizedMessage))
                }
            }.addOnFailureListener {
                trySend(ResultState.Error(it.localizedMessage))
            }
            awaitClose {
                close()
            }

        }


    override fun getCartOrders(collection: String): Flow<ResultState<List<OrderModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseFirestore.collection(USER_COLLECTION)
                .document(firebaseAuth.currentUser?.uid.toString()).collection(
                    collection
                ).get().addOnSuccessListener {
                    val products = mutableListOf<OrderModel>()
                    it.documents.forEach {
                        val product = it.toObject(OrderModel::class.java)
                        if (product != null) {
                            products.add(product)
                        }
                    }
                    trySend(ResultState.Success(products))


                }.addOnFailureListener {
                    trySend(ResultState.Error(it.localizedMessage))
                }
            awaitClose {
                close()
            }
        }

    override fun getOrders(collection: String): Flow<ResultState<List<OrderParentModel>>> =
        callbackFlow {
            trySend(ResultState.Loading)
            firebaseFirestore.collection(USER_COLLECTION)
                .document(firebaseAuth.currentUser?.uid.toString()).collection(
                    collection
                ).get().addOnSuccessListener {
                    val orders = mutableListOf<OrderParentModel>()
                    it.documents.forEach {
                        val order = it.toObject(OrderParentModel::class.java)
                        if (order != null) {
                            orders.add(order)
                        }
                    }
                    trySend(ResultState.Success(orders))


                }.addOnFailureListener {
                    trySend(ResultState.Error(it.localizedMessage))
                }
            awaitClose {
                close()
            }
        }
}