package com.example.bi3echri.firestore
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.example.bi3echri.models.CartItem
import com.example.bi3echri.models.Product
import com.example.bi3echri.models.User
import com.example.bi3echri.ui.ui.activities.*
import com.example.bi3echri.ui.ui.fragments.DashboardFragment
import com.example.bi3echri.ui.ui.fragments.ProductsFragment
import com.example.bi3echri.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import android.util.Log.e as e1

class FirstoreClass
{
    private val mFirestore=FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo : User)
    {
        mFirestore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnCanceledListener {
                activity.userRegistrationSuccess()

            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                e1(
                    activity.javaClass.simpleName,
                    "Error while registering the user",
                    e
                )
            }
    }
    fun getCurrentUserID(): String
    {
        val currentUser=FirebaseAuth.getInstance().currentUser

        Log.i(javaClass.simpleName,currentUser.toString())
        var currentUserID=""
        if(currentUser!=null)
        {
            currentUserID =currentUser.uid
        }
        return  currentUserID

    }

    fun getUsersDetails(activity: Activity)
    {
        mFirestore.collection(Constants.USERS).document(getCurrentUserID()).get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName,document.toString())

                val user=document.toObject(User::class.java)!!

                val sharedPreferences =
                    activity.getSharedPreferences(Constants.BI3ECHRI_PREFERENCES,
                    Context.MODE_PRIVATE)

                 val editor:SharedPreferences.Editor =sharedPreferences.edit()
                //key : logged in username
                //value
                    editor.putString(Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}")
                editor.apply()
                //pass the result to the login activity
                when(activity)
                {
                    is LoginActivity -> {
                        activity.userLoggedInSucess(user)
                    }
                    is SettingsActivity -> {
                        activity.userDetailsSuccess(user)
                    }
                }
            }
            .addOnFailureListener{e->
            //if there is an error hide the progress dialog
                when(activity)
                {
                    is LoginActivity ->   {
                        activity.hideProgressDialog()
                    }
                    is SettingsActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                e1(
                    activity.javaClass.simpleName, "Error while getting user details",
                    e
                )
            }
    }
    fun updateUserProfilData(activity: Activity, userHashMap:HashMap <String, Any> )
    {
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener{
                when (activity) {
                    is UserProfilActivity ->
                    {
                        activity.userProfilUpdateSucess()
                    }
                }

            }
            .addOnFailureListener { e ->
                when (activity) {
                    is UserProfilActivity ->
                    {
                        activity.hideProgressDialog()
                    }
                }
                e1(
                    activity.javaClass.simpleName,
                    "Error while updating the user details",
                    e
                )
            }

    }
    fun uploadImageToCouldStorage(activity: Activity,imageFileURI:Uri?, imageType:String)
    {
        val sRef: StorageReference=FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "."
        + Constants.getFileExtension(
                activity,
                imageFileURI)
        )
        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->
            e1(
                "Firebase image URL",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )
            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    e1("DownLoadable Image URL", uri.toString())
                    when (activity)
                    {
                        is UserProfilActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                        is AddProductActivity ->
                        {
                            activity.imageUploadSuccess(uri.toString())

                        }
                    }
                }
        }
            .addOnFailureListener { exception ->
                when (activity)
                {
                    is UserProfilActivity -> {
                        activity.hideProgressDialog()
                    }
                    is AddProductActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                e1(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }
    fun uploadProductDetails(activity: AddProductActivity,productInfo:Product)
    {
        mFirestore.collection(Constants.PRODUCTS)
            .document()
            .set(productInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.productUploadSuccess()
            }
            .addOnFailureListener{
                e->
                activity.hideProgressDialog()
                e1(
                    activity.javaClass.simpleName,
                    "Error while uploading the product details",
                    e
                )
            }
    }

    fun getProductList(fragment: ProductsFragment)
    {
        mFirestore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID,getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                e1("Product List",document.documents.toString())
                val productsList : ArrayList <Product> = ArrayList()
                for (i in document.documents){
                    val product=i.toObject(Product::class.java)
                    product!!.product_id=i.id
                    productsList.add(product)
                }
                when (fragment)
                {
                    is ProductsFragment ->
                    {
                        fragment.successProductsListFromFirestore(productsList)
                    }

                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error based on the base class instance.
                when (fragment) {
                    is ProductsFragment -> {
                        fragment.hideProgressDialog()
                    }
                }
                e1("Get Product List", "Error while getting product list.", e)
            }
    }
    fun getProductDetails(activity: ProductDetailsActivity, productID : String)
    {
        mFirestore.collection(Constants.PRODUCTS)
            .document(productID)
            .get()
            .addOnSuccessListener {
                document ->
                e1(activity.javaClass.simpleName,document.toString())
                val product= document.toObject(Product::class.java)
                if (product != null) {
                    activity.productDetailsSuccess(product)
                }

            }
            .addOnFailureListener {
                e->
              activity.hideProgressDialog()
                e1(activity.javaClass.simpleName,"Error while getting the product details.",e)
            }
    }
    fun addCartItems(activity: ProductDetailsActivity, addToCart:CartItem)
    {
       mFirestore.collection(Constants.CART_ITEMS)
           .document()
           .set(addToCart, SetOptions.merge())
           .addOnSuccessListener {
                activity.addToCartSuccess()
           }
           .addOnFailureListener{
               e->
                   activity.hideProgressDialog()
                   Log.e(
                       activity.javaClass.simpleName,
                       "Error while creating the document item",
                       e
                   )
             
           }
    }

    fun removeItemFromCart(context: Context,cart_id:String)
    {
        mFirestore.collection(Constants.CART_ITEMS)
            .document(cart_id)
            .delete()
            .addOnSuccessListener {
                when(context)
                {
                    is CartListActivity->
                    {
                        context.itemRemovedSucess()
                    }
                }
            }
            .addOnFailureListener {
                e->
                when(context)
                {
                    is CartListActivity ->
                    {
                        context.hideProgressDialog()
                    }
                }
                Log.e(context.javaClass.simpleName,"Error while removing the item from cart list",e)
            }
    }
    fun deleteProduct(fragment : ProductsFragment, productId:String)
    {
        mFirestore.collection(Constants.PRODUCTS)
            .document(productId)
            .delete()
            .addOnSuccessListener {
                fragment.productDeleteSucess()
            }
            .addOnFailureListener {
                e ->
                fragment.hideProgressDialog()
                e1(
                    fragment.requireActivity().javaClass.simpleName,
                    "Error while deleting the product.",
                    e
                )
            }
    }

    fun getCartList(activity: Activity)
    {
        mFirestore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID,getCurrentUserID())
            .get()
            .addOnSuccessListener {
                document ->
                Log.e(activity.javaClass.simpleName,document.documents.toString())
                val list : ArrayList<CartItem> = ArrayList()
                for(i in document.documents)
                {
                    val cartItem=i.toObject(CartItem::class.java)!!
                    cartItem.id=i.id
                    list.add(cartItem)
                }
                when(activity)
                {
                   is CartListActivity ->
                   {
                     activity.successCartItemsList(list)
                   }
                }
            }
            .addOnFailureListener{
                e->
                when(activity)
                {
                    is CartListActivity ->
                    {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName,"Error while getting the cart list items",e)
            }
    }
    fun checkIfItemExistInCart(activity: ProductDetailsActivity,productId: String)
    {
        mFirestore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.PRODUCT_ID,productId)
            .get()
            .addOnSuccessListener {
                docuemnt->
                Log.e(activity.javaClass.simpleName, docuemnt.documents.toString())
                if(docuemnt.documents.size>0)
                {
                    activity.productExistsInCart()
                }
                else
                {
                    activity.hideProgressDialog()
                }
            }
            .addOnFailureListener{
                e->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while checking the existing cart list",
                    e
                )
            }
    }
    fun getAllProductList(activity: CartListActivity)
    {
        mFirestore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener {
                document->
                val productsList : ArrayList<Product> = ArrayList()
                for(i in document.documents)
                {
                    val product=i.toObject(Product::class.java)
                    product!!.product_id=i.id
                    productsList.add(product)
                }
                activity.successProductsListFromFireStore(productsList)
            }
            .addOnFailureListener {e->
                activity.hideProgressDialog()
                Log.e("Get Product list","Error while gettung all product list.",e)
            }
    }
    fun getDashBoardItemsList(fragment: DashboardFragment) {

        e1("Product List", "I'm in the function")

        mFirestore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                e1("Product List", document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()
                for (i in document.documents) {
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id
                    productsList.add(product)

                }
                when (fragment) {
                    is DashboardFragment -> {
                        fragment.hideProgressDialog()
                        fragment.successDashBordItemsList(productsList)

                    }
                }
            }
            .addOnFailureListener { e ->
                        // Hide the progress dialog if there is any error based on the base class instance.
                     fragment.hideProgressDialog()
                e1(fragment.javaClass.simpleName, "Error while getting dashbord items product list.", e)
                    }
    }
}
