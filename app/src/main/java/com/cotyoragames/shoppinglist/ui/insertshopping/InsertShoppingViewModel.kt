package com.cotyoragames.shoppinglist.ui.insertshopping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cotyoragames.shoppinglist.data.db.entities.ShoppingItem
import com.cotyoragames.shoppinglist.data.db.entities.Shoppings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app

class InsertShoppingViewModel() : ViewModel() {

    private val db=Firebase.firestore
    private val auth=Firebase.auth
    private val _shoppingList  = mutableListOf<Any>()
    private val _shoppingItemList  = mutableListOf<ShoppingItem>()
    private val _shoppings = MutableLiveData<Any>()
    val shoppings : LiveData<Any>
        get()=_shoppings

    fun getShoppings()
    {
        _shoppingList.clear()
        db.collection("shoppinglists").whereEqualTo("receiverId",auth.currentUser!!.uid).get().addOnSuccessListener { query->
            val docs= query.documents
            for (doc in docs)
            {
                val sender=doc["senderId"] as String
                val query = db.collection("users").whereEqualTo("uid",sender)
                query.get().addOnSuccessListener {
                    val userdoc = it.documents[0]
                    
                }
            }
        }
    }

    fun accept(shareid:String){

    }
}