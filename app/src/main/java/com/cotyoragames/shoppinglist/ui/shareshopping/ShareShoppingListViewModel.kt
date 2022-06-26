package com.cotyoragames.shoppinglist.ui.shareshopping

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cotyoragames.shoppinglist.data.db.entities.ShoppingItem
import com.cotyoragames.shoppinglist.data.db.entities.Shoppings
import com.cotyoragames.shoppinglist.data.db.entities.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.lang.Exception

class ShareShoppingListViewModel(private val shopping: Shoppings,private val currItems:List<ShoppingItem>) : ViewModel() {

    val db = Firebase.firestore
    private val auth: FirebaseAuth = Firebase.auth
    private val _friends = MutableLiveData<List<Users>>()
    val users : LiveData<List<Users>>
        get() = _friends

    val friendList: MutableList<String> =mutableListOf()
    private val _userList: MutableList<Users> =mutableListOf()
    init {
        getUsers()
    }
    private fun getUsers(){
        val query = db.collection("users").whereEqualTo("uid",auth.currentUser!!.uid)

        try {
            query.get().addOnSuccessListener { docs->
                val userdoc = docs.documents[0]
                friendList.addAll(userdoc["friends"] as List<String>)
                db.collection("users").whereIn("uid",friendList).get().addOnSuccessListener { snap->
                    for (doc in snap.documents)
                    {
                        val newUser = Users(doc["uid"] as String,
                            doc["displayName"] as String,doc["email"] as String ,doc["photoUrl"] as String)
                        _userList.add(newUser)
                    }
                    _friends.postValue(_userList)
                }
            }.addOnFailureListener { ex->

            }
        }
        catch (ex: Exception)
        {
            var x=""
        }

    }

    fun shareShopping(user: Users)
    {
        val docData = hashMapOf(
            "date" to shopping.date,
            "items" to currItems,
            "senderId" to auth.currentUser!!.uid,
            "receiverId" to user.useruid,
            "senderName" to auth.currentUser!!.displayName,
            "receiverName" to user.displayName
        )
        db.collection("shoppinglists").add(docData).addOnSuccessListener {

        }.addOnFailureListener {  e -> Log.w("FireStore", "Error writing document", e)  }
    }
}