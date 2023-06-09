package com.example.myshop.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.myshop.models.*
import com.example.myshop.ui.activities.*
import com.example.myshop.ui.fragments.DashboardFragment
import com.example.myshop.ui.fragments.OrdersFragment
import com.example.myshop.ui.fragments.ProductsFragment
import com.example.myshop.ui.fragments.SoldProductsFragment
import com.example.myshop.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoreClass {

    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){

        // The "users" is collection name. If the collection is already created then it will not create the same one again.
        //first Type
        //mFireStore.collection("users")
            //second type
            mFireStore.collection(Constants.USERS)
            // Document ID for users fields. Here the document it is the User ID.
            .document(userInfo.id)
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later on instead of replacing the fields.
            .set(userInfo, SetOptions.merge())
            .addOnCompleteListener{
                // Here call a function of base activity for transferring the result to it.
                activity.userRegisterSuccess()
            }
            .addOnFailureListener{e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user",
                    e
                )
            }

    }

    fun getCurrentuserID(): String{
        //An Instance of currentUser using FireBaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        // A variable to assign the currentUserId if is not null or else it will be blank
        var currentuserID = ""
        if(currentUser != null){
            currentuserID = currentUser.uid
        }
        return currentuserID
    }
//
//    fun getCurrentProductID(): String{
//        val currentProduct = FirebaseAuth.getInstance().uid
//
//        var currentProductId = ""
//        if(currentProduct != null){
//            currentProductId = currentProduct
//        }
//        return currentProductId
//    }

    fun getUserDetails(activity: Activity){
        //Here we pass the collection name from which we wants the data
        mFireStore.collection(Constants.USERS)
            //The document id to get the Fields of user
            .document(getCurrentuserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                //Here we have received the document snapshot which is converted into the User Data model object
                val user = document.toObject(User::class.java)!!

                val sharedPreferences = activity.getSharedPreferences(
                    Constants.MYSHOP_PREFERENCES,
                    Context.MODE_PRIVATE
                )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                // Key : logged_in_username
                // Value : ${user.firstName} ${user.lastName}
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    //edit
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()


                //TODO : Pass the result to the login Activity
                //Start
                when(activity){
                    is LoginActivity -> {
                        //Call a function of base activity for transferring the result to it
                        activity.userLoggedInSuccess(user)
                    }
                    is SettingsActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                }
                //End
            }
            .addOnFailureListener { e ->
                when(activity){
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                    is SettingsActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName,
                    "Error while getting user details", e
                )

            }

    }
    fun uploadProductDetails(activity: AddProductActivity, productInfo: Product){
        mFireStore.collection(Constants.PRODUCTS)
            .document()
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge later on instead of replacing the fields.
            .set(productInfo, SetOptions.merge())
            .addOnCompleteListener{
                // Here call a function of base activity for transferring the result to it.
                activity.productUploadSuccess()
            }
            .addOnFailureListener { e ->

                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,
                    "Error while uploading the product details", e
                )

            }
    }

    fun getProductList(fragment: Fragment){
        mFireStore.collection(Constants.PRODUCTS)
                //only see my product
            .whereEqualTo(Constants.USER_ID, getCurrentuserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List",document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()
                for(i in document.documents){
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)
                }
                when(fragment){
                    is ProductsFragment ->{
                        fragment.successProductsListFromFireStore(productsList)
                    }
                }
            }
    }

    fun getProductDetails(activity: ProductDetailsActivity, productId: String){
        mFireStore.collection(Constants.PRODUCTS)
            .document(productId)
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.toString())
                val product = document.toObject(Product::class.java)
                if (product != null) {
                    activity.productDetailsSuccess(product)
                }
            }
            .addOnFailureListener{ e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,"Error while getting the product details", e)
            }
    }

    fun addCartItems(activity: ProductDetailsActivity, addToCart: CartItem){
        mFireStore.collection(Constants.CART_ITEMS)
            .document()
            .set(addToCart, SetOptions.merge())
            .addOnSuccessListener {
                //TODO addtocart success
                activity.addToCartSuccess()


            }
            .addOnFailureListener{ e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,
                    "Error while creating the document for cart item", e)
            }

    }

    fun getCartList(activity: Activity){
        mFireStore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID,getCurrentuserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName, document.documents.toString())
                val list: ArrayList<CartItem> = ArrayList()

                for(i in document.documents){
                    val cartItem = i.toObject(CartItem::class.java)!!
                    cartItem.id = i.id

                    list.add(cartItem)
                }
                when(activity){
                    is CartListActivity ->{
                        activity.successCartItemsList(list)
                    }
                    is CheckoutActivity ->{
                        activity.successCartItemsList(list)
                    }
                }
            }
            .addOnFailureListener{ e ->
                when(activity){
                    is CartListActivity ->{
                        activity.hideProgressDialog()
                    }
                    is CheckoutActivity ->{
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting the cart list items", e
                )
            }
    }

    fun checkIfItemExitinCart(activity: ProductDetailsActivity, productId: String){
        mFireStore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID,getCurrentuserID())
            .whereEqualTo(Constants.PRODUCT_ID, productId)
            .get()
            .addOnSuccessListener { document ->
                Log.e(
                    activity.javaClass.simpleName,document.documents.toString())
                if(document.documents.size > 0){
                    activity.productExistsInCart()
                }else{
                    activity.hideProgressDialog()
                }
            }
            .addOnFailureListener{ e ->
                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while checking the exiting cart list", e
                )
            }
    }

    fun updateAllDetails(activity: CheckoutActivity, cartList: ArrayList<CartItem>, order: Order){
        val writeBatch = mFireStore.batch()

        for(cartItem in cartList){
//            val productHashMap = HashMap<String, Any>()

//            productHashMap[Constants.STOCK_QUANTITY] =
//                (cartItem.stock_quantity.toInt() - cartItem.cart_quantity.toInt()).toString()

        val soldProduct = SoldProduct(
//            FirestoreClass().getCurrentuserID(),
            cartItem.product_owner_id,
            cartItem.title,
            cartItem.price,
            cartItem.cart_quantity,
            cartItem.image,
            order.title,
            order.order_datetime,
            order.sub_total_amount,
            order.shipping_charge,
            order.total_amount,
            order.address


        )

//            val documentReference = mFireStore.collection(Constants.PRODUCTS)
//                .document(cartItem.product_id)
            val documentReference = mFireStore.collection(Constants.SOLD_PRODUCTS)
                .document(cartItem.product_id)

//            writeBatch.update(documentReference, productHashMap)
            writeBatch.set(documentReference, soldProduct)
        }

        for(cartItem in cartList){
            val documentReference = mFireStore.collection(Constants.CART_ITEMS)
                .document(cartItem.id)

            writeBatch.delete(documentReference)
        }

        writeBatch.commit().addOnSuccessListener {
            activity.allDetailsUpdatedSuccessfully()

        }
            .addOnFailureListener{ e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating all the details after order placed", e
                )
            }

    }


    fun placeOrder(activity: CheckoutActivity, order: Order){
        mFireStore.collection(Constants.ORDERS)
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {
                activity.orderPlacedSuccess()
            }
            .addOnFailureListener{ e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while placing an order", e
                )
            }
    }

    fun deleteProduct(fragment: ProductsFragment, productId: String){
        mFireStore.collection(Constants.PRODUCTS)
            .document(productId)
            .delete()
            .addOnSuccessListener {
                fragment.productDeleteSuccess()
            }
            .addOnFailureListener{ e ->
                fragment.hideProgressDialog()

                Log.e(
                    fragment.requireActivity().javaClass.simpleName,
                    "Error while deleteing the product", e
                )
            }
    }

    fun removeItemFromCart(context: Context, cart_id: String){
        mFireStore.collection(Constants.CART_ITEMS)
            .document(cart_id)
            .delete()
            .addOnSuccessListener {
                when(context){
                    is CartListActivity -> {
                        context.itemRemovedSuccess()
                    }
                }
            }
            .addOnFailureListener{ e ->
                when(context){
                    is CartListActivity -> {
                        context.hideProgressDialog()
                    }
                }

                Log.e(
                    context.javaClass.simpleName,
                    "Error while removing the item from the cart list", e
                )
            }
    }

    fun updateMyCart(context: Context, cart_id: String, itemHashMap: HashMap<String, Any>){
        mFireStore.collection(Constants.CART_ITEMS)
            .document(cart_id)
            .update(itemHashMap)
            .addOnSuccessListener {
                when(context){
                    is CartListActivity -> {
                        context.itemUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener{ e ->
                when(context){
                    is CartListActivity -> {
                        context.hideProgressDialog()
                    }
                }
                Log.e(context.javaClass.simpleName,
                    "Error while updating the cart item",e)
            }

    }

    fun getAllProductsList(activity: Activity){
        mFireStore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
//                activity.hideProgressDialog()
                Log.e("Products List", document.documents.toString())
                val productList : ArrayList<Product> = ArrayList()
                for(i in document.documents){
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id
                    productList.add(product)
                }

                when(activity){
                    is CartListActivity -> {
                        activity.successProductsListFromFireStore(productList)
                    }
                    is CheckoutActivity -> {
                        activity.successProductsListFromFireStore(productList)
                    }
                }



            }
            .addOnFailureListener{ e ->
                when(activity){
                    is CartListActivity -> {
                        activity.hideProgressDialog()
                    }
                    is CheckoutActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e("Get Product List","Error while getting dashboard items list",e)
            }
    }

    fun getDashboardItemList(fragment: DashboardFragment){
        mFireStore.collection(Constants.PRODUCTS)
        //see all product
            .get()
            .addOnSuccessListener { document ->
                Log.e(fragment.javaClass.simpleName, document.documents.toString())

                val productsList: ArrayList<Product> = ArrayList()
                for(i in document.documents){
                    val product = i.toObject(Product::class.java)!!
                    product.product_id = i.id
                    productsList.add(product)

                }
                fragment.successDashboardItemsList(productsList)
            }
            .addOnFailureListener{ e ->
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName,"Error while getting dashboard items list", e)
            }


    }


    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>){
        mFireStore.collection(Constants.USERS)
            .document(getCurrentuserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when(activity){
                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener{e ->
                when(activity){
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName,
                "Error while updating the user details", e)
            }

    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String){
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "."
                +Constants.getFileExtension(
                activity, imageFileURI
                )
        )
        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->
            //the image upload is success
            Log.e(
                "Firebase Image URL",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )
            //Get the downloadable url from the task snapshot
            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    Log.e("Downloadable Image URL", uri.toString())
                    when(activity){
                        is UserProfileActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                        is AddProductActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }


                    }
                }
        }
            .addOnFailureListener{exception ->
                //Hide the progress dialog if there is nay error.And print the error in log
                when(activity){
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                    is AddProductActivity -> {
                        activity.hideProgressDialog()
                }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

    fun getAddressList(activity: AddressListActivity){
        mFireStore.collection(Constants.ADDRESSES)
            .whereEqualTo(Constants.USER_ID, getCurrentuserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e(activity.javaClass.simpleName,document.documents.toString())
                val addressList: ArrayList<Address> = ArrayList()
                for(i in document.documents){
                    val address = i.toObject(Address::class.java)!!
                    address.id = i.id
                    addressList.add(address)
                }
                activity.successAddressListFromFirestore(addressList)
            } .addOnFailureListener{ e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,
                    "Error while getting the address", e)
            }

    }
    fun addAddress(activity: AddEditAddressActivity, addressInfo: Address){
        mFireStore.collection(Constants.ADDRESSES)
            .document()
            .set(addressInfo, SetOptions.merge())
            .addOnSuccessListener {

                activity.addUpdateAddressSuccess()
            }
            .addOnFailureListener{ e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,
                    "Error while adding the address", e)
            }
    }

    fun deleteAddress(activity: AddressListActivity, addressId: String){
        mFireStore.collection(Constants.ADDRESSES)
            .document(addressId)
            .delete()
            .addOnSuccessListener {
                activity.deleteAddressSuccess()
            }
            .addOnFailureListener{ e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,
                    "Error while deleting the address", e)
            }
    }

    fun updateAddress(activity: AddEditAddressActivity, addressInfo: Address, addressId: String){
        mFireStore.collection(Constants.ADDRESSES)
            .document(addressId)
            .set(addressInfo,SetOptions.merge())
            .addOnSuccessListener {
                activity.addUpdateAddressSuccess()
            }
            .addOnFailureListener{ e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName,
                    "Error while updating the address", e)
            }
    }

    fun getMyOrdersList(fragment: OrdersFragment){
        mFireStore.collection(Constants.ORDERS)
            .whereEqualTo(Constants.USER_ID, getCurrentuserID())
            .get()
            .addOnSuccessListener { document ->
                val list: ArrayList<Order> = ArrayList()

                for(i in document.documents){
                    val orderItem = i.toObject(Order::class.java)!!
                    orderItem.id = i.id

                    list.add(orderItem)
                }
                fragment.populateOrdersListInUI(list)

            }
            .addOnFailureListener{ e ->
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName,"Error while getting the orders list", e)
            }
    }

    fun getSoldProductsList(fragment: SoldProductsFragment){
        mFireStore.collection(Constants.SOLD_PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getCurrentuserID())
            .get()
            .addOnSuccessListener { document ->
                val list: ArrayList<SoldProduct> = ArrayList()
                for(i in document.documents){
                    val soldProduct = i.toObject(SoldProduct::class.java)!!
                    soldProduct.id = i.id

                    list.add(soldProduct)
                }

                fragment.successSoldProductList(list)
            }
            .addOnFailureListener{ e ->
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName,"Error while getting the list of sold products", e)
            }
    }


}